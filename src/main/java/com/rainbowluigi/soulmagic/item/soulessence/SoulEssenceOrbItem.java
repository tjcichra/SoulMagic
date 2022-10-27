package com.rainbowluigi.soulmagic.item.soulessence;

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
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class SoulEssenceOrbItem extends Item implements SoulEssenceContainer {

    public SoulEssenceOrbItem(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
	@Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		for(SoulType st : ModSoulTypes.SOUL_TYPE) {
			if (this.getSoul(stack, world, st) > 0) {
				tooltip.add(Text.translatable("soulmagic.soul_essence_orb.amount", st.getName(), this.getSoul(stack, world, st)).formatted(st.getTextColor()));
			}
		}
	}

    @Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> items) {
        MinecraftClient client = MinecraftClient.getInstance();

		if (this.isIn(group)) {
			ItemStack stack = new ItemStack(this);
			
			for(SoulType st : ModSoulTypes.SOUL_TYPE) {
				this.setSoul(stack, client.world, st, this.getMaxSoul(stack, client.world, st));
			}
			
			items.add(stack);
		}
    }

    //Fixed at 1000, this may change somehow to have no limit
    @Override
    public int getMaxSoul(ItemStack stack, World world, SoulType type) {
        return 1000;
    }
}