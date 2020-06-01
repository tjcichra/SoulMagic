package com.rainbowluigi.soulmagic.soultype;

import com.mojang.serialization.Lifecycle;
import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class ModSoulTypes {

    public static final RegistryKey<Registry<SoulType>> SOUL_TYPE_KEY = RegistryKey.ofRegistry(new Identifier(Reference.MOD_ID, "soul_type"));
    public static final DefaultedRegistry<SoulType> SOUL_TYPE = new DefaultedRegistry<SoulType>("light", SOUL_TYPE_KEY, Lifecycle.experimental());

    public static final SoulType LIGHT = new SoulType(0xEEC56A, Formatting.YELLOW);
    public static final SoulType PRIDEFUL = new SoulType(0xFF55FF, Formatting.LIGHT_PURPLE);
    public static final SoulType DARK = new SoulType(0x3D2E4C, Formatting.DARK_GRAY);

    public static void registerSoulTypes() {
    	//Registry.register(Registry.REGISTRIES, new Identifier(Reference.MOD_ID, "soul_type"), SOUL_TYPE);
    	
    	registerSoulType(LIGHT, "light");
    	registerSoulType(PRIDEFUL, "prideful");
    	registerSoulType(DARK, "dark");
    }

    private static void registerSoulType(SoulType st, String name) {
    	Registry.register(SOUL_TYPE, new Identifier(Reference.MOD_ID, name), st);
    }
}