package com.rainbowluigi.soulmagic.stats;

import com.rainbowluigi.soulmagic.util.Reference;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModStats {

    public static final Identifier INTERACT_WITH_UPGRADE_STATION = registerStat("interact_with_upgrade_station");

    public static void registerStats() {
    }

    private static Identifier registerStat(String name) {
        Identifier id = new Identifier(Reference.MOD_ID, name);
        Registry.register(Registry.CUSTOM_STAT, id, id);
        Stats.CUSTOM.getOrCreateStat(id, StatFormatter.DEFAULT);
        return id;
    }
}
