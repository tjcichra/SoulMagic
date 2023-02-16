package com.rainbowluigi.soulmagic.item;

import com.rainbowluigi.soulmagic.entity.SpiritFlameEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
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
    //Called as the player is using the item
    public void usageTick(World world, LivingEntity caster, ItemStack stack, int remainingUseTicks) {
        //Only calls the summoning code every second
        if (remainingUseTicks % 20 == 0) {
            //Calculates the position of the spirit flame
            double x = MathHelper.nextDouble(world.random, caster.getX() - 3, caster.getX() + 3);
            double y = MathHelper.nextDouble(world.random, caster.getY(), caster.getY() + 3);
            double z = MathHelper.nextDouble(world.random, caster.getZ() - 3, caster.getZ() + 3);

            //Sets the caster of the flame as the player and spawns it into the world
            SpiritFlameEntity flame = new SpiritFlameEntity(world, x, y, z);
            flame.setCaster(caster);
            world.spawnEntity(flame);
        }
    }
}