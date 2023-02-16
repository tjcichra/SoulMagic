package com.rainbowluigi.soulmagic.rei;

import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.item.crafting.ModRecipes;
import com.rainbowluigi.soulmagic.item.crafting.SoulInfusionRecipe;
import com.rainbowluigi.soulmagic.rei.autocrafting.SoulEssenceTransferHandler;
import com.rainbowluigi.soulmagic.util.Reference;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SoulMagicClientPlugin implements REIClientPlugin {
    //The identifiers for the categories
    public static final CategoryIdentifier<SoulInfusionDisplay> SOUL_INFUSION = CategoryIdentifier.of(Reference.MOD_ID, "plugins/soul_infusion");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new SoulInfusionCategory());

        registry.addWorkstations(SOUL_INFUSION, EntryStacks.of(ModBlocks.SOUL_ESSENCE_INFUSER));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(SoulInfusionRecipe.class, ModRecipes.SOUL_ESSENCE_INFUSION_TYPE, SoulInfusionDisplay::new);
    }

    //States where the display textures are
    private static final Identifier DISPLAY_TEXTURE = new Identifier(Reference.MOD_ID, "textures/gui/rei/display.png");
    private static final Identifier DISPLAY_TEXTURE_DARK = new Identifier(Reference.MOD_ID, "textures/gui/rei/dark_display.png");

    //Returns which display texture to use depending on what the current theme is
    public static Identifier getDisplayTexture(boolean dark) {
        return dark ? DISPLAY_TEXTURE_DARK : DISPLAY_TEXTURE;
    }
//
//	@Override
//	public Identifier getPluginIdentifier() {
//		return PLUGIN;
//	}
//
//	@Override
//	public void registerPluginCategories(RecipeHelper recipeHelper) {
//		if(!ConfigObject.getInstance().isLoadingDefaultPlugin()) {
//			return;
//		}
//
//		recipeHelper.registerCategories(
//			new SoulInfusionCategory(),
//			new SoulSeparationCategory()
//		);
//	}
//
//	@Override
//	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
//		if(!ConfigObject.getInstance().isLoadingDefaultPlugin()) {
//			return;
//		}
//
//		recipeHelper.registerRecipes(SOUL_INFUSION, SoulInfusionRecipe.class, SoulInfusionDisplay::new);
//		recipeHelper.registerRecipes(SOUL_SEPARATION, SoulSeparatorRecipe.class, SoulSeparationDisplay::new);
//	}
//
//	@Override
//	public void registerOthers(RecipeHelper recipeHelper) {
//		if(!ConfigObject.getInstance().isLoadingDefaultPlugin()) {
//			return;
//		}
//
//		//ContainerInfoHandler.registerContainerInfo(new Identifier(Reference.MOD_ID, "plugins/crafting"), CraftingContainerInfoWrapper.create(SoulInfuserScreenHandler.class));
//
//		recipeHelper.registerWorkingStations(SOUL_INFUSION, EntryStack.create(ModBlocks.SOUL_ESSENCE_INFUSER));
//		recipeHelper.registerWorkingStations(SOUL_SEPARATION, EntryStack.create(ModBlocks.SOUL_SEPARATOR));
//
//		recipeHelper.registerScreenClickArea(new Rectangle(63, 31, 46, 25), SoulSeparatorScreen.class, SOUL_SEPARATION);
//	}

    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        registry.register(new SoulEssenceTransferHandler());
    }
}