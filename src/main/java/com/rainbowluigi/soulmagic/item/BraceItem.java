package com.rainbowluigi.soulmagic.item;

import java.util.List;

import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.annotation.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class BraceItem extends Item {

	private final double MULTIPLIER;
	private int color;
	
	public BraceItem(double multiplier, int color, Settings item$Settings_1) {
		super(item$Settings_1);
		this.MULTIPLIER = multiplier;
		this.color = color;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack itemStack_1, @Nullable World world_1, List<Text> list, TooltipContext tooltipContext_1) {
		list.add(new LiteralText(this.MULTIPLIER + "x"));
	}
	
	public int getColor(ItemStack stack) {
		return this.color;
	}
}
