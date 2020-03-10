package com.rainbowluigi.soulmagic.item.bound;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class BoundToolMaterial implements ToolMaterial {

	public static final BoundToolMaterial BOUND_MATERIAL = new BoundToolMaterial();
	
	@Override
	public int getDurability() {
		return 0;
	}

	@Override
	public float getMiningSpeed() {
		return 8;
	}

	@Override
	public float getAttackDamage() {
		return 3;
	}

	@Override
	public int getMiningLevel() {
		return 3;
	}

	@Override
	public int getEnchantability() {
		return 0;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return null;
	}

}
