package com.rainbowluigi.soulmagic.entity.villager;

import com.google.common.collect.ImmutableMap;
import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.util.Reference;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class ModVillagerProfessions {
	
	public static PointOfInterestType SOUL_TINKERE_POI = PointOfInterestHelper.register(new Identifier(Reference.MOD_ID, "soul_tinkerer_poi"), 1, 1, ModBlocks.SOUL_ESSENCE_INFUSER);

	public static VillagerProfession SOUL_TINKERER = VillagerProfessionBuilder.create().id(new Identifier(Reference.MOD_ID, "soul_tinkerer")).workstation(SOUL_TINKERE_POI).build();

	public static void registerPointsOfInterest() {

	}

	public static void registerVillagerProfessions() {
		registerVillagerProfession(SOUL_TINKERER, "soul_tinkerer");
	}

	private static void registerVillagerProfession(VillagerProfession profession, String name) {
		Registry.register(Registry.VILLAGER_PROFESSION, new Identifier(Reference.MOD_ID, name), profession);
	}
}