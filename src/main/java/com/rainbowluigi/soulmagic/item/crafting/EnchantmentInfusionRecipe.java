package com.rainbowluigi.soulmagic.item.crafting;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rainbowluigi.soulmagic.block.entity.SoulEssenceInfuserBlockEntity;
import com.rainbowluigi.soulmagic.item.EnchantmentTemplateItem;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class EnchantmentInfusionRecipe extends SoulInfusionRecipe {
	
	public EnchantmentInfusionRecipe(Identifier id, String group, DefaultedList<Ingredient> inputs, Upgrade[] upgradesNeeded, int progressColor) {
		super(id, group, inputs, Collections.EMPTY_MAP, upgradesNeeded, progressColor, ItemStack.EMPTY);
	}
	
	@Override
	public boolean matches(SoulEssenceInfuserBlockEntity sibe, World worldIn) {
		for (int i = 0; i < this.inputs.size(); i++) {
			if (!this.inputs.get(i).test(sibe.getStack(i))) {
				return false;
			}
		}

		if(sibe.getStack(8).hasEnchantments()) {
			return false;
		}

		ItemStack template = sibe.getStack(0);
		EnchantmentTemplateItem eti = (EnchantmentTemplateItem) template.getItem();

		return eti.getTarget().isAcceptableItem(sibe.getStack(8).getItem());
	}

	@Override
	public ItemStack craft(SoulEssenceInfuserBlockEntity sibe) {
		ItemStack template = sibe.getStack(0);
		EnchantmentTemplateItem eti = (EnchantmentTemplateItem) template.getItem();
		
		ItemStack item = sibe.getStack(8).copy();

		Map<Enchantment, Integer> enchantmentMap = eti.getEnchantments(template);
		for(Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()) {
			item.addEnchantment(entry.getKey(), entry.getValue());
		}

		return item;
	}

	@Override
	public Map<SoulType, Integer> getSoulMap(Inventory inv, World worldIn) {
		ItemStack template = inv.getStack(0);
		EnchantmentTemplateItem eti = (EnchantmentTemplateItem) template.getItem();

		return eti.getCost(template);
	}

	@Override
	public DefaultedList<ItemStack> getRemainingStacks(SoulEssenceInfuserBlockEntity inventory) {
		DefaultedList<ItemStack> defaultedList = super.getRemainingStacks(inventory);
		defaultedList.set(0, inventory.getStack(0).copy());
		return defaultedList;
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
			Upgrade[] upgradesNeeded = {ModUpgrades.ENCHANTING_COMPONENT};

			return new EnchantmentInfusionRecipe(recipeId, s, inputs, upgradesNeeded, color);
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
			
			Upgrade[] upgradesNeeded = {ModUpgrades.ENCHANTING_COMPONENT};
			return new EnchantmentInfusionRecipe(recipeId, group, inputs, upgradesNeeded, color);
		}

		@Override
		public void write(PacketByteBuf buffer, EnchantmentInfusionRecipe recipe) {
			buffer.writeString(recipe.group);
			buffer.writeInt(recipe.progressColor);
			buffer.writeVarInt(recipe.inputs.size());
			
			for(Ingredient i : recipe.inputs) {
				i.write(buffer);
			}
		}
	}
}
