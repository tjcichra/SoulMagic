package com.rainbowluigi.soulmagic.block;

import com.rainbowluigi.soulmagic.block.entity.SoulSeparatorBlockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SoulSeparatorBlock extends BlockWithEntity {

	public SoulSeparatorBlock(Block.Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SoulSeparatorBlockEntity(pos, state);
	}

	@Override
	public void onPlaced(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (stack.hasCustomName()) {
			BlockEntity blockEntity = worldIn.getBlockEntity(pos);

			if (blockEntity instanceof SoulSeparatorBlockEntity) {
				((SoulSeparatorBlockEntity) blockEntity).setCustomName(stack.getName());
			}
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean notify) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof SoulSeparatorBlockEntity) {
				ItemScatterer.spawn(world, pos, (SoulSeparatorBlockEntity) blockEntity);
			}

			super.onStateReplaced(state, world, pos, newState, notify);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult bhr) {
		if (!worldIn.isClient) {
			BlockEntity blockEntity = worldIn.getBlockEntity(pos);

			if (blockEntity instanceof SoulSeparatorBlockEntity) {
				player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
			}
		}
		return ActionResult.SUCCESS;
	}
}
