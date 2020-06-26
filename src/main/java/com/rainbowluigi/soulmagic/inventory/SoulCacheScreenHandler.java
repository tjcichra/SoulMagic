package com.rainbowluigi.soulmagic.inventory;

import com.rainbowluigi.soulmagic.block.entity.SoulCacheBlockEntity;
import com.rainbowluigi.soulmagic.item.soulessence.ReferenceStaffItem;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaff;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class SoulCacheScreenHandler extends ScreenHandler {

	private SoulCacheBlockEntity sibe;

	public SoulCacheScreenHandler(int id, PlayerInventory playerInv, SoulCacheBlockEntity sibe) {
		super(ModScreenHandlerTypes.SOUL_STAFF_CACHE, id);
		this.sibe = sibe;
		
		this.addSlot(new StaffSlot(sibe, 0, 44, 35));
		this.addSlot(new StaffSlot(sibe, 1, 80, 35));
		this.addSlot(new StaffSlot(sibe, 2, 116, 35));
		
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

	    if (slot != null && slot.hasStack()) {
	        ItemStack current = slot.getStack();
	        previous = current.copy();

	        if (fromSlot < 3) {
	            if (!this.insertItem(current, 3, 39, true))
	                return ItemStack.EMPTY;
	        } else {
	            if (!this.insertItem(current, 0, 3, false))
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
		//return ItemStack.EMPTY;
	}
	
	public Text getDisplayName() {
		return this.sibe.getDisplayName();
	}
	
	public class StaffSlot extends Slot {

		public StaffSlot(Inventory inventory_1, int int_1, int int_2, int int_3) {
			super(inventory_1, int_1, int_2, int_3);
			// TODO Auto-generated constructor stub
		}
		
		public boolean canInsert(ItemStack stack) {
			return stack.getItem() instanceof SoulEssenceStaff && !(stack.getItem() instanceof ReferenceStaffItem);
		}
	}
}
