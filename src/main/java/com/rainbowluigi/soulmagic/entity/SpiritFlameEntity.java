package com.rainbowluigi.soulmagic.entity;

import java.util.List;
import java.util.UUID;

import com.rainbowluigi.soulmagic.network.EntityRenderMessage;
import com.rainbowluigi.soulmagic.network.ModNetwork;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SpiritFlameEntity extends Entity {

	private UUID casterUUID;
	private UUID targetUUID;

	public SpiritFlameEntity(EntityType<? extends SpiritFlameEntity> et, World w) {
		super(et, w);
	}

	public SpiritFlameEntity(World world, double x, double y, double z) {
		this(ModEntityTypes.SPIRIT_FLAME, world);
		this.setPosition(x, y, z);
		this.setVelocity(0, 0, 0);
	}

	@Override
	public void tick() {
		super.tick();

		//Only run if the world is client
		if(!this.world.isClient) {
			Entity target = this.getTarget();

			if(target != null) {
				if(target.isAlive()) {
					double dx = target.getX() - this.getX();
					double dy = target.getY() - this.getY();
					double dz = target.getZ() - this.getZ();

					double d = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));

					double t = d/1;

					this.setVelocity(dx/t, dy/t, dz/t);
				}

				this.prevX = this.getX();
				this.prevY = this.getY();
				this.prevZ = this.getZ();

				Vec3d v = this.getVelocity();
				double nx = this.getX() + v.x;
				double ny = this.getY() + v.y;
				double nz = this.getZ() + v.z;

				this.setPosition(nx, ny, nz);

				HitResult collider = ProjectileUtil.getCollision(this, this::hitEntity);
				if (collider.getType() != HitResult.Type.MISS) {
					this.onCollision(collider);
				}
				
			} else if(this.age >= 100) {
				this.remove(RemovalReason.KILLED);
			}
		}
	}

	@Nullable
	//Gets the entity that casted the flame
	public Entity getCaster() {
		if (this.casterUUID != null && this.world instanceof ServerWorld) {
			return ((ServerWorld)this.world).getEntity(this.casterUUID);
		}
		//If there is no entity, return null
		return null;
	}

	//Sets the entity that casted the flame
	public void setCaster(LivingEntity caster) {
		//Converts from caster's UUID to its entity in the world
		this.casterUUID = caster.getUuid();
	}

	@Nullable
	//Gets the entity the flame is targeting
	public Entity getTarget() {
		//If the flame is not targeting an entity
		if(this.targetUUID == null) {
			//Get a list of entities around it excluding the caster and non-living entities
			List<Entity> entities = this.world.getOtherEntities(this.getCaster(), this.getBoundingBox().expand(10));
			entities.removeIf((e) -> !(e instanceof LivingEntity) && e.isAlive());

			//If there are entities around it
			if(entities.size() > 0) {
				//Get one of those entities and set it as the flame's target
				Entity target = entities.get(MathHelper.nextInt(this.random, 0, entities.size() - 1));
				this.targetUUID = target.getUuid();
				//Return the target
				return target;
			}

			//If there are not, return null
			return null;
		}
		//If it is, get the target
		return this.getTargetFromUUID();
	}

	@Nullable
	//Gets the target from the target's UUID
	public Entity getTargetFromUUID() {
		if (this.targetUUID != null && this.world instanceof ServerWorld) {
			return ((ServerWorld)this.world).getEntity(this.targetUUID);
		}
		//If there is no entity, return null
		return null;
	}

	//Sets the entity the flame is targeting
	public void setTarget(LivingEntity target) {
		//Converts from caster's UUID to its entity in the world
		this.targetUUID = target.getUuid();
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
				
				entity.damage(new EntityDamageSource("fire", this.getCaster()), 6);
				this.applyDamageEffects((LivingEntity) this.getCaster(), entity);
				this.remove(RemovalReason.KILLED);
			}
		}
		
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return ServerPlayNetworking.createS2CPacket(ModNetwork.ENTITY_RENDER, EntityRenderMessage.makePacket(this, this.getCaster() == null ? 0 : this.getCaster().getId()));
	}

	@Override
	protected void initDataTracker() {

	}

	@Override
	//Reads the caster's and target's UUID from NBT
	protected void readCustomDataFromNbt(NbtCompound tag) {
		this.casterUUID = tag.getUuid("caster");

		if(tag.contains("target")) {
			this.targetUUID = tag.getUuid("target");
		}
	}

	@Override
	//Writes the caster's and target's UUID to NBT
	protected void writeCustomDataToNbt(NbtCompound tag) {
		tag.putUuid("caster", this.casterUUID);

		if(this.targetUUID != null) {
			tag.putUuid("target", this.targetUUID);
		}
	}
}
