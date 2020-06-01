package com.rainbowluigi.soulmagic.mixin;

import com.rainbowluigi.soulmagic.item.BraceItem;
import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.util.SoulGemHelper;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilContainerMixin extends ForgingScreenHandler {

	@Shadow
	@Final
	private Property levelCost;
	
	public AnvilContainerMixin(ScreenHandlerType<?> containerType_1, int int_1, PlayerInventory playerInventory_1, ScreenHandlerContext blockContext_1) {
		super(containerType_1, int_1, playerInventory_1, blockContext_1);
		// TODO Auto-generated constructor stub
	}

	@Inject(method = "updateResult", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "getItem"), cancellable = true)
	public void anvil(CallbackInfo info, ItemStack itemStack_1, int int_1, int int_2, int int_3, ItemStack left, ItemStack right) {
		
		if(left.getItem() == ModItems.SOUL_GEM && right.getItem() instanceof BraceItem) {
			ItemStack output = left.copy();
			SoulGemHelper.setBrace(output, right.copy());
			
			this.getSlot(2).setStack(output);
			this.levelCost.set(3);
			info.cancel();
		}
	}
}
