package com.rainbowluigi.soulmagic.block.entity;

import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.util.Reference;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntity {
	
	public static final BlockEntityType<SoulEssenceInfuserBlockEntity> SOUL_INFUSER = FabricBlockEntityTypeBuilder.create(SoulEssenceInfuserBlockEntity::new, ModBlocks.SOUL_ESSENCE_INFUSER).build(null);
	public static final BlockEntityType<SoulSeparatorBlockEntity> SOUL_SEPARATOR = FabricBlockEntityTypeBuilder.create(SoulSeparatorBlockEntity::new, ModBlocks.SOUL_SEPARATOR).build(null);
	public static final BlockEntityType<SoulCacheBlockEntity> SOUL_CACHE = FabricBlockEntityTypeBuilder.create(SoulCacheBlockEntity::new, ModBlocks.SOUL_CACHE).build(null);

	public static void registerBlockEntityTypes() {
		registerBlockEntityType(SOUL_INFUSER, "soul_infuser");
		registerBlockEntityType(SOUL_SEPARATOR, "soul_separator");
		registerBlockEntityType(SOUL_CACHE, "soul_cache");
	}

	private static void registerBlockEntityType(BlockEntityType<?> blockEntityType, String name) {
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Reference.MOD_ID, name), blockEntityType);
	}
}