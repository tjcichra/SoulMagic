package com.rainbowluigi.soulmagic.inventory;

import com.rainbowluigi.soulmagic.block.entity.SoulSeparatorBlockEntity;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class SoulSeparatorScreenHandler extends ScreenHandler {
	
	private SoulSeparatorBlockEntity sibe;
	
	public SoulSeparatorScreenHandler(int id, PlayerInventory playerInv, SoulSeparatorBlockEntity sibe) {
		super(ModScreenHandlerTypes.SOUL_ESSENCE_SEPARATOR, id);
		this.sibe = sibe;
		
		this.addSlot(new Slot(sibe, SoulSeparatorBlockEntity.INPUT_SLOT, 38, 35));
		this.addSlot(new Slot(sibe, SoulSeparatorBlockEntity.OUTPUT_SLOT, 134, 57));
		this.addSlot(new Slot(sibe, SoulSeparatorBlockEntity.STAFF_SLOT, 134, 35));
		this.addSlot(new FuelSlot(sibe, SoulSeparatorBlockEntity.FUEL_SLOT, 16, 35));
		
		// Player Inventory, Slot 9-35, Slot IDs 9-35
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		// Player Inventory, Slot 0-8, Slot IDs 36-44
		for (int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(playerInv, x, 8 + x * 18, 142));
		}
	}

	@Override
	public boolean canUse(PlayerEntity playerIn) {
		return this.sibe.canPlayerUse(playerIn);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity playerIn, int fromSlot) {
		ItemStack previous = ItemStack.EMPTY;
		Slot slot = this.getSlot(fromSlot);

		if(slot != null && slot.hasStack()) {
			ItemStack current = slot.getStack();
			previous = current.copy();

			if(current.getItem() instanceof SoulEssenceStaff) {
				if(fromSlot >= 4) {
					if(!this.insertItem(current, 2, 3, false)) {
						if(!this.insertItem(current, 0, 1, false))
							return ItemStack.EMPTY;
					}
				} else {
					if (!this.insertItem(current, 4, 40, fromSlot == 2))
						return ItemStack.EMPTY;
				}
			} else if(fromSlot < 4) {
				if(!this.insertItem(current, 4, 40, fromSlot == 1))
					return ItemStack.EMPTY;
			} else {
				if(!this.insertItem(current, 3, 4, false))
					if(!this.insertItem(current, 0, 4, false))
						return ItemStack.EMPTY;
			}

			if(current.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
			
			if(current.getCount() == previous.getCount())
				return ItemStack.EMPTY;
			
			slot.onTakeItem(playerIn, current);
		}
		return previous;
	}

	/*
	public int getCookProgress() {
		//SoulMagic.LOGGER.info("Container: " + this.tesi.getCookTime());
		//return 0;
		double current = 0;
		double total = 0;
		
		if(!this.tesi.getRecipeSoulMap().isEmpty()) {
			for (double d : this.tesi.getCookSoulMap().values()) {
				current += d;
			}

			for (double d : this.tesi.getRecipeSoulMap().values()) {
				total += d;
			}
			return (int) ((current / (total)) * 101);
		}
		
		return 0;
	}
	
	public ISoulHandler getStaffCap() {
		return this.tesi.getStaffCap();
	}
	
	public Map<SoulType, Double> getRecipeSoulMap() {
		return this.tesi.getRecipeSoulMap();
	}*/
	
	public int getCookProgress() {
		if(this.sibe.filling && this.sibe.getStaffCap() != null) {
			ItemStack stack = this.getStaffCap();
			SoulEssenceStaff ses = (SoulEssenceStaff) stack.getItem();
			
			int total = 0;
			int current = 0;
			for (SoulType type : ModSoulTypes.SOUL_TYPE) {
				if (ses.getSoul(stack, this.sibe.getWorld(), type) > 0) {
					current += ses.getSoul(stack, this.sibe.getWorld(), type);
					total += ses.getMaxSoul(stack, this.sibe.getWorld(), type);
				}
			}
			return (int) (((double) current / total) * 38);
		}
		return (int) (((double) this.sibe.progress / this.sibe.maxProgress) * 38);
	}

	public int getBurn() {
		return this.sibe.fuelTime != 0 ? this.sibe.burnTime * 13 / this.sibe.fuelTime : 0;
	}
	
	public ItemStack getStaffCap() {
		return this.sibe.getStaffCap();
	}
	
	public Text getDisplayName() {
		return this.sibe.getDisplayName();
	}

	private static class FuelSlot extends Slot {

		public FuelSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		public boolean canInsert(ItemStack stack) {
			return FuelRegistry.INSTANCE.get(stack.getItem()) != null || stack.getItem() == Items.BUCKET;
		}

		public int getMaxStackAmount(ItemStack stack) {
			return stack.getItem() == Items.BUCKET ? 1 : super.getMaxStackAmount(stack);
		}
	}
}
