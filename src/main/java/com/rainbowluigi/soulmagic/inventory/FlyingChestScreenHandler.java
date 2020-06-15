package com.rainbowluigi.soulmagic.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class FlyingChestScreenHandler extends ScreenHandler {

	private Text text;

	protected FlyingChestScreenHandler(int id, PlayerInventory pInv, Inventory inv, Text text) {
		super(null, id);
		this.text = text;
		
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
	public boolean canUse(PlayerEntity var1) {
		return true;
	}

	public Text getDisplayName() {
		return text != null ? text : new TranslatableText("container.soulmagic.flying_chest");
	}
}
