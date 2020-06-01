package com.rainbowluigi.soulmagic.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;

public class ItemVacuumMessage {

	public static PacketByteBuf makePacket(ItemEntity e, double x, double y, double z) {
		PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
		pbb.writeDouble(x);
		pbb.writeDouble(y);
		pbb.writeDouble(z);
		pbb.writeInt(e.getEntityId());
		return pbb;
	}
	
	public static void handle(PacketContext context, PacketByteBuf buffer) {
		double x = buffer.readDouble();
		double y = buffer.readDouble();
		double z = buffer.readDouble();
		
		int id = buffer.readInt();
		
		World world = context.getPlayer().world;
		
		ItemEntity e = (ItemEntity) world.getEntityById(id);
		
		e.setVelocity(x, y, z);
		//e.getVelocity().multiply(1, 0, 1);
	}
}
