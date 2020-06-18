package com.rainbowluigi.soulmagic.block;

import com.rainbowluigi.soulmagic.block.entity.SoulEssenceInfuserBlockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SoulEssenceInfuserBlock extends BlockWithEntity {

	public SoulEssenceInfuserBlock(Block.Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new SoulEssenceInfuserBlockEntity();
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (stack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			
			if (blockEntity instanceof SoulEssenceInfuserBlockEntity) {
				((SoulEssenceInfuserBlockEntity)blockEntity).setCustomName(stack.getName());
			}
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean notify) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof SoulEssenceInfuserBlockEntity) {
				ItemScatterer.spawn(world, pos, (SoulEssenceInfuserBlockEntity) blockEntity);
			}
			
			super.onStateReplaced(state, world, pos, newState, notify);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult bhr) {
		if (!worldIn.isClient) {
			BlockEntity blockEntity = worldIn.getBlockEntity(pos);
			
			if (blockEntity instanceof SoulEssenceInfuserBlockEntity) {
				player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
			}
			return ActionResult.CONSUME;
		}
		return ActionResult.SUCCESS;
	}
	
	@Override
	public boolean hasComparatorOutput(BlockState blockState_1) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState blockState_1, World world_1, BlockPos blockPos_1) {
		return ScreenHandler.calculateComparatorOutput(world_1.getBlockEntity(blockPos_1));
	}
}
