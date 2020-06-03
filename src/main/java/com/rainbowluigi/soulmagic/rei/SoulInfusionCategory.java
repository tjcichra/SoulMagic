package com.rainbowluigi.soulmagic.rei;

import java.util.List;

import com.google.common.collect.Lists;
import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.item.crafting.SoulInfusionRecipe;
import com.rainbowluigi.soulmagic.item.crafting.SpellInfusionRecipe;
import com.rainbowluigi.soulmagic.rei.widgets.InfusionCircleWidget;
import com.rainbowluigi.soulmagic.rei.widgets.SoulEssenceStaffWidget;

import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.entries.RecipeEntry;
import me.shedaniel.rei.gui.entries.SimpleRecipeEntry;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

//The REI category of all soul infusion recipes (including spell infusion recipes)
public class SoulInfusionCategory implements TransferRecipeCategory<SoulInfusionDisplay> {

    //Returns idenifier of the category
    @Override
    public Identifier getIdentifier() {
        return SoulMagicPlugin.SOUL_INFUSION;
    }

    //Sets up the icon of the category
    @Override
    public EntryStack getLogo() {
        //Just have a picture of the soul infuser block as the icon
        return EntryStack.create(new ItemStack(ModBlocks.SOUL_INFUSER));
    }

    //Returns display name of the category
    @Override
    public String getCategoryName() {
        return I18n.translate("soulmagic.rei.category.soul_infusion");
    }

    //Handles "villager-style" crafting display
    @Override
    public RecipeEntry getSimpleRenderer(SoulInfusionDisplay recipe) {
        return SimpleRecipeEntry.create(recipe::getInputEntries, recipe::getOutputEntries);
    }

    //Sets up the display of the recipes in the category
    @Override
    public List<Widget> setupDisplay(SoulInfusionDisplay recipeDisplay, Rectangle bounds) {
        //Create list of widgets and add the recipe base (square box) and the infusion circle
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(new InfusionCircleWidget(new Rectangle(bounds.x + 12, bounds.y + 9, 100, 100), recipeDisplay.progressColor));
        widgets.add(new SoulEssenceStaffWidget(new Rectangle(bounds.x + 122, bounds.y + 28, 16, 16), recipeDisplay.recipe.getSoulMap()));

        //Get the list of inputs and add recipe slots with them.
        List<List<EntryStack>> input = recipeDisplay.getInputEntries();
        widgets.add(Widgets.createSlot(new Point(bounds.x + 54, bounds.y + 16)).entries(input.get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(bounds.x + 77, bounds.y + 28)).entries(input.get(1)).markInput());
        widgets.add(Widgets.createSlot(new Point(bounds.x + 89, bounds.y + 51)).entries(input.get(2)).markInput());
        widgets.add(Widgets.createSlot(new Point(bounds.x + 77, bounds.y + 74)).entries(input.get(3)).markInput());
        widgets.add(Widgets.createSlot(new Point(bounds.x + 54, bounds.y + 86)).entries(input.get(4)).markInput());
        widgets.add(Widgets.createSlot(new Point(bounds.x + 31, bounds.y + 74)).entries(input.get(5)).markInput());
        widgets.add(Widgets.createSlot(new Point(bounds.x + 19, bounds.y + 51)).entries(input.get(6)).markInput());
        widgets.add(Widgets.createSlot(new Point(bounds.x + 31, bounds.y + 28)).entries(input.get(7)).markInput());

        //If there is a center input, display it
        if (input.size() >= 9) {
            widgets.add(Widgets.createSlot(new Point(bounds.x + 54, bounds.y + 51)).entries(input.get(8)).markInput());
        }
        
        //If the recipe is a spell infusion recipe, display the name of the spell
        SoulInfusionRecipe recipe = recipeDisplay.recipe;
        if (recipe instanceof SpellInfusionRecipe) {
            SpellInfusionRecipe sirecipe = (SpellInfusionRecipe) recipe;
            widgets.add(Widgets.createLabel(new Point(bounds.x + 75, bounds.y + 113), new TranslatableText("soulmagic.rei.spell", sirecipe.spell.getName())).color(0xFF404040, 0xFFBBBBBB).noShadow().centered());
        }

        //Add recipe slot with the output
        widgets.add(Widgets.createResultSlotBackground(new Point(bounds.x + 122, bounds.y + 51)));
        widgets.add(Widgets.createSlot(new Point(bounds.x + 122, bounds.y + 51)).entries(recipeDisplay.getOutputEntries()).disableBackground().markOutput());
        //Return all the widgets
        return widgets;
    }

    //Gets the height of the display
    @Override
    public int getDisplayHeight() {
        return 128;
    }

    //Handles the rendered red slots when using auto-transer
    @Override
    public void renderRedSlots(MatrixStack matrices, List<Widget> widgets, Rectangle bounds, SoulInfusionDisplay display, IntList redSlots) {
        // TODO Setup automatic insert of items when desired
    }
}