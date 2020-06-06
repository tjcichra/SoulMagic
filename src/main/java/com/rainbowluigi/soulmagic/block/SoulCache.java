package com.rainbowluigi.soulmagic.block;

import com.rainbowluigi.soulmagic.block.entity.SoulCacheBlockEntity;
import com.rainbowluigi.soulmagic.inventory.ModContainerFactories;
import com.rainbowluigi.soulmagic.item.ReferenceStaffItem;

import net.fabricmc.fabric.impl.container.ContainerProviderImpl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SoulCache extends BlockWithEntity {

	public SoulCache(Block.Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView_1) {
		return new SoulCacheBlockEntity();
	}

	@Override
	public void onPlaced(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (stack.hasCustomName()) {
			BlockEntity blockEntity = worldIn.getBlockEntity(pos);

			if (blockEntity instanceof SoulCacheBlockEntity) {
				((SoulCacheBlockEntity) blockEntity).setCustomName(stack.getName());
			}
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean notify) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof SoulCacheBlockEntity) {
				ItemScatterer.spawn(world, pos, (SoulCacheBlockEntity) blockEntity);
			}

			super.onStateReplaced(state, world, pos, newState, notify);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult bhr) {
		if (!worldIn.isClient) {
			BlockEntity blockEntity = worldIn.getBlockEntity(pos);

			if (blockEntity instanceof SoulCacheBlockEntity) {
				ItemStack stack = player.getStackInHand(hand);

				if (stack.getItem() instanceof ReferenceStaffItem) {

					player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.1F,
							(worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.35F + 0.9F);

					CompoundTag tag = stack.getOrCreateSubTag("cache");
					tag.putInt("x", pos.getX());
					tag.putInt("y", pos.getY());
					tag.putInt("z", pos.getZ());

					player.sendMessage(new TranslatableText("soulmagic.reference_staff.text", pos.getX(), + pos.getY(), + pos.getZ()), true);
				} else {
					ContainerProviderImpl.INSTANCE.openContainer(ModContainerFactories.SOUL_CACHE, player, buf -> {
						buf.writeBlockPos(pos);
					});
				}
			}
		}
		return ActionResult.SUCCESS;
	}
}
