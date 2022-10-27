package com.rainbowluigi.soulmagic.util;

import java.util.ArrayList;
import java.util.List;

import com.rainbowluigi.soulmagic.SoulMagic;
import com.rainbowluigi.soulmagic.item.Upgradeable;
import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;
import com.rainbowluigi.soulmagic.spelltype.SpellType;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.upgrade.spells.SpellUpgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class SoulGemHelper {

	public static SpellType getSpellType(ItemStack stack) {
        if(stack.hasNbt() && stack.getNbt().contains("spelltype")) {
            return ModSpellTypes.SPELL_TYPE.get(new Identifier(stack.getNbt().getString("spelltype")));
        }
        return null;
    }
	
	public static void setSpellType(ItemStack stack, SpellType st) {
		if(!stack.hasNbt()) {
        	stack.setNbt(new NbtCompound());
        }
		
		stack.getNbt().putString("spelltype", ModSpellTypes.SPELL_TYPE.getId(st).toString());
    }
	
	public static SpellUpgrade getCurrentSpell(ItemStack stack) {
		int spellIndex = getCurrentSpellIndex(stack);
		if(spellIndex < 0 || spellIndex >= getCurrentList(stack).size()) {
			return null;
		}

		return getCurrentList(stack).get(spellIndex);
	}
	
	public static void setCurrentSpellIndex(ItemStack stack, int index) {
		if(!stack.hasNbt()) {
			stack.setNbt(new NbtCompound());
		}
		
		stack.getNbt().putInt("spellindex", index);
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
	
	public static int getCurrentSpellIndex(ItemStack stack) {
        if(stack.getNbt().contains("spellindex")) {
            return stack.getNbt().getInt("spellindex");
        }
        return 0;
	}
	
	public static void setActivated(ItemStack gem, boolean activated) {
		NbtCompound tag = gem.getOrCreateNbt();
		tag.putBoolean("activated", activated);
	}

	public static boolean getActivated(ItemStack gem) {
		if(gem.hasNbt() && gem.getNbt().contains("activated")) {
			return gem.getNbt().getBoolean("activated");
		}
		return false;
	}

	public static void toggleActivation(ItemStack gem) {
		if(gem.hasNbt() && gem.getNbt().contains("activated")) {
			gem.getNbt().putBoolean("activated", !gem.getNbt().getBoolean("activated"));
		} else {
			gem.getOrCreateNbt().putBoolean("activated", true);
		}
	}
	
	public static void setBrace(ItemStack gem, ItemStack brace) {
		NbtCompound tag = gem.getOrCreateSubNbt("brace");
		brace.writeNbt(tag);
	}
	
	public static ItemStack getBrace(ItemStack gem) {
		NbtCompound tag = gem.getSubNbt("brace");
		if(tag != null) {
			return ItemStack.fromNbt(tag);
		}
		return null;
	}
}