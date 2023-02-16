package com.rainbowluigi.soulmagic.item;

import com.rainbowluigi.soulmagic.entity.UniverseRingEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class UniverseRingItem extends Item {

	public UniverseRingItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if(!world.isClient) {
			UniverseRingEntity ure = new UniverseRingEntity(world, player);
			Vec3d cam = player.getCameraPosVec(1);
			world.spawnEntity(ure);
		}
		return new TypedActionResult<ItemStack>(ActionResult.PASS, player.getStackInHand(hand));
	}
}
