package com.rainbowluigi.soulmagic.entity;

import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntityTypes {

	public static final EntityType<MagicFireballEntity> MAGIC_FIREBALL = FabricEntityTypeBuilder.<MagicFireballEntity>create(SpawnGroup.MISC, MagicFireballEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).trackable(4, 10, true).build();
	public static final EntityType<BarrageEntity> BARRAGE = FabricEntityTypeBuilder.<BarrageEntity>create(SpawnGroup.MISC, BarrageEntity::new).dimensions(EntityDimensions.changing(3, 3)).trackable(4, 10, true).build();
	public static final EntityType<TendrilEntity> TENDRIL = FabricEntityTypeBuilder.<TendrilEntity>create(SpawnGroup.MISC, TendrilEntity::new).dimensions(EntityDimensions.changing(1, 1)).trackable(4, 10, true).build();
	public static final EntityType<UniverseRingEntity> UNIVERSE_RING = FabricEntityTypeBuilder.<UniverseRingEntity>create(SpawnGroup.MISC, UniverseRingEntity::new).dimensions(EntityDimensions.fixed(1, 1)).trackable(4, 10, true).build();
	public static final EntityType<SoulArrowEntity> SOUL_ARROW_ENTITY = FabricEntityTypeBuilder.<SoulArrowEntity>create(SpawnGroup.MISC, SoulArrowEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).trackable(4, 20, true).build();
	public static final EntityType<SpiritFlameEntity> SPIRIT_FLAME = FabricEntityTypeBuilder.<SpiritFlameEntity>create(SpawnGroup.MISC, SpiritFlameEntity::new).dimensions(EntityDimensions.fixed(1, 1)).trackable(4, 10, true).build();

	public static void registerEntityTypes() {
		registerEntityType(MAGIC_FIREBALL, "magic_fireball");
		registerEntityType(UNIVERSE_RING, "universe_ring");
		registerEntityType(BARRAGE, "barrage");
		registerEntityType(TENDRIL, "trendril");
		registerEntityType(SOUL_ARROW_ENTITY, "soul_arrow");
		registerEntityType(SPIRIT_FLAME, "spirit_flame");
	}

	private static void registerEntityType(EntityType<?> entityType, String name) {
		Registry.register(Registries.ENTITY_TYPE, new Identifier(Reference.MOD_ID, name), entityType);
	}
}
