package com.rainbowluigi.soulmagic.block.entity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.google.common.collect.Maps;
import com.rainbowluigi.soulmagic.inventory.SoulInfuserScreenHandler;
import com.rainbowluigi.soulmagic.item.SoulGemItem;
import com.rainbowluigi.soulmagic.item.crafting.ModRecipes;
import com.rainbowluigi.soulmagic.item.crafting.SoulInfusionRecipe;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

public class SoulEssenceInfuserBlockEntity extends LockableContainerBlockEntity implements SidedInventory, BlockEntityClientSerializable, Tickable, ExtendedScreenHandlerFactory {

	private static final int[] TOP_SLOTS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	private static final int[] SIDE_SLOTS = new int[] { 10 };
	private static final int[] BOTTOM_SLOTS = new int[] { 9 };

	private DefaultedList<ItemStack> inventory;
	private List<UpgradeAndSelection> upgrades;
	private int selectorPoints;

	private Map<SoulType, Integer> cookSoulMap = Maps.newHashMap();
	private Map<SoulType, Integer> recipeSoulMap = Maps.newHashMap();
	private int progressColor = 0xFFFFFF;

	public SoulEssenceInfuserBlockEntity() {
		super(ModBlockEntity.SOUL_INFUSER);
		this.inventory = DefaultedList.ofSize(11, ItemStack.EMPTY);
		this.upgrades = Collections.emptyList();
		this.selectorPoints = 0;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag compound) {
		super.fromTag(state, compound);
		this.inventory = DefaultedList.ofSize(11, ItemStack.EMPTY);
		this.fromClientTag(compound);
		Inventories.fromTag(compound, this.inventory);

		if(compound.contains("upgrades")) {
			ListTag t = (ListTag) compound.get("upgrades");

			for(int i = 0; i < t.size(); i++) {
				CompoundTag tag = (CompoundTag) t.get(i);
				upgrades.add(new UpgradeAndSelection(ModUpgrades.UPGRADE.get(new Identifier(tag.getString("name"))), tag.getBoolean("selected")));
			}
		}

		this.selectorPoints = compound.getInt("selectorPoints");
	}

	@Override
	public CompoundTag toTag(CompoundTag compound) {
		super.toTag(compound);
		this.toClientTag(compound);
		Inventories.toTag(compound, this.inventory);

		if(!this.upgrades.isEmpty()) {
			if(!compound.contains("upgrades")) {
				ListTag list = new ListTag();
				compound.put("upgrades", list);
			}

			ListTag list = (ListTag) compound.get("upgrades");

			for(UpgradeAndSelection u : this.upgrades) {
				CompoundTag upgradeTag = new CompoundTag();
				upgradeTag.putString("name", ModUpgrades.UPGRADE.getId(u.u).toString());
				upgradeTag.putBoolean("selected", u.selected);
				list.add(upgradeTag);
			}
		}
		compound.putInt("selectorPoints", this.selectorPoints);
		
		return compound;
	}

	@Override
	public void tick() {
		if (!this.world.isClient) {
			boolean flag = false;

			if (this.hasCenterItem()) {
				Optional<SoulInfusionRecipe> irecipe = this.world.getRecipeManager()
						.getFirstMatch(ModRecipes.SOUL_ESSENCE_INFUSION_TYPE, this, this.world);

				if (irecipe.isPresent()) {
					flag = true;

					this.recipeSoulMap = irecipe.get().getSoulMap(this, this.world);
					this.progressColor = irecipe.get().getProgressColor();

					ItemStack staff = this.getStaffCap();

					if (this.canCook(irecipe.get(), staff)) {
						ItemStack stack = irecipe.get().craft(this);

						if (this.inventory.get(9).isEmpty()) {
							this.inventory.set(9, stack);
						} else {
							this.inventory.get(9).setCount(this.inventory.get(9).getCount() + stack.getCount());
						}

						this.cookSoulMap.clear();
						this.sync();

						DefaultedList<ItemStack> results = irecipe.get().getRemainingStacks(this);

						for (int i = 0; i < 9; i++) {
							ItemStack stackF = this.inventory.get(i);

							if (!stackF.isEmpty()) {

								stackF.decrement(1);

								if (stackF.isEmpty()) {
									this.inventory.set(i, results.get(i));
								}
							}
						}
					}
				}
			}

			if (!flag && !this.cookSoulMap.isEmpty()) {
				this.cookSoulMap.clear();
				this.sync();
			}
		}
	}

	public boolean hasCenterItem() {
		return !this.getStack(8).isEmpty();
	}

	public ItemStack getStaffCap() {
		return this.inventory.get(10).getItem() instanceof SoulEssenceStaff ? this.inventory.get(10) : null;
	}

	public boolean canCook(SoulInfusionRecipe recipe, ItemStack staff) {
		if (staff == null) {
			return false;
		}

		if (!this.inventory.get(9).isEmpty()) {
			ItemStack stack = this.inventory.get(9);
			if (!stack.isItemEqualIgnoreDamage(recipe.getOutput())
					|| stack.getCount() > stack.getMaxCount() - recipe.getOutput().getCount()) {
				return false;
			}
		}

		SoulEssenceStaff staff2 = (SoulEssenceStaff) staff.getItem();

		boolean good = true;

		for (Entry<SoulType, Integer> entry : recipe.getSoulMap(this, this.world).entrySet()) {
			Integer d = this.cookSoulMap.get(entry.getKey());
			int d2 = d != null ? d : 0;

			if (d2 < entry.getValue()) {
				if (staff2.subtractSoul(staff, this.world, entry.getKey(), 1)) {
					this.cookSoulMap.put(entry.getKey(), d2 + 1);
					this.sync();
				}
				good = false;
			}
		}

		return good;
	}

	public void setUpgrades(List<UpgradeAndSelection> upgrades) {
		this.upgrades = upgrades;
	}

	public void setSelectorPoints(int selectorPoints) {
		this.selectorPoints = selectorPoints;
	}

	public Map<SoulType, Integer> getCookSoulMap() {
		return this.cookSoulMap;
	}

	public void setCookSoulMap(Map<SoulType, Integer> cookSoulMap) {
		this.cookSoulMap = cookSoulMap;
	}

	public Map<SoulType, Integer> getRecipeSoulMap() {
		return this.recipeSoulMap;
	}

	public void setRecipeSoulMap(Map<SoulType, Integer> recipeSoulMap) {
		this.recipeSoulMap = recipeSoulMap;
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
	public int[] getAvailableSlots(Direction d) {
		switch (d) {
			case DOWN:
				return BOTTOM_SLOTS;
			case UP:
				return TOP_SLOTS;
			default:
				return SIDE_SLOTS;
		}
	}

	@Override
	public boolean canInsert(int var1, ItemStack var2, Direction var3) {
		return this.isValid(var1, var2);
	}

	@Override
	public boolean canExtract(int var1, ItemStack var2, Direction var3) {
		return true;
	}

	@Override
	public boolean isValid(int slot, ItemStack stack) {
		if (slot == 9) {
			return false;
		} else if (slot != 10) {
			return !(slot != 8 && this.inventory.get(8).isEmpty() && stack.getItem() instanceof SoulGemItem);
		} else {
			return stack.getItem() instanceof SoulEssenceStaff;
		}
	}

	@Override
	protected Text getContainerName() {
		return new TranslatableText("container.soulmagic.soul_essence_infuser");
	}

	@Override
	protected ScreenHandler createScreenHandler(int i, PlayerInventory pi) {
		return new SoulInfuserScreenHandler(i, pi, this);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		this.progressColor = tag.getInt("colorProgress");
		this.selectorPoints = tag.getInt("selectorPoints");

		CompoundTag cookSoul = tag.getCompound("cookSoul");

		for (SoulType st : ModSoulTypes.SOUL_TYPE) {
			this.cookSoulMap.put(st, cookSoul.getInt(ModSoulTypes.SOUL_TYPE.getId(st).toString()));
		}

		CompoundTag recipeSoul = tag.getCompound("recipeSoul");
		for (SoulType st : ModSoulTypes.SOUL_TYPE) {
			this.recipeSoulMap.put(st, recipeSoul.getInt(ModSoulTypes.SOUL_TYPE.getId(st).toString()));
		}
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		CompoundTag cookSoul = new CompoundTag();
		for (SoulType st : ModSoulTypes.SOUL_TYPE) {
			int x = this.cookSoulMap.get(st) != null ? this.cookSoulMap.get(st) : 0;
			cookSoul.putInt(ModSoulTypes.SOUL_TYPE.getId(st).toString(), x);
		}

		CompoundTag recipeSoul = new CompoundTag();
		for (SoulType st : ModSoulTypes.SOUL_TYPE) {
			// System.out.println("Recipe Soul: " + e);
			int x = this.recipeSoulMap.get(st) != null ? this.recipeSoulMap.get(st) : 0;
			recipeSoul.putInt(ModSoulTypes.SOUL_TYPE.getId(st).toString(), x);
			// recipeSoul.putInt(ModSoulTypes.SOUL_TYPE_REG.getId(e.getKey()).toString(),
			// e.getValue());
		}

		tag.putInt("colorProgress", this.progressColor);
		tag.put("cookSoul", cookSoul);
		tag.put("recipeSoul", recipeSoul);
		tag.putInt("selectorPoints", this.selectorPoints);
		return tag;
	}

	public int getProgressColor() {
		return this.progressColor;
	}

	public int getCookProgress() {
		double current = 0;
		double total = 0;

		if (!recipeSoulMap.isEmpty()) {
			for (double d : cookSoulMap.values()) {
				current += d;
			}

			for (double d : recipeSoulMap.values()) {
				total += d;
			}

			return (int) ((current / (total)) * 100);
		}

		return 0;
	}

	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
		buf.writeBlockPos(this.pos);
	}

	public static class UpgradeAndSelection {
		private Upgrade u;
		private boolean selected;

		public UpgradeAndSelection(Upgrade u, boolean selected) {
			this.u = u;
			this.selected = selected;
		}
	}
}
