package com.rainbowluigi.soulmagic.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InfiniteWellBlock extends Block {

    public InfiniteWellBlock(Settings block$Settings_1) {
        super(block$Settings_1);
    }

    //TODO FIX and add cool features (like auto filling bottles)
    @Override
    public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult bhr) {
        ItemStack stack = player.getStackInHand(hand);
        Item water_bucket = Fluids.WATER.getBucketItem();

        //if (player.abilities.creativeMode) {
        //	return ActionResult.PASS;
        //} else {

        player.incrementStat(Stats.USED.getOrCreateStat(Items.BUCKET));
        player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);

        if (!worldIn.isClient) {
            stack.decrement(1);
            if (stack.isEmpty()) {
                player.setStackInHand(player.getActiveHand(), new ItemStack(water_bucket));
                ;
                return ActionResult.PASS;
            } else {
                if (!player.getInventory().insertStack(new ItemStack(water_bucket))) {
                    player.dropItem(new ItemStack(water_bucket), false);
                }


            }
        }
        return ActionResult.PASS;
        //}
    }
}
