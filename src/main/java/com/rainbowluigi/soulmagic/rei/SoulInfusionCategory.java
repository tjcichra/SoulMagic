package com.rainbowluigi.soulmagic.rei;

import com.google.common.collect.Lists;
import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.rei.widgets.InfusionCircleWidget;
import com.rainbowluigi.soulmagic.rei.widgets.SoulEssenceStaffWidget;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.List;

//The REI category of all soul infusion recipes (including spell infusion recipes)
@Environment(EnvType.CLIENT)
public class SoulInfusionCategory implements DisplayCategory<SoulInfusionDisplay> {

	@Override
	public CategoryIdentifier<? extends SoulInfusionDisplay> getCategoryIdentifier() {
		return SoulMagicClientPlugin.SOUL_INFUSION;
	}

	//Sets up the icon of the category
	@Override
	public Renderer getIcon() {
		//Just have a picture of the soul infuser block as the icon
		return EntryStacks.of(ModBlocks.SOUL_ESSENCE_INFUSER);
	}

	//Returns display name of the category
	@Override
	public Text getTitle() {
		return Text.translatable("soulmagic.rei.category.soul_infusion");
	}

	//Sets up the display of the recipes in the category
	@Override
	public List<Widget> setupDisplay(SoulInfusionDisplay display, Rectangle bounds) {
		//Create list of widgets and add the recipe base (square box) and the infusion circle
		List<Widget> widgets = Lists.newArrayList();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(new InfusionCircleWidget(new Rectangle(bounds.x + 12, bounds.y + 9, 100, 100), display.getProgressColor()));
		widgets.add(new SoulEssenceStaffWidget(new Rectangle(bounds.x + 122, bounds.y + 28, 16, 16), display.getSoulCost()));

//		if(recipeDisplay.recipe.getUpgradesNeeded().length > 0) {
//			widgets.add(new UpgradeRequirementWidget(new Rectangle(bounds.x + 122, bounds.y + 12, 16, 16), recipeDisplay.recipe.getUpgradesNeeded()));
//		}

		//Get the list of inputs and add recipe slots with them.
		List<EntryIngredient> input = display.getInputEntries();
		widgets.add(Widgets.createSlot(new Point(bounds.x + 54, bounds.y + 16)).entries(input.get(0)).markInput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 77, bounds.y + 28)).entries(input.get(1)).markInput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 89, bounds.y + 51)).entries(input.get(2)).markInput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 77, bounds.y + 74)).entries(input.get(3)).markInput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 54, bounds.y + 86)).entries(input.get(4)).markInput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 31, bounds.y + 74)).entries(input.get(5)).markInput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 19, bounds.y + 51)).entries(input.get(6)).markInput());
		widgets.add(Widgets.createSlot(new Point(bounds.x + 31, bounds.y + 28)).entries(input.get(7)).markInput());

		//If there is a center input, display it
		if(input.size() >= 9) {
			widgets.add(Widgets.createSlot(new Point(bounds.x + 54, bounds.y + 51)).entries(input.get(8)).markInput());
		}

		//Add recipe slot with the output
		widgets.add(Widgets.createResultSlotBackground(new Point(bounds.x + 122, bounds.y + 51)));
		widgets.add(Widgets.createSlot(new Point(bounds.x + 122, bounds.y + 51)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
		//Return all the widgets
		return widgets;
	}

	//Gets the height of the display
	@Override
	public int getDisplayHeight() {
		return 118;
	}
}