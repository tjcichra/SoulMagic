package com.rainbowluigi.soulmagic.inventory;

import com.rainbowluigi.soulmagic.block.entity.SoulCacheBlockEntity;
import com.rainbowluigi.soulmagic.block.entity.SoulInfuserBlockEntity;
import com.rainbowluigi.soulmagic.block.entity.SoulSeparatorBlockEntity;
import com.rainbowluigi.soulmagic.client.screen.AccessoryScreen;
import com.rainbowluigi.soulmagic.client.screen.PersonalChestScreen;
import com.rainbowluigi.soulmagic.client.screen.SoulCacheScreen;
import com.rainbowluigi.soulmagic.client.screen.SoulInfuserScreen;
import com.rainbowluigi.soulmagic.client.screen.SoulSeparatorScreen;
import com.rainbowluigi.soulmagic.util.PacketBufferUtils;
import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class ModContainerFactories {
	
	public static final Identifier SOUL_INFUSER_FACTORY = new Identifier(Reference.MOD_ID, "soul_infuser");
	public static final Identifier SOUL_SEPARATOR = new Identifier(Reference.MOD_ID, "soul_separator");
	public static final Identifier SOUL_CACHE = new Identifier(Reference.MOD_ID, "soul_cache");
	public static final Identifier ACCESSORY = new Identifier(Reference.MOD_ID, "accessory");
	public static final Identifier PERSONAL_CHEST = new Identifier(Reference.MOD_ID, "personal_chest");

    public static void registerContainerTypes() {
    	ContainerProviderRegistry.INSTANCE.registerFactory(SOUL_INFUSER_FACTORY, (s, i, player, buf) -> {
    		SoulInfuserBlockEntity be = (SoulInfuserBlockEntity) PacketBufferUtils.getBlockEntity(buf, player);
    		return new SoulInfuserContainer(s, player.inventory, be);
    	});
    	
    	ScreenProviderRegistry.INSTANCE.registerFactory(SOUL_INFUSER_FACTORY, (SoulInfuserContainer c) -> {
    		return new SoulInfuserScreen(c, MinecraftClient.getInstance().player.inventory, c.getDisplayName());
    	});
    	
    	ContainerProviderRegistry.INSTANCE.registerFactory(SOUL_SEPARATOR, (s, i, player, buf) -> {
    		SoulSeparatorBlockEntity be = (SoulSeparatorBlockEntity) PacketBufferUtils.getBlockEntity(buf, player);
    		return new SoulSeparatorContainer(s, player.inventory, be);
    	});
    	
    	ScreenProviderRegistry.INSTANCE.registerFactory(SOUL_SEPARATOR, (SoulSeparatorContainer c) -> {
    		return new SoulSeparatorScreen(c, MinecraftClient.getInstance().player.inventory, c.getDisplayName());
    	});
    	
    	ContainerProviderRegistry.INSTANCE.registerFactory(SOUL_CACHE, (s, i, player, buf) -> {
    		SoulCacheBlockEntity be = (SoulCacheBlockEntity) PacketBufferUtils.getBlockEntity(buf, player);
    		return new SoulCacheContainer(s, player.inventory, be);
    	});
    	
    	ScreenProviderRegistry.INSTANCE.registerFactory(SOUL_CACHE, (SoulCacheContainer c) -> {
    		return new SoulCacheScreen(c, MinecraftClient.getInstance().player.inventory, c.getDisplayName());
    	});
    	
    	ContainerProviderRegistry.INSTANCE.registerFactory(ACCESSORY, (s, i, player, buf) -> {
    		return new AccessoryContainer(player, s);
    	});
    	
    	ScreenProviderRegistry.INSTANCE.registerFactory(ACCESSORY, (AccessoryContainer c) -> {
    		return new AccessoryScreen(c, MinecraftClient.getInstance().player, new LiteralText("Maggie is my love"));
    	});
    	
    	ScreenProviderRegistry.INSTANCE.registerFactory(PERSONAL_CHEST, (PersonalChestContainer c) -> {
    		return new PersonalChestScreen(c, MinecraftClient.getInstance().player.inventory, c.getDisplayName());
    	});
    	
    	ContainerProviderRegistry.INSTANCE.registerFactory(PERSONAL_CHEST, (s, i, player, buf) -> {
    		ItemStack stack = buf.readItemStack();
    		ItemInventory list = new ItemInventory(27, stack);
    		
    		if(stack.hasTag()) {
    			CompoundTag tag = stack.getTag();
				ListTag listTag_1 = (ListTag) tag.get("Items");
	
				for(int i1 = 0; i1 < listTag_1.size(); i1++) {
					ItemStack stack2 = ItemStack.fromTag((CompoundTag) listTag_1.get(i1));
					System.out.println(stack2.getName().asFormattedString() + " x" + stack2.getCount());
				}
				
				for (int int_1 = 0; int_1 < listTag_1.size(); ++int_1) {
					CompoundTag compoundTag_2 = listTag_1.getCompound(int_1);
					int int_2 = compoundTag_2.getByte("Slot") & 255;
					if (int_2 >= 0 && int_2 < list.getInvSize()) {
						list.setInvStack(int_2, ItemStack.fromTag(compoundTag_2));
					}
				}
    		}
    		
    		return new PersonalChestContainer(s, player.inventory, list);
    	});
    }
}

class ItemInventory extends BasicInventory {
	
	private ItemStack stack;

	public ItemInventory(int size, ItemStack stack) {
		super(size);
		this.stack = stack;
	}
	
	public void markDirty() {
		CompoundTag tag = stack.getOrCreateTag();
		
		if(!tag.contains("Items")) {
			tag.put("Items", new ListTag());
		}
		
		ListTag list = (ListTag) tag.get("Items");
		
		for (int i = 0; i < this.getInvSize(); i++) {
			ItemStack stack = this.getInvStack(i);
			if (!stack.isEmpty()) {
				CompoundTag tag2 = new CompoundTag();
				tag2.putByte("Slot", (byte) i);
				stack.toTag(tag2);
				list.add(tag2);
			}
		}
		
		if (!list.isEmpty() || true) {
			tag.put("Items", list);
			
			list = (ListTag) tag.get("Items");
			for(int i = 0; i < list.size(); i++) {
				ItemStack stack = ItemStack.fromTag((CompoundTag) list.get(i));
				System.out.println(((CompoundTag) list.get(i)).getByte("Slot") + ": " + stack.getName().asFormattedString() + " x" + stack.getCount());
			}
		}
		
	}
}