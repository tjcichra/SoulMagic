package com.rainbowluigi.soulmagic.network;

import com.rainbowluigi.soulmagic.inventory.FlyingChestScreenHandler;
import com.rainbowluigi.soulmagic.inventory.ModScreenHandlerTypes;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class FlyingChestOpenMessage {

    public static PacketByteBuf makePacket(int slot) {
        PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
        pbb.writeInt(slot);
        return pbb;
    }

    public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int slot = buf.readInt();

        player.openHandledScreen(new ExtendedScreenHandlerFactory() {

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
                pbb.writeInt(slot);
                ExtendedScreenHandlerType<FlyingChestScreenHandler> e = (ExtendedScreenHandlerType<FlyingChestScreenHandler>) ModScreenHandlerTypes.FLYING_CHEST;
                return e.create(syncId, inv, pbb);
            }

            @Override
            public Text getDisplayName() {
                return Text.translatable("container.soulmagic.flying_chest");
            }

            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeInt(slot);
            }
        });
    }
}
