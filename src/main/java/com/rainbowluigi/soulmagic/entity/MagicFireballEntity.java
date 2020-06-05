package com.rainbowluigi.soulmagic.entity;

import java.util.stream.Stream;

import com.rainbowluigi.soulmagic.client.SoulMagicClient;
import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.network.EntityRenderMessage;
import com.rainbowluigi.soulmagic.util.ItemHelper;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.collection.ReusableStream;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

public class MagicFireballEntity extends Entity {
	
	private PlayerEntity entity;
	public int ticks;
	public int size;
	private double prevVY;
	
	private float nextStepDistance;
	private float aerialStepDelta;
	private int fireTime;
	
	public MagicFireballEntity(EntityType<? extends MagicFireballEntity> et, World w) {
		super(et, w);
	}
	
	public MagicFireballEntity(World world, double x, double y, double z) {
		this(ModEntityTypes.MAGIC_FIREBALL, world);
		this.updatePosition(x, y, z);
		this.setVelocity(0, 0, 0);
	}

	public MagicFireballEntity(World world, PlayerEntity entity) {
		this(world, entity.getX(), entity.getY() + entity.getStandingEyeHeight() - 0.10000000149011612D, entity.getZ());
		this.entity = entity;
	}
	
	@Override
	public boolean collides() {
		return true;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		this.prevX = this.getX();
		this.prevY = this.getY();
		this.prevZ = this.getZ();
		
		this.prevVY = this.getVelocity().y;
		
		++this.ticks;
		HitResult hitResult_1 = ProjectileUtil.getCollision(this, this::method_26958, RayTraceContext.ShapeType.COLLIDER);
        if (hitResult_1.getType() != HitResult.Type.MISS) {
           this.onCollision(hitResult_1);
        }
        
		Vec3d vec3d_1 = this.getVelocity();
		if (this.isTouchingWater()) {
			if(this.ticks < 100) {
				this.ticks = 100;
			}
		} else if (!this.hasNoGravity()) {
			this.setVelocity(this.getVelocity().add(0.0D, -0.04D, 0.0D));
		}

		if (this.world.isClient) {
			this.noClip = false;
		} else {
			this.noClip = !this.world.doesNotCollide(this);
		}

		if (!this.onGround || squaredHorizontalLength(this.getVelocity()) > 9.999999747378752E-6D) {
			this.move(MovementType.SELF, this.getVelocity());
			//float float_1 = 0.98F;

			//this.setVelocity(this.getVelocity().multiply((double) float_1, 0.98D, (double) float_1));
			if (this.onGround) {
				this.onGround = false;
				this.setVelocity(this.getVelocity().x, -this.prevVY + 0.03, this.getVelocity().z);
				
				//this.setVelocity(this.getVelocity().multiply(1.0D, -1D, 1.0D));
			}
		}

		//CHANGE THIS IF NESSCESSARY
		//this.velocityDirty |= this.checkWaterState();
		if (!this.world.isClient) {
			double double_1 = this.getVelocity().subtract(vec3d_1).lengthSquared();
			if (double_1 > 0.01D) {
				this.velocityDirty = true;
			}
		}
		
		if(this.ticks >= 120) {
			this.remove();
		}
		if(this.isTouchingWater()) {
			this.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1, 1);
			this.world.addParticle(ParticleTypes.SMOKE, MathHelper.nextDouble(this.random, 0, 0.75) + this.getX() - 0.25, MathHelper.nextDouble(this.random, 0, 0.75) + this.getY() - 0.25, MathHelper.nextDouble(this.random, 0, 0.75) + this.getZ() - 0.25, 0.0D, 0.0D, 0.0D);
		}
	}

	protected boolean method_26958(Entity entity) {
		if (!entity.isSpectator() && entity.isAlive() && entity.collides()) {
			return true;
		}
		return false;
	}
	
	protected void fall(double double_1, boolean boolean_1, BlockState blockState_1, BlockPos blockPos_1) {
		if (boolean_1) {
			if (this.fallDistance > 0.0F) {
				blockState_1.getBlock().onLandedUpon(this.world, blockPos_1, this, this.fallDistance);
				this.getVelocity().multiply(1, -1, 1);
				
			}
			this.fallDistance = 0.0F;
		} else if (double_1 < 0.0D) {
			this.fallDistance = (float) ((double) this.fallDistance - double_1);
		}

	}
	
	protected void onCollision(HitResult result) {
		if (!this.world.isClient) {
			if (result.getType() == HitResult.Type.ENTITY) {
				Entity entity = ((EntityHitResult) result).getEntity();
				
				if(entity instanceof MagicFireballEntity || entity instanceof PlayerEntity && ItemHelper.findAccessory((PlayerEntity) entity, ModItems.RING_OF_RECKLESSNESS) != null)
					return;
				
				entity.damage(new EntityDamageSource("fire", this.entity), 6);
				entity.setOnFireFor(3);
				this.dealDamage(this.entity, entity);
				this.remove();
			}
		}
		
	}

	@Override
	protected void initDataTracker() {
		
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return ServerSidePacketRegistry.INSTANCE.toPacket(SoulMagicClient.ENTITY_RENDER, EntityRenderMessage.makePacket(this, 0));
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag var1) {
		
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag var1) {
		
	}
	
	public void move(MovementType movementType_1, Vec3d vec3d_1) {
		if (this.noClip) {
			this.setBoundingBox(this.getBoundingBox().offset(vec3d_1));
			this.moveToBoundingBoxCenter();
		} else {
			if (movementType_1 == MovementType.PISTON) {
				vec3d_1 = this.adjustMovementForPiston(vec3d_1);
				if (vec3d_1.equals(Vec3d.ZERO)) {
					return;
				}
			}

			this.world.getProfiler().push("move");
			if (this.movementMultiplier.lengthSquared() > 1.0E-7D) {
				vec3d_1 = vec3d_1.multiply(this.movementMultiplier);
				this.movementMultiplier = Vec3d.ZERO;
				this.setVelocity(Vec3d.ZERO);
			}

			vec3d_1 = this.adjustMovementForSneaking(vec3d_1, movementType_1);
			Vec3d vec3d_2 = this.adjustMovementForCollisions(vec3d_1);
			if (vec3d_2.lengthSquared() > 1.0E-7D) {
				this.setBoundingBox(this.getBoundingBox().offset(vec3d_2));
				this.moveToBoundingBoxCenter();
			}

			this.world.getProfiler().pop();
			this.world.getProfiler().push("rest");
			this.horizontalCollision = !MathHelper.approximatelyEquals(vec3d_1.x, vec3d_2.x) || !MathHelper.approximatelyEquals(vec3d_1.z, vec3d_2.z);
			this.verticalCollision = vec3d_1.y != vec3d_2.y;
			this.onGround = this.verticalCollision && vec3d_1.y < 0.0D;
			//this.collided = this.horizontalCollision || this.verticalCollision;
			int int_1 = MathHelper.floor(this.getX());
			int int_2 = MathHelper.floor(this.getY() - 0.20000000298023224D);
			int int_3 = MathHelper.floor(this.getZ());
			BlockPos blockPos_1 = new BlockPos(int_1, int_2, int_3);
			BlockState blockState_1 = this.world.getBlockState(blockPos_1);
			if (blockState_1.isAir()) {
				BlockPos blockPos_2 = blockPos_1.down(1);
				BlockState blockState_2 = this.world.getBlockState(blockPos_2);
				Block block_1 = blockState_2.getBlock();
				if (block_1.isIn(BlockTags.FENCES) || block_1.isIn(BlockTags.WALLS)
						|| block_1 instanceof FenceGateBlock) {
					blockState_1 = blockState_2;
					blockPos_1 = blockPos_2;
				}
			}

			this.fall(vec3d_2.y, this.onGround, blockState_1, blockPos_1);
			Vec3d vec3d_3 = this.getVelocity();
			if (vec3d_1.x != vec3d_2.x) {
				this.setVelocity(-vec3d_3.x, vec3d_3.y, vec3d_3.z);
			}

			if (vec3d_1.z != vec3d_2.z) {
				this.setVelocity(vec3d_3.x, vec3d_3.y, -vec3d_3.z);
			}

			Block block_2 = blockState_1.getBlock();
			if (vec3d_1.y != vec3d_2.y) {
				block_2.onEntityLand(this.world, this);
			}

			if (this.onGround && !this.bypassesSteppingEffects()) {
				block_2.onSteppedOn(this.world, blockPos_1, this);
			}

			if (this.canClimb() && !this.hasVehicle()) {
				double double_1 = vec3d_2.x;
				double double_2 = vec3d_2.y;
				double double_3 = vec3d_2.z;
				if (block_2 != Blocks.LADDER && block_2 != Blocks.SCAFFOLDING) {
					double_2 = 0.0D;
				}

				this.horizontalSpeed = (float) ((double) this.horizontalSpeed
						+ (double) MathHelper.sqrt(squaredHorizontalLength(vec3d_2)) * 0.6D);
				this.distanceTraveled = (float) ((double) this.distanceTraveled
						+ (double) MathHelper.sqrt(double_1 * double_1 + double_2 * double_2 + double_3 * double_3)
								* 0.6D);
				if (this.distanceTraveled > this.nextStepDistance && !blockState_1.isAir()) {
					this.nextStepDistance = this.calculateNextStepSoundDistance();
					if (this.isTouchingWater()) {
						Entity entity_1 = this.hasPassengers() && this.getPrimaryPassenger() != null
								? this.getPrimaryPassenger()
								: this;
						float float_1 = entity_1 == this ? 0.35F : 0.4F;
						Vec3d vec3d_4 = entity_1.getVelocity();
						float float_2 = MathHelper.sqrt(vec3d_4.x * vec3d_4.x * 0.20000000298023224D
								+ vec3d_4.y * vec3d_4.y + vec3d_4.z * vec3d_4.z * 0.20000000298023224D) * float_1;
						if (float_2 > 1.0F) {
							float_2 = 1.0F;
						}

						this.playSwimSound(float_2);
					} else {
						this.playStepSound(blockPos_1, blockState_1);
					}
				} else if (this.distanceTraveled > this.aerialStepDelta && this.hasWings() && blockState_1.isAir()) {
					this.aerialStepDelta = this.playFlySound(this.distanceTraveled);
				}
			}

			try {
				this.inLava = false;
				this.checkBlockCollision();
			} catch (Throwable var21) {
				CrashReport crashReport_1 = CrashReport.create(var21, "Checking entity block collision");
				CrashReportSection crashReportSection_1 = crashReport_1
						.addElement("Entity being checked for collision");
				this.populateCrashReport(crashReportSection_1);
				throw new CrashException(crashReport_1);
			}

			boolean boolean_1 = this.isTouchingWater();
			if (this.world.doesAreaContainFireSource(this.getBoundingBox().contract(0.001D))) {
				if (!boolean_1) {
					++this.fireTime;
					if (this.fireTime == 0) {
						this.setOnFireFor(8);
					}
				}

				this.setFireTicks(1);
			} else if (this.fireTime <= 0) {
				this.fireTime = -this.getBurningDuration();
			}

			if (boolean_1 && this.isOnFire()) {
				this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.7F,
						1.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
				this.fireTime = -this.getBurningDuration();
			}

			this.world.getProfiler().pop();
		}
	}
	
	private Vec3d adjustMovementForCollisions(Vec3d vec3d_1) {
	      Box box_1 = this.getBoundingBox();
	      ShapeContext entityContext_1 = ShapeContext.of(this);
	      VoxelShape voxelShape_1 = this.world.getWorldBorder().asVoxelShape();
	      Stream<VoxelShape> stream_1 = VoxelShapes.matchesAnywhere(voxelShape_1, VoxelShapes.cuboid(box_1.contract(1.0E-7D)), BooleanBiFunction.AND) ? Stream.empty() : Stream.of(voxelShape_1);
	      Stream<VoxelShape> stream_2 = this.world.getEntityCollisions(this, box_1.stretch(vec3d_1), (entity_1) -> {
			return true;
		 });
	      ReusableStream<VoxelShape> reusableStream_1 = new ReusableStream<VoxelShape>(Stream.concat(stream_2, stream_1));
	      Vec3d vec3d_2 = vec3d_1.lengthSquared() == 0.0D ? vec3d_1 : adjustMovementForCollisions(this, vec3d_1, box_1, this.world, entityContext_1, reusableStream_1);
	      boolean boolean_1 = vec3d_1.x != vec3d_2.x;
	      boolean boolean_2 = vec3d_1.y != vec3d_2.y;
	      boolean boolean_3 = vec3d_1.z != vec3d_2.z;
	      boolean boolean_4 = this.onGround || boolean_2 && vec3d_1.y < 0.0D;
	      if (this.stepHeight > 0.0F && boolean_4 && (boolean_1 || boolean_3)) {
	         Vec3d vec3d_3 = adjustMovementForCollisions(this, new Vec3d(vec3d_1.x, (double)this.stepHeight, vec3d_1.z), box_1, this.world, entityContext_1, reusableStream_1);
	         Vec3d vec3d_4 = adjustMovementForCollisions(this, new Vec3d(0.0D, (double)this.stepHeight, 0.0D), box_1.stretch(vec3d_1.x, 0.0D, vec3d_1.z), this.world, entityContext_1, reusableStream_1);
	         if (vec3d_4.y < (double)this.stepHeight) {
	            Vec3d vec3d_5 = adjustMovementForCollisions(this, new Vec3d(vec3d_1.x, 0.0D, vec3d_1.z), box_1.offset(vec3d_4), this.world, entityContext_1, reusableStream_1).add(vec3d_4);
	            if (squaredHorizontalLength(vec3d_5) > squaredHorizontalLength(vec3d_3)) {
	               vec3d_3 = vec3d_5;
	            }
	         }

	         if (squaredHorizontalLength(vec3d_3) > squaredHorizontalLength(vec3d_2)) {
	            return vec3d_3.add(adjustMovementForCollisions(this, new Vec3d(0.0D, -vec3d_3.y + vec3d_1.y, 0.0D), box_1.offset(vec3d_3), this.world, entityContext_1, reusableStream_1));
	         }
	      }

	      return vec3d_2;
	   }
	
	public void setVelocity(double double_1, double double_2, double double_3, float float_1, float float_2) {
	      Vec3d vec3d_1 = (new Vec3d(double_1, double_2, double_3)).normalize()/*.add(this.random.nextGaussian() * 0.007499999832361937D * (double)float_2, this.random.nextGaussian() * 0.007499999832361937D * (double)float_2, this.random.nextGaussian() * 0.007499999832361937D * (double)float_2)*/.multiply((double)float_1);
	      this.setVelocity(vec3d_1);
	   }
}
