package com.rainbowluigi.soulmagic.soultype;

import com.rainbowluigi.soulmagic.util.Reference;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ModSoulTypes {

    public static final SimpleRegistry<SoulType> SOUL_TYPE = FabricRegistryBuilder.createSimple(SoulType.class, new Identifier(Reference.MOD_ID, "soul_type")).buildAndRegister();

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