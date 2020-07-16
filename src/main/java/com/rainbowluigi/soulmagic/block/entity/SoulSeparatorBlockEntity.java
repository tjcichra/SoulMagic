package com.rainbowluigi.soulmagic.block.entity;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import com.rainbowluigi.soulmagic.inventory.SoulSeparatorScreenHandler;
import com.rainbowluigi.soulmagic.item.crafting.ModRecipes;
import com.rainbowluigi.soulmagic.item.crafting.SoulSeparatorRecipe;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

public class SoulSeparatorBlockEntity extends LockableContainerBlockEntity implements SidedInventory, BlockEntityClientSerializable, Tickable, ExtendedScreenHandlerFactory {

	public static final int INPUT_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;
	public static final int STAFF_SLOT = 2;
	public static final int FUEL_SLOT = 3;

	private DefaultedList<ItemStack> inventory;

	public int progress, maxProgress, burnTime, fuelTime;
	public boolean filling;

	public SoulSeparatorBlockEntity() {
		super(ModBlockEntity.SOUL_SEPARATOR);
		this.inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag compound) {
		super.fromTag(state, compound);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		Inventories.fromTag(compound, this.inventory);
	}

	@Override
	public CompoundTag toTag(CompoundTag compound) {
		super.toTag(compound);
		Inventories.toTag(compound, this.inventory);
		return compound;
	}

	@Override
	public void tick() {
		if(this.burnTime > 0) {
			this.burnTime--;
		}

		if (!this.world.isClient) {
			if (!this.inventory.get(INPUT_SLOT).isEmpty() && this.inventory.get(STAFF_SLOT).getItem() instanceof SoulEssenceStaff) {
				SoulSeparatorRecipe irecipe = this.world.getRecipeManager().getFirstMatch(ModRecipes.SOUL_ESSENCE_SEPARATION_TYPE, this, this.world).orElse(null);

				if (irecipe != null) {
					if(this.burnTime <= 0) {
						ItemStack stack = this.inventory.get(FUEL_SLOT);

						Integer i = FuelRegistry.INSTANCE.get(stack.getItem());
						this.burnTime = i != null ? i : 0;

						if(this.burnTime > 0 && !stack.isEmpty()) {
							this.fuelTime = this.burnTime;

							Item item = stack.getItem();
							stack.decrement(1);
							if(stack.isEmpty()) {
								Item item2 = item.getRecipeRemainder();
								this.inventory.set(FUEL_SLOT, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
							}
						} else if(this.progress > 0) {
							this.progress--;
						}

						this.sync();
					} else {
						Map<SoulType, Integer> soulMap = irecipe.getSoulMap(this, this.world);
						ItemStack staff = this.getStaffCap();

						if (!this.isFull(soulMap.keySet(), staff)) {
							this.maxProgress = irecipe.getCookTime();
							this.progress++;
							this.filling = irecipe.getFiling();
							this.sync();

							if (this.progress >= this.maxProgress) {
								this.progress = 0;
								this.filling = false;
								this.sync();

								this.fillSoul(soulMap, staff);

								irecipe.postCraft(this, this.world, soulMap);

							}
						}
					}
				}
			} else {
				this.progress = 0;
			}

			// if(this.progress > 0){
			// this.progress--;
			// this.sync();
			// }
		}
		// for(Entry<SoulType, Double> entry : this.soul.entrySet()) {
		// System.out.println(entry.getKey() + " has " + entry.getValue());
		// }
	}

	public boolean isFull(Set<SoulType> set, ItemStack stack) {
		boolean test = true;
		SoulEssenceStaff ses = (SoulEssenceStaff) stack.getItem();
		for (SoulType s : set) {
			if (ses.getSoul(stack, this.world, s) < ses.getMaxSoul(stack, this.world, s)) {

				test = false;
			}
		}
		return test;
	}

	public void fillSoul(Map<SoulType, Integer> soulMap, ItemStack stack) {
		SoulEssenceStaff ses = (SoulEssenceStaff) stack.getItem();
		for (Entry<SoulType, Integer> entry : soulMap.entrySet()) {

			ses.addSoul(stack, this.world, entry.getKey(), entry.getValue());
		}
	}

	@Override
	public int size() {
		return this.inventory.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack stack : this.inventory) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStack(int i) {
		return i >= 0 && i < this.inventory.size() ? this.inventory.get(i) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int i1, int i2) {
		return Inventories.splitStack(this.inventory, i1, i2);
	}

	@Override
	public ItemStack removeStack(int i) {
		return Inventories.removeStack(this.inventory, i);
	}

	@Override
	public void setStack(int i, ItemStack stack) {
		if (i >= 0 && i < this.inventory.size()) {
			this.inventory.set(i, stack);
		}
	}

	@Override
	public boolean canPlayerUse(PlayerEntity playerEntity_1) {
		if (this.world.getBlockEntity(this.pos) != this) {
			return false;
		} else {
			return playerEntity_1.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	public int[] getAvailableSlots(Direction var1) {
		return new int[] { 0, 1 };
	}

	@Override
	public boolean canInsert(int var1, ItemStack var2, Direction var3) {
		return true;
	}

	@Override
	public boolean canExtract(int var1, ItemStack var2, Direction var3) {
		return true;
	}

	@Override
	protected Text getContainerName() {
		return new TranslatableText("container.soulmagic.soul_separator");
	}

	@Override
	protected ScreenHandler createScreenHandler(int i, PlayerInventory pi) {
		return new SoulSeparatorScreenHandler(i, pi, this);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		this.progress = tag.getInt("progress");
		this.maxProgress = tag.getInt("maxProgress");
		this.burnTime = tag.getInt("burnTime");
		this.fuelTime = tag.getInt("fuelTime");
		this.filling = tag.getBoolean("filling");
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		tag.putInt("progress", this.progress);
		tag.putInt("maxProgress", this.maxProgress);
		tag.putInt("burnTime", this.burnTime);
		tag.putInt("fuelTime", this.fuelTime);
		tag.putBoolean("filling", this.filling);
		return tag;
	}

	public ItemStack getStaffCap() {
		return this.inventory.get(STAFF_SLOT).getItem() instanceof SoulEssenceStaff ? this.inventory.get(STAFF_SLOT) : null;
	}

	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeBlockPos(this.pos);
	}
}
