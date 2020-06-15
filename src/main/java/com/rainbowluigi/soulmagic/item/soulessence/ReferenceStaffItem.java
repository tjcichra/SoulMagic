package com.rainbowluigi.soulmagic.item;

import java.util.List;

import com.rainbowluigi.soulmagic.block.entity.SoulCacheBlockEntity;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ReferenceStaffItem extends Item implements SoulEssenceStaff {

	public ReferenceStaffItem(Item.Settings settings) {
		super(settings);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		for(SoulType st : ModSoulTypes.SOUL_TYPE) {
			if (this.getSoul(stack, world, st) > 0) {
				tooltip.add(new TranslatableText("soulmagic.soul_essence_staff.amount", st.getName(), this.getSoul(stack, world, st), this.getMaxSoul(stack, world, st)).formatted(st.getTextColor()));
			}
		}
	}

	@Override
    public int getSoul(ItemStack stack, World world, SoulType type) {
    	SoulCacheBlockEntity be = this.getInventory(stack, world);
		int amount = 0;
    	
    	if(be != null) {
			
			for(int i = 0; i < be.size(); i++) {
				ItemStack stack2 = be.getStack(i);
				
				if(stack2.getItem() instanceof SoulEssenceStaff) {
					SoulEssenceStaff ses = (SoulEssenceStaff) stack2.getItem();
					amount += ses.getSoul(stack2, world, type);
				}
			}
		}
		return amount;
    }

	@Override
	public int getMaxSoul(ItemStack stack, World world, SoulType type) {
		SoulCacheBlockEntity be = this.getInventory(stack, world);
		int total = 0;
    	
    	if(be != null) {
			
			for(int i = 0; i < be.size(); i++) {
				ItemStack stack2 = be.getStack(i);
				
				if(stack2.getItem() instanceof SoulEssenceStaff) {
					SoulEssenceStaff ses = (SoulEssenceStaff) stack2.getItem();
					total += ses.getMaxSoul(stack2, world, type);
				}
			}
		}
		return total;
	}
	
	//@Override
	//public void setSoul(ItemStack stack, World world, SoulType type, int amount) {
	//	SoulCacheBlockEntity eb = this.getInventory(stack, world);
	//	
	//	eb.getIn
	//	SoulEssenceStaff.super.setSoul(stack, world, type, amount);
	//}
	
	@Override
	public int addSoul(ItemStack stack, World world, SoulType type, int amount) {
		SoulCacheBlockEntity be = this.getInventory(stack, world);
		
		if(be != null) {
			
			for(int i = 0; i < be.size(); i++) {
				ItemStack stack2 = be.getStack(i);
				
				if(stack2.getItem() instanceof SoulEssenceStaff) {
					SoulEssenceStaff ses = (SoulEssenceStaff) stack2.getItem();
					amount = ses.addSoul(stack2, world, type, amount);
					
					if(amount == 0) break;
				}
			}
			return amount;
		}
		return 0;
    }
	
	@Override
	public boolean subtractSoul(ItemStack stack, World world, SoulType type, int amount) {
		SoulCacheBlockEntity be = this.getInventory(stack, world);
		
		if(be != null) {
			
			for(int i = be.size() - 1; i >= 0; i++) {
				ItemStack stack2 = be.getStack(i);
				
				if(stack2.getItem() instanceof SoulEssenceStaff) {
					SoulEssenceStaff ses = (SoulEssenceStaff) stack2.getItem();
					
					if(ses.subtractSoul(stack2, world, type, amount)) {
						return true;
					}
				}
			}
		}
		return false;
    }
	
	public SoulCacheBlockEntity getInventory(ItemStack stack, World world) {
		CompoundTag tag = stack.getSubTag("cache");
		
		if(tag != null) {
			BlockEntity be = world.getBlockEntity(new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z")));
			
			if(be instanceof SoulCacheBlockEntity) {
				return (SoulCacheBlockEntity) be;
			}
		}
		
		return null;
	}
}
