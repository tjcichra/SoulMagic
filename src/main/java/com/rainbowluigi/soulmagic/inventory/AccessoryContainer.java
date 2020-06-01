package com.rainbowluigi.soulmagic.inventory;

import com.rainbowluigi.soulmagic.item.Accessory;
import com.rainbowluigi.soulmagic.item.Accessory.AccessoryType;
import com.rainbowluigi.soulmagic.item.crafting.PlayerAccessories;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class AccessoryContainer extends ScreenHandler {

	public AccessoryContainer(PlayerEntity player, int s) {
		super(null, s);
		PlayerInventory playerInv = player.inventory;
		AccessoriesInventory accessories = ((PlayerAccessories) player).getAccessories();

		this.addSlot(new AccessorySlot(accessories, 0, 80, 56));
		this.addSlot(new AccessorySlot(accessories, 1, 134, 56));
		this.addSlot(new AccessorySlot(accessories, 2, 107, 34));
		this.addSlot(new AccessorySlot(accessories, 3, 80, 12));
		this.addSlot(new AccessorySlot(accessories, 4, 134, 12));

		// Player Inventory, Slot 9-35, Slot IDs 9-35
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}

		// Player Inventory, Slot 0-8, Slot IDs 36-44
		for (int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(playerInv, x, 8 + x * 18, 142));
		}
	}

	public boolean canUse(PlayerEntity playerEntity_1) {
		return true;
	}

	public static class AccessorySlot extends Slot {

		public AccessorySlot(Inventory inv, int id, int x, int y) {
			super(inv, id, x, y);
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			if(stack.getItem() instanceof Accessory) {
				AccessoryType type = ((Accessory) stack.getItem()).getType();
				
				if(type == null) {
					return true;
				}
				
				int count = 0;
				
				for(int x = 0; x < this.inventory.size(); x++) {
					if(!this.inventory.getStack(x).isEmpty()) {
						if(((Accessory) this.inventory.getStack(x).getItem()).getType().equals(type)) {
							count++;
						}
					}
				}
				
				if(count < type.getMax()) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public ItemStack transferSlot(PlayerEntity playerIn, int fromSlot) {
		ItemStack previous = ItemStack.EMPTY;
		Slot slot = this.getSlot(fromSlot);

		if (slot != null && slot.hasStack()) {
			ItemStack current = slot.getStack();
			previous = current.copy();

			if (fromSlot < 5) {
				if (!this.insertItem(current, 5, 41, false))
					return ItemStack.EMPTY;
			} else {
				if (!this.insertItem(current, 0, 5, false))
					return ItemStack.EMPTY;
			}

			if (current.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}

			if (current.getCount() == previous.getCount())
				return ItemStack.EMPTY;

			slot.onTakeItem(playerIn, current);
		}
		return previous;
	}
}
