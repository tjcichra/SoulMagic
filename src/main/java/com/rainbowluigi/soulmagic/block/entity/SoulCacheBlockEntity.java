package com.rainbowluigi.soulmagic.block.entity;

import com.rainbowluigi.soulmagic.inventory.SoulCacheContainer;
import com.rainbowluigi.soulmagic.item.ReferenceStaffItem;
import com.rainbowluigi.soulmagic.item.SoulEssenceStaff;

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
import net.minecraft.util.math.Direction;

public class SoulCacheBlockEntity extends LockableContainerBlockEntity implements SidedInventory {

	private static final int[] ALL_SLOTS = new int[]{0,1,2};
	
	private DefaultedList<ItemStack> inventory;
	
	public SoulCacheBlockEntity() {
		super(ModBlockEntity.SOUL_INFUSER);
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
	public int[] getInvAvailableSlots(Direction d) {
		return ALL_SLOTS;
	}

	@Override
	public boolean canInsertInvStack(int var1, ItemStack var2, Direction var3) {
		return this.isValidInvStack(var1, var2);
	}

	@Override
	public boolean canExtractInvStack(int var1, ItemStack var2, Direction var3) {
		return true;
	}

	@Override
	public boolean isValidInvStack(int slot, ItemStack stack) {
		return stack.getItem() instanceof SoulEssenceStaff && !(stack.getItem() instanceof ReferenceStaffItem);
	}
	
	@Override
	protected Text getContainerName() {
		return new TranslatableText("container.soulmagic.soul_cache");
	}

	@Override
	protected Container createContainer(int i, PlayerInventory pi) {
		return new SoulCacheContainer(i, pi, this);
	}
}
