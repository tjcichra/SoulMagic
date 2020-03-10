package com.rainbowluigi.soulmagic.block;

import com.rainbowluigi.soulmagic.block.entity.SoulSeparatorBlockEntity;
import com.rainbowluigi.soulmagic.inventory.ModContainerFactories;

import net.fabricmc.fabric.impl.container.ContainerProviderImpl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
	public BlockEntity createBlockEntity(BlockView blockView_1) {
		return new SoulSeparatorBlockEntity();
	}
	
	@Override
	public void onPlaced(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (stack.hasCustomName()) {
			BlockEntity blockEntity = worldIn.getBlockEntity(pos);
			
			if (blockEntity instanceof SoulSeparatorBlockEntity) {
				((SoulSeparatorBlockEntity)blockEntity).setCustomName(stack.getName());
			}
		}
	}
	
	@Override
	public void onBlockRemoved(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = worldIn.getBlockEntity(pos);

			if (blockEntity instanceof SoulSeparatorBlockEntity) {
				ItemScatterer.spawn(worldIn, pos, (SoulSeparatorBlockEntity)blockEntity);
				worldIn.updateHorizontalAdjacent(pos, this);
			}
			
			super.onBlockRemoved(state, worldIn, pos, newState, isMoving);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult bhr) {
		if (!worldIn.isClient) {
			BlockEntity blockEntity = worldIn.getBlockEntity(pos);
			
			if (blockEntity instanceof SoulSeparatorBlockEntity) {
				ContainerProviderImpl.INSTANCE.openContainer(ModContainerFactories.SOUL_SEPARATOR, player, buf -> {
					buf.writeBlockPos(pos);
				});
			}
		}
		return ActionResult.SUCCESS;
	}
}
