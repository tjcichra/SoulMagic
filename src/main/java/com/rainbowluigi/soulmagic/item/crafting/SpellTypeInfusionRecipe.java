package com.rainbowluigi.soulmagic.item.crafting;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rainbowluigi.soulmagic.item.Upgradeable;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;
import com.rainbowluigi.soulmagic.spelltype.SpellType;
import com.rainbowluigi.soulmagic.util.SoulGemHelper;
import com.rainbowluigi.soulmagic.util.SoulUtils;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class SpellTypeInfusionRecipe extends SoulInfusionRecipe {
	
	public SpellType soulType;
	
	public SpellTypeInfusionRecipe(Identifier id, String group, DefaultedList<Ingredient> inputs, Map<SoulType, Integer> soulMap, int progressColor, SpellType soulType) {
		super(id, group, inputs, soulMap, progressColor, ItemStack.EMPTY);
		this.soulType = soulType;
	}
	
	@Override
	public boolean matches(Inventory sibe, World worldIn) {
		for (int i = 0; i < this.inputs.size(); i++) {
			if (!this.inputs.get(i).test(sibe.getStack(i))) {
				return false;
			}
		}
		
		return SoulGemHelper.getSpellType(sibe.getStack(8)) == null;
	}

	@Override
	public ItemStack craft(Inventory sibe) {
		ItemStack stack = sibe.getStack(8).copy();
		SoulGemHelper.setSpellType(stack, this.soulType);
		
		if(stack.getItem() instanceof Upgradeable) {
			Upgradeable u = (Upgradeable) stack.getItem();

			u.addUpgrade(stack, this.soulType.getBaseUpgrade());
			u.incrementSelectorPoints(stack);
			u.setUpgradeSelection(stack, SoulGemHelper.getSpellType(stack).getBaseUpgrade(), true);
		}

		return stack;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.SPELL_INFUSION;
	}

	@Override
	public ItemStack getOutput() {
		ItemStack stack = this.inputs.get(8).getMatchingStacksClient()[0];
		SoulGemHelper.setSpellType(stack, this.soulType);
		return stack;
	}
	
	public static class Serializer implements RecipeSerializer<SpellTypeInfusionRecipe> {
		
		@Override
		public SpellTypeInfusionRecipe read(Identifier recipeId, JsonObject json) {
			String s = JsonHelper.getString(json, "group", "");
			int color = JsonHelper.getInt(json, "color", 0xFFFFFF);
			DefaultedList<Ingredient> inputs = readIngredients(JsonHelper.getArray(json, "ingredients"));
			
			SpellType type = ModSpellTypes.SPELL_TYPE.get(new Identifier(JsonHelper.getString(json, "spelltype")));

			if(type == null) {
				throw new IllegalArgumentException(JsonHelper.getString(json, "spelltype") + " is not a valid spell type.");
			}

			Map<SoulType, Integer> soulMap = SoulUtils.deserializeSoulMap(JsonHelper.getObject(json, "soul"));
			
			return new SpellTypeInfusionRecipe(recipeId, s, inputs, soulMap, color, type);
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
		public SpellTypeInfusionRecipe read(Identifier recipeId, PacketByteBuf buffer) {
			String group = buffer.readString();
			int color = buffer.readInt();
			int i = buffer.readVarInt();
			
			DefaultedList<Ingredient> inputs = DefaultedList.ofSize(i, Ingredient.EMPTY);
			for(int j = 0; j < inputs.size(); j++) {
				inputs.set(j, Ingredient.fromPacket(buffer));
			}
			
			SpellType type = ModSpellTypes.SPELL_TYPE.get(buffer.readIdentifier());
			
			int n = buffer.readInt();
			Map<SoulType, Integer> soulMap = Maps.newHashMap();
			for(int l = 0; l < n; l++) {
				soulMap.put(ModSoulTypes.SOUL_TYPE.get(buffer.readIdentifier()), buffer.readInt());
			}
			
			return new SpellTypeInfusionRecipe(recipeId, group, inputs, soulMap, color, type);
		}

		@Override
		public void write(PacketByteBuf buffer, SpellTypeInfusionRecipe recipe) {
			buffer.writeString(recipe.group);
			buffer.writeInt(recipe.progressColor);
			buffer.writeVarInt(recipe.inputs.size());
			
			for(Ingredient i : recipe.inputs) {
				i.write(buffer);
			}
			
			buffer.writeIdentifier(ModSpellTypes.SPELL_TYPE.getId(recipe.soulType));
			
			buffer.writeInt(recipe.soulMap.size());
			for(Entry<SoulType, Integer> entry : recipe.soulMap.entrySet()) {
				buffer.writeIdentifier(ModSoulTypes.SOUL_TYPE.getId(entry.getKey()));
				buffer.writeInt(entry.getValue());
			}
		}
	}
}
