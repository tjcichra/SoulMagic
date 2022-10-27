package com.rainbowluigi.soulmagic.item.bound;

import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaffDisplayer;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BoundPickaxeItem extends PickaxeItem implements SoulEssenceStaffDisplayer {

	private SoulType[] types = {ModSoulTypes.LIGHT, ModSoulTypes.DARK};
	
	public BoundPickaxeItem(Settings item$Settings_1) {
		super(BoundToolMaterial.BOUND_MATERIAL, 1, -2.8F, item$Settings_1);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if(stack.hasNbt() && stack.getNbt().contains("soulGem")) {
			ItemStack gem = ItemStack.fromNbt(stack.getNbt().getCompound("soulGem"));
			player.setStackInHand(hand, gem);
		}
		return new TypedActionResult<ItemStack>(ActionResult.PASS, stack);
	}
	
	@Override
	public boolean postMine(ItemStack stack, World world, BlockState blockState_1, BlockPos blockPos_1, LivingEntity entity) {
		if(entity instanceof PlayerEntity) {
			SoulEssenceStaff.hasSoul((PlayerEntity) entity, world, ModSoulTypes.LIGHT, 5);
			super.postMine(stack, world, blockState_1, blockPos_1, entity);
		}
		return true;
	}
	
	@Override
	public SoulType[] getSoulTypesToShow(ItemStack stack, PlayerEntity player) {
		return types;
	}
}
