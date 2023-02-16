package com.rainbowluigi.soulmagic.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class DisarmingEnchantment extends Enchantment {

	protected DisarmingEnchantment(Enchantment.Rarity rarity, EnchantmentTarget enchantmentTarget, EquipmentSlot ... equipmentSlots) {
		super(rarity, enchantmentTarget, equipmentSlots);
	}

	@Override
	public int getMinPower(int level) {
		return level * 10;
	}

	@Override
	public int getMaxPower(int level) {
		return this.getMinPower(level) + 15;
	}

	@Override
	public boolean isTreasure() {
		return true;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}
}