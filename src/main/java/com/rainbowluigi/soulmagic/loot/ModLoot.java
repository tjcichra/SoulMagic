package com.rainbowluigi.soulmagic.loot;

import com.rainbowluigi.soulmagic.item.ModItems;

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModLoot {

	private static final Identifier SPAWN_BONUS_CHEST = new Identifier("minecraft", "chests/simple_dungeon");
	public static final LootFunctionType RANDOM_SOUL_ESSENCE = new LootFunctionType(new RandomSoulEssenceFunction.Serializer());
	
	public static void handleLoot() {
		Registry.register(Registry.LOOT_FUNCTION_TYPE, "random_soul_essence", RANDOM_SOUL_ESSENCE);

		LootTableLoadingCallback.EVENT.register((resourceManager, manager, id, supplier, setter) -> {
			if(SPAWN_BONUS_CHEST.equals(id)) {
				FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder().rolls(UniformLootTableRange.between(0, 1)).withFunction(RandomSoulEssenceFunction.builder(UniformLootTableRange.between(100, 200)).build()).withEntry(ItemEntry.builder(ModItems.SOUL_ESSENCE_ORB).build());
				supplier.withPool(poolBuilder.build());
			}
		});
	}
}