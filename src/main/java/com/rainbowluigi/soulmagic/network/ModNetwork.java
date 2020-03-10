package com.rainbowluigi.soulmagic.network;

import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.util.Identifier;

public class ModNetwork {
	
	public static final Identifier SOUL_GEM_INDEX = new Identifier(Reference.MOD_ID, "soul_gem_idex");
	public static final Identifier OPEN_ACCESSORIES = new Identifier(Reference.MOD_ID, "accessories");

	public static void registerPackets() {
		ServerSidePacketRegistry.INSTANCE.register(SOUL_GEM_INDEX, SoulGemIndexMessage::handle);
		ServerSidePacketRegistry.INSTANCE.register(OPEN_ACCESSORIES, OpenBabulesMessage::handle);
	}
}
