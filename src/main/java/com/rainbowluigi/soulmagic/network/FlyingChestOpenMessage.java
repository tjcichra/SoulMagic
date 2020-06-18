package com.rainbowluigi.soulmagic.network;

import com.rainbowluigi.soulmagic.inventory.FlyingChestScreenHandler;
import com.rainbowluigi.soulmagic.inventory.ModContainerFactories;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.impl.screenhandler.ExtendedScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class FlyingChestOpenMessage {

	public static PacketByteBuf makePacket(int slot) {
		PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
		pbb.writeInt(slot);
		return pbb;
	}

	public static void handle(PacketContext context, PacketByteBuf buffer) {
		int slot = buffer.readInt();

		context.getTaskQueue().execute(() -> {
			context.getPlayer().openHandledScreen(new ExtendedScreenHandlerFactory() {

				@Override
				public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
					PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
					pbb.writeInt(slot);
					ExtendedScreenHandlerType<FlyingChestScreenHandler> e = (ExtendedScreenHandlerType<FlyingChestScreenHandler>) ModContainerFactories.FLYING_CHEST;
					return e.create(syncId, inv, pbb);
				}
	
				@Override
				public Text getDisplayName() {
					return new TranslatableText("container.soulmagic.flying_chest");
				}
	
				@Override
				public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
					buf.writeInt(slot);
				}
			});
		});
	}
}
