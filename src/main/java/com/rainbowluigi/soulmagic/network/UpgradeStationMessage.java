package com.rainbowluigi.soulmagic.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public class UpgradeStationMessage {

	public static PacketByteBuf makePacket(ItemStack stack) {
		PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
		pbb.writeItemStack(stack);
		return pbb;
	}

	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		ItemStack stack = buf.readItemStack();
		player.setStackInHand(Hand.MAIN_HAND, stack);
	}
}
