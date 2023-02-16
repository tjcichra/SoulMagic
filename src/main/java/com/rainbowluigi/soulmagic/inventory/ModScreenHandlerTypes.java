package com.rainbowluigi.soulmagic.inventory;

import com.rainbowluigi.soulmagic.util.ItemHelper;
import com.rainbowluigi.soulmagic.util.PacketBufferUtils;
import com.rainbowluigi.soulmagic.util.Reference;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModScreenHandlerTypes {

	public static ScreenHandlerType<SoulInfuserScreenHandler> SOUL_ESSENCE_INFUSER;
	public static ScreenHandlerType<SoulSeparatorScreenHandler> SOUL_ESSENCE_SEPARATOR;
	public static ScreenHandlerType<SoulCacheScreenHandler> SOUL_STAFF_CACHE;
	public static ScreenHandlerType<UpgradeStationScreenHandler> UPGRADE_STATION = new ScreenHandlerType<>(UpgradeStationScreenHandler::new);
	public static ScreenHandlerType<AccessoriesScreenHandler> ACCESSORIES = new ScreenHandlerType<>(AccessoriesScreenHandler::new);
	public static ScreenHandlerType<FlyingChestScreenHandler> FLYING_CHEST;

	public static void registerScreenHandlerTypes() {

		//Soul Infuser Stuff
		SOUL_ESSENCE_INFUSER = registerScreenHandler("soul_essence_infuser", SoulInfuserScreenHandler::new);

		// Soul Separator Stuff
		SOUL_ESSENCE_SEPARATOR = registerScreenHandler("soul_essence_separator", SoulSeparatorScreenHandler::new);

		// Soul Cache Stuff
		SOUL_STAFF_CACHE = registerScreenHandler("soul_cache", SoulCacheScreenHandler::new);

		registerScreenHandlerType(UPGRADE_STATION, "upgrade_station");
		registerScreenHandlerType(ACCESSORIES, "accessories");

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

	}

	private static void registerScreenHandlerType(ScreenHandlerType<?> screenHandlerType, String name) {
		Registry.register(Registries.SCREEN_HANDLER, new Identifier(Reference.MOD_ID, name), screenHandlerType);
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
	interface BlockEntityScreenHandlerFactory<T extends ScreenHandler, S extends BlockEntity> {
		T create(int syncId, PlayerInventory playerInv, S inv);
	}

	@FunctionalInterface
	interface ScreenFactory<T extends HandledScreen<S>, S extends ScreenHandler> {
		T create(S screenHandler, PlayerInventory playerInv, Text text);
	}
}