package com.rainbowluigi.soulmagic.item.soulessence;

import java.util.List;

import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
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

	public void addToCreativeMenu(FabricItemGroupEntries entries) {
		MinecraftClient client = MinecraftClient.getInstance();
		ItemStack stack = new ItemStack(this);
		for(SoulType st : ModSoulTypes.SOUL_TYPE) {
			this.setSoul(stack, client.world, st, this.getMaxSoul(stack, client.world, st));
		}

		entries.add(stack);
	}

    //Fixed at 1000, this may change somehow to have no limit
    @Override
    public int getMaxSoul(ItemStack stack, World world, SoulType type) {
        return 1000;
    }
}