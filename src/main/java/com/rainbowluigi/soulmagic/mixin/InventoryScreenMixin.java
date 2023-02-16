package com.rainbowluigi.soulmagic.mixin;

import com.rainbowluigi.soulmagic.tabs.ModTabs;
import com.rainbowluigi.soulmagic.tabs.TabHelper;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {

    public InventoryScreenMixin(PlayerScreenHandler container_1, PlayerInventory playerInventory_1, Text text_1) {
        super(container_1, playerInventory_1, text_1);
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    protected void drawBackground(MatrixStack matrix, float float_1, int int_1, int int_2, CallbackInfo info) {
        TabHelper.drawTabsBackground(ModTabs.INVENTORY, matrix, this, this.x, this.y);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(MatrixStack matrix, int mouseX, int mouseY, float float_1, CallbackInfo info) {
        TabHelper.tabsRender(ModTabs.INVENTORY, matrix, this, this.x, this.y, mouseX, mouseY);
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"))
    protected void mouseReleased(double mouseX, double mouseY, int int_1, CallbackInfoReturnable<Boolean> info) {
        TabHelper.tabsMouseReleased(ModTabs.INVENTORY, this, this.x, this.y, mouseX, mouseY);
    }
}
