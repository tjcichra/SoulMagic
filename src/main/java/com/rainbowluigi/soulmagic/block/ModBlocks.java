package com.rainbowluigi.soulmagic.block;

import com.rainbowluigi.soulmagic.item.UpgradeableBlockItem;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.util.Reference;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.Collections;

public class ModBlocks {
	
	public static final Block SOUL_ESSENCE_INFUSER = new SoulEssenceInfuserBlock(FabricBlockSettings.of(Material.STONE).nonOpaque().strength(3.5f, 3.5f));
	public static final BlockItem SOUL_ESSENCE_INFUSER_ITEM = new UpgradeableBlockItem(SOUL_ESSENCE_INFUSER, new Item.Settings().maxCount(1), Collections.singletonList(ModUpgrades.ENCHANTING_COMPONENT));
	public static final Block SOUL_SEPARATOR = new SoulSeparatorBlock(FabricBlockSettings.of(Material.STONE).strength(3.5f, 3.5f));
	public static final Block UPGRADE_STATION = new UpgradeStationBlock(FabricBlockSettings.of(Material.STONE).strength(3.5f, 3.5f));
	public static final Block SOUL_CACHE = new UpgradeStationBlock(FabricBlockSettings.of(Material.WOOD).strength(3.5f, 3.5f));
	public static final Block SOUL_FLAME = new SoulFlameBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(14).sounds(BlockSoundGroup.WOOL));
	public static final Block INFINITE_WELL = new InfiniteWellBlock(FabricBlockSettings.of(Material.STONE));

	public static void registerBlocks() {
		registerBlockAndItem(SOUL_ESSENCE_INFUSER, "soul_essence_infuser", SOUL_ESSENCE_INFUSER_ITEM);
		registerBlockAndItem(SOUL_SEPARATOR, "soul_separator");
		registerBlockAndItem(UPGRADE_STATION, "upgrade_station");
		registerBlockAndItem(SOUL_CACHE, "soul_cache");
		registerBlockAndItem(SOUL_FLAME, "soul_flame");
		registerBlockAndItem(INFINITE_WELL, "infinite_well");
	}
	
	private static void registerBlockAndItem(Block block, String name) {
		registerBlockAndItem(block, name, new BlockItem(block, new Item.Settings()));
	}

	private static void registerBlockAndItem(Block block, String name, BlockItem item) {
		Registry.register(Registries.BLOCK, new Identifier(Reference.MOD_ID, name), block);
		Registry.register(Registries.ITEM, new Identifier(Reference.MOD_ID, name), item);
	}
}