package com.rainbowluigi.soulmagic.util;

import java.util.ArrayList;
import java.util.List;

import com.rainbowluigi.soulmagic.inventory.AccessoriesInventory;
import com.rainbowluigi.soulmagic.item.crafting.PlayerAccessories;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

public class ItemHelper {

	public static ItemStack findItem(PlayerEntity player, ItemConvertible item) {
		for (int i = 0; i < player.getInventory().size(); i++) {
			if(player.getInventory().getStack(i).getItem().equals(item.asItem())) {
				return player.getInventory().getStack(i);
			}
		}
		return null;
	}

	public static List<ItemStack> findAllItem(PlayerEntity player, ItemConvertible item) {
		List<ItemStack> list = new ArrayList<>();

		for (int i = 0; i < player.getInventory().size(); i++) {
			if(player.getInventory().getStack(i).getItem().equals(item.asItem())) {
				list.add(player.getInventory().getStack(i));
			}
		}
		return list;
	}
	
	public static int findAccessorySlot(PlayerEntity player, Item item) {
		AccessoriesInventory accessories = ((PlayerAccessories) player).getAccessories();
		
		for (int i = 0; i < accessories.size(); i++) {
			if(accessories.getStack(i).getItem().equals(item)) {
				return i;
			}
		}
		return -1;
	}

	public static ItemStack getAccessoryFromSlot(PlayerEntity player, int slot) {
		AccessoriesInventory accessories = ((PlayerAccessories) player).getAccessories();
		return accessories.getStack(slot);
	}
}
