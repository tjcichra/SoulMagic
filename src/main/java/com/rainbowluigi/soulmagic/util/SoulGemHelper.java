package com.rainbowluigi.soulmagic.util;

import com.rainbowluigi.soulmagic.item.Upgradeable;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.upgrade.spells.SpellUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;

public class SoulGemHelper {

    private static final String SPELL_INDEX_KEY = "spellindex";

    public static SpellUpgrade getCurrentSpell(ItemStack stack) {
        int spellIndex = getCurrentSpellIndex(stack);
        if (spellIndex < 0 || spellIndex >= getCurrentList(stack).size()) {
            return null;
        }

        return getCurrentList(stack).get(spellIndex);
    }

    public static void setCurrentSpellIndex(ItemStack stack, int index) {
        if (!stack.hasNbt()) {
            stack.setNbt(new NbtCompound());
        }

        stack.getNbt().putInt(SPELL_INDEX_KEY, index);
    }

    public static List<SpellUpgrade> getCurrentList(ItemStack stack) {
        List<SpellUpgrade> spells = new ArrayList<>();
        Upgradeable u = (Upgradeable) stack.getItem();

        for (Upgrade u2 : u.getUpgradesSelected(stack)) {
            if (u2 instanceof SpellUpgrade) {
                spells.add((SpellUpgrade) u2);
            }
        }
        return spells;
    }

    public static int getCurrentSpellIndex(ItemStack stack) {
        return NBTHelper.getIntFromNbt(stack.getNbt(), SPELL_INDEX_KEY, 0);
    }

    public static void setActivated(ItemStack gem, boolean activated) {
        NbtCompound tag = gem.getOrCreateNbt();
        tag.putBoolean("activated", activated);
    }

    public static boolean getActivated(ItemStack gem) {
        return NBTHelper.getBooleanFromNbt(gem.getNbt(), "activated", false);
    }

    public static void toggleActivation(ItemStack gem) {
        if (gem.hasNbt() && gem.getNbt().contains("activated")) {
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
        if (tag != null) {
            return ItemStack.fromNbt(tag);
        }
        return null;
    }
}