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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class BaseSoulEssenceStaffItem extends Item implements SoulEssenceStaff {

	private final int maxSoul;

	public BaseSoulEssenceStaffItem(int maxSoul, Item.Settings settings) {
		super(settings);
        this.maxSoul = maxSoul;
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
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> items) {
		if (this.isIn(group)) {
			items.add(new ItemStack(this));
			
			ItemStack stack = new ItemStack(this);
			
			for(SoulType st : ModSoulTypes.SOUL_TYPE) {
				this.setSoul(stack, MinecraftClient.getInstance().world, st, this.getMaxSoul(stack, MinecraftClient.getInstance().world, st));
			}
			
			items.add(stack);
		}
    }

	@Override
	public int getMaxSoul(ItemStack stack, World world, SoulType type) {
		return this.maxSoul;
	}
}
