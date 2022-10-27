package com.rainbowluigi.soulmagic.network;

import com.rainbowluigi.soulmagic.entity.*;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class EntityRenderMessage {
	public static PacketByteBuf makePacket(Entity e, int data) {
		PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
		pbb.writeVarInt(e.getId());
		pbb.writeUuid(e.getUuid());
		pbb.writeVarInt(Registry.ENTITY_TYPE.getRawId(e.getType()));
		pbb.writeDouble(e.getX());
		pbb.writeDouble(e.getY());
		pbb.writeDouble(e.getZ());
		pbb.writeByte(MathHelper.floor(e.getPitch() * 256.0F / 360.0F));
		pbb.writeByte(MathHelper.floor(e.getYaw() * 256.0F / 360.0F));
		pbb.writeInt(data);
		pbb.writeShort((int) (MathHelper.clamp(e.getVelocity().x, -3.9D, 3.9D) * 8000.0D));
		pbb.writeShort((int) (MathHelper.clamp(e.getVelocity().y, -3.9D, 3.9D) * 8000.0D));
		pbb.writeShort((int) (MathHelper.clamp(e.getVelocity().z, -3.9D, 3.9D) * 8000.0D));
		return pbb;
	}

	public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		int id = buf.readVarInt();
		UUID uuid = buf.readUuid();
		EntityType<?> entityTypeId = Registry.ENTITY_TYPE.get(buf.readVarInt());
		double x = buf.readDouble();
		double y = buf.readDouble();
		double z = buf.readDouble();
		int pitch = buf.readByte();
		int yaw = buf.readByte();
		int data = buf.readInt();
		//int velocityX = buffer.readShort();
		//int velocityY = buffer.readShort();
		//int velocityZ = buffer.readShort();

		ClientWorld w = client.world;

		Entity e = null;
		if (entityTypeId == ModEntityTypes.BARRAGE) {
			e = new BarrageEntity(w, x, y, z);
			Entity e2 = w.getEntityById(data);
			if (e2 != null) {
				((BarrageEntity) e).setOwner(e2);
			}
		} else if (entityTypeId == ModEntityTypes.MAGIC_FIREBALL) {
			e = new MagicFireballEntity(w, x, y, z);
		} else if (entityTypeId == ModEntityTypes.UNIVERSE_RING) {
			e = new UniverseRingEntity(w, x, y, z);
		} else if (entityTypeId == ModEntityTypes.TENDRIL) {
			e = new TendrilEntity(w, x, y, z);
		} else if (entityTypeId == ModEntityTypes.SOUL_ARROW_ENTITY) {
			e = new SoulArrowEntity(w, x, y, z);
			Entity e2 = w.getEntityById(data);
			if (e2 != null) {
				((ProjectileEntity) e).setOwner(e2);
			}
		} else if (entityTypeId == ModEntityTypes.SPIRIT_FLAME) {
			e = new SpiritFlameEntity(w, x, y, z);
			Entity e2 = w.getEntityById(data);
			if (e2 != null) {
				((SpiritFlameEntity) e).setCaster((LivingEntity) e2);
			}
		}

		if (e != null) {
			e.updateTrackedPosition(x, y, z);
			e.setId(id);
			e.setUuid(uuid);
			e.setPitch(pitch);
			e.setYaw(yaw);
			w.addEntity(id, e);
		}
	}
}
