package com.rainbowluigi.soulmagic.item.crafting;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.util.SoulUtils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.World;

public class SoulInfusionRecipe implements Recipe<Inventory> {
	
	protected Identifier id;
	protected String group;
	protected DefaultedList<Ingredient> inputs;
	protected ItemStack output;
	protected Map<SoulType, Integer> soulMap;
	protected int progressColor;
	
	public SoulInfusionRecipe(Identifier id, String group, DefaultedList<Ingredient> inputs, Map<SoulType, Integer> soulMap, int progressColor, ItemStack output) {
		this.id = id;
		this.group = group;
		this.inputs = inputs;
		this.output = output;
		this.soulMap = soulMap;
		this.progressColor = progressColor;
	}
	
	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return this.inputs;
	}

	@Override
	public ItemStack craft(Inventory sibe) {
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
		return ModRecipes.SOUL_INFUSION_TYPE;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(ModBlocks.SOUL_INFUSER);
	}
	
	public Map<SoulType, Integer> getSoulMap() {
		return this.soulMap;
	}
	
	public int getProgressColor() {
		return this.progressColor;
	}
	
	@Override
	public boolean matches(Inventory inv, World worldIn) {
		for(int i = 0; i < this.inputs.size(); i++) {
			if(!this.inputs.get(i).test(inv.getInvStack(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.SOUL_INFUSION;
	}
	
	public static class Serializer implements RecipeSerializer<SoulInfusionRecipe> {
		
		@Override
		public SoulInfusionRecipe read(Identifier recipeId, JsonObject json) {
			String s = JsonHelper.getString(json, "group", "");
			int color = JsonHelper.getInt(json, "color", 0xFFFFFF);
			DefaultedList<Ingredient> inputs = readIngredients(JsonHelper.getArray(json, "ingredients"));
			
			ItemStack stack = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));
			Map<SoulType, Integer> soulMap = SoulUtils.deserializeSoulMap(JsonHelper.getObject(json, "soul"));
			
			return new SoulInfusionRecipe(recipeId, s, inputs, soulMap, color, stack);
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
				soulMap.put(ModSoulTypes.SOUL_TYPE_REG.get(buffer.readIdentifier()), buffer.readInt());
			}
			
			ItemStack output = buffer.readItemStack();
			return new SoulInfusionRecipe(recipeId, group, inputs, null, color, output);
		}

		@Override
		public void write(PacketByteBuf buffer, SoulInfusionRecipe recipe) {
			buffer.writeString(recipe.group);
			buffer.writeInt(recipe.progressColor);
			buffer.writeVarInt(recipe.inputs.size());
			
			for(Ingredient i : recipe.inputs) {
				i.write(buffer);
			}
			
			buffer.writeInt(recipe.getSoulMap().size());
			for(Entry<SoulType, Integer> entry : recipe.getSoulMap().entrySet()) {
				buffer.writeIdentifier(ModSoulTypes.SOUL_TYPE_REG.getId(entry.getKey()));
				buffer.writeInt(entry.getValue());
			}
			
			buffer.writeItemStack(recipe.output);
		}
	}
}
