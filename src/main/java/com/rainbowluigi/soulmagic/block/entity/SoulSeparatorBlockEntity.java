package com.rainbowluigi.soulmagic.block.entity;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import com.rainbowluigi.soulmagic.inventory.SoulSeparatorContainer;
import com.rainbowluigi.soulmagic.item.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.item.crafting.ModRecipes;
import com.rainbowluigi.soulmagic.item.crafting.SoulSeparatorRecipe;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class SoulSeparatorBlockEntity extends LockableContainerBlockEntity implements SidedInventory, BlockEntityClientSerializable, Tickable {

	private DefaultedList<ItemStack> inventory;
	
	public int progress, maxProgress;
	public boolean filling;
	
	public SoulSeparatorBlockEntity() {
		super(ModBlockEntity.SOUL_SEPARATOR);
		this.inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
	}
	
	@Override
	public void fromTag(CompoundTag compound) {
		super.fromTag(compound);
		this.inventory = DefaultedList.ofSize(this.getInvSize(), ItemStack.EMPTY);
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
		if(!this.world.isClient) {
			if(!this.inventory.get(0).isEmpty() && !this.inventory.get(2).isEmpty() && this.inventory.get(2).getItem() instanceof SoulEssenceStaff) {
				Optional<SoulSeparatorRecipe> irecipe = this.world.getRecipeManager().getFirstMatch(ModRecipes.SOUL_SEPARATOR_TYPE, this, this.world);
				
				if(irecipe.isPresent()) {
					
					Map<SoulType, Integer> soulMap = irecipe.get().getSoulMap(this, this.world);
					ItemStack staff = this.getStaffCap();
					
					if(!this.isFull(soulMap.keySet(), staff)) {
						this.maxProgress = irecipe.get().getCookTime();
						this.progress++;
						this.filling = irecipe.get().getFiling();
						this.sync();
						
						if(this.progress >= this.maxProgress) {
							this.progress = 0;
							this.filling = false;
							this.sync();
							
							
							
							this.fillSoul(soulMap, staff);
							
							irecipe.get().postCraft(this, this.world, soulMap);
							
						}
					}
				}
			}
			
			//if(this.progress > 0){
			//	this.progress--;
			//	this.sync();
			//}
		}
		//for(Entry<SoulType, Double> entry : this.soul.entrySet()) {
		//	System.out.println(entry.getKey() + " has " + entry.getValue());
		//}
	}
	
	public boolean isFull(Set<SoulType> set, ItemStack stack) {
		boolean test = true;
		SoulEssenceStaff ses = (SoulEssenceStaff) stack.getItem();
		for(SoulType s : set) {
			if(ses.getSoul(stack, this.world, s) < ses.getMaxSoul(stack, this.world, s)) {
				
				test = false;
			}
		}
		return test;
	}
	
	public void fillSoul(Map<SoulType, Integer> soulMap, ItemStack stack) {
		SoulEssenceStaff ses = (SoulEssenceStaff) stack.getItem();
		for(Entry<SoulType, Integer> entry : soulMap.entrySet()) {
			
			ses.addSoul(stack, this.world, entry.getKey(), entry.getValue());
		}
	}

	@Override
	public int getInvSize() {
		return this.inventory.size();
	}

	@Override
	public boolean isInvEmpty() {
		for(ItemStack stack : this.inventory) {
			if(!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getInvStack(int i) {
		return i >= 0 && i < this.inventory.size() ? this.inventory.get(i) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack takeInvStack(int i1, int i2) {
		return Inventories.splitStack(this.inventory, i1, i2);
	}

	@Override
	public ItemStack removeInvStack(int i) {
		return Inventories.removeStack(this.inventory, i);
	}

	@Override
	public void setInvStack(int i, ItemStack stack) {
		if(i >= 0 && i < this.inventory.size()) {
			this.inventory.set(i, stack);
		}
	}

	@Override
	public boolean canPlayerUseInv(PlayerEntity playerEntity_1) {
		if (this.world.getBlockEntity(this.pos) != this) {
			return false;
		} else {
			return playerEntity_1.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	public int[] getInvAvailableSlots(Direction var1) {
		return new int[] {0,1};
	}

	@Override
	public boolean canInsertInvStack(int var1, ItemStack var2, Direction var3) {
		
		return true;
	}

	@Override
	public boolean canExtractInvStack(int var1, ItemStack var2, Direction var3) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected Text getContainerName() {
		return new TranslatableText("container.soulmagic.soul_separator");
	}

	@Override
	protected Container createContainer(int i, PlayerInventory pi) {
		return new SoulSeparatorContainer(i, pi, this);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		this.progress = tag.getInt("progress");
		this.maxProgress = tag.getInt("maxProgress");
		this.filling = tag.getBoolean("filling");
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		tag.putInt("progress", this.progress);
		tag.putInt("maxProgress", this.maxProgress);
		tag.putBoolean("filling", this.filling);
		return tag;
	}

	public ItemStack getStaffCap() {
		return this.inventory.get(2).getItem() instanceof SoulEssenceStaff ? this.inventory.get(2) : null;
	}
}
