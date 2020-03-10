package com.rainbowluigi.soulmagic.item.crafting;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.util.SoulUtils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class EnchantmentInfusionRecipe extends SoulInfusionRecipe {
	
	public Enchantment enchantment;
	
	public EnchantmentInfusionRecipe(Identifier id, String group, DefaultedList<Ingredient> inputs, Map<SoulType, Integer> soulMap, int progressColor, Enchantment enchantment) {
		super(id, group, inputs, soulMap, progressColor, ItemStack.EMPTY);
		this.enchantment = enchantment;
	}
	
	@Override
	public boolean matches(Inventory sibe, World worldIn) {
		for (int i = 0; i < this.inputs.size(); i++) {
			if (!this.inputs.get(i).test(sibe.getInvStack(i))) {
				return false;
			}
		}
		
		ItemStack stack = sibe.getInvStack(8);
		
		if(!this.enchantment.isAcceptableItem(stack)) {
			return false;
		}
			
		//if(this.spell.isBase() && SoulGemHelper.getSpellType(stack) == null || this.spell.getParent() == SoulGemHelper.getSpellType(stack) || SoulGemHelper.getSpellType(stack) == ModSpellTypes.ULTIMATE) {
		//	return true;
		//}
		
		//return false;
		return true;
	}

	@Override
	public ItemStack craft(Inventory sibe) {
		ItemStack stack = sibe.getInvStack(8).copy();
		//FIX THIS PLZ
		stack.addEnchantment(this.enchantment, 1);
		return stack;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.ENCHANTMENT_INFUSION;
	}
	
	public static class Serializer implements RecipeSerializer<EnchantmentInfusionRecipe> {
		
		@Override
		public EnchantmentInfusionRecipe read(Identifier recipeId, JsonObject json) {
			String s = JsonHelper.getString(json, "group", "");
			int color = JsonHelper.getInt(json, "color", 0xFFFFFF);
			DefaultedList<Ingredient> inputs = readIngredients(JsonHelper.getArray(json, "ingredients"));
			
			Enchantment enchantment = Registry.ENCHANTMENT.get(new Identifier(JsonHelper.getString(json, "enchantment")));
			Map<SoulType, Integer> soulMap = SoulUtils.deserializeSoulMap(JsonHelper.getObject(json, "soul"));
			
			return new EnchantmentInfusionRecipe(recipeId, s, inputs, soulMap, color, enchantment);
		}
		
		private static DefaultedList<Ingredient> readIngredients(JsonArray array) {
			DefaultedList<Ingredient> nonnulllist = DefaultedList.of();

			for(int i = 0; i < array.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(array.get(i));
				nonnulllist.add(ingredient);
			}
			
			return nonnulllist;
		}

		@Override
		public EnchantmentInfusionRecipe read(Identifier recipeId, PacketByteBuf buffer) {
			String group = buffer.readString();
			int color = buffer.readInt();
			int i = buffer.readVarInt();
			
			DefaultedList<Ingredient> inputs = DefaultedList.ofSize(i, Ingredient.EMPTY);
			for(int j = 0; j < inputs.size(); j++) {
				inputs.set(j, Ingredient.fromPacket(buffer));
			}
			
			Enchantment enchantment = Registry.ENCHANTMENT.get(buffer.readIdentifier());
			
			int n = buffer.readInt();
			Map<SoulType, Integer> soulMap = Maps.newHashMap();
			for(int l = 0; l < n; l++) {
				soulMap.put(ModSoulTypes.SOUL_TYPE_REG.get(buffer.readIdentifier()), buffer.readInt());
			}
			
			return new EnchantmentInfusionRecipe(recipeId, group, inputs, soulMap, color, enchantment);
		}

		@Override
		public void write(PacketByteBuf buffer, EnchantmentInfusionRecipe recipe) {
			buffer.writeString(recipe.group);
			buffer.writeInt(recipe.progressColor);
			buffer.writeVarInt(recipe.inputs.size());
			
			for(Ingredient i : recipe.inputs) {
				i.write(buffer);
			}
			
			buffer.writeIdentifier(Registry.ENCHANTMENT.getId(recipe.enchantment));
			
			buffer.writeInt(recipe.getSoulMap().size());
			for(Entry<SoulType, Integer> entry : recipe.getSoulMap().entrySet()) {
				buffer.writeIdentifier(ModSoulTypes.SOUL_TYPE_REG.getId(entry.getKey()));
				buffer.writeInt(entry.getValue());
			}
		}
	}
}
