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

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModContainerFactories {

	public static final Identifier SOUL_INFUSER_FACTORY = new Identifier(Reference.MOD_ID, "soul_infuser");
	public static final Identifier SOUL_SEPARATOR = new Identifier(Reference.MOD_ID, "soul_separator");
	public static final Identifier SOUL_CACHE = new Identifier(Reference.MOD_ID, "soul_cache");
	public static final Identifier ACCESSORY = new Identifier(Reference.MOD_ID, "accessory");
	public static final Identifier FLYING_CHEST = new Identifier(Reference.MOD_ID, "flying_chest");

	public static ScreenHandlerType<SoulInfuserScreenHandler> SOUL_INFUSER_HANDLER;

	public static void registerContainerTypes() {

		//Soul Infuser Stuff
		SOUL_INFUSER_HANDLER = ScreenHandlerRegistry.registerSimple(SOUL_INFUSER_FACTORY, SoulInfuserScreenHandler::new);

		ScreenRegistry.<SoulInfuserScreenHandler, SoulInfuserScreen>register(SOUL_INFUSER_HANDLER,
				new ScreenRegistry.Factory<SoulInfuserScreenHandler, SoulInfuserScreen>() {

					@Override
					public SoulInfuserScreen create(SoulInfuserScreenHandler handler, PlayerInventory inventory, Text title) {
						return new SoulInfuserScreen(handler, inventory, title);
					}
		});
		
		//Soul Separator Stuff
		ScreenHandlerType<SoulSeparatorScreenHandler> soul_separator_handler = ScreenHandlerRegistry.registerExtended(SOUL_SEPARATOR, (syncId, inv, buf) -> {
    		SoulSeparatorBlockEntity be = (SoulSeparatorBlockEntity) PacketBufferUtils.getBlockEntity(buf, inv.player);
    		return new SoulSeparatorScreenHandler(syncId, inv, be);
    	});
		
		ScreenRegistry.<SoulSeparatorScreenHandler, SoulSeparatorScreen>register(soul_separator_handler,
				new ScreenRegistry.Factory<SoulSeparatorScreenHandler, SoulSeparatorScreen>() {

					@Override
					public SoulSeparatorScreen create(SoulSeparatorScreenHandler handler, PlayerInventory inventory, Text title) {
						return new SoulSeparatorScreen(handler, inventory, title);
					}
		});

		//Soul Cache Stuff
		ScreenHandlerType<SoulCacheScreenHandler> soul_cache_handler = ScreenHandlerRegistry.registerExtended(SOUL_CACHE, (syncId, inv, buf) -> {
    		SoulCacheBlockEntity be = (SoulCacheBlockEntity) PacketBufferUtils.getBlockEntity(buf, inv.player);
    		return new SoulCacheScreenHandler(syncId, inv, be);
    	});
		
		ScreenRegistry.<SoulCacheScreenHandler, SoulCacheScreen>register(soul_cache_handler,
				new ScreenRegistry.Factory<SoulCacheScreenHandler, SoulCacheScreen>() {

					@Override
					public SoulCacheScreen create(SoulCacheScreenHandler handler, PlayerInventory inventory, Text title) {
						return new SoulCacheScreen(handler, inventory, title);
					}
		});
		
		//Accessory Stuff
		ScreenHandlerType<AccessoryContainer> accessory_handler = ScreenHandlerRegistry.registerSimple(ACCESSORY, AccessoryContainer::new);
		
		ScreenRegistry.<AccessoryContainer, AccessoryScreen>register(accessory_handler,
				new ScreenRegistry.Factory<AccessoryContainer, AccessoryScreen>() {

					@Override
					public AccessoryScreen create(AccessoryContainer handler, PlayerInventory inventory, Text title) {
						return new AccessoryScreen(handler, inventory, title);
					}
		});

    	//ScreenProviderRegistry.INSTANCE.registerFactory(ACCESSORY, (AccessoryContainer c) -> {
    	//	return new AccessoryScreen(c, MinecraftClient.getInstance().player, new TranslatableText("container.soulmagic.accessories"));
    	//});
		
		//Flying Chest Stuff
		ScreenHandlerType<FlyingChestScreenHandler> flying_chest_handler = ScreenHandlerRegistry.registerExtended(FLYING_CHEST, (syncId, inv, buf) -> {
			ItemStack stack = ItemHelper.getAccessoryFromSlot(inv.player, buf.readInt());
			
			SimpleInventory chestInv = new SimpleInventory(27);
			chestInv.addListener(new FlyingChestInventory(stack));
    		
    		if(stack.hasTag()) {
    			CompoundTag tag = stack.getTag();
				ListTag invNBT = (ListTag) tag.get("Items");
				FlyingChestInventory.readTags(invNBT, chestInv);
    		}
    		
    		return new FlyingChestScreenHandler(syncId, inv, chestInv, stack.hasCustomName() ? stack.getName() : null);
		});

		ScreenRegistry.<FlyingChestScreenHandler, FlyingChestScreen>register(flying_chest_handler,
				new ScreenRegistry.Factory<FlyingChestScreenHandler, FlyingChestScreen>() {

					@Override
					public FlyingChestScreen create(FlyingChestScreenHandler handler, PlayerInventory inventory, Text title) {
						return new FlyingChestScreen(handler, inventory, title);
					}
		});
    }
}