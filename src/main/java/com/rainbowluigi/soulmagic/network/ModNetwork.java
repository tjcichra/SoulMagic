package com.rainbowluigi.soulmagic.network;

import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.util.Identifier;

public class ModNetwork {
	
	//Client to server packets
	public static final Identifier SOUL_GEM_INDEX = new Identifier(Reference.MOD_ID, "soul_gem_idex");
	public static final Identifier ACCESSORIES_OPEN = new Identifier(Reference.MOD_ID, "accessories_open");
	public static final Identifier FLYING_CHEST_OPEN = new Identifier(Reference.MOD_ID, "flying_chest_open");
	public static final Identifier UPGRADE_STATION = new Identifier(Reference.MOD_ID, "upgrade_station");
	public static final Identifier UPGRADE_STATION_TAKE_ITEMS = new Identifier(Reference.MOD_ID, "upgrade_station_take_items");

	//Server to client packets
	public static final Identifier ENTITY_RENDER = new Identifier(Reference.MOD_ID, "entity_render");

	public static void registerClientToServerPackets() {
		ServerSidePacketRegistry.INSTANCE.register(SOUL_GEM_INDEX, SoulGemIndexMessage::handle);
		ServerSidePacketRegistry.INSTANCE.register(ACCESSORIES_OPEN, AccessoriesOpenMessage::handle);
		ServerSidePacketRegistry.INSTANCE.register(FLYING_CHEST_OPEN, FlyingChestOpenMessage::handle);
		ServerSidePacketRegistry.INSTANCE.register(UPGRADE_STATION, UpgradeStationMessage::handle);
		ServerSidePacketRegistry.INSTANCE.register(UPGRADE_STATION_TAKE_ITEMS, UpgradeStationTakeItemsMessage::handle);
	}

	public static void registerServerToClientPackets() {
		ClientSidePacketRegistry.INSTANCE.register(ENTITY_RENDER, EntityRenderMessage::handle);
	}
}
