package com.rainbowluigi.soulmagic.upgrade;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public class EnchantmentUpgrade extends Upgrade {

    private Enchantment e;
    private int level;

    public EnchantmentUpgrade(ItemStack icon, int x, int y, Upgrade prev, UpgradeSprite s, Enchantment e, int level) {
        super(icon, x, y, prev, s);
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