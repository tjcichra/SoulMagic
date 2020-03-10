package com.rainbowluigi.soulmagic.spelltype;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

public class SpellType {

	private String translationKey;
	private int color;
	
	public SpellType(int color) {
		this.color = color;
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
			this.translationKey = Util.createTranslationKey("spell_type", ModSpellTypes.SPELL_TYPE_REG.getId(this));
		}

		return this.translationKey;
	}

	public String getTranslationKey() {
		return this.getOrCreateTranslationKey();
	}
}
