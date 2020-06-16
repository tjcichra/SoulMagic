package com.rainbowluigi.soulmagic;

import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.block.entity.ModBlockEntity;
import com.rainbowluigi.soulmagic.enchantment.ModEnchantments;
import com.rainbowluigi.soulmagic.entity.ModEntityTypes;
import com.rainbowluigi.soulmagic.inventory.ModContainerFactories;
import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.item.crafting.ModRecipes;
import com.rainbowluigi.soulmagic.network.ModNetwork;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.spell.ModSpells;
import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;
import com.rainbowluigi.soulmagic.tabs.ModTabs;
import com.rainbowluigi.soulmagic.util.Reference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SoulMagic implements ModInitializer {

	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(Reference.MOD_ID, "creative_tab"), () -> new ItemStack(ModItems.SOUL_ESSENCE_STAFF));

	public static final Logger LOGGER = LogManager.getLogger();
	
	@Override
	public void onInitialize() {
		ModItems.registerItems();
		ModBlocks.registerBlocks();
		ModEnchantments.registerEnchantments();
		ModEntityTypes.registerEntityTypes();
		ModBlockEntity.registerBlockEntityTypes();
		ModRecipes.registerRecipeTypes();
		ModRecipes.registerRecipeSerializers();

		ModSoulTypes.registerSoulTypes();
		ModSpellTypes.registerSpellTypes();
		ModSpells.registerSpells();

		ModContainerFactories.registerContainerTypes();
		ModNetwork.registerPackets();
		ModTabs.registerTabs();
		ModLoot.handleLoot();
	}
}
