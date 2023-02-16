package com.rainbowluigi.soulmagic.tabs;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ModTabs {

    public static List<Tab> tabsList = new ArrayList<>();

    public static final Tab INVENTORY = new Tab(new ItemStack(Items.CHEST), "container.soulmagic.inventory");
    public static final Tab HORSE = new Tab(new ItemStack(Items.SADDLE), "container.soulmagic.horse");
    public static final Tab ACCESSORIES = new AccessoriesTab();
    public static final Tab FLYING_CHEST = new FlyingChestTab();

    public static void registerTabs() {
        registerTab(INVENTORY);
        registerTab(HORSE);
        registerTab(ACCESSORIES);
        registerTab(FLYING_CHEST);
    }

    public static void registerTab(Tab t) {
        tabsList.add(t);
    }
}