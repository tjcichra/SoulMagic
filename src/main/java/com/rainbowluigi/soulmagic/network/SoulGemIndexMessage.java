package com.rainbowluigi.soulmagic.network;

import com.rainbowluigi.soulmagic.item.SoulGemItem;
import com.rainbowluigi.soulmagic.util.SoulGemHelper;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class SoulGemIndexMessage {
	
	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		ItemStack stack = player.getMainHandStack();
		if(stack.getItem() instanceof SoulGemItem) {
			SoulGemHelper.setCurrentSpellIndex(stack, buf.readInt());
			SoulGemHelper.setActivated(stack, false);
		}
	}
}
