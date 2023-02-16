package com.rainbowluigi.soulmagic.inventory;

import com.rainbowluigi.soulmagic.item.accessory.Accessory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

public class AccessoriesInventory implements Inventory {

    private final DefaultedList<ItemStack> accessories;
    public final PlayerEntity player;

    public AccessoriesInventory(PlayerEntity player) {
        this.accessories = DefaultedList.ofSize(5, ItemStack.EMPTY);
        this.player = player;
    }

    @Override
    public void clear() {
        this.accessories.clear();
    }

    @Override
    public int size() {
        return this.accessories.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.accessories) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.accessories.get(slot);
    }

    @Override
    public ItemStack removeStack(int var1, int var2) {
        return Inventories.splitStack(this.accessories, var1, var2);
    }

    @Override
    public ItemStack removeStack(int var1) {
        return Inventories.removeStack(this.accessories, var1);
    }

    @Override
    public void setStack(int var1, ItemStack var2) {
        this.accessories.set(var1, var2);
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity var1) {
        return true;
    }

    public NbtList serialize(NbtList list) {
        for (int i = 0; i < this.accessories.size(); ++i) {
            if (!this.accessories.get(i).isEmpty()) {
                NbtCompound tag = new NbtCompound();
                tag.putByte("Slot", (byte) i);
                this.accessories.get(i).writeNbt(tag);
                list.add(tag);
            }
        }

        return list;
    }

    public void deserialize(NbtList list) {
        this.accessories.clear();

        for (int i = 0; i < list.size(); ++i) {
            NbtCompound tag = list.getCompound(i);

            int slot = tag.getByte("Slot");
            ItemStack stack = ItemStack.fromNbt(tag);
            if (!stack.isEmpty()) {
                this.accessories.set(slot, stack);
            }
        }
    }

    public void dropAccessories() {
        for (int i = 0; i < this.accessories.size(); ++i) {
            ItemStack stack = (ItemStack) this.accessories.get(i);
            if (!stack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(stack)) {
                this.player.dropItem(stack, true, false);
                this.accessories.set(i, ItemStack.EMPTY);
            }
        }
    }

    public void updateItems() {
        for (int i = 0; i < this.accessories.size(); ++i) {
            if (!this.accessories.get(i).isEmpty()) {
                ((Accessory) this.accessories.get(i).getItem()).onWearTick(this.accessories.get(i), this.player.world, this.player, i);
            }
        }
    }
}
