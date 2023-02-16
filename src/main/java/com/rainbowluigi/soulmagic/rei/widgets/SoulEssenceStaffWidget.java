package com.rainbowluigi.soulmagic.rei.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.rei.SoulMagicClientPlugin;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import me.shedaniel.clothconfig2.api.animator.NumberAnimator;
import me.shedaniel.clothconfig2.api.animator.ValueAnimator;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.WidgetWithBounds;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class SoulEssenceStaffWidget extends WidgetWithBounds {
    //Widget requires the bounds of the recipe display and soul essence cost
    private Rectangle bounds;
    private Map<SoulType, Integer> soulCost;
    private final NumberAnimator<Float> darkBackgroundAlpha = ValueAnimator.ofFloat()
            .withConvention(() -> REIRuntime.getInstance().isDarkThemeEnabled() ? 1.0F : 0.0F, ValueAnimator.typicalTransitionTime())
            .asFloat();

    public SoulEssenceStaffWidget(Rectangle bounds, Map<SoulType, Integer> soulCost) {
        this.bounds = new Rectangle(Objects.requireNonNull(bounds));
        this.soulCost = soulCost;
    }

    //Gets the bounds of the widget
    @Override
    public @NotNull Rectangle getBounds() {
        return this.bounds;
    }

    //Render ("display") for the widget
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.darkBackgroundAlpha.update(delta);
        renderBackground(matrices, mouseX, mouseY, false, 1.0F);
        if (darkBackgroundAlpha.value() > 0.0F) {
            renderBackground(matrices, mouseX, mouseY, true, this.darkBackgroundAlpha.value());
        }
    }

    public void renderBackground(MatrixStack matrices, int mouseX, int mouseY, boolean dark, float alpha) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.setShaderTexture(0, SoulMagicClientPlugin.getDisplayTexture(dark));
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        RenderSystem.blendFunc(770, 771);

        //Draw the soul essence staff icon
        this.drawTexture(matrices, bounds.x, bounds.y, 200, 0, bounds.width, bounds.height);

        //If the widget has the mouse hovered over it
        if (this.containsMouse(mouseX, mouseY)) {
            //Create a list of text to represent the required soul essence cost
            List<Text> tooltips = new ArrayList<>();
            tooltips.add(Text.translatable("soulmagic.rei.required"));
            //Loop through the soul types and their amount required
            for (Entry<SoulType, Integer> e : this.soulCost.entrySet()) {
                tooltips.add(Text.translatable("soulmagic.rei.soul_amount", e.getKey().getName(), e.getValue()).formatted(e.getKey().getTextColor()));
            }

            //Create a tooltip object at the mouse position and display it
            Tooltip.create(new Point(mouseX, mouseY), tooltips).queue();
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
    }

    //Returns the children of the widget (in this case none)
    @Override
    public List<? extends Element> children() {
        return Collections.emptyList();
    }
}