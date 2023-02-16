package com.rainbowluigi.soulmagic.util;

import com.google.common.collect.Maps;
import com.rainbowluigi.soulmagic.upgrade.spells.SpellUpgrade;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Map;
import java.util.Map.Entry;

public class SpellCooldownManager {

    private Map<SpellUpgrade, Cooldown> entries = Maps.newHashMap();

    public void setCooldown(SpellUpgrade s, int ticks) {
        this.entries.put(s, new Cooldown(ticks));
    }

    public boolean hasCooldown(SpellUpgrade s) {
        return this.entries.containsKey(s);
    }

    public void update() {
        for (Entry<SpellUpgrade, Cooldown> e : entries.entrySet()) {
            e.getValue().currentTick--;

            if (e.getValue().currentTick <= 0) {
                this.entries.remove(e.getKey());
            }
        }
    }

    public static SpellCooldownManager getSpellCooldownManager(PlayerEntity player) {
        return ((SpellCooldownProvider) player).getSpellCooldownManager();
    }

    public static class Cooldown {
        public int currentTick, totalTick;

        public Cooldown(int totalTick) {
            this.currentTick = totalTick;
            this.totalTick = totalTick;
        }
    }
}