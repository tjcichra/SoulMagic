package com.rainbowluigi.soulmagic.item.crafting;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rainbowluigi.soulmagic.item.Upgradeable;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.spell.ModSpells;
import com.rainbowluigi.soulmagic.spell.Spell;
import com.rainbowluigi.soulmagic.spelltype.ModSpellTypes;
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

public class SpellInfusionRecipe extends SoulInfusionRecipe {
	
	public Spell spell;
	
	public SpellInfusionRecipe(Identifier id, String group, DefaultedList<Ingredient> inputs, Map<SoulType, Integer> soulMap, int progressColor, Spell spell) {
		super(id, group, inputs, soulMap, progressColor, ItemStack.EMPTY);
		this.spell = spell;
	}
	
	@Override
	public boolean matches(Inventory sibe, World worldIn) {
		for (int i = 0; i < this.inputs.size(); i++) {
			if (!this.inputs.get(i).test(sibe.getStack(i))) {
				return false;
			}
		}
		
		ItemStack stack = sibe.getStack(8);
			
		if(this.spell.isBase() && SoulGemHelper.getSpellType(stack) == null || this.spell.getParent() == SoulGemHelper.getSpellType(stack) || SoulGemHelper.getSpellType(stack) == ModSpellTypes.ULTIMATE) {
			return true;
		}
		
		return false;
	}

	@Override
	public ItemStack craft(Inventory sibe) {
		ItemStack stack = sibe.getStack(8).copy();
		if(SoulGemHelper.getSpellType(stack) == null) {
			SoulGemHelper.setSpellType(stack, this.spell.getParent());
		}
		
		if(stack.getItem() instanceof Upgradeable) {
			Upgradeable u = (Upgradeable) stack.getItem();

			u.addUpgrade(stack, SoulGemHelper.getSpellType(stack).getBaseUpgrade());
			u.incrementSelectorPoints(stack);
			u.setUpgradeSelection(stack, SoulGemHelper.getSpellType(stack).getBaseUpgrade(), true);
		}
		return stack;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.SPELL_INFUSION;
	}
	
	public static class Serializer implements RecipeSerializer<SpellInfusionRecipe> {
		
		@Override
		public SpellInfusionRecipe read(Identifier recipeId, JsonObject json) {
			String s = JsonHelper.getString(json, "group", "");
			int color = JsonHelper.getInt(json, "color", 0xFFFFFF);
			DefaultedList<Ingredient> inputs = readIngredients(JsonHelper.getArray(json, "ingredients"));
			
			Spell spell = ModSpells.SPELL.get(new Identifier(JsonHelper.getString(json, "spell")));
			Map<SoulType, Integer> soulMap = SoulUtils.deserializeSoulMap(JsonHelper.getObject(json, "soul"));
			
			return new SpellInfusionRecipe(recipeId, s, inputs, soulMap, color, spell);
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
		public SpellInfusionRecipe read(Identifier recipeId, PacketByteBuf buffer) {
			String group = buffer.readString();
			int color = buffer.readInt();
			int i = buffer.readVarInt();
			
			DefaultedList<Ingredient> inputs = DefaultedList.ofSize(i, Ingredient.EMPTY);
			for(int j = 0; j < inputs.size(); j++) {
				inputs.set(j, Ingredient.fromPacket(buffer));
			}
			
			Spell spell = ModSpells.SPELL.get(buffer.readIdentifier());
			
			int n = buffer.readInt();
			Map<SoulType, Integer> soulMap = Maps.newHashMap();
			for(int l = 0; l < n; l++) {
				soulMap.put(ModSoulTypes.SOUL_TYPE.get(buffer.readIdentifier()), buffer.readInt());
			}
			
			return new SpellInfusionRecipe(recipeId, group, inputs, soulMap, color, spell);
		}

		@Override
		public void write(PacketByteBuf buffer, SpellInfusionRecipe recipe) {
			buffer.writeString(recipe.group);
			buffer.writeInt(recipe.progressColor);
			buffer.writeVarInt(recipe.inputs.size());
			
			for(Ingredient i : recipe.inputs) {
				i.write(buffer);
			}
			
			buffer.writeIdentifier(ModSpells.SPELL.getId(recipe.spell));
			
			buffer.writeInt(recipe.getSoulMap().size());
			for(Entry<SoulType, Integer> entry : recipe.getSoulMap().entrySet()) {
				buffer.writeIdentifier(ModSoulTypes.SOUL_TYPE.getId(entry.getKey()));
				buffer.writeInt(entry.getValue());
			}
		}
	}
}
