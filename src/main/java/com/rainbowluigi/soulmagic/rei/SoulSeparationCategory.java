package com.rainbowluigi.soulmagic.rei;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import com.rainbowluigi.soulmagic.block.ModBlocks;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.entries.RecipeEntry;
import me.shedaniel.rei.gui.entries.SimpleRecipeEntry;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.impl.widgets.LabelWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

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
    public List<Widget> setupDisplay(SoulSeparationDisplay recipeDisplay, Rectangle bounds) {
    
        Point startPoint = new Point((int) bounds.getCenterX() - 63, (int) bounds.getCenterY() - 30);
        List<Widget> widgets = new LinkedList<>();
        /*    @Override
            public void render(int mouseX, int mouseY, float delta) {
                super.render(mouseX, mouseY, delta);
                //RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                //GuiLighting.disable();
                MinecraftClient.getInstance().getTextureManager().bindTexture(SoulSeparatorScreen.rl);
                blit(startPoint.x, startPoint.y, 43, 13, 126, 61);
                
                int width = MathHelper.ceil(System.currentTimeMillis() / 150 % 37d);
                blit(startPoint.x + 24, startPoint.y + 22, 176, 14, width, 17);
            }
        }));*/
        
        List<List<EntryStack>> input = recipeDisplay.getInputEntries();
        if(input.size() > 0) {
        	widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 22)).entries(input.get(0)));
        }
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 69, startPoint.y + 22)).entries(recipeDisplay.getSoulStaffs()).backgroundEnabled(false));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 109, startPoint.y + 22)).entries(recipeDisplay.getOutputEntries()).backgroundEnabled(false));
        String s = df.format(recipeDisplay.chance * 100);
        widgets.add(new LabelWidget(new Point(startPoint.x + 118, startPoint.y + 42), new TranslatableText("soulmagic.rei.soul_separation_percentage", s)));
        return widgets;
    }
    
	@Override
	public int getDisplayHeight() {
		return 73;
	}
}