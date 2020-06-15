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
        ListTag items = ((SimpleInventory) sender).getTags();
        //System.out.println(this.chestStack);
        CompoundTag tag = this.chestStack.getOrCreateTag();
        tag.put("Items", items);
    }
}