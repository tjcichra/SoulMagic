package com.rainbowluigi.soulmagic.util;

import com.rainbowluigi.soulmagic.inventory.AccessoriesInventory;
import com.rainbowluigi.soulmagic.item.crafting.PlayerAccessories;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemHelper {

	public static ItemStack findItem(PlayerEntity player, Item item) {
		for (int i = 0; i < player.inventory.size(); i++) {
			if(player.inventory.getStack(i).getItem().equals(item)) {
				return player.inventory.getStack(i);
			}
		}
		return null;
	}
	
	public static ItemStack findAccessory(PlayerEntity player, Item item) {
		AccessoriesInventory accessories = ((PlayerAccessories) player).getAccessories();
		
		for (int i = 0; i < accessories.size(); i++) {
			if(accessories.getStack(i).getItem().equals(item)) {
				return accessories.getStack(i);
			}
		}
		return null;
	}
}
