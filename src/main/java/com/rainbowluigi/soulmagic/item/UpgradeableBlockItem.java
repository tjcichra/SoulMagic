package com.rainbowluigi.soulmagic.item;

import com.rainbowluigi.soulmagic.upgrade.Upgrade;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

import java.util.List;

public class UpgradeableBlockItem extends BlockItem implements Upgradeable {

    private List<Upgrade> u;

    public UpgradeableBlockItem(Block block, Settings settings, List<Upgrade> u) {
        super(block, settings);
        this.u = u;
    }

    @Override
    public List<Upgrade> getPossibleUpgrades(ItemStack stack) {
        return this.u;
    }
}