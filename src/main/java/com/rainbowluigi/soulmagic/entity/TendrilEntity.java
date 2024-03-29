package com.rainbowluigi.soulmagic.entity;

import com.rainbowluigi.soulmagic.network.EntityRenderMessage;
import com.rainbowluigi.soulmagic.network.ModNetwork;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TendrilEntity extends Entity {

	public PlayerEntity owner;
	private boolean grapping;

	public TendrilEntity(EntityType<? extends TendrilEntity> et, World w) {
		super(et, w);
	}

	public TendrilEntity(World world, double x, double y, double z) {
		this(ModEntityTypes.TENDRIL, world);
		this.setPosition(x, y, z);
		this.setVelocity(0, 0, 0);
	}

	public TendrilEntity(World world, PlayerEntity owner) {
		this(world, owner.getX(), owner.getY(), owner.getZ());
		this.owner = owner;
	}

	@Override
	public void tick() {
		super.tick();

		

		if(!grapping) {
			this.prevX = this.getX();
			this.prevY = this.getY();
			this.prevZ = this.getZ();

			Vec3d v = this.getVelocity();
			double d = this.getX() + v.x;
			double e = this.getY() + v.y;
			double f = this.getZ() + v.z;

			this.setPosition(d, e, f);
		}

		HitResult collider = ProjectileUtil.getCollision(this, this::hitEntity);
		if (collider.getType() != HitResult.Type.MISS) {
			this.onCollision(collider);
		}
	}

	protected boolean hitEntity(Entity entity) {
		if (!entity.isSpectator() && entity.isAlive() && entity.collides()) {
			return true;
		}
		return false;
	}

	protected void onCollision(HitResult result) {
		if (!this.world.isClient) {
			if (result.getType() == HitResult.Type.ENTITY) {
				Entity entity = ((EntityHitResult) result).getEntity();

				if (entity instanceof TendrilEntity || entity instanceof PlayerEntity)
					return;
				System.out.println("ds");
				this.grapping = true;
				entity.setVelocity(0, 0, 0);
			}
		}
		
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return ServerPlayNetworking.createS2CPacket(ModNetwork.ENTITY_RENDER, EntityRenderMessage.makePacket(this, 0));
	}

	@Override
	protected void initDataTracker() {
		
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound var1) {
		
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound var1) {
		
	}
}
