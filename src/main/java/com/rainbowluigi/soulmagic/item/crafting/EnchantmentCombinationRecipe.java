package com.rainbowluigi.soulmagic.item.crafting;

import java.util.Collections;
import java.util.Map;

import com.rainbowluigi.soulmagic.block.entity.SoulEssenceInfuserBlockEntity;
import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class EnchantmentCombinationRecipe extends SoulInfusionRecipe {

	public EnchantmentCombinationRecipe(Identifier id) {
		super(id, "", DefaultedList.of(), Collections.EMPTY_MAP, new Upgrade[0], 0xFFFFFF, new ItemStack(Items.POTATO));
	}

	@Override
	public boolean matches(SoulEssenceInfuserBlockEntity inv, World worldIn) {
		if(inv.getSelectedUpgrades().contains(ModUpgrades.ENCHANTING_COMPONENT)) {
			boolean hasOneCatalyst = false;
			boolean hasBooks = false;

			for(int i = 0; i < inv.size(); i++) {
				ItemStack stack = inv.getStack(i);

				if(!stack.isEmpty()) {
					if(stack.getItem() == ModItems.ENCHANTMENT_COMBINATION_CATALYST) {
						if(hasOneCatalyst) {
							return false;
						}

						hasOneCatalyst = true;
					} else if(stack.getItem() == Items.ENCHANTED_BOOK) {
						hasBooks = true;
					}
				}
			}
			return hasOneCatalyst && hasBooks;
		}
		return false;
	}
}