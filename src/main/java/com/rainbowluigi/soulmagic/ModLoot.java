package com.rainbowluigi.soulmagic;

import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetNbtLootFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class ModLoot {

	private static final Identifier SPAWN_BONUS_CHEST = new Identifier("minecraft", "chests/simple_dungeon");
	
	public static void handleLoot() {
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