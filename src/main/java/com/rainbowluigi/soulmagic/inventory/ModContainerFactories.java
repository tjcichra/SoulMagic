package com.rainbowluigi.soulmagic.inventory;

import com.rainbowluigi.soulmagic.block.entity.SoulCacheBlockEntity;
import com.rainbowluigi.soulmagic.block.entity.SoulEssenceInfuserBlockEntity;
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

	public static ScreenHandlerType<SoulInfuserScreenHandler> SOUL_ESSENCE_INFUSER;
	public static ScreenHandlerType<SoulSeparatorScreenHandler> SOUL_ESSENCE_SEPARATOR;
	public static ScreenHandlerType<SoulCacheScreenHandler> SOUL_STAFF_CACHE;
	public static ScreenHandlerType<AccessoryContainer> ACCESSORIES;
	public static ScreenHandlerType<FlyingChestScreenHandler> FLYING_CHEST;

	public static void registerContainerTypes() {

		//Soul Infuser Stuff
		SOUL_ESSENCE_INFUSER = ScreenHandlerRegistry.registerExtended(new Identifier(Reference.MOD_ID, "soul_essence_infuser"), (syncId, inventory, buf) -> {
			SoulEssenceInfuserBlockEntity be = (SoulEssenceInfuserBlockEntity) PacketBufferUtils.getBlockEntity(buf, inventory.player);
			return new SoulInfuserScreenHandler(syncId, inventory, be);
		});

		ScreenRegistry.<SoulInfuserScreenHandler, SoulInfuserScreen>register(SOUL_ESSENCE_INFUSER,
				new ScreenRegistry.Factory<SoulInfuserScreenHandler, SoulInfuserScreen>() {

					@Override
					public SoulInfuserScreen create(SoulInfuserScreenHandler handler, PlayerInventory inventory, Text title) {
						return new SoulInfuserScreen(handler, inventory, title);
					}
		});
		
		//Soul Separator Stuff
		SOUL_ESSENCE_SEPARATOR = ScreenHandlerRegistry.registerExtended(new Identifier(Reference.MOD_ID, "soul_essence_separator"), (syncId, inv, buf) -> {
			SoulSeparatorBlockEntity be = (SoulSeparatorBlockEntity) PacketBufferUtils.getBlockEntity(buf, inv.player);
			return new SoulSeparatorScreenHandler(syncId, inv, be);
		});
		
		ScreenRegistry.<SoulSeparatorScreenHandler, SoulSeparatorScreen>register(SOUL_ESSENCE_SEPARATOR,
				new ScreenRegistry.Factory<SoulSeparatorScreenHandler, SoulSeparatorScreen>() {

					@Override
					public SoulSeparatorScreen create(SoulSeparatorScreenHandler handler, PlayerInventory inventory, Text title) {
						return new SoulSeparatorScreen(handler, inventory, title);
					}
		});

		//Soul Cache Stuff
		SOUL_STAFF_CACHE = ScreenHandlerRegistry.registerExtended(new Identifier(Reference.MOD_ID, "soul_cache"), (syncId, inv, buf) -> {
			SoulCacheBlockEntity be = (SoulCacheBlockEntity) PacketBufferUtils.getBlockEntity(buf, inv.player);
			return new SoulCacheScreenHandler(syncId, inv, be);
		});
		
		ScreenRegistry.<SoulCacheScreenHandler, SoulCacheScreen>register(SOUL_STAFF_CACHE,
				new ScreenRegistry.Factory<SoulCacheScreenHandler, SoulCacheScreen>() {

					@Override
					public SoulCacheScreen create(SoulCacheScreenHandler handler, PlayerInventory inventory, Text title) {
						return new SoulCacheScreen(handler, inventory, title);
					}
		});
		
		//Accessory Stuff
		ACCESSORIES = ScreenHandlerRegistry.registerSimple(new Identifier(Reference.MOD_ID, "accessories"), AccessoryContainer::new);
		
		ScreenRegistry.<AccessoryContainer, AccessoryScreen>register(ACCESSORIES,
				new ScreenRegistry.Factory<AccessoryContainer, AccessoryScreen>() {

					@Override
					public AccessoryScreen create(AccessoryContainer handler, PlayerInventory inventory, Text title) {
						return new AccessoryScreen(handler, inventory, title);
					}
		});
		
		//Flying Chest Stuff
		FLYING_CHEST = ScreenHandlerRegistry.registerExtended(new Identifier(Reference.MOD_ID, "flying_chest"), (syncId, inv, buf) -> {
			ItemStack stack = ItemHelper.getAccessoryFromSlot(inv.player, buf.readInt());
			
			SimpleInventory chestInv = new SimpleInventory(27);
			chestInv.addListener(new FlyingChestInventory(stack));
			
			if(stack.hasTag()) {
				CompoundTag tag = stack.getTag();
				ListTag invNBT = (ListTag) tag.get("Items");
				FlyingChestInventory.readTags(invNBT, chestInv);
			}
			
			return new FlyingChestScreenHandler(syncId, inv, chestInv);
		});

		ScreenRegistry.<FlyingChestScreenHandler, FlyingChestScreen>register(FLYING_CHEST,
				new ScreenRegistry.Factory<FlyingChestScreenHandler, FlyingChestScreen>() {

					@Override
					public FlyingChestScreen create(FlyingChestScreenHandler handler, PlayerInventory inventory, Text title) {
						return new FlyingChestScreen(handler, inventory, title);
					}
		});
	}
}