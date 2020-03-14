package com.rainbowluigi.soulmagic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.rainbowluigi.soulmagic.entity.SoulArrowEntity;
import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.item.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.item.SoulEssenceStaffDisplayer;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.util.ItemHelper;
import com.rainbowluigi.soulmagic.util.SoulQuiverHelper;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Mixin(BowItem.class)
public class BowItemMixin implements SoulEssenceStaffDisplayer {

	private static SoulType[] types = new SoulType[] {ModSoulTypes.LIGHT, ModSoulTypes.DARK};
	
	@ModifyVariable(method = "onStoppedUsing", name = {"boolean_1"}, at = @At("STORE"))
	public boolean hasSoulQuiver(boolean base, ItemStack stack, World world, LivingEntity entity, int int_1) {
		if(base) {
			return base;
		}
		
		ItemStack quiver = ItemHelper.findItem((PlayerEntity) entity, ModItems.SOUL_QUIVER);
		if(quiver == null) {
			return base;
		}
		
		return base || SoulEssenceStaff.hasAtLeastSoul((PlayerEntity) entity, world, SoulQuiverHelper.getSoulType(quiver), 5);
	}
	
	@ModifyVariable(method = "onStoppedUsing", name = {"projectileEntity_1"}, at = @At("STORE"))
	public ProjectileEntity getSoulQuiverArrow(ProjectileEntity base, ItemStack itemStack_1, World world, LivingEntity entity, int int_1) {
		ItemStack quiver = ItemHelper.findItem((PlayerEntity) entity, ModItems.SOUL_QUIVER);
		if(quiver != null && SoulEssenceStaff.hasAtLeastSoul((PlayerEntity) entity, world, SoulQuiverHelper.getSoulType(quiver), 5) ) {
			SoulArrowEntity arrow = new SoulArrowEntity(world, entity);
			arrow.setSoulType(SoulQuiverHelper.getSoulType(quiver));
			return arrow;
		}
		return base;
	}
	
	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	public void useSneaking(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info) {
		if(player.isSneaking()) {
			ItemStack quiver = ItemHelper.findItem(player, ModItems.SOUL_QUIVER);
			if(quiver != null) {
				SoulQuiverHelper.incrementType(quiver);
				SoulType st = SoulQuiverHelper.getSoulType(quiver);
				player.addMessage(new TranslatableText("soulmagic.soul_quiver.text", st.getName()).formatted(st.getTextColor()), true);
				
				if(!world.isClient)
					player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.1F, (world.random.nextFloat() - world.random.nextFloat()) * 0.35F + 0.9F);
				
				info.setReturnValue(TypedActionResult.fail(player.getStackInHand(hand)));
			}
		}
	}
	
	@ModifyVariable(method = "use", name = "boolean_1", at = @At(value = "STORE"))
	public boolean hasSoulQuiverOnUse(boolean base, World world_1, PlayerEntity player, Hand hand_1) {
		if(base) {
			return base;
		}
		
		ItemStack quiver = ItemHelper.findItem(player, ModItems.SOUL_QUIVER);
		if(quiver == null) {
			return base;
		}
		
		return base || SoulEssenceStaff.hasAtLeastSoul(player, world_1, SoulQuiverHelper.getSoulType(quiver), 5);
	}
	
	@Inject(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "incrementStat"))
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity entity, int drawback, CallbackInfo callback) {
		ItemStack quiver = ItemHelper.findItem((PlayerEntity) entity, ModItems.SOUL_QUIVER);
		if(quiver != null) {
			SoulEssenceStaff.hasSoul((PlayerEntity) entity, world, SoulQuiverHelper.getSoulType(quiver), 5);
		}
	}
	
	@Override
	public boolean showDisplay(ItemStack stack, PlayerEntity player) {
		return ItemHelper.findItem(player, ModItems.SOUL_QUIVER) != null;
	}

	@Override
	public SoulType[] getSoulTypesToShow(ItemStack stack, PlayerEntity player) {
		ItemStack quiver = ItemHelper.findItem(player, ModItems.SOUL_QUIVER);
		return new SoulType[] {SoulQuiverHelper.getSoulType(quiver)};
	}
}
