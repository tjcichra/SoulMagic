package com.rainbowluigi.soulmagic.network;

import com.rainbowluigi.soulmagic.inventory.AccessoriesScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class AccessoriesOpenMessage {

	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		player.openHandledScreen(new NamedScreenHandlerFactory() {
			@Override
			public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
				return new AccessoriesScreenHandler(syncId, inv);
			}

			@Override
			public Text getDisplayName() {
				return Text.translatable("container.soulmagic.accessories");
			}
		});
	}
}
