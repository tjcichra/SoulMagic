package com.rainbowluigi.soulmagic.loot;

import com.rainbowluigi.soulmagic.item.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModLoot {

	public static final LootFunctionType RANDOM_SOUL_ESSENCE = new LootFunctionType(new RandomSoulEssenceFunction.Serializer());
	
	public static void handleLoot() {
		Registry.register(Registries.LOOT_FUNCTION_TYPE, "random_soul_essence", RANDOM_SOUL_ESSENCE);

		LootTableEvents.MODIFY.register((resourceManager, manager, id, builder, source) -> {
			if(id.equals(LootTables.SIMPLE_DUNGEON_CHEST)) {
				LootPool soulEssenceOrbPool = LootPool.builder().rolls(UniformLootNumberProvider.create(0, 1)).with(ItemEntry.builder(ModItems.SOUL_ESSENCE_ORB)).apply(RandomSoulEssenceFunction.builder(UniformLootNumberProvider.create(100, 200))).build();
				builder.pool(soulEssenceOrbPool);
			}
		});
	}
}