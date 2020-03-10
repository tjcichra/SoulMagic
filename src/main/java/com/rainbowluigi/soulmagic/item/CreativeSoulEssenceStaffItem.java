package com.rainbowluigi.soulmagic.item;

import java.util.List;

import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

public class CreativeSoulEssenceStaffItem extends Item implements SoulEssenceStaff {

	public CreativeSoulEssenceStaffItem(Item.Settings settings) {
		super(settings);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		for(SoulType st : ModSoulTypes.SOUL_TYPE_REG) {
			if (this.getSoul(stack, world, st) > 0) {
				tooltip.add(new TranslatableText("soulmagic.soul_essence_staff.amount", st.getName(), this.getSoul(stack, world, st), this.getMaxSoul(stack, world, st)).formatted(st.getTextColor()));
			}
		}
	}
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> items) {
		if (this.isIn(group)) {
			ItemStack stack = new ItemStack(this);
			
			for(SoulType st : ModSoulTypes.SOUL_TYPE_REG) {
				this.setSoul(stack, MinecraftClient.getInstance().world, st, this.getMaxSoul(stack, MinecraftClient.getInstance().world, st));
			}
			
			items.add(stack);
		}
    }

	@Override
	public int getMaxSoul(ItemStack stack, World world, SoulType type) {
		return 1000;
	}
	
	@Override
	public boolean hasEnchantmentGlint(ItemStack stack) {
		return true;
	}
	
	@Override
	public Rarity getRarity(ItemStack stack) {
		return Rarity.EPIC;
	}
	
	@Override
	public boolean subtractSoul(ItemStack stack, World world, SoulType type, int amount) {
        return true;
    }
}
