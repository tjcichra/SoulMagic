package com.rainbowluigi.soulmagic.item;

import java.util.ArrayList;
import java.util.List;

import com.rainbowluigi.soulmagic.block.entity.SoulEssenceInfuserBlockEntity.UpgradeAndSelection;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public interface Upgradeable {
	
	public List<Upgrade> getPossibleUpgrades(ItemStack stack);

	public default List<Upgrade> getUpgradesSelected(ItemStack stack) {
		return this.getUpgradesUnlocked(stack, false);
	}

	public default List<Upgrade> getUpgradesUnlocked(ItemStack stack, boolean all) {
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

	public default List<UpgradeAndSelection> getUpgradesAndSelectionsUnlocked(ItemStack stack) {
		List<UpgradeAndSelection> upgrades = new ArrayList<>();

		if(stack.hasTag() && stack.getTag().contains("upgrades")) {
			ListTag t = (ListTag) stack.getTag().get("upgrades");

			for(int i = 0; i < t.size(); i++) {
				CompoundTag tag = (CompoundTag) t.get(i);
				Upgrade u = ModUpgrades.UPGRADE.get(new Identifier(tag.getString("name")));
				boolean selected = tag.getBoolean("selected");
				upgrades.add(new UpgradeAndSelection(u, selected));
			}
		}

		return upgrades;
	}

	public default boolean hasUpgradeUnlocked(ItemStack stack, Upgrade target) {
		if(stack.hasTag() && stack.getTag().contains("upgrades")) {
			ListTag t = (ListTag) stack.getTag().get("upgrades");

			for(int i = 0; i < t.size(); i++) {
				CompoundTag tag = (CompoundTag) t.get(i);
				Upgrade u = ModUpgrades.UPGRADE.get(new Identifier(tag.getString("name")));
				
				if(u.equals(target)) {
					return true;
				}
			}
		}

		return false;
	}

	public default boolean hasUpgradeSelected(ItemStack stack, Upgrade target) {
		if(stack.hasTag() && stack.getTag().contains("upgrades")) {
			ListTag t = (ListTag) stack.getTag().get("upgrades");

			for(int i = 0; i < t.size(); i++) {
				CompoundTag tag = (CompoundTag) t.get(i);
				Upgrade u = ModUpgrades.UPGRADE.get(new Identifier(tag.getString("name")));
				
				if(u.equals(target)) {
					return tag.contains("selected") && tag.getBoolean("selected");
				}
			}
		}

		return false;
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
		list.add(upgradeTag);
	}

	public default void setUpgradeSelection(ItemStack stack, Upgrade target, boolean selected) {
		if(stack.hasTag() && stack.getTag().contains("upgrades")) {
			ListTag t = (ListTag) stack.getTag().get("upgrades");

			for(int i = 0; i < t.size(); i++) {
				CompoundTag tag = (CompoundTag) t.get(i);
				Upgrade u = ModUpgrades.UPGRADE.get(new Identifier(tag.getString("name")));
				
				if(u.equals(target)) {
					tag.putBoolean("selected", selected);
				}
			}
		}
	}

	public default int getSelectorPointsNumber(ItemStack stack) {
		if(stack.hasTag() && stack.getTag().contains("selectorPoints")) {
			return stack.getTag().getInt("selectorPoints");
		}
		return 0;
	}

	public default void incrementSelectorPoints(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();

		int points = tag.contains("selectorPoints") ? tag.getInt("selectorPoints") : 0;

		if(points < this.getMaxSelectorPoints(stack)) {
			tag.putInt("selectorPoints", points + 1);
		}
	}

	public default int getSelectorPointsUsed(ItemStack stack) {
		return this.getUpgradesSelected(stack).size();
	}

	public default int getMaxSelectorPoints(ItemStack stack) {
		return this.getPossibleUpgrades(stack).size();
	}

	public default void onSelection(ItemStack stack, World w, Upgrade u) {

	}

	public default void onUnselection(ItemStack stack, World w, Upgrade u) {

	}
}