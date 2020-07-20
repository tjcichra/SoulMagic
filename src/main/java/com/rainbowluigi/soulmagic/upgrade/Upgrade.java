package com.rainbowluigi.soulmagic.upgrade;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

public class Upgrade {

	private ItemStack icon;
	private int x, y;
	private Upgrade prev;
	private UpgradeSprite s;
	private ItemStack[] requirements;

	private String translationKey;
	private String descTranslationKey;

	public Upgrade(ItemStack icon, int x, int y, Upgrade prev, UpgradeSprite s) {
		this.icon = icon;
		this.x = x;
		this.y = y;
		this.prev = prev;
		this.s = s;
	}

	public ItemStack getIcon() {
		return this.icon;
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

	public UpgradeSprite getUpgradeSprite() {
		return this.s;
	}

	@Environment(EnvType.CLIENT)
	public MutableText getName() {
		return new TranslatableText(this.getOrCreateTranslationKey());
	}

	public String getOrCreateTranslationKey() {
		if (this.translationKey == null) {
			this.translationKey = Util.createTranslationKey("upgrade", ModUpgrades.UPGRADE.getId(this));
		}

		return this.translationKey;
	}

	@Environment(EnvType.CLIENT)
	public MutableText getDesc() {
		return new TranslatableText(this.getOrCreateDescTranslationKey());
	}

	public String getOrCreateDescTranslationKey() {
		if (this.descTranslationKey == null) {
			this.descTranslationKey = Util.createTranslationKey("upgrade.desc", ModUpgrades.UPGRADE.getId(this));
		}

		return this.descTranslationKey;
	}
}