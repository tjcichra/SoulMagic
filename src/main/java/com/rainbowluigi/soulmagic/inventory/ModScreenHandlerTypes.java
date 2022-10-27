package com.rainbowluigi.soulmagic.inventory;

import com.rainbowluigi.soulmagic.client.screen.AccessoryScreen;
import com.rainbowluigi.soulmagic.client.screen.FlyingChestScreen;
import com.rainbowluigi.soulmagic.client.screen.SoulCacheScreen;
import com.rainbowluigi.soulmagic.client.screen.SoulEssenceInfuserScreen;
import com.rainbowluigi.soulmagic.client.screen.SoulSeparatorScreen;
import com.rainbowluigi.soulmagic.client.screen.UpgradeStationScreen;
import com.rainbowluigi.soulmagic.util.ItemHelper;
import com.rainbowluigi.soulmagic.util.PacketBufferUtils;
import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModScreenHandlerTypes {

	public static ScreenHandlerType<SoulInfuserScreenHandler> SOUL_ESSENCE_INFUSER;
	public static ScreenHandlerType<SoulSeparatorScreenHandler> SOUL_ESSENCE_SEPARATOR;
	public static ScreenHandlerType<SoulCacheScreenHandler> SOUL_STAFF_CACHE;
	public static ScreenHandlerType<UpgradeStationScreenHandler> UPGRADE_STATION;
	public static ScreenHandlerType<AccessoriesScreenHandler> ACCESSORIES;
	public static ScreenHandlerType<FlyingChestScreenHandler> FLYING_CHEST;

	public static void registerScreenHandlerTypes() {

		//Soul Infuser Stuff
		SOUL_ESSENCE_INFUSER = registerScreenHandler("soul_essence_infuser", SoulInfuserScreenHandler::new);
		registerScreen(SOUL_ESSENCE_INFUSER, SoulEssenceInfuserScreen::new);

		// Soul Separator Stuff
		SOUL_ESSENCE_SEPARATOR = registerScreenHandler("soul_essence_separator", SoulSeparatorScreenHandler::new);
		registerScreen(SOUL_ESSENCE_SEPARATOR, SoulSeparatorScreen::new);

		// Soul Cache Stuff
		SOUL_STAFF_CACHE = registerScreenHandler("soul_cache", SoulCacheScreenHandler::new);
		registerScreen(SOUL_STAFF_CACHE, SoulCacheScreen::new);

		//Upgrade Station
		UPGRADE_STATION = ScreenHandlerRegistry.registerSimple(new Identifier(Reference.MOD_ID, "upgrade_station"), UpgradeStationScreenHandler::new);
		registerScreen(UPGRADE_STATION, UpgradeStationScreen::new);

		// Accessory Stuff
		ACCESSORIES = ScreenHandlerRegistry.registerSimple(new Identifier(Reference.MOD_ID, "accessories"), AccessoriesScreenHandler::new);
		registerScreen(ACCESSORIES, AccessoryScreen::new);

		// Flying Chest Stuff
		FLYING_CHEST = ScreenHandlerRegistry.registerExtended(new Identifier(Reference.MOD_ID, "flying_chest"),
				(syncId, inv, buf) -> {
					ItemStack stack = ItemHelper.getAccessoryFromSlot(inv.player, buf.readInt());

					SimpleInventory chestInv = new SimpleInventory(27);
					chestInv.addListener(new FlyingChestInventory(stack));

					if (stack.hasNbt()) {
						NbtCompound tag = stack.getNbt();
						NbtList invNBT = (NbtList) tag.get("Items");
						FlyingChestInventory.readTags(invNBT, chestInv);
					}

					return new FlyingChestScreenHandler(syncId, inv, chestInv);
				});

		registerScreen(FLYING_CHEST, FlyingChestScreen::new);
	}

	public static <T extends ScreenHandler, S extends BlockEntity> ScreenHandlerType<T> registerScreenHandler(String name, BlockEntityScreenHandlerFactory<T, S> f) {
		return ScreenHandlerRegistry.registerExtended(new Identifier(Reference.MOD_ID, name), (syncId, inventory, buf) -> {
			S be = PacketBufferUtils.getBlockEntity(buf, inventory.player);
			return f.create(syncId, inventory, be);
		});
	}

	public static <T extends ScreenHandler, S extends HandledScreen<T>> void registerScreen(ScreenHandlerType<T> type, ScreenFactory<S, T> f) {
		ScreenRegistry.register(type, new ScreenRegistry.Factory<T, S>() {

			@Override
			public S create(T handler, PlayerInventory inventory, Text title) {
				return f.create(handler, inventory, title);
			}
		});
	}

	@FunctionalInterface
	static interface BlockEntityScreenHandlerFactory<T extends ScreenHandler, S extends BlockEntity> {
		T create(int syncId, PlayerInventory playerInv, S inv);
	}

	@FunctionalInterface
	static interface ScreenFactory<T extends HandledScreen<S>, S extends ScreenHandler> {
		T create(S screenHandler, PlayerInventory playerInv, Text text);
	}
}