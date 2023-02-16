package com.rainbowluigi.soulmagic.inventory;

import com.rainbowluigi.soulmagic.block.ModBlocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public class UpgradeStationScreenHandler extends ScreenHandler {
	private final ScreenHandlerContext context;

	public UpgradeStationScreenHandler(int syncId, PlayerInventory playerInv) {
		this(syncId, playerInv, ScreenHandlerContext.EMPTY);
	}

	public UpgradeStationScreenHandler(int syncId, PlayerInventory playerInv, ScreenHandlerContext context) {
		super(ModScreenHandlerTypes.UPGRADE_STATION, syncId);
		this.context = context;
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return canUse(this.context, player, ModBlocks.UPGRADE_STATION);
	}
}