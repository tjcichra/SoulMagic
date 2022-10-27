package com.rainbowluigi.soulmagic.item.soulessence;

import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

//Used for holding soul essence for use by magic items and machines
//Can add, subtract, and manage soul essence in any way (unlike @SouLEssenceContainer)
public interface SoulEssenceStaff extends SoulEssenceContainer {
	
	//Adds the value of the soul type in the stack, returns the remainder if it overflows
	default int addSoul(ItemStack stack, World world, SoulType type, int amount) {
		//If there is no tag
		if(!stack.hasNbt()) {
			//Give the stack one
			stack.setNbt(new NbtCompound());
		}
		
		//If the stack doesn't have a tag of soul values in it
		if(!stack.getNbt().contains("souls")) {
			//Give the stack one
			stack.getNbt().put("souls", new NbtCompound());
		}
		
		//Gets the tag of soul values and get the maximum soul value
		NbtCompound tag = (NbtCompound) stack.getNbt().get("souls");
		int max = this.getMaxSoul(stack, world, type);
		
		//Gets the registry name of the soul type (which is also the key)
		String s = ModSoulTypes.SOUL_TYPE.getId(type).toString();
		int remainder = 0;
		
		//Add the amount to what the staff stack already has
		if(tag.contains(s)) {
			amount += tag.getInt(s);
		}
		
		//If the amount goes above the maximum soul essence value
		if(amount > max) {
			//Get the remainder and give the max to the staff stack
			remainder = amount - max;
			tag.putInt(s, max);
		} else {
			//Or else just set it to the new amount
			tag.putInt(s, amount);
		}
		
		//Return the remainder
		return remainder;
	}

    public static boolean hasSoul(PlayerEntity player, World world, Object... objects) {
    	if(!player.isCreative()) {
	    	for (int i = 0; i < player.getInventory().size(); i++) {
	    		if(player.getInventory().getStack(i).getItem() instanceof SoulEssenceStaff) {
					ItemStack stack = player.getInventory().getStack(i);
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
	    	for (int i = 0; i < player.getInventory().size(); i++) {
	    		if(player.getInventory().getStack(i).getItem() instanceof SoulEssenceStaff) {
					ItemStack stack = player.getInventory().getStack(i);
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
