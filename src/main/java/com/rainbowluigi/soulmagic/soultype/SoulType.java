package com.rainbowluigi.soulmagic.soultype;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

public class SoulType {
    
    private int color;
	private Formatting textColor;
	private String translationKey;
	
	public SoulType(int color, Formatting textColor) {
		this.color = color;
		this.textColor = textColor;
	}
	
	public int getColor() {
		return this.color;
	}
	
	public Formatting getTextColor() {
		return this.textColor;
	}
	
	@Environment(EnvType.CLIENT)
	public MutableText getName() {
		return new TranslatableText(this.getTranslationKey());
	}
	
	public String getOrCreateTranslationKey() {
		if (this.translationKey == null) {
			this.translationKey = Util.createTranslationKey("soul_type", ModSoulTypes.SOUL_TYPE.getId(this));
		}

		return this.translationKey;
	}
	
	public String getTranslationKey() {
		return this.getOrCreateTranslationKey();
	}
}