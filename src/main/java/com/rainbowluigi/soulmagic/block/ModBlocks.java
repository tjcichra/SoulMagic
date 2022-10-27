package com.rainbowluigi.soulmagic.block;

import java.util.Arrays;
import java.util.Collections;

import com.rainbowluigi.soulmagic.SoulMagic;
import com.rainbowluigi.soulmagic.item.UpgradeableBlockItem;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
	
	public static final Block SOUL_ESSENCE_INFUSER = new SoulEssenceInfuserBlock(FabricBlockSettings.of(Material.STONE).nonOpaque().strength(3.5f, 3.5f));
	public static final BlockItem SOUL_ESSENCE_INFUSER_ITEM = new UpgradeableBlockItem(SOUL_ESSENCE_INFUSER, new Item.Settings().maxCount(1).group(SoulMagic.ITEM_GROUP), Collections.singletonList(ModUpgrades.ENCHANTING_COMPONENT));
	public static final Block SOUL_SEPARATOR = new SoulSeparatorBlock(FabricBlockSettings.of(Material.STONE).strength(3.5f, 3.5f));
	public static final Block UPGRADE_STATION = new UpgradeStationBlock(FabricBlockSettings.of(Material.STONE).strength(3.5f, 3.5f));
	public static final Block SOUL_CACHE = new UpgradeStationBlock(FabricBlockSettings.of(Material.WOOD).strength(3.5f, 3.5f));
	public static final Block SOUL_FLAME = new SoulFlameBlock(FabricBlockSettings.of(Material.DECORATION).noCollision().breakInstantly().luminance(14).sounds(BlockSoundGroup.WOOL));
	public static final Block INFINITE_WELL = new InfiniteWellBlock(FabricBlockSettings.of(Material.STONE));

	public static void registerBlocks() {
		registerBlockAndItem(SOUL_ESSENCE_INFUSER, "soul_essence_infuser", SOUL_ESSENCE_INFUSER_ITEM);
		registerBlockAndItem(SOUL_SEPARATOR, "soul_separator", SoulMagic.ITEM_GROUP);
		registerBlockAndItem(UPGRADE_STATION, "upgrade_station", SoulMagic.ITEM_GROUP);
		registerBlockAndItem(SOUL_CACHE, "soul_cache", SoulMagic.ITEM_GROUP);
		registerBlockAndItem(SOUL_FLAME, "soul_flame", (ItemGroup) null);
		registerBlockAndItem(INFINITE_WELL, "infinite_well", SoulMagic.ITEM_GROUP);
	}
	
	private static void registerBlockAndItem(Block block, String name, ItemGroup group) {
		registerBlockAndItem(block, name, new BlockItem(block, new Item.Settings().group(group)));
	}

	private static void registerBlockAndItem(Block block, String name, BlockItem item) {
		Registry.register(Registry.BLOCK, new Identifier(Reference.MOD_ID, name), block);
		Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, name), item);
	}
}