package com.rainbowluigi.soulmagic.entity;

import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntityTypes {

    public static final EntityType<MagicFireballEntity> MAGIC_FIREBALL = FabricEntityTypeBuilder.<MagicFireballEntity>create(EntityCategory.MISC, MagicFireballEntity::new).size(EntityDimensions.fixed(0.75f, 0.75f)).trackable(64, 1, true).build();
    public static final EntityType<BarrageEntity> BARRAGE = FabricEntityTypeBuilder.<BarrageEntity>create(EntityCategory.MISC, BarrageEntity::new).size(EntityDimensions.changing(3, 3)).trackable(64, 1, true).build();
    public static final EntityType<TendrilEntity> TENDRIL = FabricEntityTypeBuilder.<TendrilEntity>create(EntityCategory.MISC, TendrilEntity::new).size(EntityDimensions.changing(1, 1)).trackable(64, 1, true).build();
    public static final EntityType<UniverseRingEntity> UNIVERSE_RING = FabricEntityTypeBuilder.<UniverseRingEntity>create(EntityCategory.MISC, UniverseRingEntity::new).size(EntityDimensions.fixed(1, 1)).trackable(64, 1, true).build();
    public static final EntityType<SoulArrowEntity> SOUL_ARROW_ENTITY = FabricEntityTypeBuilder.<SoulArrowEntity>create(EntityCategory.MISC, SoulArrowEntity::new).size(EntityDimensions.fixed(0.5f, 0.5f)).trackable(4, 20, true).build();
    
    public static void registerEntityTypes() {
    	registerEntityType(MAGIC_FIREBALL, "magic_fireball");
    	registerEntityType(UNIVERSE_RING, "universe_ring");
    	registerEntityType(BARRAGE, "barrage");
    	registerEntityType(TENDRIL, "trendril");
    	registerEntityType(SOUL_ARROW_ENTITY, "soul_arrow");
    }

    private static void registerEntityType(EntityType<?> entityType, String name) {
        Registry.register(Registry.ENTITY_TYPE, new Identifier(Reference.MOD_ID, name), entityType);
    }
}
