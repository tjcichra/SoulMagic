package com.rainbowluigi.soulmagic.upgrade;

import net.minecraft.enchantment.Enchantment;

public class EnchantmentUpgrade extends Upgrade {

    private Enchantment e;
    private int level;

    public EnchantmentUpgrade(int x, int y, Upgrade prev, UpgradeSprite icon, UpgradeSprite s, Enchantment e, int level) {
        super(x, y, prev, icon, s);
        this.e = e;
        this.level = level;
    }

    public Enchantment getEnchantment() {
        return this.e;
    }

    public int getLevel() {
        return this.level;
    }
}