package com.rainbowluigi.soulmagic.item.crafting;

import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipes {

	public static final RecipeType<SoulInfusionRecipe> SOUL_ESSENCE_INFUSION_TYPE = new RecipeTypeImpl<>("soul_essence_infusion");
	public static final RecipeType<SoulSeparatorRecipe> SOUL_ESSENCE_SEPARATION_TYPE = new RecipeTypeImpl<>("soul_essence_separation");
	
	public static final RecipeSerializer<ShapedSoulStaffRecipe> SOUL_ESSENCE_STAFF_SHAPED = new ShapedSoulStaffRecipe.Serializer();
	public static final RecipeSerializer<SoulInfusionRecipe> SOUL_ESSENCE_INFUSION = new SoulInfusionRecipe.Serializer();
	public static final RecipeSerializer<SpellInfusionRecipe> SPELL_INFUSION = new SpellInfusionRecipe.Serializer();
	public static final RecipeSerializer<EnchantmentInfusionRecipe> ENCHANTMENT_INFUSION = new EnchantmentInfusionRecipe.Serializer();
	public static final RecipeSerializer<SoulSeparatorRecipe> SOUL_ESSENCE_SEPARATION = new SoulSeparatorRecipe.Serializer();
	public static final SpecialRecipeSerializer<SoulStaffTransferRecipe> SOUL_ESSENCE_STAFF_TRANSFER = new SpecialRecipeSerializer<>(SoulStaffTransferRecipe::new);
	
	public static void registerRecipeTypes() {
		registerRecipeType(SOUL_ESSENCE_INFUSION_TYPE, "soul_essence_infusion");
		registerRecipeType(SOUL_ESSENCE_SEPARATION_TYPE, "soul_essence_separation");
	}
	
	public static void registerRecipeSerializers() {
		registerRecipeSerializer(SOUL_ESSENCE_STAFF_SHAPED, "soul_essence_staff_shaped");
		registerRecipeSerializer(SOUL_ESSENCE_INFUSION, "soul_essence_infusion");
		registerRecipeSerializer(SPELL_INFUSION, "spell_infusion");
		registerRecipeSerializer(ENCHANTMENT_INFUSION, "enchantment_infusion");
		registerRecipeSerializer(SOUL_ESSENCE_SEPARATION, "soul_essence_separation");
		registerRecipeSerializer(SOUL_ESSENCE_STAFF_TRANSFER, "soul_essence_staff_transfer");
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
