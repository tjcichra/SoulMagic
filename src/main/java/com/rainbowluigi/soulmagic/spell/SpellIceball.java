package com.rainbowluigi.soulmagic.spell;

import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;

public class SpellIceball extends Spell {

	public SpellIceball() {
		super(ModSpellTypes.ICY);
	}
	
	@Override
	public boolean isBase() {
		return true;
	}
}
