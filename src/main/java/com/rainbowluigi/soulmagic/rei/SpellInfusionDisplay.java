package com.rainbowluigi.soulmagic.rei;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.rainbowluigi.soulmagic.item.crafting.SpellInfusionRecipe;
import com.rainbowluigi.soulmagic.util.SoulGemHelper;

import me.shedaniel.rei.api.EntryStack;
import net.minecraft.item.ItemStack;

public class SpellInfusionDisplay extends SoulInfusionDisplay {
	
	public SpellInfusionDisplay(SpellInfusionRecipe recipe) {
		super(recipe);
		this.input = recipe.getPreviewInputs().stream().map(i -> {
            List<EntryStack> entries = new ArrayList<>();
            for (ItemStack stack : i.getMatchingStacksClient()) {
                entries.add(EntryStack.create(stack));
            }
            return entries;
        }).collect(Collectors.toList());
		
		if(!recipe.spell.isBase()) {
			for(EntryStack stack : this.input.get(8)) {
				SoulGemHelper.setSpellType(stack.getItemStack(), recipe.spell.getParent());
			}
		}
	
		List<EntryStack> output = Lists.newArrayList();
		for(ItemStack stack : recipe.getPreviewInputs().get(8).getMatchingStacksClient()) {
			ItemStack stack2 = stack.copy();
			
			if(SoulGemHelper.getSpellType(stack2) == null)
				SoulGemHelper.setSpellType(stack2, recipe.spell.getParent());
			
			SoulGemHelper.addSpell(stack2, recipe.spell);
			output.add(EntryStack.create(stack2));
		}
		
		this.output = output;
	}
}