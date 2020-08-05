package com.rainbowluigi.soulmagic.item;

import java.util.List;

import com.rainbowluigi.soulmagic.SoulMagic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CalmingFluteItem extends Item {

	public CalmingFluteItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		List<Entity> entities = world.getEntities(player, player.getBoundingBox().expand(4));

		for(Entity e : entities) {
			if(e instanceof LivingEntity) {
				LivingEntity le = (LivingEntity) e;
				SoulMagic.LOGGER.info(le);
				le.setAttacking(null);
			}
		}

		return TypedActionResult.pass(player.getStackInHand(hand));
	}
}