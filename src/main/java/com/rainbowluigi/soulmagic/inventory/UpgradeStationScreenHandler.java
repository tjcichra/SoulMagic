package com.rainbowluigi.soulmagic.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class UpgradeStationScreenHandler extends ScreenHandler {
	private final Inventory input;

	public UpgradeStationScreenHandler(int syncId, PlayerInventory playerInv) {
		super(ModScreenHandlerTypes.UPGRADE_STATION, syncId);
		this.input = new SimpleInventory(1);

		this.addSlot(new Slot(this.input, 0, 20, 20));
		
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
	public boolean canUse(PlayerEntity player) {
		return true;
	}

	public void close(PlayerEntity player) {
		super.close(player);
		this.dropInventory(player, player.world, this.input);
	}
}