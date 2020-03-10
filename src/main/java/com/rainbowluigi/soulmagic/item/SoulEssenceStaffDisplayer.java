package com.rainbowluigi.soulmagic.item;

import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface SoulEssenceStaffDisplayer {

	public SoulType[] getSoulTypesToShow(ItemStack stack, PlayerEntity player);
}
