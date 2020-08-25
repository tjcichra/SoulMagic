package com.rainbowluigi.soulmagic.rei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rainbowluigi.soulmagic.item.crafting.SoulInfusionRecipe;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeDisplay;
import me.shedaniel.rei.server.ContainerInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

public class SoulInfusionDisplay implements TransferRecipeDisplay {

	protected SoulInfusionRecipe recipe;
	protected List<List<EntryStack>> input;
	protected List<EntryStack> output;
	protected int progressColor;

	public SoulInfusionDisplay(SoulInfusionRecipe recipe) {
		this.recipe = recipe;
		this.input = recipe.getPreviewInputs().stream().map(i -> {
			List<EntryStack> entries = new ArrayList<>();
			for (ItemStack stack : i.getMatchingStacksClient()) {
				entries.add(EntryStack.create(stack));
			}
			return entries;
		}).collect(Collectors.toList());
		this.output = Collections.singletonList(EntryStack.create(recipe.getOutput()));
		this.progressColor = recipe.getProgressColor();
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(this.recipe).map(SoulInfusionRecipe::getId);
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return this.input;
	}

	@Override
	public List<EntryStack> getOutputEntries() {
		return this.output;
	}

	@Override
	public List<List<EntryStack>> getRequiredEntries() {
		return this.input;
	}

	public int getProgressColor() {
		return this.progressColor;
	}

	@Override
	public Identifier getRecipeCategory() {
		return SoulMagicPlugin.SOUL_INFUSION;
	}

	@Override
	public int getWidth() {
		return 1;
	}

	@Override
	public int getHeight() {
		return 1;
	}

	@Override
	public List<List<EntryStack>> getOrganisedInputEntries(ContainerInfo<ScreenHandler> containerInfo, ScreenHandler container) {
		return this.input;
	}
}
