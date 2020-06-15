package com.rainbowluigi.soulmagic.tabs;

import com.rainbowluigi.soulmagic.item.ModItems;
import com.rainbowluigi.soulmagic.network.FlyingChestOpenMessage;
import com.rainbowluigi.soulmagic.network.ModNetwork;
import com.rainbowluigi.soulmagic.util.ItemHelper;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FlyingChestTab extends Tab {

    public FlyingChestTab() {
        super(new ItemStack(ModItems.FLYING_CHEST), "container.soulmagic.flying_chest");
    }

    @Override
    public boolean isVisible(PlayerEntity player, World world) {
        return ItemHelper.findAccessorySlot(player, ModItems.FLYING_CHEST) != -1;
    }

    @Override
    public void whenClicked(PlayerEntity player, World world) {
        ClientSidePacketRegistry.INSTANCE.sendToServer(ModNetwork.FLYING_CHEST_OPEN, FlyingChestOpenMessage.makePacket(ItemHelper.findAccessorySlot(player, ModItems.FLYING_CHEST)));
    }
}