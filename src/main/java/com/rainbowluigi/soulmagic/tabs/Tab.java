package com.rainbowluigi.soulmagic.tabs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

public class Tab {
    
    private ItemStack icon;
    private Text text;

    public Tab(ItemStack icon, String text) {
        this.icon = icon;
        this.text = new TranslatableText(text);
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    public Text getText(PlayerEntity player, World world) {
        return this.text;
    }

    public boolean isVisible(PlayerEntity player, World world) {
        return true;
    }

    public void whenClicked(PlayerEntity player, World world) {
        
    }
}