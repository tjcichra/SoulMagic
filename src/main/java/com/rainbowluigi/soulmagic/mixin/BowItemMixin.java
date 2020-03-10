package com.rainbowluigi.soulmagic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.item.BowItem;

@Mixin(BowItem.class)
public class BowItemMixin {

	@ModifyVariable(method = "onStoppedUsing", at = @At(value = "STORE"))
	public boolean hasSoulQuiver(boolean base) {
		System.out.println("onStoppedUsing");
		return base;
	}
	
	@ModifyVariable(method = "use", at = @At(value = "STORE"))
	public boolean hasSoulQuiverOnUse(boolean base) {
		System.out.println("use");
		return base;
	}
}
