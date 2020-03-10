package com.rainbowluigi.soulmagic.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.item.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.util.ItemHelper;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Mixin(Item.class)
public class ItemMixin {

	@Inject(at = @At("TAIL"), method = "onCraft")
	public void onCraft(ItemStack stack, World world, PlayerEntity player, CallbackInfo callback) {
		if(stack.isEnchantable()) {
			ItemStack charm = ItemHelper.findAccessory(player, ModItems.ENCHANTMENT_CHARM);
			
			if(charm != null) {
				if(SoulEssenceStaff.hasSoul(player, world, ModSoulTypes.LIGHT, 5)) {
					EnchantmentHelper.enchant(player.getRandom(), stack, MathHelper.nextInt(player.getRandom(), 5, 15), false);
					
					charm.damage(1, player, (Consumer<LivingEntity>)((livingEntity_1x) -> {
						livingEntity_1x.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
					}));
				}
			}
		}
	}
}
