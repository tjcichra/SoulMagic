package com.rainbowluigi.soulmagic.soultype;

import com.mojang.serialization.Lifecycle;
import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public class ModSoulTypes {

    public static final Registry<SoulType> SOUL_TYPE = FabricRegistryBuilder.createSimple(SoulType.class, new Identifier(Reference.MOD_ID, "soul_type")).buildAndRegister();

    public static final SoulType LIGHT = new SoulType(0xEEC56A, Formatting.YELLOW);
    public static final SoulType PRIDEFUL = new SoulType(0xFF55FF, Formatting.LIGHT_PURPLE);
    public static final SoulType DARK = new SoulType(0x3D2E4C, Formatting.DARK_GRAY);

    public static void registerSoulTypes() {
    	registerSoulType(LIGHT, "light");
    	registerSoulType(PRIDEFUL, "prideful");
    	registerSoulType(DARK, "dark");
    }

    private static void registerSoulType(SoulType st, String name) {
    	Registry.register(SOUL_TYPE, new Identifier(Reference.MOD_ID, name), st);
    }
}