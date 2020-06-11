package com.rainbowluigi.soulmagic.entity;

import java.util.List;

import com.rainbowluigi.soulmagic.client.SoulMagicClient;
import com.rainbowluigi.soulmagic.network.EntityRenderMessage;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

public class SpiritFlameEntity extends Entity {

	private LivingEntity caster;
	private LivingEntity target;

	public SpiritFlameEntity(EntityType<? extends SpiritFlameEntity> et, World w) {
		super(et, w);
	}

	public SpiritFlameEntity(World world, double x, double y, double z) {
		this(ModEntityTypes.SPIRIT_FLAME, world);
		this.updatePosition(x, y, z);
		this.setVelocity(0, 0, 0);
	}

	public SpiritFlameEntity(World world, double x, double y, double z, LivingEntity caster) {
		this(world, x, y, z);
		this.caster = caster;
	}

	@Override
	public void tick() {
		super.tick();

		//Only run if the world is client
		if(!this.world.isClient) {
			//If it doesn't have an entity to target, get one.
			if(this.target == null) {
				this.target = this.getTargetEntity();
			} else {
				double dx = target.getX() - this.getX();
				double dy = target.getY() - this.getY();
				double dz = target.getZ() - this.getZ();

				double d = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));

				double t = d/1;

				this.setVelocity(dx/t, dy/t, dz/t);

				this.prevX = this.getX();
				this.prevY = this.getY();
				this.prevZ = this.getZ();

				Vec3d v = this.getVelocity();
				double nx = this.getX() + v.x;
				double ny = this.getY() + v.y;
				double nz = this.getZ() + v.z;

				this.updatePosition(nx, ny, nz);

				HitResult collider = ProjectileUtil.getCollision(this, this::hitEntity, RayTraceContext.ShapeType.COLLIDER);
				if (collider.getType() != HitResult.Type.MISS) {
					this.onCollision(collider);
				}
			}
		}
	}

	public LivingEntity getTargetEntity() {
		List<Entity> entities = this.world.getEntities(this.caster, this.getBoundingBox().expand(5));
		entities.removeIf((e) -> !(e instanceof LivingEntity));
		//System.out.println(entities);

		if(entities.size() > 0) {
			return (LivingEntity) entities.get(MathHelper.nextInt(this.random, 0, entities.size() - 1));
		}

		return null;
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

				if (entity instanceof SpiritFlameEntity || entity instanceof PlayerEntity)
					return;
				
				entity.damage(new EntityDamageSource("fire", this.caster), 6);
				this.dealDamage(this.caster, entity);
				this.remove();
			}
		}
		
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return ServerSidePacketRegistry.INSTANCE.toPacket(SoulMagicClient.ENTITY_RENDER, EntityRenderMessage.makePacket(this, this.caster == null ? 0 : this.caster .getEntityId()));
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

	public void setCaster(Entity e2) {
		this.caster = (LivingEntity) e2;
	}
}
