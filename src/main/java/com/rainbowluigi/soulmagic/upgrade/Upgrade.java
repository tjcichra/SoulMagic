package com.rainbowluigi.soulmagic.upgrade;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class Upgrade {

    private int x, y;
    private Upgrade prev;
    private UpgradeSprite icon;
    private UpgradeSprite s;

    private ItemStack[] requirements;

    private String translationKey;
    private String descTranslationKey;

    public Upgrade(int x, int y, Upgrade prev, UpgradeSprite icon, UpgradeSprite s) {
        this.x = x;
        this.y = y;
        this.prev = prev;
        this.icon = icon;
        this.s = s;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Upgrade getPrev() {
        return this.prev;
    }

    public Upgrade setRequirements(ItemStack... stacks) {
        this.requirements = stacks;
        return this;
    }

    public ItemStack[] getRequirements() {
        return this.requirements;
    }

    public UpgradeSprite getIcon() {
        return this.icon;
    }

    public UpgradeSprite getUpgradeSprite() {
        return this.s;
    }

    @Environment(EnvType.CLIENT)
    public MutableText getName() {
        return Text.translatable(this.getOrCreateTranslationKey());
    }

    public String getOrCreateTranslationKey() {
        if (this.translationKey == null) {
            this.translationKey = Util.createTranslationKey("upgrade", ModUpgrades.UPGRADE.getId(this));
        }

        return this.translationKey;
    }

    @Environment(EnvType.CLIENT)
    public MutableText getDesc() {
        return Text.translatable(this.getOrCreateDescTranslationKey());
    }

    public String getOrCreateDescTranslationKey() {
        if (this.descTranslationKey == null) {
            this.descTranslationKey = Util.createTranslationKey("upgrade.desc", ModUpgrades.UPGRADE.getId(this));
        }

        return this.descTranslationKey;
    }
}