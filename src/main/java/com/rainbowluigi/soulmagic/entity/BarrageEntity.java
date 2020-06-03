package com.rainbowluigi.soulmagic.entity;

import com.rainbowluigi.soulmagic.client.SoulMagicClient;
import com.rainbowluigi.soulmagic.network.EntityRenderMessage;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class BarrageEntity extends Entity {
	
	public PlayerEntity owner;
	
	public BarrageEntity(EntityType<? extends BarrageEntity> et, World w) {
		super(et, w);
	}
	
	public BarrageEntity(World world, double x, double y, double z) {
		this(ModEntityTypes.BARRAGE, world);
		this.updatePosition(x, y, z);
		this.setVelocity(0, 0, 0);
	}

	public BarrageEntity(World world, PlayerEntity owner) {
		this(world, owner.getX(), owner.getY(), owner.getZ());
		this.owner = owner;
	}
	
	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return ServerSidePacketRegistry.INSTANCE.toPacket(SoulMagicClient.ENTITY_RENDER, EntityRenderMessage.makePacket(this, 0));
	}

	@Override
	protected void initDataTracker() {
		
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag var1) {
		
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag var1) {
		
	}

	@Override
	public Box getCollisionBox() {
		return this.getBoundingBox();
	}

	@Override
	public Box getHardCollisionBox(Entity collidingEntity) {
		return this.isAlive() && !(collidingEntity instanceof PlayerEntity) ? this.getBoundingBox() : null;
	}  
}
