package com.rainbowluigi.soulmagic.util;

import com.rainbowluigi.soulmagic.inventory.AccessoriesInventory;
import com.rainbowluigi.soulmagic.item.crafting.PlayerAccessories;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemHelper {

	public static ItemStack findItem(PlayerEntity player, Item item) {
		for (int i = 0; i < player.inventory.getInvSize(); i++) {
			if(player.inventory.getInvStack(i).getItem().equals(item)) {
				return player.inventory.getInvStack(i);
			}
		}
		return null;
	}
	
	public static ItemStack findAccessory(PlayerEntity player, Item item) {
		AccessoriesInventory accessories = ((PlayerAccessories) player).getAccessories();
		
		for (int i = 0; i < accessories.getInvSize(); i++) {
			if(accessories.getInvStack(i).getItem().equals(item)) {
				return accessories.getInvStack(i);
			}
		}
		return null;
	}
}
