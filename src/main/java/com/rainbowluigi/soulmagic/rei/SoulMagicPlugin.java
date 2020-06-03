package com.rainbowluigi.soulmagic.rei;

import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.client.screen.SoulSeparatorScreen;
import com.rainbowluigi.soulmagic.item.crafting.SoulInfusionRecipe;
import com.rainbowluigi.soulmagic.item.crafting.SoulSeparatorRecipe;
import com.rainbowluigi.soulmagic.item.crafting.SpellInfusionRecipe;
import com.rainbowluigi.soulmagic.util.Reference;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.REIHelper;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;
import net.minecraft.util.Identifier;

public class SoulMagicPlugin implements REIPluginV0 {
	
	//The identifier for the plugin itself
	public static final Identifier PLUGIN = new Identifier(Reference.MOD_ID, "rei_plugin");

	//The identifiers for the categories
	public static final Identifier SOUL_INFUSION = new Identifier(Reference.MOD_ID, "plugins/soul_infusion");
	public static final Identifier SOUL_SEPARATION = new Identifier(Reference.MOD_ID, "plugins/soul_separation");

	//States where the display textures are
	private static final Identifier DISPLAY_TEXTURE = new Identifier(Reference.MOD_ID, "textures/gui/rei/display.png");
	private static final Identifier DISPLAY_TEXTURE_DARK = new Identifier(Reference.MOD_ID, "textures/gui/rei/dark_display.png");

	//Returns which display texture to use depending on what the current theme is
	public static Identifier getDisplayTexture() {
        return REIHelper.getInstance().isDarkThemeEnabled() ? DISPLAY_TEXTURE_DARK : DISPLAY_TEXTURE;
	}
	
	@Override
	public Identifier getPluginIdentifier() {
		return PLUGIN;
	}

	@Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategory(new SoulInfusionCategory());
		recipeHelper.registerCategory(new SoulSeparationCategory());
	}
	
    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
    	recipeHelper.registerRecipes(SOUL_INFUSION, SoulInfusionRecipe.class, SoulInfusionDisplay::new);
    	recipeHelper.registerRecipes(SOUL_INFUSION, SpellInfusionRecipe.class, SpellInfusionDisplay::new);
    	recipeHelper.registerRecipes(SOUL_SEPARATION, SoulSeparatorRecipe.class, SoulSeparationDisplay::new);
    }
    
    @Override
    public void registerOthers(RecipeHelper recipeHelper) {
    	recipeHelper.registerWorkingStations(SOUL_INFUSION, EntryStack.create(ModBlocks.SOUL_INFUSER));
    	recipeHelper.registerWorkingStations(SOUL_SEPARATION, EntryStack.create(ModBlocks.SOUL_SEPARATOR));
    	
    	recipeHelper.registerScreenClickArea(new Rectangle(63, 31, 46, 25), SoulSeparatorScreen.class, SOUL_SEPARATION);
    }

	@Override
	public SemanticVersion getMinimumVersion() throws VersionParsingException {
		return SemanticVersion.parse("1.0");
	}
}