package com.rainbowluigi.soulmagic.item.soulessence;

import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface SoulEssenceStaffDisplayer {

	public default boolean showDisplay(ItemStack stack, PlayerEntity player) {
		return true;
	}
	
	public SoulType[] getSoulTypesToShow(ItemStack stack, PlayerEntity player);
}
