package com.rainbowluigi.soulmagic.entity.villager;

import com.google.common.collect.ImmutableSet;
import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.util.Reference;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class ModVillagerProfessions {

    public static RegistryKey<PointOfInterestType> SOUL_TINKERE_POI = RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, new Identifier(Reference.MOD_ID, "soul_tinkerer_poi"));

    public static VillagerProfession SOUL_TINKERER = VillagerProfessionBuilder.create().id(new Identifier(Reference.MOD_ID, "soul_tinkerer")).workstation(SOUL_TINKERE_POI).build();

    public static void registerPointsOfInterest() {
        registerPointOfInterest(SOUL_TINKERE_POI, ModBlocks.SOUL_ESSENCE_INFUSER, 1, 1);
    }

    private static void registerPointOfInterest(RegistryKey<PointOfInterestType> key, Block block, int ticketCount, int searchDistance) {
        ImmutableSet<BlockState> states = ImmutableSet.copyOf(block.getStateManager().getStates());
        PointOfInterestType pointOfInterestType = new PointOfInterestType(states, ticketCount, searchDistance);

        Registry.register(Registries.POINT_OF_INTEREST_TYPE, key, pointOfInterestType);
    }

    public static void registerVillagerProfessions() {
        registerVillagerProfession(SOUL_TINKERER, "soul_tinkerer");
    }

    private static void registerVillagerProfession(VillagerProfession profession, String name) {
        Registry.register(Registries.VILLAGER_PROFESSION, new Identifier(Reference.MOD_ID, name), profession);
    }
}