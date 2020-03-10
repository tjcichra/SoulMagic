package com.rainbowluigi.soulmagic.util;

import java.util.ArrayList;
import java.util.List;

import com.rainbowluigi.soulmagic.spell.ModSpells;
import com.rainbowluigi.soulmagic.spell.Spell;
import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;
import com.rainbowluigi.soulmagic.spelltype.SpellType;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;

public class SoulGemHelper {

	public static SpellType getSpellType(ItemStack stack) {
        if(stack.hasTag() && stack.getTag().contains("spelltype")) {
            return ModSpellTypes.SPELL_TYPE_REG.get(new Identifier(stack.getTag().getString("spelltype")));
        }
        return null;
    }
	
	public static void setSpellType(ItemStack stack, SpellType st) {
		if(!stack.hasTag()) {
        	stack.setTag(new CompoundTag());
        }
		
		stack.getTag().putString("spelltype", ModSpellTypes.SPELL_TYPE_REG.getId(st).toString());
    }
	
	public static void addSpell(ItemStack stack, Spell s) {
		if(!stack.hasTag()) {
			stack.setTag(new CompoundTag());
		}
		
		if(!stack.getTag().contains("spells")) {
			stack.getTag().put("spells", new ListTag());
		}
		
		ListTag nbtTagList = getSpells(stack);
		
		CompoundTag tag = new CompoundTag();
		tag.putString("spell", ModSpells.SPELL_REG.getId(s).toString());
		nbtTagList.add(tag);
	}
	
	public static boolean hasSpell(ItemStack stack, Spell s) {
		if(stack.hasTag() && stack.getTag().contains("spells")) {
			ListTag list = getSpells(stack);
			
			for(Tag t : list) {
				Spell s2 = ModSpells.SPELL_REG.get(new Identifier(((CompoundTag)t).getString("spell")));
				if(s.equals(s2)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static Spell getCurrentSpell(ItemStack stack) {
		if(stack.hasTag() && stack.getTag().contains("spells")) {
			ListTag list = getSpells(stack);
			
			int spellIndex = getCurrentSpellIndex(stack);
			return ModSpells.SPELL_REG.get(new Identifier(((CompoundTag)list.get(spellIndex)).getString("spell")));
		}
		return null;
	}
	
	public static void setCurrentSpellIndex(ItemStack stack, int index) {
		if(!stack.hasTag()) {
			stack.setTag(new CompoundTag());
		}
		
		stack.getTag().putInt("spellindex", index);
	}
	
	public static List<Spell> getCurrentList(ItemStack stack) {
		List<Spell> spells = new ArrayList<>();
		if(stack.hasTag() && stack.getTag().contains("spells")) {
			ListTag list = getSpells(stack);
			
			for(int i = 0; i < list.size(); i++) {
				spells.add(ModSpells.SPELL_REG.get(new Identifier(((CompoundTag)list.get(i)).getString("spell"))));
			}
		}
		return spells;
	}
	
	private static ListTag getSpells(ItemStack stack) {
		return (ListTag) stack.getTag().get("spells");
	}
	
	private static int getCurrentSpellIndex(ItemStack stack) {
        if(stack.getTag().contains("spellindex")) {
            return stack.getTag().getInt("spellindex");
        }
        return 0;
    }
}