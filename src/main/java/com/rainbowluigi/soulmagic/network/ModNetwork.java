package com.rainbowluigi.soulmagic.network;

import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.util.Identifier;

public class ModNetwork {
	
	public static final Identifier SOUL_GEM_INDEX = new Identifier(Reference.MOD_ID, "soul_gem_idex");
	public static final Identifier OPEN_CONTAINER = new Identifier(Reference.MOD_ID, "open_container");
	public static final Identifier FLYING_CHEST_OPEN = new Identifier(Reference.MOD_ID, "flying_chest_open");

	public static void registerPackets() {
		ServerSidePacketRegistry.INSTANCE.register(SOUL_GEM_INDEX, SoulGemIndexMessage::handle);
		ServerSidePacketRegistry.INSTANCE.register(OPEN_CONTAINER, OpenContainerMessage::handle);
		ServerSidePacketRegistry.INSTANCE.register(FLYING_CHEST_OPEN, FlyingChestOpenMessage::handle);
	}
}
