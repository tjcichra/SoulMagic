package com.rainbowluigi.soulmagic.item;

import com.rainbowluigi.soulmagic.entity.SpiritFlameEntity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class SpiritLampItem extends Item {

	public SpiritLampItem(Settings settings) {
		super(settings);
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getStackInHand(hand);
		player.setCurrentHand(hand);
		return TypedActionResult.consume(itemStack);
	}

	@Override
	public void usageTick(World world, LivingEntity entity, ItemStack stack, int remainingUseTicks) {
		if(remainingUseTicks % 20 == 0) {
			System.out.println(remainingUseTicks);
			
			world.spawnEntity(new SpiritFlameEntity(world, entity.getX(), entity.getY(), entity.getZ(), entity));
		}
	}
}