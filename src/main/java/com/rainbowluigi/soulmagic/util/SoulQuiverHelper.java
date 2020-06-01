package com.rainbowluigi.soulmagic.util;

import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class SoulQuiverHelper {

	public static SoulType getSoulType(ItemStack stack) {
		//Checks if the stack has a tag of the soul type
		if (stack.hasTag() && stack.getTag().contains("soultype")) {
			// Gets the registry name of the soul type (which is also the key)
			return ModSoulTypes.SOUL_TYPE.get(stack.getTag().getInt("soultype"));
		}
		return ModSoulTypes.LIGHT;
	}
	
	public static void incrementType(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		
		int i = ModSoulTypes.SOUL_TYPE.getRawId(getSoulType(stack));
		i++;
		
		if(ModSoulTypes.SOUL_TYPE.get(i) != null)
			tag.putInt("soultype", i);
		else
			tag.putInt("soultype", 0);
	}
}