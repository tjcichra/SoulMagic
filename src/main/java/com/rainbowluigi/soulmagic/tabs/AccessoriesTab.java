package com.rainbowluigi.soulmagic.tabs;

import com.rainbowluigi.soulmagic.inventory.ModContainerFactories;
import com.rainbowluigi.soulmagic.network.ModNetwork;
import com.rainbowluigi.soulmagic.network.OpenContainerMessage;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class AccessoriesTab extends Tab {

	public AccessoriesTab() {
		super(new ItemStack(Items.DIAMOND_CHESTPLATE), "container.soulmagic.accessories");
	}

	@Override
	public void whenClicked(PlayerEntity player, World world) {
		player.openHandledScreen(new NamedScreenHandlerFactory() {

			@Override
			public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
				return ModContainerFactories.ACCESSORIES.create(syncId, inv);
			}

			@Override
			public Text getDisplayName() {
				return null;
			}
		});
	}
}