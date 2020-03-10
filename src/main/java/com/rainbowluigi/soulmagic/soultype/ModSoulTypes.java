package com.rainbowluigi.soulmagic.soultype;

import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;

public class ModSoulTypes {
    
    public static final DefaultedRegistry<SoulType> SOUL_TYPE_REG = new DefaultedRegistry<>("unknown");

    public static final SoulType LIGHT = new SoulType(0xEEC56A, Formatting.YELLOW);
    public static final SoulType PRIDEFUL = new SoulType(0xFF55FF, Formatting.LIGHT_PURPLE);
    public static final SoulType DARK = new SoulType(0x3D2E4C, Formatting.DARK_GRAY);

    public static void registerSoulTypes() {
    	Registry.register(Registry.REGISTRIES, new Identifier(Reference.MOD_ID, "soul_type"), SOUL_TYPE_REG);
    	
    	registerSoulType(LIGHT, "light");
    	registerSoulType(PRIDEFUL, "prideful");
    	registerSoulType(DARK, "dark");
    }

    private static void registerSoulType(SoulType st, String name) {
    	Registry.register(SOUL_TYPE_REG, new Identifier(Reference.MOD_ID, name), st);
    }
}