package com.rainbowluigi.soulmagic.item;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class FoodCharmItem extends Item implements Accessory {

	public FoodCharmItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		
		if (player.canConsume(false) && stack.hasTag()) {
			player.setCurrentHand(hand);
			return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, stack);
		} else {
			return new TypedActionResult<ItemStack>(ActionResult.FAIL, stack);
		}
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.EAT;
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 8;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int int_1, boolean boolean_1) {
		if(entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			
			for (int i = 0; i < player.inventory.getInvSize(); i++) {
				if(player.inventory.getInvStack(i).isFood()) {
					ItemStack foodStack = player.inventory.getInvStack(i);
					FoodComponent foodComponent = foodStack.getItem().getFoodComponent();
					
					if(foodComponent.getStatusEffects().isEmpty()) {
						if(!stack.hasTag()) {
							stack.setTag(new CompoundTag());
						}
						int hunger = stack.getTag().contains("hunger") ? stack.getTag().getInt("hunger") : 0;
						float saturationModifier = stack.getTag().contains("saturationModifier") ? stack.getTag().getFloat("saturationModifier") : 0;
						
						stack.getTag().putInt("hunger", hunger + foodComponent.getHunger());
						stack.getTag().putFloat("saturationModifier", saturationModifier + foodComponent.getSaturationModifier());
						
						foodStack.decrement(1);
					}
				}
			}
		}
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity entity) {
		if(entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			
			if(stack.hasTag()) {
				int hunger = stack.getTag().contains("hunger") ? stack.getTag().getInt("hunger") : 0;
				float saturationModifier = stack.getTag().contains("saturationModifier") ? stack.getTag().getFloat("saturationModifier") : 0;
				
				if(hunger > 0) {
					player.getHungerManager().add(1, 0.1f);
					
					if(stack.getTag().contains("hunger")) {
						stack.getTag().putInt("hunger", hunger - 1);
					}
					
					if(stack.getTag().contains("saturationModifier") && saturationModifier >= 0.1) {
						stack.getTag().putFloat("saturationModifier", saturationModifier - 0.1f);
					}

					player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
					world.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_BURP,
							SoundCategory.PLAYERS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
					if (player instanceof ServerPlayerEntity) {
						Criterions.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
					}
				}
			}
		}
		
		world.playSound((PlayerEntity)null, entity.getX(), entity.getY(), entity.getZ(), entity.getEatSound(stack), SoundCategory.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
		return super.finishUsing(stack, world, entity);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world_1, List<Text> list, TooltipContext tooltipContext_1) {
		if(stack.hasTag()) {
			int hunger = stack.getTag().contains("hunger") ? stack.getTag().getInt("hunger") : 0;
			float saturationModifier = stack.getTag().contains("saturationModifier") ? stack.getTag().getFloat("saturationModifier") : 0;
			
			list.add(new LiteralText("" + hunger));
			list.add(new LiteralText("" + saturationModifier));
		}
	}
}
