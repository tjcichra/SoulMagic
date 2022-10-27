package com.rainbowluigi.soulmagic.item.accessory;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class FoodCharmItem extends Item implements Accessory {

	public FoodCharmItem(Settings settings) {
		super(settings);
	}

	@Override
	public void onWearTick(ItemStack stack, World world, PlayerEntity entity, int int_1) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;

			if (!stack.hasNbt() || (stack.getNbt().contains("hunger") ? stack.getNbt().getInt("hunger") : 0) < 50) {
				for (int i = 0; i < player.getInventory().size(); i++) {
					if (player.getInventory().getStack(i).isFood()) {
						ItemStack foodStack = player.getInventory().getStack(i);
						FoodComponent foodComponent = foodStack.getItem().getFoodComponent();

						if (foodComponent.getStatusEffects().isEmpty()) {
							if (!stack.hasNbt()) {
								stack.setNbt(new NbtCompound());
							}

							int hunger = stack.getNbt().contains("hunger") ? stack.getNbt().getInt("hunger") : 0;
							float saturationModifier = stack.getNbt().contains("saturationModifier")
									? stack.getNbt().getFloat("saturationModifier")
									: 0;

							stack.getNbt().putInt("hunger", hunger + foodComponent.getHunger());
							stack.getNbt().putFloat("saturationModifier",
									saturationModifier + foodComponent.getSaturationModifier());

							// world.playSound((PlayerEntity) null, player.getX(), player.getY(),
							// player.getZ(), SoundEvents.CONS,
							// SoundCategory.PLAYERS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
							foodStack.decrement(1);
						}
					}
				}
			}

			if (player.getHungerManager().isNotFull()) {
				if (stack.hasNbt() && stack.getNbt().contains("hunger")
						&& stack.getNbt().contains("saturationModifier")) {
					int hunger = stack.getNbt().getInt("hunger");
					float saturationModifier = stack.getNbt().getFloat("saturationModifier");

					if (hunger > 0) {
						int hungerConsumed = Math.min(hunger, 20 - player.getHungerManager().getFoodLevel());
						float saturationConsumed = hungerConsumed * saturationModifier / hunger;

						// if(hunger >= hungerConsumed) {

						player.getHungerManager().add(hungerConsumed, saturationConsumed);

						stack.getNbt().putInt("hunger", hunger - hungerConsumed);
						stack.getNbt().putFloat("saturationModifier", saturationModifier - saturationConsumed);

						player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
						world.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(),
								SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F,
								world.random.nextFloat() * 0.1F + 0.9F);
						if (player instanceof ServerPlayerEntity) {
							Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
						}
						//}
					}
				}
			}
		}
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world_1, List<Text> list, TooltipContext tooltipContext_1) {
		if(stack.hasNbt()) {
			//int hunger = stack.getTag().contains("hunger") ? stack.getTag().getInt("hunger") : 0;
			//float saturationModifier = stack.getTag().contains("saturationModifier") ? stack.getTag().getFloat("saturationModifier") : 0;
			
			//TODO Add way to display food charm info
		}
	}
}
