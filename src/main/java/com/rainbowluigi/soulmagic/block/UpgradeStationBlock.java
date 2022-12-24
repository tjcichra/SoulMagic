package com.rainbowluigi.soulmagic.block;

import com.rainbowluigi.soulmagic.inventory.UpgradeStationScreenHandler;
import com.rainbowluigi.soulmagic.item.Upgradeable;
import com.rainbowluigi.soulmagic.stats.ModStats;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UpgradeStationBlock extends Block {
	private static final Text TITLE = Text.translatable("container.soulmagic.upgrading");

	public UpgradeStationBlock(Block.Settings settings) {
		super(settings);
	}

	@Override
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		}

		player.incrementStat(ModStats.INTERACT_WITH_UPGRADE_STATION);

		ItemStack stack = player.getStackInHand(hand);
		if (!(stack.getItem() instanceof Upgradeable)) {
			if (!stack.isEmpty()) {
				player.sendMessage(Text.translatable("soulmagic.upgrade_station.not_upgradeable", stack.getName()));
			}

			return ActionResult.FAIL;
		}

		player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
		return ActionResult.CONSUME;
	}

	@Override
	@SuppressWarnings("deprecation")
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> new UpgradeStationScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos)), TITLE);
	}
}