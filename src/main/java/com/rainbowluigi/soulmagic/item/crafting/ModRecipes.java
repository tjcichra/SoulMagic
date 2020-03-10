package com.rainbowluigi.soulmagic.item.crafting;

import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipes {

	public static final RecipeType<SoulInfusionRecipe> SOUL_INFUSION_TYPE = new RecipeTypeImpl<>("soul_infusion_type");
	public static final RecipeType<SoulSeparatorRecipe> SOUL_SEPARATOR_TYPE = new RecipeTypeImpl<>("soul_separator_type");
	
	public static final RecipeSerializer<ShapedSoulStaffRecipe> SOUL_STAFF_SHAPED = new ShapedSoulStaffRecipe.Serializer();
	public static final RecipeSerializer<SoulInfusionRecipe> SOUL_INFUSION = new SoulInfusionRecipe.Serializer();
	public static final RecipeSerializer<SpellInfusionRecipe> SPELL_INFUSION = new SpellInfusionRecipe.Serializer();
	public static final RecipeSerializer<EnchantmentInfusionRecipe> ENCHANTMENT_INFUSION = new EnchantmentInfusionRecipe.Serializer();
	public static final RecipeSerializer<SoulSeparatorRecipe> SOUL_SEPARATION = new SoulSeparatorRecipe.Serializer();
	public static final SpecialRecipeSerializer<SoulStaffTransferRecipe> SOUL_STAFF_TRANSFER = new SpecialRecipeSerializer<>(SoulStaffTransferRecipe::new);
	
	public static void registerRecipeTypes() {
    	registerRecipeType(SOUL_INFUSION_TYPE, "soul_infusion_type");
    	registerRecipeType(SOUL_SEPARATOR_TYPE, "soul_separator_type");
    }
	
    public static void registerRecipeSerializers() {
    	registerRecipeSerializer(SOUL_STAFF_SHAPED, "soul_staff_shaped");
    	registerRecipeSerializer(SOUL_INFUSION, "soul_infusion");
    	registerRecipeSerializer(SPELL_INFUSION, "spell_infusion");
    	registerRecipeSerializer(ENCHANTMENT_INFUSION, "enchantment_infusion");
    	registerRecipeSerializer(SOUL_SEPARATION, "soul_separation");
    	registerRecipeSerializer(SOUL_STAFF_TRANSFER, "soul_staff_transfer");
    }

    private static void registerRecipeType(RecipeType<?> recipeType, String name) {
        Registry.register(Registry.RECIPE_TYPE, new Identifier(Reference.MOD_ID, name), recipeType);
    }

    private static void registerRecipeSerializer(RecipeSerializer<?> recipeSerializer, String name) {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Reference.MOD_ID, name), recipeSerializer);
    }
    
    public static class RecipeTypeImpl<T extends Recipe<?>> implements RecipeType<T> {
    	
    	private String name;
    	
    	public RecipeTypeImpl(String name) {
    		this.name = name;
    	}
    	
    	@Override
		public String toString() {
			return name;
		}
    }
}
