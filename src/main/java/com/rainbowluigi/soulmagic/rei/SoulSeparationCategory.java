package com.rainbowluigi.soulmagic.rei;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.client.screen.SoulSeparatorScreen;

import me.shedaniel.math.api.Point;
import me.shedaniel.math.api.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.gui.entries.RecipeEntry;
import me.shedaniel.rei.gui.entries.SimpleRecipeEntry;
import me.shedaniel.rei.gui.widget.EntryWidget;
import me.shedaniel.rei.gui.widget.LabelWidget;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SoulSeparationCategory implements RecipeCategory<SoulSeparationDisplay> {
	
	private static DecimalFormat df = new DecimalFormat(".#");
	
	@Override
	public Identifier getIdentifier() {
		return SoulMagicPlugin.SOUL_SEPARATION;
	}
	
	@Override
    public EntryStack getLogo() {
        return EntryStack.create(ModBlocks.SOUL_SEPARATOR);
    }

	@Override
	public String getCategoryName() {
		return I18n.translate("soulmagic.rei.category.soul_separation");
	}

    @Override
    public RecipeEntry getSimpleRenderer(SoulSeparationDisplay recipe) {
    	return SimpleRecipeEntry.create(recipe::getInputEntries, recipe::getOutputEntries);
    }
    
    @Override
    public List<Widget> setupDisplay(Supplier<SoulSeparationDisplay> recipeDisplaySupplier, Rectangle bounds) {
    	SoulSeparationDisplay recipeDisplay = recipeDisplaySupplier.get();
        
        Point startPoint = new Point((int) bounds.getCenterX() - 63, (int) bounds.getCenterY() - 30);
        List<Widget> widgets = new LinkedList<>(Arrays.asList(new RecipeBaseWidget(bounds) {
            @Override
            public void render(int mouseX, int mouseY, float delta) {
                super.render(mouseX, mouseY, delta);
                //RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                //GuiLighting.disable();
                MinecraftClient.getInstance().getTextureManager().bindTexture(SoulSeparatorScreen.rl);
                blit(startPoint.x, startPoint.y, 43, 13, 126, 61);
                
                int width = MathHelper.ceil(System.currentTimeMillis() / 150 % 37d);
                blit(startPoint.x + 24, startPoint.y + 22, 176, 14, width, 17);
            }
        }));
        
        List<List<EntryStack>> input = recipeDisplay.getInputEntries();
        if(input.size() > 0) {
        	widgets.add(EntryWidget.create(startPoint.x + 1, startPoint.y + 22).entries(input.get(0)));
        }
        widgets.add(EntryWidget.create(startPoint.x + 69, startPoint.y + 22).entries(recipeDisplay.getSoulStaffs()).noBackground());
        widgets.add(EntryWidget.create(startPoint.x + 109, startPoint.y + 22).entries(recipeDisplay.getOutputEntries()).noBackground());
        String s = df.format(recipeDisplay.chance * 100) + "%";
        widgets.add(LabelWidget.create(new Point(startPoint.x + 118, startPoint.y + 42), s));
        return widgets;
    }
    
	@Override
	public int getDisplayHeight() {
		return 73;
	}
}