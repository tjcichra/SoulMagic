package com.rainbowluigi.soulmagic.network;

import com.rainbowluigi.soulmagic.inventory.ModContainerFactories;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.impl.container.ContainerProviderImpl;
import net.minecraft.network.PacketByteBuf;

public class FlyingChestOpenMessage {

	public static PacketByteBuf makePacket(int slot) {
		PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
		pbb.writeInt(slot);
		return pbb;
	}
	
	public static void handle(PacketContext context, PacketByteBuf buffer) {
		ContainerProviderImpl.INSTANCE.openContainer(ModContainerFactories.FLYING_CHEST, context.getPlayer(), buf -> {buf.writeInt(buffer.readInt());});
	}
}
