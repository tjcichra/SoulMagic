package com.rainbowluigi.soulmagic.entity;

import java.util.Iterator;

import com.rainbowluigi.soulmagic.client.SoulMagicClient;
import com.rainbowluigi.soulmagic.network.EntityRenderMessage;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SoulArrowEntity extends ArrowEntity {
	
	private static final TrackedData<Integer> TYPE;
	
	public SoulArrowEntity(EntityType<? extends SoulArrowEntity> entityType_1, World world) {
		super(entityType_1, world);
	}
	
	public SoulArrowEntity(World world, double x, double y, double z) {
		this(ModEntityTypes.SOUL_ARROW_ENTITY, world);
		this.updatePosition(x, y, z);
	}

	public SoulArrowEntity(World world, LivingEntity entity) {
		this(world, entity.getX(), entity.getEyeY() - 0.10000000149011612D, entity.getZ());
		this.setOwner(entity);
	}
	
	public void setSoulType(SoulType type) {
		this.dataTracker.set(TYPE, ModSoulTypes.SOUL_TYPE_REG.getRawId(type));
		
		SoulType st = this.getSoulType();
		if(st == ModSoulTypes.DARK) {
			this.setDamage(3);
		} else if(st == ModSoulTypes.DARK) {
			this.setDamage(4);
		} else if(st == ModSoulTypes.PRIDEFUL) {
			this.setDamage(6);
		}
	}
	
	protected void onHit(LivingEntity target) {
		super.onHit(target);
		if(this.getSoulType() == ModSoulTypes.LIGHT) {
			target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 300, 1, false, true));
		} else if(this.getSoulType() == ModSoulTypes.PRIDEFUL) {
			target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 300, 1, false, true));
			target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 300, 1, false, true));
			target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 300, 1, false, true));
		}
	}
	
	public SoulType getSoulType() {
		if(this.dataTracker.get(TYPE) != -1)
			return ModSoulTypes.SOUL_TYPE_REG.get(this.dataTracker.get(TYPE));
		return null;
	}
	
	@Override
	public Packet<?> createSpawnPacket() {
		Entity owner = this.getOwner();
		return ServerSidePacketRegistry.INSTANCE.toPacket(SoulMagicClient.ENTITY_RENDER, EntityRenderMessage.makePacket(this,  owner == null ? 0 : owner.getEntityId()));
	}
	
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(TYPE, -1);
	}
	
	static {
		TYPE = DataTracker.registerData(SoulArrowEntity.class, TrackedDataHandlerRegistry.INTEGER);
	}
}
