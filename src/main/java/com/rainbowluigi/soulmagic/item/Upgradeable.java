package com.rainbowluigi.soulmagic.item;

import java.util.ArrayList;
import java.util.List;

import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;

public interface Upgradeable {
	
	public List<Upgrade> getPossibleUpgrades(ItemStack stack);

	public default List<Upgrade> getUpgrades(ItemStack stack, boolean all) {
		List<Upgrade> upgrades = new ArrayList<>();

		if(stack.hasTag() && stack.getTag().contains("upgrades")) {
			ListTag t = (ListTag) stack.getTag().get("upgrades");

			for(int i = 0; i < t.size(); i++) {
				CompoundTag tag = (CompoundTag) t.get(i);
				Upgrade u = ModUpgrades.UPGRADE.get(new Identifier(tag.getString("name")));
				boolean selected = tag.getBoolean("selected");

				if(all || selected) {
					upgrades.add(u);
				}
			}
		}

		return upgrades;
	}

	public default void addUpgrade(ItemStack stack, Upgrade u) {
		CompoundTag tag = stack.getOrCreateTag();

		if(!tag.contains("upgrades")) {
			ListTag list = new ListTag();
			tag.put("upgrades", list);
		}

		ListTag list = (ListTag) tag.get("upgrades");

		CompoundTag upgradeTag = new CompoundTag();
		upgradeTag.putString("name", ModUpgrades.UPGRADE.getId(u).toString());
		upgradeTag.putBoolean("selected", false);
		list.add(upgradeTag);
	}
}