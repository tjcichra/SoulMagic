package com.rainbowluigi.soulmagic.item.crafting;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.util.SoulUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ShapedSoulStaffRecipe extends ShapedRecipe {

	private final Map<SoulType, Integer> soulMap;
	
	public ShapedSoulStaffRecipe(ShapedRecipe srecipe, Map<SoulType, Integer> soulMap) {
		super(srecipe.getId(), srecipe.getGroup(), srecipe.getWidth(), srecipe.getHeight(), srecipe.getIngredients(), srecipe.getOutput());
		this.soulMap = soulMap;
	}
	
	@Override
	public DefaultedList<ItemStack> getRemainder(CraftingInventory inv) {
		DefaultedList<ItemStack> nonnulllist = DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);

		for (int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack stack = inv.getStack(i);
			
			if(stack.getItem() instanceof SoulEssenceStaff staff) {
				for(Entry<SoulType, Integer> e : this.soulMap.entrySet()) {
					MinecraftClient client = MinecraftClient.getInstance();
					staff.subtractSoul(stack, client.world, e.getKey(), e.getValue());
				}
				nonnulllist.set(i, stack.copy());
			} else if (stack.getItem().hasRecipeRemainder()) {
				nonnulllist.set(i, new ItemStack(stack.getItem().getRecipeRemainder()));
			}
		}

		return nonnulllist;
	}
	
	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		for (int i = 0; i <= inv.getWidth() - inv.getWidth(); ++i) {
			for (int j = 0; j <= inv.getHeight() - this.getHeight(); ++j) {
				if (this.checkMatch(inv, i, j, true)) {
					return true;
				}

				if (this.checkMatch(inv, i, j, false)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks if the region of a crafting inventory is match for the recipe.
	 */
	private boolean checkMatch(CraftingInventory craftingInventory, int p_77573_2_, int p_77573_3_, boolean p_77573_4_) {
		for (int i = 0; i < craftingInventory.getWidth(); ++i) {
			for (int j = 0; j < craftingInventory.getHeight(); ++j) {
				int k = i - p_77573_2_;
				int l = j - p_77573_3_;
				Ingredient ingredient = Ingredient.EMPTY;
				if (k >= 0 && l >= 0 && k < this.getWidth() && l < this.getHeight()) {
					if (p_77573_4_) {
						ingredient = this.getIngredients().get(this.getWidth() - k - 1 + l * this.getWidth());
					} else {
						ingredient = this.getIngredients().get(k + l * this.getWidth());
					}
				}

				
				if (!ingredient.test(craftingInventory.getStack(i + j * craftingInventory.getWidth()))) {
					return false;
				} else if(ingredient.getMatchingStacks().length > 0 && ingredient.getMatchingStacks()[0].getItem() instanceof SoulEssenceStaff) {
					ItemStack stack = craftingInventory.getStack(i + j * craftingInventory.getWidth());
					SoulEssenceStaff staff = (SoulEssenceStaff) stack.getItem();
					
					for(Entry<SoulType, Integer> e : this.soulMap.entrySet()) {
						//TODO FIX
						MinecraftClient client = MinecraftClient.getInstance();
						if(staff.getSoul(stack, client.world, e.getKey()) < e.getValue()) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}
	
	public Map<SoulType, Integer> getSoulMap() {
		return this.soulMap;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.SOUL_ESSENCE_STAFF_SHAPED;
	}

	public static class Serializer implements RecipeSerializer<ShapedSoulStaffRecipe> {
		
		@Override
		public ShapedSoulStaffRecipe read(Identifier recipeId, JsonObject json) {
			ShapedRecipe shaped = RecipeSerializer.SHAPED.read(recipeId, json);
			Map<SoulType, Integer> soulMap = SoulUtils.deserializeSoulMap(JsonHelper.getObject(json, "soul"));
			
			return new ShapedSoulStaffRecipe(shaped, soulMap);
		}

		@Override
		public ShapedSoulStaffRecipe read(Identifier recipeId, PacketByteBuf buffer) {
			ShapedRecipe shaped = RecipeSerializer.SHAPED.read(recipeId, buffer);
			
			int n = buffer.readInt();
			Map<SoulType, Integer> soulMap = Maps.newHashMap();
			for(int l = 0; l < n; l++) {
				soulMap.put(ModSoulTypes.SOUL_TYPE.get(buffer.readIdentifier()), buffer.readInt());
			}
			
			return new ShapedSoulStaffRecipe(shaped, soulMap);
		}

		@Override
		public void write(PacketByteBuf buffer, ShapedSoulStaffRecipe recipe) {
			if(recipe instanceof ShapedSoulStaffRecipe) {
				ShapedSoulStaffRecipe ssrecipe = (ShapedSoulStaffRecipe) recipe;
				buffer.writeInt(ssrecipe.getSoulMap().size());
				
				for(Entry<SoulType, Integer> entry : ssrecipe.getSoulMap().entrySet()) {
					buffer.writeIdentifier(ModSoulTypes.SOUL_TYPE.getId(entry.getKey()));
					buffer.writeInt(entry.getValue());
				}
			}
		}
	}
}
