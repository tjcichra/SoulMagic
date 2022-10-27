package com.rainbowluigi.soulmagic.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class FlyingChestScreenHandler extends ScreenHandler {

	public FlyingChestScreenHandler(int id, PlayerInventory pInv, Inventory inv) {
		super(ModScreenHandlerTypes.FLYING_CHEST, id);
		
		for (int y = 0; y < 3; ++y) {
	        for (int x = 0; x < 9; ++x) {
	            this.addSlot(new Slot(inv, x + y * 9, 8 + x * 18, 18 + y * 18));
	        }
	    }
		
	    // Player Inventory, Slot 9-35, Slot IDs 9-35
	    for (int y = 0; y < 3; ++y) {
	        for (int x = 0; x < 9; ++x) {
	            this.addSlot(new Slot(pInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
	        }
	    }

	    // Player Inventory, Slot 0-8, Slot IDs 36-44
	    for (int x = 0; x < 9; ++x) {
	        this.addSlot(new Slot(pInv, x, 8 + x * 18, 142));
	    }
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		return null;
	}

	@Override
	public boolean canUse(PlayerEntity var1) {
		return true;
	}
}
