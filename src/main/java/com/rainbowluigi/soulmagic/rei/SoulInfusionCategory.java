package com.rainbowluigi.soulmagic.rei;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.client.screen.SoulInfuserScreen;
import com.rainbowluigi.soulmagic.item.crafting.SoulInfusionRecipe;
import com.rainbowluigi.soulmagic.item.crafting.SpellInfusionRecipe;

import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.math.api.Point;
import me.shedaniel.math.api.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeCategory;
import me.shedaniel.rei.gui.entries.RecipeEntry;
import me.shedaniel.rei.gui.entries.SimpleRecipeEntry;
import me.shedaniel.rei.gui.widget.EntryWidget;
import me.shedaniel.rei.gui.widget.LabelWidget;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SoulInfusionCategory implements TransferRecipeCategory<SoulInfusionDisplay> {
	
	@Override
	public Identifier getIdentifier() {
		return SoulMagicPlugin.SOUL_INFUSION;
	}
	
	@Override
    public EntryStack getLogo() {
        return EntryStack.create(new ItemStack(ModBlocks.SOUL_INFUSER));
    }

	@Override
	public String getCategoryName() {
		return I18n.translate("soulmagic.rei.category.soul_infusion");
	}

    @Override
    public RecipeEntry getSimpleRenderer(SoulInfusionDisplay recipe) {
        return SimpleRecipeEntry.create(recipe::getInputEntries, recipe::getOutputEntries);
    }
    
    @Override
    public List<Widget> setupDisplay(Supplier<SoulInfusionDisplay> recipeDisplaySupplier, Rectangle bounds) {
    	SoulInfusionDisplay recipeDisplay = recipeDisplaySupplier.get();
        
        Point startPoint = new Point((int) bounds.getCenterX() - 63, (int) bounds.getCenterY() - 51);
        List<Widget> widgets = new LinkedList<>(Arrays.asList(new RecipeBaseWidget(bounds) {
            @Override
            public void render(int mouseX, int mouseY, float delta) {
                super.render(mouseX, mouseY, delta);
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                ////GuiLighting.disable();
                MinecraftClient.getInstance().getTextureManager().bindTexture(SoulInfuserScreen.rl);
                blit(startPoint.x, startPoint.y, 38, 16, 127, 101);
                
                
                int color = recipeDisplay.getProgressColor();
    			RenderSystem.color4f((color >>> 16) / 255f, (color >>> 8 & 255) / 255f, (color & 255) / 255f, 1);
                int i = MathHelper.ceil(System.currentTimeMillis() / 25 % 101d);
                blit(startPoint.x, startPoint.y + 101 - i, 176, 101 - i, 50, i);
                blit(startPoint.x + 50, startPoint.y + 101 - i, 176, 202 - i, 50, i);
                RenderSystem.color4f(1, 1, 1, 1);
                //int width = MathHelper.ceil((System.currentTimeMillis() / 250 % 24d) / 1f);
                //blit(startPoint.x + 24, startPoint.y + 18, 82, 91, width, 17);
            }
        }));
        
        List<List<EntryStack>> input = recipeDisplaySupplier.get().getInputEntries();
        widgets.add(EntryWidget.create(startPoint.x + 42, startPoint.y + 7).entries(input.get(0)));
        widgets.add(EntryWidget.create(startPoint.x + 65, startPoint.y + 19).entries(input.get(1)));
        widgets.add(EntryWidget.create(startPoint.x + 77, startPoint.y + 42).entries(input.get(2)));
        widgets.add(EntryWidget.create(startPoint.x + 65, startPoint.y + 65).entries(input.get(3)));
        widgets.add(EntryWidget.create(startPoint.x + 42, startPoint.y + 77).entries(input.get(4)));
        widgets.add(EntryWidget.create(startPoint.x + 19, startPoint.y + 65).entries(input.get(5)));
        widgets.add(EntryWidget.create(startPoint.x + 7, startPoint.y + 42).entries(input.get(6)));
        widgets.add(EntryWidget.create(startPoint.x + 19, startPoint.y + 19).entries(input.get(7)));
        
        if(input.size() >= 9)
        	widgets.add(EntryWidget.create(startPoint.x + 42, startPoint.y + 42).entries(input.get(8)));
        
        SoulInfusionRecipe recipe = recipeDisplay.recipe;
        if(recipe instanceof SpellInfusionRecipe) {
        	SpellInfusionRecipe sirecipe = (SpellInfusionRecipe) recipe;
        	widgets.add(new LabelWidget(new Point(startPoint.x + 42, startPoint.y + 102), sirecipe.spell.getName().asFormattedString()));
        }
        widgets.add(EntryWidget.create(startPoint.x + 110, startPoint.y + 42).entries(recipeDisplaySupplier.get().getOutputEntries()).noBackground());
        return widgets;
    }
    
	@Override
	public int getDisplayHeight() {
		return 120;
	}

	@Override
	public void renderRedSlots(List<Widget> widgets, Rectangle bounds, SoulInfusionDisplay display, IntList redSlots) {
		// TODO Auto-generated method stub
		
	}
}