package com.rainbowluigi.soulmagic.spelltype;

import java.util.List;

import com.rainbowluigi.soulmagic.upgrade.Upgrade;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

public class SpellType {

	private String translationKey;
	private int color;
	private List<Upgrade> upgrades;
	
	public SpellType(int color, List<Upgrade> upgrades) {
		this.color = color;
		this.upgrades = upgrades;
	}
	
	public int getColor() {
		return this.color;
	}
	
	@Environment(EnvType.CLIENT)
	public Text getName() {
		return new TranslatableText(this.getTranslationKey());
	}
	
	public String getOrCreateTranslationKey() {
		if (this.translationKey == null) {
			this.translationKey = Util.createTranslationKey("spell_type", ModSpellTypes.SPELL_TYPE.getId(this));
		}

		return this.translationKey;
	}

	public String getTranslationKey() {
		return this.getOrCreateTranslationKey();
	}

	public List<Upgrade> getPossibleUpgrades() {
		return this.upgrades;
	};
}
