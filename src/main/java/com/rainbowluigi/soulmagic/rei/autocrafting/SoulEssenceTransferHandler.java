package com.rainbowluigi.soulmagic.rei.autocrafting;

import com.rainbowluigi.soulmagic.rei.SoulInfusionDisplay;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandler;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.transfer.info.MenuInfo;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoContext;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.MenuTransferException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;

@Environment(EnvType.CLIENT)
public class SoulEssenceTransferHandler implements TransferHandler {
    @Override
    public Result handle(Context context) {
        if (context.getDisplay() instanceof SoulInfusionDisplay) {
            Display display = context.getDisplay();
            HandledScreen<?> containerScreen = context.getContainerScreen();
            if (containerScreen == null) {
                return Result.createNotApplicable();
            }
            ScreenHandler menu = context.getMenu();
            MenuInfoContext<ScreenHandler, PlayerEntity, Display> menuInfoContext = ofContext(menu, display);
            MenuInfo<ScreenHandler, Display> menuInfo = MenuInfoRegistry.getInstance().getClient(display, menuInfoContext, menu);
            if (menuInfo == null) {
                return Result.createNotApplicable();
            }
            try {
                menuInfo.validate(menuInfoContext);
            } catch (MenuTransferException e) {
                if (e.isApplicable()) {
                    return Result.createFailed(e.getError());
                } else {
                    return Result.createNotApplicable();
                }
            }

            if (!context.isActuallyCrafting()) {
                return Result.createSuccessful();
            }

            context.getMinecraft().setScreen(containerScreen);
//            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
//            buf.writeIdentifier(display.getCategoryIdentifier().getIdentifier());
//            buf.writeNbt(menuInfo.save(menuInfoContext, display));
//            NetworkManager.sendToServer(RoughlyEnoughItemsNetwork.MOVE_ITEMS_PACKET, buf);

            return Result.createSuccessful();
        }
        return Result.createNotApplicable();
    }

    private static MenuInfoContext<ScreenHandler, PlayerEntity, Display> ofContext(ScreenHandler menu, Display display) {
        return new MenuInfoContext<>() {
            @Override
            public ScreenHandler getMenu() {
                return menu;
            }

            @Override
            public PlayerEntity getPlayerEntity() {
                return MinecraftClient.getInstance().player;
            }

            @Override
            public CategoryIdentifier<Display> getCategoryIdentifier() {
                return (CategoryIdentifier<Display>) display.getCategoryIdentifier();
            }

            @Override
            public Display getDisplay() {
                return display;
            }
        };
    }
}
