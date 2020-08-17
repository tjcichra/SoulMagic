package com.rainbowluigi.soulmagic.upgrade;

import java.util.Map;

import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.minecraft.enchantment.Enchantment;

public class EnchantmentUpgrade extends Upgrade {

    private Enchantment e;
    private int level;
    private Map<SoulType, Integer> se;

    public EnchantmentUpgrade(int x, int y, Upgrade prev, UpgradeSprite icon, UpgradeSprite s, Enchantment e, int level, Map<SoulType, Integer> se) {
        super(x, y, prev, icon, s);
        this.e = e;
        this.level = level;
        this.se = se;
    }

    public Enchantment getEnchantment() {
        return this.e;
    }

    public int getLevel() {
        return this.level;
    }

    public Map<SoulType, Integer> getSoulEssenceRequirement() {
        return this.se;
    }
}