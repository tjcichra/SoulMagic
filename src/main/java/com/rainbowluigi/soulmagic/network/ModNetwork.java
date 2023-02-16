package com.rainbowluigi.soulmagic.network;

import com.rainbowluigi.soulmagic.util.Reference;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ModNetwork {

    //Client to server packets
    public static final Identifier SOUL_GEM_INDEX = new Identifier(Reference.MOD_ID, "soul_gem_idex");
    public static final Identifier ACCESSORIES_OPEN = new Identifier(Reference.MOD_ID, "accessories_open");
    public static final Identifier FLYING_CHEST_OPEN = new Identifier(Reference.MOD_ID, "flying_chest_open");
    public static final Identifier UPGRADE_STATION = new Identifier(Reference.MOD_ID, "upgrade_station");
    public static final Identifier UPGRADE_STATION_TAKE_ITEMS = new Identifier(Reference.MOD_ID, "upgrade_station_take_items");
    public static final Identifier MOVE_ITEMS_PACKET = new Identifier(Reference.MOD_ID, "upgrade_station_take_items");

    //Server to client packets
    public static final Identifier ENTITY_RENDER = new Identifier(Reference.MOD_ID, "entity_render");

    public static void registerClientToServerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(SOUL_GEM_INDEX, SoulGemIndexMessage::handle);
        ServerPlayNetworking.registerGlobalReceiver(ACCESSORIES_OPEN, AccessoriesOpenMessage::handle);
        ServerPlayNetworking.registerGlobalReceiver(FLYING_CHEST_OPEN, FlyingChestOpenMessage::handle);
        ServerPlayNetworking.registerGlobalReceiver(UPGRADE_STATION, UpgradeStationMessage::handle);
        ServerPlayNetworking.registerGlobalReceiver(UPGRADE_STATION_TAKE_ITEMS, UpgradeStationTakeItemsMessage::handle);
        ServerPlayNetworking.registerGlobalReceiver(UPGRADE_STATION_TAKE_ITEMS, UpgradeStationTakeItemsMessage::handle);
    }

    public static void registerServerToClientPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ENTITY_RENDER, EntityRenderMessage::handle);
    }
}
