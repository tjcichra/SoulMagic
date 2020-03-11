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
		//Box b = this.getBoundingBox();
		//b.offset(0, 0.1, 0);
		//this.setBoundingBox(b);
		System.out.println(this.getBoundingBox());
		super.tick();
	}
	
	public void calculateDimensions() {
		Box box_1 = this.getBoundingBox();
		this.setBoundingBox(new Box(box_1.x1, box_1.y1, box_1.z1, box_1.x2, box_1.y2, box_1.z2));
			//if (entityDimensions_2.width > entityDimensions_1.width && !this.firstUpdate && !this.world.isClient) {
			//	float float_1 = entityDimensions_1.width - entityDimensions_2.width;
			///	this.move(MovementType.SELF, new Vec3d((double) float_1, 0.0D, (double) float_1));
			//}
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
}
