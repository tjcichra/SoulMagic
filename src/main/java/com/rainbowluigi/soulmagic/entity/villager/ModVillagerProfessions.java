package com.rainbowluigi.soulmagic.entity.villager;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.util.Reference;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;

public class ModVillagerProfessions {
	
	public static RegistryKey<PointOfInterestType> SOUL_TINKERE_POI = RegistryKey.of(Registry.POINT_OF_INTEREST_TYPE_KEY, new Identifier(Reference.MOD_ID, "soul_tinkerer_poi"));

	public static VillagerProfession SOUL_TINKERER = VillagerProfessionBuilder.create().id(new Identifier(Reference.MOD_ID, "soul_tinkerer")).workstation(SOUL_TINKERE_POI).build();

	public static void registerPointsOfInterest() {
		registerPointOfInterest(SOUL_TINKERE_POI, ModBlocks.SOUL_ESSENCE_INFUSER, 1, 1);
	}

	private static void registerPointOfInterest(RegistryKey<PointOfInterestType> key, Block block, int ticketCount, int searchDistance) {
		ImmutableSet<BlockState> states = ImmutableSet.copyOf(block.getStateManager().getStates());
		PointOfInterestType pointOfInterestType = new PointOfInterestType(states, ticketCount, searchDistance);

		Registry.register(Registry.POINT_OF_INTEREST_TYPE, key, pointOfInterestType);
	}

	public static void registerVillagerProfessions() {
		registerVillagerProfession(SOUL_TINKERER, "soul_tinkerer");
	}

	private static void registerVillagerProfession(VillagerProfession profession, String name) {
		Registry.register(Registry.VILLAGER_PROFESSION, new Identifier(Reference.MOD_ID, name), profession);
	}
}