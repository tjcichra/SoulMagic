package com.rainbowluigi.soulmagic.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class UpgradeStationTakeItemsMessage {
	
	public static PacketByteBuf makePacket(ItemStack[] requirements) {
		PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
		pbb.writeInt(requirements.length);
		
		for(ItemStack stack : requirements) {
			pbb.writeItemStack(stack);
		}

		return pbb;
	}

	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		int num = buf.readInt();
		List<ItemStack> list = new ArrayList<>();

		for(int i = 0; i < num; i++) {
			list.add(buf.readItemStack());
		}

		for(ItemStack requirement : list) {
			for(ItemStack stack : player.getInventory().main) {
				if(stack.isItemEqual(requirement) && stack.getCount() >= requirement.getCount()) {
					stack.decrement(requirement.getCount());
				}
			}
		}
	}
}