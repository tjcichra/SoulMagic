package com.rainbowluigi.soulmagic.spell;

import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;

public class SpellAirStrike extends Spell {

	public SpellAirStrike() {
		super(ModSpellTypes.WINDY);
	}
	
	@Override
	public boolean isBase() {
		return true;
	}
}
