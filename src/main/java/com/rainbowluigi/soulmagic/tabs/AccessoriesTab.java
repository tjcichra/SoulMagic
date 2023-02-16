package com.rainbowluigi.soulmagic.tabs;

import com.rainbowluigi.soulmagic.network.ModNetwork;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;

public class AccessoriesTab extends Tab {

    public AccessoriesTab() {
        super(new ItemStack(Items.DIAMOND_CHESTPLATE), "container.soulmagic.accessories");
    }

    @Override
    public void whenClicked(PlayerEntity player, World world) {
        ClientPlayNetworking.send(ModNetwork.ACCESSORIES_OPEN, new PacketByteBuf(Unpooled.buffer()));
    }
}