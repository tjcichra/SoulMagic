package com.rainbowluigi.soulmagic.entity;

import com.rainbowluigi.soulmagic.network.EntityRenderMessage;
import com.rainbowluigi.soulmagic.network.ModNetwork;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public class UniverseRingEntity extends Entity {
	
	public PlayerEntity owner;
	
	public UniverseRingEntity(EntityType<? extends UniverseRingEntity> et, World w) {
		super(et, w);
	}
	
	public UniverseRingEntity(World world, double x, double y, double z) {
		this(ModEntityTypes.UNIVERSE_RING, world);
		this.updatePosition(x, y, z);
		this.setVelocity(0, 0, 0);
	}

	public UniverseRingEntity(World world, PlayerEntity owner) {
		this(world, owner.getX(), owner.getY(), owner.getZ());
		this.owner = owner;
	}
	
	@Override
	public void tick() {
		super.tick();
		
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return ServerSidePacketRegistry.INSTANCE.toPacket(ModNetwork.ENTITY_RENDER, EntityRenderMessage.makePacket(this, 0));
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
}
