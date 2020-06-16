package com.rainbowluigi.soulmagic.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class DisarmingEnchantment extends Enchantment {

	protected DisarmingEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
		super(weight, type, slotTypes);
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