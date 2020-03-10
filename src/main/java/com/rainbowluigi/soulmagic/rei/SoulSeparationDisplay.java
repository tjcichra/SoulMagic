package com.rainbowluigi.soulmagic.rei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rainbowluigi.soulmagic.item.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.item.crafting.SoulSeparatorRecipe;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SoulSeparationDisplay implements RecipeDisplay {

	protected SoulSeparatorRecipe recipe;
	protected List<List<EntryStack>> input;
	protected float chance;
	protected List<EntryStack> output;
	protected List<EntryStack> staffs;
	
	public SoulSeparationDisplay(SoulSeparatorRecipe recipe) {
		this.recipe = recipe;
		this.recipe = recipe;
		this.input = recipe.getPreviewInputs().stream().map(i -> {
            List<EntryStack> entries = new ArrayList<>();
            for (ItemStack stack : i.getMatchingStacksClient()) {
                entries.add(EntryStack.create(stack));
            }
            return entries;
        }).collect(Collectors.toList());
		this.chance = recipe.getChance();
		this.output = Collections.singletonList(EntryStack.create(recipe.getOutput()));
		this.staffs = DefaultedList.of();
		for(Item i : Registry.ITEM) {
			if(i instanceof SoulEssenceStaff) {
				this.staffs.add(EntryStack.create(i));
			}
		}
	}
	
	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(this.recipe).map(SoulSeparatorRecipe::getId);
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return this.input;
	}
	
	public float getChance() {
		return this.chance;
	}

	@Override
	public List<EntryStack> getOutputEntries() {
		return this.output;
	}
	
	@Override
    public List<List<EntryStack>> getRequiredEntries() {
        return this.input;
    }
	
    public List<EntryStack> getSoulStaffs() {
        return this.staffs;
    }
	
	@Override
	public Identifier getRecipeCategory() {
		return SoulMagicPlugin.SOUL_SEPARATION;
	}
}
