package com.rainbowluigi.soulmagic.inventory;

import com.rainbowluigi.soulmagic.block.entity.SoulCacheBlockEntity;
import com.rainbowluigi.soulmagic.block.entity.SoulInfuserBlockEntity;
import com.rainbowluigi.soulmagic.block.entity.SoulSeparatorBlockEntity;
import com.rainbowluigi.soulmagic.client.screen.AccessoryScreen;
import com.rainbowluigi.soulmagic.client.screen.FlyingChestScreen;
import com.rainbowluigi.soulmagic.client.screen.SoulCacheScreen;
import com.rainbowluigi.soulmagic.client.screen.SoulInfuserScreen;
import com.rainbowluigi.soulmagic.client.screen.SoulSeparatorScreen;
import com.rainbowluigi.soulmagic.util.ItemHelper;
import com.rainbowluigi.soulmagic.util.PacketBufferUtils;
import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class ModContainerFactories {
	
	public static final Identifier SOUL_INFUSER_FACTORY = new Identifier(Reference.MOD_ID, "soul_infuser");
	public static final Identifier SOUL_SEPARATOR = new Identifier(Reference.MOD_ID, "soul_separator");
	public static final Identifier SOUL_CACHE = new Identifier(Reference.MOD_ID, "soul_cache");
	public static final Identifier ACCESSORY = new Identifier(Reference.MOD_ID, "accessory");
	public static final Identifier FLYING_CHEST = new Identifier(Reference.MOD_ID, "flying_chest");

    public static void registerContainerTypes() {
    	ContainerProviderRegistry.INSTANCE.registerFactory(SOUL_INFUSER_FACTORY, (s, i, player, buf) -> {
    		SoulInfuserBlockEntity be = (SoulInfuserBlockEntity) PacketBufferUtils.getBlockEntity(buf, player);
    		return new SoulInfuserScreenHandler(s, player.inventory, be);
    	});
    	
    	ScreenProviderRegistry.INSTANCE.registerFactory(SOUL_INFUSER_FACTORY, (SoulInfuserScreenHandler c) -> {
    		return new SoulInfuserScreen(c, MinecraftClient.getInstance().player.inventory, c.getDisplayName());
    	});
    	
    	ContainerProviderRegistry.INSTANCE.registerFactory(SOUL_SEPARATOR, (s, i, player, buf) -> {
    		SoulSeparatorBlockEntity be = (SoulSeparatorBlockEntity) PacketBufferUtils.getBlockEntity(buf, player);
    		return new SoulSeparatorScreenHandler(s, player.inventory, be);
    	});
    	
    	ScreenProviderRegistry.INSTANCE.registerFactory(SOUL_SEPARATOR, (SoulSeparatorScreenHandler c) -> {
    		return new SoulSeparatorScreen(c, MinecraftClient.getInstance().player.inventory, c.getDisplayName());
    	});
    	
    	ContainerProviderRegistry.INSTANCE.registerFactory(SOUL_CACHE, (s, i, player, buf) -> {
    		SoulCacheBlockEntity be = (SoulCacheBlockEntity) PacketBufferUtils.getBlockEntity(buf, player);
    		return new SoulCacheScreenHandler(s, player.inventory, be);
    	});
    	
    	ScreenProviderRegistry.INSTANCE.registerFactory(SOUL_CACHE, (SoulCacheScreenHandler c) -> {
    		return new SoulCacheScreen(c, MinecraftClient.getInstance().player.inventory, c.getDisplayName());
    	});
    	
    	ContainerProviderRegistry.INSTANCE.registerFactory(ACCESSORY, (s, i, player, buf) -> {
    		return new AccessoryContainer(player, s);
    	});
    	
    	ScreenProviderRegistry.INSTANCE.registerFactory(ACCESSORY, (AccessoryContainer c) -> {
    		return new AccessoryScreen(c, MinecraftClient.getInstance().player, new TranslatableText("container.soulmagic.accessories"));
    	});
    	
    	ContainerProviderRegistry.INSTANCE.registerFactory(FLYING_CHEST, (s, i, player, buf) -> {
			ItemStack stack = ItemHelper.getAccessoryFromSlot(player, buf.readInt());
			
			SimpleInventory chestInv = new SimpleInventory(27);
			chestInv.addListener(new FlyingChestInventory(stack));
    		
    		if(stack.hasTag()) {
    			CompoundTag tag = stack.getTag();
				ListTag invNBT = (ListTag) tag.get("Items");
				chestInv.readTags(invNBT);
    		}
    		
    		return new FlyingChestScreenHandler(s, player.inventory, chestInv, stack.hasCustomName() ? stack.getName() : null);
		});
		
		ScreenProviderRegistry.INSTANCE.registerFactory(FLYING_CHEST, (FlyingChestScreenHandler c) -> {
    		return new FlyingChestScreen(c, MinecraftClient.getInstance().player.inventory, c.getDisplayName());
    	});
    }
}