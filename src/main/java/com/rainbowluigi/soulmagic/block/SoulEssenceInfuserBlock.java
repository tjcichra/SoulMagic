package com.rainbowluigi.soulmagic.block;

import com.rainbowluigi.soulmagic.block.entity.ModBlockEntity;
import com.rainbowluigi.soulmagic.block.entity.SoulEssenceInfuserBlockEntity;
import com.rainbowluigi.soulmagic.item.Upgradeable;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.block.enums.ChestType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class SoulEssenceInfuserBlock extends BlockWithEntity {
	public SoulEssenceInfuserBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(Properties.HORIZONTAL_FACING);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SoulEssenceInfuserBlockEntity(pos, state);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		
		if (blockEntity instanceof SoulEssenceInfuserBlockEntity soulEssenceInfuserBlockEntity) {
			if (stack.hasCustomName()) {
				soulEssenceInfuserBlockEntity.setCustomName(stack.getName());
			}

			Upgradeable u = (Upgradeable) stack.getItem();

			soulEssenceInfuserBlockEntity.setUpgrades(u.getUpgradesAndSelectionsUnlocked(stack));
			soulEssenceInfuserBlockEntity.setSelectorPoints(u.getSelectorPointsNumber(stack));
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean notify) {
		if (state.isOf(newState.getBlock())) {
			return;
		}

		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof Inventory inv) {
			ItemScatterer.spawn(world, pos, inv);
			world.updateComparators(pos, this);
		}
		super.onStateReplaced(state, world, pos, newState, notify);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		}

		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof SoulEssenceInfuserBlockEntity soulEssenceInfuserBlockEntity) {
			player.openHandledScreen(soulEssenceInfuserBlockEntity);
		}
		return ActionResult.CONSUME;
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? null : SoulEssenceInfuserBlock.checkType(type, ModBlockEntity.SOUL_INFUSER, SoulEssenceInfuserBlockEntity::tick);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean hasComparatorOutput(BlockState blockState) {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public int getComparatorOutput(BlockState blockState, World world, BlockPos blockPos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(blockPos));
	}
}
