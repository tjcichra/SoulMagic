package com.rainbowluigi.soulmagic.rei.widgets;

import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.rei.SoulMagicPlugin;

import org.jetbrains.annotations.NotNull;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.gui.widget.WidgetWithBounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

//Widget for displaying the infusion circle
public class InfusionCircleWidget extends WidgetWithBounds {

    //Widget requires the bounds of the recipe display and the color of the recipe
    private Rectangle bounds;
    private int progressColor;

    public InfusionCircleWidget(Rectangle bounds, int progressColor) {
        this.bounds = bounds;
        this.progressColor = progressColor;
    }

    //Render ("display") for the widget
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        //Draw the "empty" gray circle
        MinecraftClient.getInstance().getTextureManager().bindTexture(SoulMagicPlugin.getDisplayTexture());
        this.drawTexture(matrices, bounds.x, bounds.y, 0, 0, 100, 100);

        //Get the colors of the progress color
        RenderSystem.color4f((this.progressColor >>> 16) / 255f, (this.progressColor >>> 8 & 255) / 255f, (this.progressColor & 255) / 255f, 1);
        int i = MathHelper.ceil(System.currentTimeMillis() / 25 % 101d);
        //Draw the "filled in" circle overtop of the gray circle
        this.drawTexture(matrices, bounds.x, bounds.y + 100 - i, 100, 100 - i, 100, i);
        //Reset the colors
        RenderSystem.color4f(1, 1, 1, 1);
    }

    //Returns the children of the widget (in this case none)
    @Override
    public List<? extends Element> children() {
        return Collections.emptyList();
    }

    //Gets the bounds of the widget
    @Override
    public @NotNull Rectangle getBounds() {
        return this.bounds;
    }
}