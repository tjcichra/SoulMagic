package com.rainbowluigi.soulmagic.network;

import com.rainbowluigi.soulmagic.inventory.ModContainerFactories;

import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.impl.container.ContainerProviderImpl;
import net.minecraft.network.PacketByteBuf;

public class OpenBabulesMessage {
	
	public static void handle(PacketContext context, PacketByteBuf buffer) {
		ContainerProviderImpl.INSTANCE.openContainer(ModContainerFactories.ACCESSORY, context.getPlayer(), buf -> {});
	}
}
