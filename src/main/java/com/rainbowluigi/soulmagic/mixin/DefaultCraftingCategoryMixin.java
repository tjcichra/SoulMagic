package com.rainbowluigi.soulmagic.mixin;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.rainbowluigi.soulmagic.item.crafting.ShapedSoulStaffRecipe;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.impl.widgets.LabelWidget;
import me.shedaniel.rei.plugin.crafting.DefaultCraftingCategory;
import me.shedaniel.rei.plugin.crafting.DefaultCraftingDisplay;
import net.minecraft.text.LiteralText;

@Mixin(DefaultCraftingCategory.class)
public class DefaultCraftingCategoryMixin {

	@Inject(method = "setupDisplay", at = @At("RETURN"))
	public <T> void setupDisplay(Supplier<DefaultCraftingDisplay> recipeDisplaySupplier, Rectangle bounds, CallbackInfoReturnable<List<Widget>> call) {
		
		if (recipeDisplaySupplier.get().getOptionalRecipe().orElse(null) instanceof ShapedSoulStaffRecipe) {
			ShapedSoulStaffRecipe recipe = (ShapedSoulStaffRecipe) recipeDisplaySupplier.get().getOptionalRecipe().orElse(null);
			
			String s = "| ";
			for(Entry<SoulType, Integer> e : recipe.getSoulMap().entrySet()) {
				s += e.getKey().getName().asString() + " " + e.getValue() + " | ";
			}
			call.getReturnValue().add(new LabelWidget(new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27), new LiteralText(s)));
		}
	}
}