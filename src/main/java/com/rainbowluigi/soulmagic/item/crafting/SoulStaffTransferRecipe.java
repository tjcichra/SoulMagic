package com.rainbowluigi.soulmagic.item.crafting;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.rainbowluigi.soulmagic.item.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SoulStaffTransferRecipe extends SoulSeparatorRecipe {

	protected Identifier id;
	protected String group;
	protected Ingredient input;
	protected Map<SoulType, int[]> soulMap;
	
	public SoulStaffTransferRecipe(Identifier e) {
		super(e, "", null, null, 0, ItemStack.EMPTY, 3);
	}

	@Override
	public void postCraft(Inventory inv, World var2, Map<SoulType, Integer> soulMap2) {
		ItemStack stack = inv.getInvStack(0);
		SoulEssenceStaff ses = (SoulEssenceStaff) stack.getItem();
		
		for(Entry<SoulType, Integer> st : soulMap2.entrySet()) {
			ses.subtractSoul(stack, var2, st.getKey(), st.getValue());
		}
	}
	
	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return DefaultedList.of();
	}

	@Override
	public boolean matches(Inventory inv, World var2) {
		return inv.getInvStack(0).getItem() instanceof SoulEssenceStaff;
	}
	
	@Override
	public boolean getFiling() {
		return true;
	}
	
	@Override
	public Map<SoulType, Integer> getSoulMap(Inventory inv, World w) {
		Map<SoulType, Integer> soulMap = Maps.newHashMap();
		
		ItemStack stack = inv.getInvStack(0);
		SoulEssenceStaff ses = (SoulEssenceStaff) stack.getItem();
			
		ItemStack stack2 = inv.getInvStack(2);
		SoulEssenceStaff ses2 = (SoulEssenceStaff) stack2.getItem();
			
		for(SoulType st : ModSoulTypes.SOUL_TYPE_REG) {
			if(ses.getSoul(stack, w, st) >= 1 && ses2.getSoul(stack2, w, st) <= ses2.getMaxSoul(stack2, w, st) - 1) {
				soulMap.put(st, 1);
			}
		}
		return soulMap;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.SOUL_STAFF_TRANSFER;
	}
}
