package com.rainbowluigi.soulmagic.rei;

import com.rainbowluigi.soulmagic.item.crafting.SoulInfusionRecipe;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SoulInfusionDisplay extends BasicDisplay {
	private final int progressColor;
	private final Map<SoulType, Integer> soulCost;

	public SoulInfusionDisplay(SoulInfusionRecipe recipe) {
		this(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getOutput())), Optional.ofNullable(recipe.getId()), recipe.getProgressColor(), recipe.soulMap);
	}

	public SoulInfusionDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location, int progressColor, Map<SoulType, Integer> soulCost) {
		super(inputs, outputs, location);
		this.progressColor = progressColor;
		this.soulCost = soulCost;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SoulMagicClientPlugin.SOUL_INFUSION;
	}

	public int getProgressColor() {
		return progressColor;
	}

	public Map<SoulType, Integer> getSoulCost() {
		return soulCost;
	}
}
