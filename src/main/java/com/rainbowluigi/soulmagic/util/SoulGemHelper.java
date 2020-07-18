package com.rainbowluigi.soulmagic.util;

import java.util.ArrayList;
import java.util.List;

import com.rainbowluigi.soulmagic.item.Upgradeable;
import com.rainbowluigi.soulmagic.spell.ModSpells;
import com.rainbowluigi.soulmagic.spell.Spell;
import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;
import com.rainbowluigi.soulmagic.spelltype.SpellType;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.upgrade.spells.SpellUpgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;

public class SoulGemHelper {

	public static SpellType getSpellType(ItemStack stack) {
        if(stack.hasTag() && stack.getTag().contains("spelltype")) {
            return ModSpellTypes.SPELL_TYPE.get(new Identifier(stack.getTag().getString("spelltype")));
        }
        return null;
    }
	
	public static void setSpellType(ItemStack stack, SpellType st) {
		if(!stack.hasTag()) {
        	stack.setTag(new CompoundTag());
        }
		
		stack.getTag().putString("spelltype", ModSpellTypes.SPELL_TYPE.getId(st).toString());
    }
	
	public static SpellUpgrade getCurrentSpell(ItemStack stack) {
		int spellIndex = getCurrentSpellIndex(stack);
		return getCurrentList(stack).get(spellIndex);
	}
	
	public static void setCurrentSpellIndex(ItemStack stack, int index) {
		if(!stack.hasTag()) {
			stack.setTag(new CompoundTag());
		}
		
		stack.getTag().putInt("spellindex", index);
	}
	
	public static List<SpellUpgrade> getCurrentList(ItemStack stack) {
		List<SpellUpgrade> spells = new ArrayList<>();
		Upgradeable u = (Upgradeable) stack.getItem();
		
		for(Upgrade u2 : u.getUpgradesSelected(stack)) {
			if(u2 instanceof SpellUpgrade) {
				spells.add((SpellUpgrade) u2);
			}
		}
		return spells;
	}
	
	private static int getCurrentSpellIndex(ItemStack stack) {
        if(stack.getTag().contains("spellindex")) {
            return stack.getTag().getInt("spellindex");
        }
        return 0;
    }
	
	public static void setBrace(ItemStack gem, ItemStack brace) {
		CompoundTag tag = gem.getOrCreateSubTag("brace");
		brace.toTag(tag);
	}
	
	public static ItemStack getBrace(ItemStack gem) {
		CompoundTag tag = gem.getSubTag("brace");
		if(tag != null) {
			return ItemStack.fromTag(tag);
		}
		return null;
	}
}