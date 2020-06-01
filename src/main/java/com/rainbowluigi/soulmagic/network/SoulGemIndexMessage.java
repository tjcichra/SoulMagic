package com.rainbowluigi.soulmagic.network;

import com.rainbowluigi.soulmagic.item.SoulGemItem;
import com.rainbowluigi.soulmagic.util.SoulGemHelper;

import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class SoulGemIndexMessage {
	
	public static void handle(PacketContext context, PacketByteBuf buffer) {
		PlayerEntity sender = context.getPlayer();
		
		ItemStack stack = sender.getMainHandStack();
		if(stack.getItem() instanceof SoulGemItem) {
			SoulGemHelper.setCurrentSpellIndex(stack, buffer.readInt());
		}
	}
}
