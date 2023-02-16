package com.rainbowluigi.soulmagic.item;

import com.rainbowluigi.soulmagic.client.screen.SoulMagicBookScreen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;

public class SoulMagicBookItem extends Item {

	public SoulMagicBookItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//		ItemStack stack = user.getStackInHand(hand);

		if (user instanceof ServerPlayerEntity serverPlayerEntity) {
//			UseItemSuccessTrigger.INSTANCE.trigger(player, stack, player.getLevel(), player.getX(), player.getY(), player.getZ());
			PatchouliAPI.get().openBookGUI(serverPlayerEntity, Registries.ITEM.getId(this));
//			playerIn.playSound(BotaniaSounds.lexiconOpen, 1F, (float) (0.7 + Math.random() * 0.4));
		}

		return TypedActionResult.pass(user.getStackInHand(hand));
	}
}