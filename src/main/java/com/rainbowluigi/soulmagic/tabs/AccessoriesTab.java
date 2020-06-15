package com.rainbowluigi.soulmagic.tabs;

import com.rainbowluigi.soulmagic.inventory.ModContainerFactories;
import com.rainbowluigi.soulmagic.network.ModNetwork;
import com.rainbowluigi.soulmagic.network.OpenContainerMessage;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class AccessoriesTab extends Tab {

    public AccessoriesTab() {
        super(new ItemStack(Items.DIAMOND_CHESTPLATE), "container.soulmagic.accessories");
    }

    @Override
    public void whenClicked(PlayerEntity player, World world) {
        ClientSidePacketRegistry.INSTANCE.sendToServer(ModNetwork.OPEN_CONTAINER, OpenContainerMessage.makePacket(ModContainerFactories.ACCESSORY));
    }
}