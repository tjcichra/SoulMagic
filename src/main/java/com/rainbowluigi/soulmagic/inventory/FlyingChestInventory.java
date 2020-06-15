package com.rainbowluigi.soulmagic.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class FlyingChestInventory implements InventoryChangedListener {

	private ItemStack chestStack;

	public FlyingChestInventory(ItemStack chestStack) {
		this.chestStack = chestStack;
	}

	@Override
	public void onInventoryChanged(Inventory sender) {
		ListTag items = getTags(sender);
		//System.out.println(this.chestStack);
		CompoundTag tag = this.chestStack.getOrCreateTag();
		tag.put("Items", items);
	}
	
	public static void readTags(ListTag tags, Inventory sender) {
		for(int i = 0; i < tags.size(); ++i) {
			CompoundTag tag = tags.getCompound(i);
			ItemStack itemStack = ItemStack.fromTag(tag);
			if (!itemStack.isEmpty()) {
				sender.setStack(tag.getInt("Slot"), itemStack);
			}
		}
	}

	public static ListTag getTags(Inventory sender) {
		ListTag listTag = new ListTag();

		for(int i = 0; i < sender.size(); i++) {
			ItemStack itemStack = sender.getStack(i);
			if (!itemStack.isEmpty()) {
				CompoundTag tag = new CompoundTag();
				itemStack.toTag(tag);
				tag.putInt("Slot", i);
				listTag.add(tag);
			}
		}
		return listTag;
	}
}