package com.rainbowluigi.soulmagic.network;

import java.util.UUID;

import com.rainbowluigi.soulmagic.entity.BarrageEntity;
import com.rainbowluigi.soulmagic.entity.MagicFireballEntity;
import com.rainbowluigi.soulmagic.entity.ModEntityTypes;
import com.rainbowluigi.soulmagic.entity.SoulArrowEntity;
import com.rainbowluigi.soulmagic.entity.TendrilEntity;
import com.rainbowluigi.soulmagic.entity.UniverseRingEntity;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

public class EntityRenderMessage {
	public static PacketByteBuf makePacket(Entity e, int data) {
		PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
		pbb.writeVarInt(e.getEntityId());
		pbb.writeUuid(e.getUuid());
		pbb.writeVarInt(Registry.ENTITY_TYPE.getRawId(e.getType()));
		pbb.writeDouble(e.getX());
		pbb.writeDouble(e.getY());
		pbb.writeDouble(e.getZ());
		pbb.writeByte(MathHelper.floor(e.pitch * 256.0F / 360.0F));
		pbb.writeByte(MathHelper.floor(e.yaw * 256.0F / 360.0F));
		pbb.writeInt(data);
		pbb.writeShort((int)(MathHelper.clamp(e.getVelocity().x, -3.9D, 3.9D) * 8000.0D));
		pbb.writeShort((int)(MathHelper.clamp(e.getVelocity().y, -3.9D, 3.9D) * 8000.0D));
		pbb.writeShort((int)(MathHelper.clamp(e.getVelocity().z, -3.9D, 3.9D) * 8000.0D));
		return pbb;
	}
	
	public static void handle(PacketContext context, PacketByteBuf buffer) {
		
		ClientWorld w = (ClientWorld) context.getPlayer().world;
		int id = buffer.readVarInt();
		UUID uuid = buffer.readUuid();
		EntityType<?> entityTypeId = Registry.ENTITY_TYPE.get(buffer.readVarInt());
		double x = buffer.readDouble();
		double y = buffer.readDouble();
		double z = buffer.readDouble();
		int pitch = buffer.readByte();
		int yaw = buffer.readByte();
		int data = buffer.readInt();
		int velocityX = buffer.readShort();
		int velocityY = buffer.readShort();
		int velocityZ = buffer.readShort();
		
		context.getTaskQueue().execute(() -> {
			Entity e = null;
			if(entityTypeId == ModEntityTypes.BARRAGE) {
				e = new BarrageEntity(w, x, y, z);
			} else if(entityTypeId == ModEntityTypes.MAGIC_FIREBALL) {
				e = new MagicFireballEntity(w, x, y, z);
			} else if(entityTypeId == ModEntityTypes.UNIVERSE_RING) {
				e = new UniverseRingEntity(w, x, y, z);
			} else if(entityTypeId == ModEntityTypes.TENDRIL) {
				e = new TendrilEntity(w, x, y, z);
			} else if(entityTypeId == ModEntityTypes.SOUL_ARROW_ENTITY) {
				e = new SoulArrowEntity(w, x, y, z);
				Entity e2 = w.getEntityById(data);
	            if (e2 != null) {
	               ((ProjectileEntity)e).setOwner(e2);
	            }
			}
			
			if (e != null) {
				e.updateTrackedPosition(x, y, z);
				e.setEntityId(id);
				e.setUuid(uuid);
				e.pitch = pitch;
				e.yaw = yaw;
				w.addEntity(id, e);
			}
			//packetContext.getPlayer().world.spawnEntity(new MagicFireballEntity(packetContext.getPlayer().world,packetContext.getPlayer()));
		});
	}
}
