package com.rainbowluigi.soulmagic.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("soulmagic.brace.multiplier", this.MULTIPLIER));
    }

    public int getColor(ItemStack stack) {
        return this.color;
    }
}
