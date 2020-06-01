package com.rainbowluigi.soulmagic.inventory;

import java.util.Map;

import com.rainbowluigi.soulmagic.block.entity.SoulInfuserBlockEntity;
import com.rainbowluigi.soulmagic.item.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.item.SoulGemItem;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class SoulInfuserScreenHandler extends ScreenHandler {
	
	private SoulInfuserBlockEntity sibe;
	
	public SoulInfuserScreenHandler(int id, PlayerInventory playerInv, SoulInfuserBlockEntity sibe) {
		super(null, id);
		this.sibe = sibe;
		
		this.addSlot(new Slot(sibe, 0, 80, 23));
		this.addSlot(new Slot(sibe, 1, 103, 35));
		this.addSlot(new Slot(sibe, 2, 115, 58));
		this.addSlot(new Slot(sibe, 3, 103, 81));
		this.addSlot(new Slot(sibe, 4, 80, 93));
		this.addSlot(new Slot(sibe, 5, 57, 81));
		this.addSlot(new Slot(sibe, 6, 45, 58));
		this.addSlot(new Slot(sibe, 7, 57, 35));
		this.addSlot(new Slot(sibe, 8, 80, 58));
		this.addSlot(new Slot(sibe, 9, 148, 58) {
			public boolean canInsert(ItemStack stack) {
				return false;
			}
		});
		
		this.addSlot(new Slot(sibe, 10, 8, 100) {
			public boolean canInsert(ItemStack stack) {
				return stack.getItem() instanceof SoulEssenceStaff;
			}
		});
		
	    // Player Inventory, Slot 9-35, Slot IDs 9-35
	    for (int y = 0; y < 3; ++y) {
	        for (int x = 0; x < 9; ++x) {
	            this.addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 129 + y * 18));
	        }
	    }

	    // Player Inventory, Slot 0-8, Slot IDs 36-44
	    for (int x = 0; x < 9; ++x) {
	        this.addSlot(new Slot(playerInv, x, 8 + x * 18, 187));
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

	    if (slot != null && slot.hasStack()) {
	        ItemStack current = slot.getStack();
	        previous = current.copy();

	        if (fromSlot < 11) {
	            if (!this.insertItem(current, 11, 47, fromSlot == 9))
	                return ItemStack.EMPTY;
	        } else {
	        	if(current.getItem() instanceof SoulEssenceStaff) {
		        	this.insertItem(current, 10, 11, false);
		        } else if (current.getItem() instanceof SoulGemItem) {
		        	this.insertItem(current, 8, 9, false);
		        }
	        	
	        	if (!this.insertItem(current, 0, 9, false))
	                return ItemStack.EMPTY;
	        }

	        if (current.isEmpty()) {
	            slot.setStack(ItemStack.EMPTY);
	        } else {
	            slot.markDirty();
	        }
	        
	        if (current.getCount() == previous.getCount())
	            return ItemStack.EMPTY;
	        
	        slot.onTakeItem(playerIn, current);
	    }
	    return previous;
	}

	public Text getDisplayName() {
		return this.sibe.getDisplayName();
	}
	
	public int getCookProgress() {
		//SoulMagic.LOGGER.info("Container: " + this.tesi.getCookTime());
		//return 0;
		double current = 0;
		double total = 0;
		
		//if(!this.sibe.getRecipeSoulMap().isEmpty()) {
			for (double d : this.sibe.getCookSoulMap().values()) {
				current += d;
			}

			for (double d : this.sibe.getRecipeSoulMap().values()) {
				total += d;
			}
			//System.out.println("current: " + current + " total: " + total);
			return (int) ((current / (total)) * 101);
		//}
		
		//return 0;
	}
	
	public ItemStack getStaffCap() {
		return this.sibe.getStaffCap();
	}
	
	public Map<SoulType, Integer> getRecipeSoulMap() {
		return this.sibe.getRecipeSoulMap();
	}
	
	public int getProgressColor() {
		return this.sibe.getProgressColor();
	}
}
