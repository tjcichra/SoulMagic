package com.rainbowluigi.soulmagic.rei.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.rei.SoulMagicClientPlugin;
import me.shedaniel.clothconfig2.api.animator.NumberAnimator;
import me.shedaniel.clothconfig2.api.animator.ValueAnimator;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.widgets.WidgetWithBounds;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

//Widget for displaying the infusion circle
public class InfusionCircleWidget extends WidgetWithBounds {
    //Widget requires the bounds of the recipe display and the color of the recipe
    private Rectangle bounds;
    private int progressColor;
    private final NumberAnimator<Float> darkBackgroundAlpha = ValueAnimator.ofFloat()
            .withConvention(() -> REIRuntime.getInstance().isDarkThemeEnabled() ? 1.0F : 0.0F, ValueAnimator.typicalTransitionTime())
            .asFloat();

    public InfusionCircleWidget(Rectangle bounds, int progressColor) {
        this.bounds = new Rectangle(Objects.requireNonNull(bounds));
        this.progressColor = progressColor;
    }

    //Gets the bounds of the widget
    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }

    //Render ("display") for the widget
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.darkBackgroundAlpha.update(delta);
        renderBackground(matrices, false, 1.0F);
        if (darkBackgroundAlpha.value() > 0.0F) {
            renderBackground(matrices, true, this.darkBackgroundAlpha.value());
        }
    }

    public void renderBackground(MatrixStack matrices, boolean dark, float alpha) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.setShaderTexture(0, SoulMagicClientPlugin.getDisplayTexture(dark));
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        RenderSystem.blendFunc(770, 771);

        //Draw the "empty" gray circle
        this.drawTexture(matrices, bounds.x, bounds.y, 0, 0, 100, 100);

        //Get the colors of the progress color
        RenderSystem.setShaderColor((this.progressColor >>> 16) / 255f, (this.progressColor >>> 8 & 255) / 255f, (this.progressColor & 255) / 255f, alpha);
        int i = MathHelper.ceil(System.currentTimeMillis() / 25 % 101d);
        //Draw the "filled in" circle overtop of the gray circle
        this.drawTexture(matrices, bounds.x, bounds.y + 100 - i, 100, 100 - i, 100, i);
        //Reset the colors
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
    }

    //Returns the children of the widget (in this case none)
    @Override
    public List<? extends Element> children() {
        return Collections.emptyList();
    }
}