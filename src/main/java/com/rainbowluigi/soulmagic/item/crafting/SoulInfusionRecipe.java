package com.rainbowluigi.soulmagic.item.crafting;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.block.entity.SoulEssenceInfuserBlockEntity;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.util.SoulUtils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class SoulInfusionRecipe implements Recipe<SoulEssenceInfuserBlockEntity> {
	
	protected Identifier id;
	protected String group;
	protected DefaultedList<Ingredient> inputs;
	protected ItemStack output;
	public Map<SoulType, Integer> soulMap;
	protected Upgrade[] upgradesNeeded;
	protected int progressColor;
	
	public SoulInfusionRecipe(Identifier id, String group, DefaultedList<Ingredient> inputs, Map<SoulType, Integer> soulMap, Upgrade[] upgradesNeeded, int progressColor, ItemStack output) {
		this.id = id;
		this.group = group;
		this.inputs = inputs;
		this.output = output;
		this.soulMap = soulMap;
		this.upgradesNeeded = upgradesNeeded;
		this.progressColor = progressColor;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return this.inputs;
	}

	@Override
	public ItemStack craft(SoulEssenceInfuserBlockEntity sibe) {
		return this.output.copy();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean fits(int var1, int var2) {
		return true;
	}

	@Override
	public ItemStack getOutput() {
		return this.output;
	}

	@Override
	public Identifier getId() {
		return this.id;
	}
	
	@Override
	public RecipeType<?> getType() {
		return ModRecipes.SOUL_ESSENCE_INFUSION_TYPE;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack createIcon() {
		return new ItemStack(ModBlocks.SOUL_ESSENCE_INFUSER);
	}
	
	public Map<SoulType, Integer> getSoulMap(Inventory inv, World worldIn) {
		return this.soulMap;
	}
	
	public int getProgressColor() {
		return this.progressColor;
	}

	public Upgrade[] getUpgradesNeeded() {
		return this.upgradesNeeded;
	}
	
	@Override
	public boolean matches(SoulEssenceInfuserBlockEntity inv, World worldIn) {
		for(Upgrade u : this.upgradesNeeded) {
			if(!inv.getSelectedUpgrades().contains(u)) {
				return false;
			}
		}

		for(int i = 0; i < this.inputs.size(); i++) {
			if(!this.inputs.get(i).test(inv.getStack(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.SOUL_ESSENCE_INFUSION;
	}
	
	public static class Serializer implements RecipeSerializer<SoulInfusionRecipe> {
		
		@Override
		public SoulInfusionRecipe read(Identifier recipeId, JsonObject json) {
			String s = JsonHelper.getString(json, "group", "");
			int color = JsonHelper.getInt(json, "color", 0xFFFFFF);
			DefaultedList<Ingredient> inputs = readIngredients(JsonHelper.getArray(json, "ingredients"));
			
			ItemStack stack = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
			Map<SoulType, Integer> soulMap = SoulUtils.deserializeSoulMap(JsonHelper.getObject(json, "soul"));

			JsonArray ja = JsonHelper.getArray(json, "upgrades", new JsonArray());
			Upgrade[] upgradesNeeded = new Upgrade[ja.size()];

			for(int i = 0; i < ja.size(); i++) {
				upgradesNeeded[i] = ModUpgrades.UPGRADE.get(new Identifier(ja.get(i).getAsString()));
			}
			
			return new SoulInfusionRecipe(recipeId, s, inputs, soulMap, upgradesNeeded, color, stack);
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
		public SoulInfusionRecipe read(Identifier recipeId, PacketByteBuf buffer) {
			String group = buffer.readString();
			int color = buffer.readInt();
			int i = buffer.readVarInt();
			
			DefaultedList<Ingredient> inputs = DefaultedList.ofSize(i, Ingredient.EMPTY);
			for(int j = 0; j < inputs.size(); j++) {
				inputs.set(j, Ingredient.fromPacket(buffer));
			}
			
			int n = buffer.readInt();
			
			Map<SoulType, Integer> soulMap = Maps.newHashMap();
			for(int l = 0; l < n; l++) {
				soulMap.put(ModSoulTypes.SOUL_TYPE.get(buffer.readIdentifier()), buffer.readInt());
			}
			
			Upgrade[] upgradesNeeded = new Upgrade[buffer.readInt()];
			for(int j = 0; j < upgradesNeeded.length; j++) {
				upgradesNeeded[j] = ModUpgrades.UPGRADE.get(buffer.readIdentifier());
			}
			
			ItemStack output = buffer.readItemStack();
			return new SoulInfusionRecipe(recipeId, group, inputs, soulMap, upgradesNeeded, color, output);
		}

		@Override
		public void write(PacketByteBuf buffer, SoulInfusionRecipe recipe) {
			buffer.writeString(recipe.group);
			buffer.writeInt(recipe.progressColor);
			buffer.writeVarInt(recipe.inputs.size());
			
			for(Ingredient i : recipe.inputs) {
				i.write(buffer);
			}
			
			buffer.writeInt(recipe.soulMap.size());
			for(Entry<SoulType, Integer> entry : recipe.soulMap.entrySet()) {
				buffer.writeIdentifier(ModSoulTypes.SOUL_TYPE.getId(entry.getKey()));
				buffer.writeInt(entry.getValue());
			}

			buffer.writeInt(recipe.upgradesNeeded.length);
			for(Upgrade u : recipe.upgradesNeeded) {
				buffer.writeIdentifier(ModUpgrades.UPGRADE.getId(u));
			}
			
			buffer.writeItemStack(recipe.output);
		}
	}
}
