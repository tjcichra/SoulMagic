package com.rainbowluigi.soulmagic.inventory;

import com.rainbowluigi.soulmagic.block.entity.SoulSeparatorBlockEntity;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class SoulSeparatorScreenHandler extends ScreenHandler {
	
	private SoulSeparatorBlockEntity sibe;
	
	public SoulSeparatorScreenHandler(int id, PlayerInventory playerInv, SoulSeparatorBlockEntity sibe) {
		super(ModScreenHandlerTypes.SOUL_ESSENCE_SEPARATOR, id);
		this.sibe = sibe;
		
		this.addSlot(new Slot(sibe, 0, 44, 35));
		this.addSlot(new Slot(sibe, 1, 152, 35));
		this.addSlot(new Slot(sibe, 2, 112, 35));
		
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
		/*ItemStack previous = ItemStack.EMPTY;
		Slot slot = this.getSlot(fromSlot);

	    if (slot != null && slot.hasStack()) {
	        ItemStack current = slot.getStack();
	        previous = current.copy();

	        if(current.getItem() instanceof SoulStaff) {
	        	if(fromSlot != 9) {
	        		if (!this.insertItem(current, 9, 10, false))
	        			return ItemStack.EMPTY;
	        	} else {
	        		if (!this.insertItem(current, 10, 46, true))
	        			return ItemStack.EMPTY;
	        	}
	        } else if (fromSlot < 10) {
	            if (!this.insertItem(current, 10, 46, true))
	                return ItemStack.EMPTY;
	        } else {
	            if (!this.insertItem(current, 0, 10, false))
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
	    */
		return ItemStack.EMPTY;
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
	
	public ItemStack getStaffCap() {
		return this.sibe.getStaffCap();
	}
	
	public Text getDisplayName() {
		return this.sibe.getDisplayName();
	}
}
