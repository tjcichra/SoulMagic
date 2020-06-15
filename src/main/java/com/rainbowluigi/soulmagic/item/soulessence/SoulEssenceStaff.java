package com.rainbowluigi.soulmagic.item.soulessence;

import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

public interface SoulEssenceStaff {
	
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
	
    public int getMaxSoul(ItemStack stack, World world, SoulType type);
    
	//Set the value of the soul type in the stack
    public default void setSoul(ItemStack stack, World world, SoulType type, int amount) {
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
        
        if(amount > max) {
        	amount = max;
        }
        
        //Put the registry name of the soul type as a key with its amount
        tag.putInt(ModSoulTypes.SOUL_TYPE.getId(type).toString(), amount);
    }
    
	//Set the value of the soul type in the stack
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
    
    public default boolean subtractSoul(ItemStack stack, World world, SoulType type, int amount) {
        if(!stack.hasTag() || !stack.getTag().contains("souls")) {
        	return false;
        }
        
        CompoundTag tag = (CompoundTag) stack.getTag().get("souls");
        
        String s = ModSoulTypes.SOUL_TYPE.getId(type).toString();
        
        if(!tag.contains(s)) {
        	return false;
        }
        
        int current = tag.getInt(s);
        if(current < amount) {
        	return false;
    	}
        
        tag.putInt(s, current - amount);
        
        return true;
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
