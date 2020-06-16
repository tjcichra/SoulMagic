package com.rainbowluigi.soulmagic.item.soulessence;

import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SoulEssenceOrbItem extends Item implements SoulEssenceContainer {

    public SoulEssenceOrbItem(Settings settings) {
        super(settings);
    }

    //Fixed at 1000, this may change somehow to have no limit
    @Override
    public int getMaxSoul(ItemStack stack, World world, SoulType type) {
        return 1000;
    }
}