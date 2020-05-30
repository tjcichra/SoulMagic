package com.rainbowluigi.soulmagic.util;

import java.util.ArrayList;
import java.util.List;

import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.spell.ModSpells;
import com.rainbowluigi.soulmagic.spell.Spell;
import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;
import com.rainbowluigi.soulmagic.spelltype.SpellType;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;

public class SoulQuiverHelper {

	public static SoulType getSoulType(ItemStack stack) {
		//Checks if the stack has a tag of the soul type
		if (stack.hasTag() && stack.getTag().contains("soultype")) {
			// Gets the registry name of the soul type (which is also the key)
			return ModSoulTypes.SOUL_TYPE_REG.get(stack.getTag().getInt("soultype"));
		}
		return ModSoulTypes.LIGHT;
	}
	
	public static void incrementType(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		
		int i = ModSoulTypes.SOUL_TYPE_REG.getRawId(getSoulType(stack));
		i++;
		
		if(ModSoulTypes.SOUL_TYPE_REG.get(i) != null)
			tag.putInt("soultype", i);
		else
			tag.putInt("soultype", 0);
	}
}