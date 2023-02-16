package com.rainbowluigi.soulmagic.item;

import com.rainbowluigi.soulmagic.upgrade.UpgradeSprite;
import com.rainbowluigi.soulmagic.util.ItemHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BuildingStaffItem extends Item implements CircleSelection {

	private final int maxMode = 3;
	private final String[] strings = {"text.soulmagic.bluidtoplayer", "text.soulmagic.buildfromplayer", "text.soulmagic.expandhorizontally"};

	public BuildingStaffItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if(context.getPlayer() != null && context.getPlayer().isSneaking()) {
			ItemStack stack = context.getStack();
			BlockState state = context.getWorld().getBlockState(context.getBlockPos());

			NbtCompound tag = stack.getOrCreateNbt();
			tag.putString("block", Registries.BLOCK.getId(state.getBlock()).toString());

			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		HitResult result = user.raycast(20, 0, false);

		if(result.getType() == HitResult.Type.BLOCK) {
			ItemStack stack = user.getStackInHand(hand);

			if(stack.hasNbt() && stack.getNbt().contains("block")) {
				Block block = Registries.BLOCK.get(new Identifier(stack.getNbt().getString("block")));
				List<ItemStack> invStack = ItemHelper.findAllItem(user, block);
				
				if(user.isCreative() || !invStack.isEmpty()) {
					BlockState state = block.getDefaultState();

					BlockHitResult blockresult = (BlockHitResult) result;
					Direction face = blockresult.getSide();
					BlockPos pos = blockresult.getBlockPos();

					int count = switch (face.getAxis()) {
						case X -> (int) (user.getX() - pos.getX());
						case Y -> (int) (user.getY() - pos.getY());
						case Z -> (int) (user.getZ() - pos.getZ());
					};

					count = Math.abs(count);

					for(int i = 1; i <= count; i++) {
						if(user.isCreative() || hasItem(invStack)) {
							BlockPos placedpos = pos.offset(face, i);
							
							if(world.getBlockState(placedpos).isAir() && state.canPlaceAt(world, placedpos)) {
								world.setBlockState(placedpos, state);
								if(!user.isCreative()) {
									decrementItem(invStack);
								}
							}
						} else {
							return TypedActionResult.fail(user.getStackInHand(hand));
						}
					}

					return TypedActionResult.success(user.getStackInHand(hand));
				}
			}
		}

		return TypedActionResult.fail(user.getStackInHand(hand));
	}

	public boolean hasItem(List<ItemStack> list) {
		for(ItemStack stack : list) {
			if(!stack.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public void decrementItem(List<ItemStack> list) {
		for(ItemStack stack : list) {
			if(!stack.isEmpty()) {
				stack.decrement(1);
				return;
			}
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		if(stack.hasNbt() && stack.getNbt().contains("block")) {
			Block block = Registries.BLOCK.get(new Identifier(stack.getNbt().getString("block")));
			tooltip.add(block.getName());
		}
	}

	@Override
	public List<CircleSelectionEntry> getEntries(ItemStack stack) {
		List<CircleSelectionEntry> l = new ArrayList<>();

		for(int i = 0; i < this.maxMode; i++) {
			l.add(new CircleSelectionEntry(Text.translatable(strings[i]), new UpgradeSprite(UpgradeSprite.baseTexture, 0, 0, 32, 32)));
		}

		return l;
	}

	@Override
	public void onSelection(int index, ItemStack stack) {

	}
}