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
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetNbtLootFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class SoulMagic implements ModInitializer {

	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder
			.build(new Identifier(Reference.MOD_ID, "creative_tab"), () -> new ItemStack(ModItems.SOUL_ESSENCE_STAFF));

	public static final Logger LOGGER = LogManager.getLogger();
	private static final Identifier SPAWN_BONUS_CHEST = new Identifier("minecraft", "chests/simple_dungeon");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModItems.registerItems();
		ModBlocks.registerBlocks();
		ModEnchantments.registerEnchantments();

		ModEntityTypes.registerEntityTypes();

		ModBlockEntity.registerBlockEntityTypes();
		ModContainerFactories.registerContainerTypes();

		ModSoulTypes.registerSoulTypes();
		ModSpellTypes.registerSpellTypes();
		ModSpells.registerSpells();

		ModRecipes.registerRecipeTypes();
		ModRecipes.registerRecipeSerializers();

		ModNetwork.registerPackets();

		ModTabs.registerTabs();

		LootTableLoadingCallback.EVENT.register((resourceManager, manager, id, supplier, setter) -> {
			if(SPAWN_BONUS_CHEST.equals(id)) {
				CompoundTag tag = new CompoundTag();
				CompoundTag soul = new CompoundTag();
				tag.put("souls", soul);
				soul.putInt(ModSoulTypes.SOUL_TYPE.getId(ModSoulTypes.LIGHT).toString(), 200);
				soul.putInt(ModSoulTypes.SOUL_TYPE.getId(ModSoulTypes.DARK).toString(), 200);

				FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder().rolls(UniformLootTableRange.between(0, 1)).withFunction(SetNbtLootFunction.builder(tag).build()).withEntry(ItemEntry.builder(ModItems.SOUL_ESSENCE_ORB).build());

				supplier.withPool(poolBuilder.build());
			}
		});
	}
}
