package com.rainbowluigi.soulmagic.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.impl.container.ContainerProviderImpl;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class OpenContainerMessage {

	public static PacketByteBuf makePacket(Identifier id) {
		PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
		pbb.writeIdentifier(id);
		return pbb;
	}
	
	public static void handle(PacketContext context, PacketByteBuf buffer) {
		ContainerProviderImpl.INSTANCE.openContainer(buffer.readIdentifier(), context.getPlayer(), buf -> {});
	}
}
