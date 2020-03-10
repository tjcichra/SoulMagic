package com.rainbowluigi.soulmagic.item.crafting;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.util.SoulUtils;

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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SoulSeparatorRecipe implements Recipe<Inventory> {

	protected Identifier id;
	protected String group;
	protected Ingredient input;
	protected ItemStack output;
	protected float chance;
	protected Map<SoulType, int[]> soulMap;
	protected int cookTime;
	
	public SoulSeparatorRecipe(Identifier id, String group, Ingredient input, Map<SoulType, int[]> maxMap, float chance, ItemStack stack, int cookTime) {
		this.id = id;
		this.group = group;
		this.input = input;
		this.output = stack;
		this.chance = chance;
		this.soulMap = maxMap;
		this.cookTime = cookTime;
	}
	
	public void postCraft(Inventory inv, World var2, Map<SoulType, Integer> soulMap2) {
		ItemStack result = this.craft(inv);
		inv.getInvStack(0).decrement(1);

		if (inv.getInvStack(0).isEmpty()) {
			inv.setInvStack(0, this.getRemainingStacks(inv).get(0));
		}
		
		if(!result.isEmpty() && Math.random() <= this.getChance()) {
			if(inv.getInvStack(1).isEmpty()) {
				inv.setInvStack(1, result);
			} else {
				inv.getInvStack(1).setCount(inv.getInvStack(1).getCount() + result.getCount());
			}
		}
	}
	
	@Override
	public boolean matches(Inventory inv, World var2) {
		return this.input.test(inv.getInvStack(0));
	}

	@Override
	public ItemStack craft(Inventory inv) {
		return this.output.copy();
	}

	@Override
	public boolean fits(int height, int width) {
		return true;
	}
	
	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		DefaultedList<Ingredient> inputs = DefaultedList.of();
		inputs.add(this.input);
		return inputs;
	}
	
	public Map<SoulType, Integer> getSoulMap(Inventory inv, World w) {
		Map<SoulType, Integer> soulMap = Maps.newHashMap();
		for(Entry<SoulType, int[]> entry : this.soulMap.entrySet()) {
			int value = MathHelper.nextInt(w.random, entry.getValue()[0], entry.getValue()[1]);
			soulMap.put(entry.getKey(), value);
		}
		
		return soulMap;
	}

	public float getChance() {
		return this.chance;
	}
	
	@Override
	public ItemStack getOutput() {
		return this.output;
	}

	@Override
	public Identifier getId() {
		return this.id;
	}
	
	public int getCookTime() {
		return this.cookTime;
	}
	
	public boolean getFiling() {
		return false;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.SOUL_SEPARATION;
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipes.SOUL_SEPARATOR_TYPE;
	}

	public static class Serializer implements RecipeSerializer<SoulSeparatorRecipe> {
		
		@Override
		public SoulSeparatorRecipe read(Identifier recipeId, JsonObject json) {
			String s = JsonHelper.getString(json, "group", "");
			Ingredient input = Ingredient.fromJson(JsonHelper.getObject(json, "ingredient"));
			
			float chance = JsonHelper.getFloat(json, "chance", 0);
			ItemStack output = JsonHelper.hasElement(json, "result") ? ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result")) : ItemStack.EMPTY;
			Map<SoulType, int[]> soulMap = SoulUtils.deserializeSoulMap2(JsonHelper.getArray(json, "souls"));
			int cookTime = JsonHelper.getInt(json, "cookingtime");
			
			return new SoulSeparatorRecipe(recipeId, s, input, soulMap, chance, output, cookTime);
		}

		@Override
		public SoulSeparatorRecipe read(Identifier recipeId, PacketByteBuf buffer) {
			String group = buffer.readString();
			Ingredient input = Ingredient.fromPacket(buffer);
			
			int n = buffer.readInt();
			Map<SoulType, int[]> soulMap = Maps.newHashMap();
			for(int l = 0; l < n; l++) {
				soulMap.put(ModSoulTypes.SOUL_TYPE_REG.get(buffer.readIdentifier()), new int[] {buffer.readInt(), buffer.readInt()});
			}
			
			float chance = buffer.readFloat();
			ItemStack output = buffer.readItemStack();
			int cookTime = buffer.readInt();
			return new SoulSeparatorRecipe(recipeId, group, input, soulMap, chance, output, cookTime);
		}

		@Override
		public void write(PacketByteBuf buffer, SoulSeparatorRecipe recipe) {
			buffer.writeString(recipe.group);
			recipe.input.write(buffer);
			
			buffer.writeInt(recipe.soulMap.size());
			for(Entry<SoulType, int[]> entry : recipe.soulMap.entrySet()) {
				buffer.writeIdentifier(ModSoulTypes.SOUL_TYPE_REG.getId(entry.getKey()));
				buffer.writeInt(entry.getValue()[0]);
				buffer.writeInt(entry.getValue()[1]);
			}
			
			buffer.writeFloat(recipe.chance);
			buffer.writeItemStack(recipe.output);
			buffer.writeInt(recipe.cookTime);
		}
	}
}
