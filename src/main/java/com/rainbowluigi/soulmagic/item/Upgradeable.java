package com.rainbowluigi.soulmagic.item;

import com.rainbowluigi.soulmagic.block.entity.SoulEssenceInfuserBlockEntity.UpgradeAndSelection;
import com.rainbowluigi.soulmagic.upgrade.ModUpgrades;
import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import com.rainbowluigi.soulmagic.util.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public interface Upgradeable {

    String SELECTOR_POINTS = "selectorPoints";

    List<Upgrade> getPossibleUpgrades(ItemStack stack);

    default List<Upgrade> getUpgradesSelected(ItemStack stack) {
        return this.getUpgradesUnlocked(stack, false);
    }

    default List<Upgrade> getUpgradesUnlocked(ItemStack stack, boolean all) {
        List<Upgrade> upgrades = new ArrayList<>();

        if (stack.hasNbt() && stack.getNbt().contains("upgrades")) {
            NbtList t = (NbtList) stack.getNbt().get("upgrades");

            for (NbtElement nbtElement : t) {
                NbtCompound tag = (NbtCompound) nbtElement;
                Upgrade u = ModUpgrades.UPGRADE.get(new Identifier(tag.getString("name")));
                boolean selected = tag.getBoolean("selected");

                if (all || selected) {
                    upgrades.add(u);
                }
            }
        }

        return upgrades;
    }

    default List<UpgradeAndSelection> getUpgradesAndSelectionsUnlocked(ItemStack stack) {
        List<UpgradeAndSelection> upgrades = new ArrayList<>();

        if (stack.hasNbt() && stack.getNbt().contains("upgrades")) {
            NbtList t = (NbtList) stack.getNbt().get("upgrades");

            for (NbtElement nbtElement : t) {
                NbtCompound tag = (NbtCompound) nbtElement;
                Upgrade u = ModUpgrades.UPGRADE.get(new Identifier(tag.getString("name")));
                boolean selected = tag.getBoolean("selected");
                upgrades.add(new UpgradeAndSelection(u, selected));
            }
        }

        return upgrades;
    }

    default boolean hasUpgradeUnlocked(ItemStack stack, Upgrade target) {
        if (stack.hasNbt() && stack.getNbt().contains("upgrades")) {
            NbtList t = (NbtList) stack.getNbt().get("upgrades");

            for (NbtElement nbtElement : t) {
                NbtCompound tag = (NbtCompound) nbtElement;
                Upgrade u = ModUpgrades.UPGRADE.get(new Identifier(tag.getString("name")));

                if (u.equals(target)) {
                    return true;
                }
            }
        }

        return false;
    }

    default boolean hasUpgradeSelected(ItemStack stack, Upgrade target) {
        if (stack.hasNbt() && stack.getNbt().contains("upgrades")) {
            NbtList t = (NbtList) stack.getNbt().get("upgrades");

            for (NbtElement nbtElement : t) {
                NbtCompound tag = (NbtCompound) nbtElement;
                Upgrade u = ModUpgrades.UPGRADE.get(new Identifier(tag.getString("name")));

                if (u.equals(target)) {
                    return tag.contains("selected") && tag.getBoolean("selected");
                }
            }
        }

        return false;
    }

    default void addUpgrade(ItemStack stack, Upgrade u) {
        NbtCompound tag = stack.getOrCreateNbt();

        if (!tag.contains("upgrades")) {
            NbtList list = new NbtList();
            tag.put("upgrades", list);
        }

        NbtList list = (NbtList) tag.get("upgrades");

        NbtCompound upgradeTag = new NbtCompound();
        upgradeTag.putString("name", ModUpgrades.UPGRADE.getId(u).toString());
        list.add(upgradeTag);
    }

    default void setUpgradeSelection(ItemStack stack, Upgrade target, boolean selected) {
        if (stack.hasNbt() && stack.getNbt().contains("upgrades")) {
            NbtList t = (NbtList) stack.getNbt().get("upgrades");

            for (NbtElement nbtElement : t) {
                NbtCompound tag = (NbtCompound) nbtElement;
                Upgrade u = ModUpgrades.UPGRADE.get(new Identifier(tag.getString("name")));

                if (u.equals(target)) {
                    tag.putBoolean("selected", selected);
                }
            }
        }
    }

    default int getSelectorPointsNumber(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(SELECTOR_POINTS)) {
            return stack.getNbt().getInt(SELECTOR_POINTS);
        }
        return 0;
    }

    default void incrementSelectorPoints(ItemStack stack) {
        NbtCompound tag = stack.getOrCreateNbt();

        int points = NBTHelper.getIntFromNbt(tag, SELECTOR_POINTS, 0);

        if (points < this.getMaxSelectorPoints(stack)) {
            tag.putInt(SELECTOR_POINTS, points + 1);
        }
    }

    default int getSelectorPointsUsed(ItemStack stack) {
        return this.getUpgradesSelected(stack).size();
    }

    default int getMaxSelectorPoints(ItemStack stack) {
        return this.getPossibleUpgrades(stack).size();
    }

    default void onSelection(ItemStack stack, World w, Upgrade u) {

    }

    default void onUnselection(ItemStack stack, World w, Upgrade u) {

    }
}