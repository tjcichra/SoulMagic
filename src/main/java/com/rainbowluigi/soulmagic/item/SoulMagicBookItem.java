package com.rainbowluigi.soulmagic.item;

import com.rainbowluigi.soulmagic.client.screen.SoulMagicBookScreen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SoulMagicBookItem extends Item {

	public SoulMagicBookItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		MinecraftClient mc = MinecraftClient.getInstance();
		mc.setScreen(new SoulMagicBookScreen());
		return TypedActionResult.pass(user.getStackInHand(hand));
	}
}