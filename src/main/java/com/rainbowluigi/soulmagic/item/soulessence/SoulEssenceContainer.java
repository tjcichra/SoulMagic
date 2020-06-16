package com.rainbowluigi.soulmagic.item.soulessence;

import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

//Used for items that can hold soul essence but can't have soul essence added to it (only subtracted from)
//Any items that implement this class can't be used by magic items or machines (other than the soul separator)
//For both issues see @SoulEssenceStaff which implements this class
public interface SoulEssenceContainer {
	
	//Get the value of the soul type in the container stack
	public default int getSoul(ItemStack stack, World world, SoulType type) {
		//Checks if the container stack has a compound tag and if it has a tag of "souls" in it
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
		int max = ((SoulEssenceContainer)stack.getItem()).getMaxSoul(stack, world, type);
		
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

	//Subtracts the value of the soul type in the container stack, return if that amount can be subtracted or not.
	public default boolean subtractSoul(ItemStack stack, World world, SoulType type, int amount) {
		//If there is no tag or the container stack doesn't have a "souls" tag, return false
		if(!stack.hasTag() || !stack.getTag().contains("souls")) {
			return false;
		}
		
		//Gets the tag with all the soul values in it
		CompoundTag tag = (CompoundTag) stack.getTag().get("souls");
		
		//Gets the registry name of the soul type (which is also the key)
		String s = ModSoulTypes.SOUL_TYPE.getId(type).toString();
		
		//If the tag doesn't have a value for the soul type, return false
		if(!tag.contains(s)) {
			return false;
		}
		
		//Get the amount of soul essence in the container stack and if it is less than the amount required, return false
		int current = tag.getInt(s);
		if(current < amount) {
			return false;
		}
		
		//Subtract the amount require for the container stack and return true
		tag.putInt(s, current - amount);
		
		return true;
	}
}