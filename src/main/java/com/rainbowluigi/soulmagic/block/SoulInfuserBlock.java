package com.rainbowluigi.soulmagic.block;

import com.rainbowluigi.soulmagic.block.entity.SoulInfuserBlockEntity;
import com.rainbowluigi.soulmagic.inventory.ModContainerFactories;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.container.ContainerProviderImpl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SoulInfuserBlock extends BlockWithEntity {

	public SoulInfuserBlock(Block.Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView blockView_1) {
		return new SoulInfuserBlockEntity();
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState blockState_1) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (stack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			
			if (blockEntity instanceof SoulInfuserBlockEntity) {
				((SoulInfuserBlockEntity)blockEntity).setCustomName(stack.getName());
			}
		}
	}
	
	@Override
	public void onBlockRemoved(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = worldIn.getBlockEntity(pos);

			if (blockEntity instanceof SoulInfuserBlockEntity) {
				ItemScatterer.spawn(worldIn, pos, (SoulInfuserBlockEntity)blockEntity);
				worldIn.updateHorizontalAdjacent(pos, this);
			}
			
			super.onBlockRemoved(state, worldIn, pos, newState, isMoving);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult bhr) {
		if (!worldIn.isClient) {
			BlockEntity blockEntity = worldIn.getBlockEntity(pos);
			
			if (blockEntity instanceof SoulInfuserBlockEntity) {
				ContainerProviderImpl.INSTANCE.openContainer(ModContainerFactories.SOUL_INFUSER_FACTORY, player, buf -> {
					buf.writeBlockPos(pos);
				});
			}
		}
		return ActionResult.SUCCESS;
	}
	
	@Override
	public boolean hasComparatorOutput(BlockState blockState_1) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState blockState_1, World world_1, BlockPos blockPos_1) {
		return Container.calculateComparatorOutput(world_1.getBlockEntity(blockPos_1));
	}
}
