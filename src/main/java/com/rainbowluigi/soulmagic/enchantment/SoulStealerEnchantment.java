package com.rainbowluigi.soulmagic.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class SoulStealerEnchantment extends Enchantment {

	protected SoulStealerEnchantment(Enchantment.Rarity rarity, EnchantmentTarget enchantmentTarget, EquipmentSlot ... equipmentSlots) {
		super(rarity, enchantmentTarget, equipmentSlots);
	}

	@Override
	public int getMinPower(int level) {
		return 15 + (level - 1) * 9;
	}

	@Override
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 50;
	}
	
	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		if (stack.getItem() instanceof AxeItem) {
			return true;
		}
		return super.isAcceptableItem(stack);
	}
}
