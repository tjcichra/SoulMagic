package com.rainbowluigi.soulmagic.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class SoulStealerEnchantment extends Enchantment {

	public SoulStealerEnchantment() {
		super(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
	}
	
	@Override
	public int getMinPower(int int_1) {
		return 10 + 15 * (int_1 - 1);
	}
	
	@Override
	public int getMaxLevel() {
		return 3;
	}
}
