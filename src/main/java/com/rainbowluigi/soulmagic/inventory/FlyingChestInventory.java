package com.rainbowluigi.soulmagic.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class FlyingChestInventory implements InventoryChangedListener {

	private ItemStack chestStack;

	public FlyingChestInventory(ItemStack chestStack) {
		this.chestStack = chestStack;
	}

	@Override
	public void onInventoryChanged(Inventory sender) {
		NbtList items = getTags(sender);
		//System.out.println(this.chestStack);
		NbtCompound tag = this.chestStack.getOrCreateNbt();
		tag.put("Items", items);
	}
	
	public static void readTags(NbtList tags, Inventory sender) {
		for(int i = 0; i < tags.size(); ++i) {
			NbtCompound tag = tags.getCompound(i);
			ItemStack itemStack = ItemStack.fromNbt(tag);
			if (!itemStack.isEmpty()) {
				sender.setStack(tag.getInt("Slot"), itemStack);
			}
		}
	}

	public static NbtList getTags(Inventory sender) {
		NbtList listTag = new NbtList();

		for(int i = 0; i < sender.size(); i++) {
			ItemStack itemStack = sender.getStack(i);
			if (!itemStack.isEmpty()) {
				NbtCompound tag = new NbtCompound();
				itemStack.writeNbt(tag);
				tag.putInt("Slot", i);
				listTag.add(tag);
			}
		}
		return listTag;
	}
}