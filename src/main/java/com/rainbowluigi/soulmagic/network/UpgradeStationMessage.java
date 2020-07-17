package com.rainbowluigi.soulmagic.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class UpgradeStationMessage {

	public static PacketByteBuf makePacket(ItemStack stack) {
		PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
		pbb.writeItemStack(stack);
		return pbb;
	}
	
	public static void handle(PacketContext context, PacketByteBuf buffer) {
		ItemStack stack = buffer.readItemStack();

		context.getTaskQueue().execute(() -> {
			context.getPlayer().currentScreenHandler.getSlot(0).setStack(stack);
		});
	}
}
