package com.rainbowluigi.soulmagic.item.soulessence;

import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

public interface SoulEssenceContainer {
	
	//Get the value of the soul type in the stack
	public default int getSoul(ItemStack stack, World world, SoulType type) {
		//Checks if the stack has a compound tag and if it has a tag of "souls" in it
		if(stack.hasTag() && stack.getTag().contains("souls")) {
			//Gets the tag with all the soul values in it
			CompoundTag tag = (CompoundTag) stack.getTag().get("souls");
			
			//Gets the registry name of the soul type (which is also the key)
			String s = ModSoulTypes.SOUL_TYPE.getId(type).toString();
			
			//If the soul type is in the soul values tag, return it's value
			if(tag.contains(s)) {
				return tag.getInt(s);
			}
		}
		//Else, return 0
		return 0;
	}

	//Set the value of the soul type in the container stack
	public default void setSoul(ItemStack stack, World world, SoulType type, int amount) {
		//If there is no tag
		if(!stack.hasTag()) {
			//Give the container stack one
			stack.setTag(new CompoundTag());
		}
		
		//If the container doesn't have a tag of soul values in it
		if(!stack.getTag().contains("souls")) {
			//Give the container stack one
			stack.getTag().put("souls", new CompoundTag());
		}
		
		//Get the tag of soul values and get the max soul value.
		CompoundTag tag = (CompoundTag) stack.getTag().get("souls");
		int max = ((SoulEssenceStaff)stack.getItem()).getMaxSoul(stack, world, type);
		
		//If the amount is greater than the container stack can hold, make the amount the max.
		if(amount > max) {
			amount = max;
		}
		
		//Put the registry name of the soul type as a key with its amount
		tag.putInt(ModSoulTypes.SOUL_TYPE.getId(type).toString(), amount);
	}

	//Get the value of the maximum soul essence per soul type in the stack
	public int getMaxSoul(ItemStack stack, World world, SoulType type);

	//Optional function if you want your max soul essence value to be variable
	public default void setMaxSoul(ItemStack stack, World world, SoulType type, int amount) {

	}
}