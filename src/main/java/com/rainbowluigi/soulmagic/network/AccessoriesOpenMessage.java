package com.rainbowluigi.soulmagic.network;

import com.rainbowluigi.soulmagic.inventory.AccessoriesScreenHandler;

import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class AccessoriesOpenMessage {
	
	public static void handle(PacketContext context, PacketByteBuf buffer) {
		context.getTaskQueue().execute(() -> {
			context.getPlayer().openHandledScreen(new NamedScreenHandlerFactory() {
				@Override
				public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
					return new AccessoriesScreenHandler(syncId, inv);
				}
	
				@Override
				public Text getDisplayName() {
					return new TranslatableText("container.soulmagic.accessories");
				}
			});
		});
	}
}
