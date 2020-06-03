package com.rainbowluigi.soulmagic.mixin;

import java.util.List;
import java.util.Map.Entry;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.rainbowluigi.soulmagic.item.crafting.ShapedSoulStaffRecipe;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.impl.widgets.LabelWidget;
import me.shedaniel.rei.plugin.crafting.DefaultCraftingDisplay;
import net.minecraft.text.LiteralText;

//@Mixin(DefaultCraftingCategory.class)
public class DefaultCraftingCategoryMixin {

	//@Inject(method = "setupDisplay", at = @At("RETURN"))
	public <T> void setupDisplay(DefaultCraftingDisplay display, Rectangle bounds, CallbackInfoReturnable<List<Widget>> call) {
		
		if (display.getOptionalRecipe().orElse(null) instanceof ShapedSoulStaffRecipe) {
			ShapedSoulStaffRecipe recipe = (ShapedSoulStaffRecipe) display.getOptionalRecipe().orElse(null);
			
			String s = "| ";
			for(Entry<SoulType, Integer> e : recipe.getSoulMap().entrySet()) {
				s += e.getKey().getName().asString() + " " + e.getValue() + " | ";
			}
			call.getReturnValue().add(new LabelWidget(new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27), new LiteralText(s)));
		}
	}
}