package com.rainbowluigi.soulmagic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.util.ItemHelper;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@Mixin(BowItem.class)
public class BowItemMixin {

	@ModifyVariable(method = "onStoppedUsing", name = {"boolean_1"}, at = @At("STORE"))
	public boolean hasSoulQuiver(boolean base, ItemStack itemStack_1, World world_1, LivingEntity livingEntity_1, int int_1) {
		//System.out.println("onStoppedUsing " + base);
		return base || ItemHelper.findItem((PlayerEntity) livingEntity_1, ModItems.SOUL_QUIVER) != null;
	}
	
	@ModifyVariable(method = "onStoppedUsing", name = {"projectileEntity_1"}, at = @At("STORE"))
	public ProjectileEntity getSoulQuiverArrow(ProjectileEntity base, ItemStack itemStack_1, World world_1, LivingEntity livingEntity_1, int int_1) {
		if(ItemHelper.findItem((PlayerEntity) livingEntity_1, ModItems.SOUL_QUIVER) != null) {
			//System.out.println("getSoulQuiverArrow " + base);
			return base;
		}
		return base;
	}
	
	@ModifyVariable(method = "use", name = "boolean_1", at = @At(value = "STORE"))
	public boolean hasSoulQuiverOnUse(boolean base, World world_1, PlayerEntity playerEntity_1, Hand hand_1) {
		return base || ItemHelper.findItem(playerEntity_1, ModItems.SOUL_QUIVER) != null;
	}
}
