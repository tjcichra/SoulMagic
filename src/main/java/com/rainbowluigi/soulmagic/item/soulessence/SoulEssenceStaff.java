package com.rainbowluigi.soulmagic.item.soulessence;

import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

//Used for holding soul essence staff and for use by magic items and machines
//Can add, subtract, and manage soul essence in any way (unlike @SouLEssenceContainer)
public interface SoulEssenceStaff extends SoulEssenceContainer {
	
	//Adds the value of the soul type in the stack, returns the remainder if it overflows
	public default int addSoul(ItemStack stack, World world, SoulType type, int amount) {
		//If there is no tag
		if(!stack.hasTag()) {
			//Give the stack one
			stack.setTag(new CompoundTag());
		}
		
		//If the stack doesn't have a tag of soul values in it
		if(!stack.getTag().contains("souls")) {
			//Give the stack one
			stack.getTag().put("souls", new CompoundTag());
		}
		
		//Get the tag of soul values.
		CompoundTag tag = (CompoundTag) stack.getTag().get("souls");
		
		int max = ((SoulEssenceStaff)stack.getItem()).getMaxSoul(stack, world, type);
		
		String s = ModSoulTypes.SOUL_TYPE.getId(type).toString();
		int remainder = 0;
		
		if(tag.contains(s)) {
			amount += tag.getInt(s);
		}
		
		if(amount > max) {
			remainder = amount - max;
			tag.putInt(s, max);
		} else {
			tag.putInt(s, amount);
		}
		
		return remainder;
	}

    public static boolean hasSoul(PlayerEntity player, World world, Object... objects) {
    	if(!player.isCreative()) {
	    	for (int i = 0; i < player.inventory.size(); i++) {
	    		if(player.inventory.getStack(i).getItem() instanceof SoulEssenceStaff) {
					ItemStack stack = player.inventory.getStack(i);
					for(int j = 0; j < objects.length; j += 2) {
						SoulEssenceStaff staff = (SoulEssenceStaff) stack.getItem();
						//if(staff.getSoul(stack, (SoulType) objects[j]) < (Integer) objects[j + 1]) {
						//	return false;
						//}
						if(!staff.subtractSoul(stack, world, (SoulType) objects[j], (Integer) objects[j + 1])) {
							return false;
						}
					}
					return true;
				}
	    	}
	    	return false;
    	}
    	return true;
    }
    
    public static boolean hasAtLeastSoul(PlayerEntity player, World world, Object... objects) {
    	if(!player.isCreative()) {
	    	for (int i = 0; i < player.inventory.size(); i++) {
	    		if(player.inventory.getStack(i).getItem() instanceof SoulEssenceStaff) {
					ItemStack stack = player.inventory.getStack(i);
					for(int j = 0; j < objects.length; j += 2) {
						SoulEssenceStaff staff = (SoulEssenceStaff) stack.getItem();
						//if(staff.getSoul(stack, (SoulType) objects[j]) < (Integer) objects[j + 1]) {
						//	return false;
						//}
						if(staff.getSoul(stack, world, (SoulType) objects[j]) < (Integer) objects[j + 1]) {
							return false;
						}
					}
					return true;
				}
	    	}
	    	return false;
    	}
    	return true;
    }
}
