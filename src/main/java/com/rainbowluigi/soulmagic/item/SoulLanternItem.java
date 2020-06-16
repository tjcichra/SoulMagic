package com.rainbowluigi.soulmagic.item;

import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.block.SoulFlameBlock;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaffDisplayer;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoulLanternItem extends Item implements DyeableItem, SoulEssenceStaffDisplayer {

	private SoulType[] types = {ModSoulTypes.LIGHT};
	
	public SoulLanternItem(Settings settings) {
		super(settings);
	}
	
	public ActionResult useOnBlock(ItemUsageContext context) {
		ActionResult actionResult_1 = this.place(new ItemPlacementContext(context));
		return actionResult_1;
	}

	public ActionResult place(ItemPlacementContext context) {
		if (!context.canPlace()) {
			return ActionResult.FAIL;
		} else {
			BlockState state = this.getPlacementState(context);
			if(state == null) {
				return ActionResult.FAIL;
			} else if (!this.place(context, state)) {
				return ActionResult.FAIL;
			} else {
				BlockPos pos = context.getBlockPos();
				World world = context.getWorld();
				PlayerEntity entity = context.getPlayer();
				ItemStack stack = context.getStack();
				BlockState state2 = world.getBlockState(pos);
				Block block = state2.getBlock();
				
				if (block == state.getBlock()) {
					if(entity instanceof ServerPlayerEntity) {
						Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) entity, pos, stack);
					}
				}

				BlockSoundGroup blockSoundGroup_1 = state2.getSoundGroup();
				world.playSound(entity, pos, state2.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, (blockSoundGroup_1.getVolume() + 1.0F) / 2.0F, blockSoundGroup_1.getPitch() * 0.8F);
				return ActionResult.SUCCESS;
			}
		}
	}
	
	protected BlockState getPlacementState(ItemPlacementContext context) {
		BlockState state = ModBlocks.SOUL_FLAME.getPlacementState(context);
		int color = this.getColor(context.getStack());
		//SoulMagic.LOGGER.info((color >>> 16) / 255f * 7);
		
		state = state.with(SoulFlameBlock.RED, (int) ((color >>> 16) / 255f * 7 + 0.5));
		state = state.with(SoulFlameBlock.GREEN, (int) ((color >>> 8 & 255) / 255f * 7 + 0.5));
		state = state.with(SoulFlameBlock.BLUE, (int) ((color & 255) / 255f * 7 + 0.5));
		
		return state != null && this.canPlace(context, state) ? state : null;
	}
	
	public void removeColor(ItemStack stack) {
		CompoundTag compoundTag_1 = stack.getSubTag("display");
		if (compoundTag_1 != null && compoundTag_1.contains("color")) {
			MinecraftClient client = MinecraftClient.getInstance();
			compoundTag_1.remove("color");
			PlayerEntity player = client.player;
			client.world.playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1, 1, false);
		}
	}
	
	public int getColor(ItemStack itemStack_1) {
		CompoundTag compoundTag_1 = itemStack_1.getSubTag("display");
		return compoundTag_1 != null && compoundTag_1.contains("color", 99) ? compoundTag_1.getInt("color") : 0xFF7100;
	}
	
	protected boolean canPlace(ItemPlacementContext context, BlockState state) {
		PlayerEntity entity = context.getPlayer();
		
		if(SoulEssenceStaff.hasSoul(context.getPlayer(), context.getWorld(), ModSoulTypes.LIGHT, 5)) {
			ShapeContext entityContext = entity == null ? ShapeContext.absent() : ShapeContext.of(entity);
			return (state.canPlaceAt(context.getWorld(), context.getBlockPos())) && context.getWorld().canPlace(state, context.getBlockPos(), entityContext);
		}
		return false;
	}
	
	private boolean place(ItemPlacementContext context, BlockState state) {
		return context.getWorld().setBlockState(context.getBlockPos(), state, 11);
	}

	@Override
	public SoulType[] getSoulTypesToShow(ItemStack stack, PlayerEntity player) {
		return this.types;
	}
}
