package com.rainbowluigi.soulmagic.block;

import com.rainbowluigi.soulmagic.SoulMagic;
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
	public static final Block SOUL_SEPARATOR = new SoulSeparatorBlock(FabricBlockSettings.of(Material.STONE).strength(3.5f, 3.5f));
	public static final Block SOUL_CACHE = new SoulStaffCacheBlock(FabricBlockSettings.of(Material.WOOD).strength(3.5f, 3.5f));
	public static final Block SOUL_FLAME = new SoulFlameBlock(FabricBlockSettings.of(Material.SUPPORTED).noCollision().breakInstantly().lightLevel(14).sounds(BlockSoundGroup.WOOL));
	public static final Block ENCHANTMENT_OBELISK = new Block(FabricBlockSettings.of(Material.STONE));
	public static final Block INFINITE_WELL = new InfiniteWellBlock(FabricBlockSettings.of(Material.STONE));

	public static void registerBlocks() {
		registerBlockAndItem(SOUL_ESSENCE_INFUSER, "soul_essence_infuser", SoulMagic.ITEM_GROUP);
		registerBlockAndItem(SOUL_SEPARATOR, "soul_separator", SoulMagic.ITEM_GROUP);
		registerBlockAndItem(SOUL_CACHE, "soul_cache", SoulMagic.ITEM_GROUP);
		registerBlockAndItem(SOUL_FLAME, "soul_flame", null);
		registerBlockAndItem(ENCHANTMENT_OBELISK, "enchantment_obelisk", SoulMagic.ITEM_GROUP);
		registerBlockAndItem(INFINITE_WELL, "infinite_well", SoulMagic.ITEM_GROUP);
	}
	
	private static void registerBlockAndItem(Block block, String name, ItemGroup group) {
		Registry.register(Registry.BLOCK, new Identifier(Reference.MOD_ID, name), block);
		Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, name), new BlockItem(block, new Item.Settings().group(group)));
	}
}