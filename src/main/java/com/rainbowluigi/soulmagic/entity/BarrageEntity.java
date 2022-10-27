package com.rainbowluigi.soulmagic.entity;

import com.rainbowluigi.soulmagic.network.EntityRenderMessage;
import com.rainbowluigi.soulmagic.network.ModNetwork;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class BarrageEntity extends Entity {
	
	public PlayerEntity owner;
	public Direction d;
	
	public BarrageEntity(EntityType<? extends BarrageEntity> et, World w) {
		super(et, w);
	}
	
	public BarrageEntity(World world, double x, double y, double z) {
		this(ModEntityTypes.BARRAGE, world);
		this.setPosition(x, y, z);
		this.setVelocity(0, 0, 0);
	}

	public BarrageEntity(World world, PlayerEntity owner) {
		this(world, owner.getX(), owner.getY(), owner.getZ());
		this.owner = owner;
		this.d = Direction.fromRotation(owner.getYaw());
	}
	
	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return ServerPlayNetworking.createS2CPacket(ModNetwork.ENTITY_RENDER, EntityRenderMessage.makePacket(this, this.owner != null ? this.owner.getId() : 0));
	}

	@Override
	protected void initDataTracker() {
		
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound tag) {
		this.d = Direction.byId(tag.getInt("direction"));
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound tag) {
		tag.putInt("direction", this.d.getId());
	}

	public void setOwner(Entity owner) {
		this.owner = (PlayerEntity) owner;
		this.d = Direction.fromRotation(owner.getYaw());
	}  
}
