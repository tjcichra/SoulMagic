package com.rainbowluigi.soulmagic.network;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class UpgradeStationTakeItemsMessage {
	
	public static PacketByteBuf makePacket(ItemStack[] requirements) {
		PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
		pbb.writeInt(requirements.length);
		
		for(ItemStack stack : requirements) {
			pbb.writeItemStack(stack);
		}

		return pbb;
	}
	
	public static void handle(PacketContext context, PacketByteBuf buffer) {
		int num = buffer.readInt();
		List<ItemStack> list = new ArrayList<>();

		for(int i = 0; i < num; i++) {
			list.add(buffer.readItemStack());
		}

		context.getTaskQueue().execute(() -> {
			for(ItemStack requirement : list) {
				for(ItemStack stack : context.getPlayer().inventory.main) {
					if(stack.isItemEqual(requirement) && stack.getCount() >= requirement.getCount()) {
						stack.decrement(requirement.getCount());
					}
				}
			}
		});
	}
}