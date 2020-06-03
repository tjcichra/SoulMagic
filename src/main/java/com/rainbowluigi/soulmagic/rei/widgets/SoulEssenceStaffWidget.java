package com.rainbowluigi.soulmagic.rei.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.rainbowluigi.soulmagic.rei.SoulMagicPlugin;
import com.rainbowluigi.soulmagic.soultype.SoulType;

import org.jetbrains.annotations.NotNull;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.widgets.Tooltip;
import me.shedaniel.rei.gui.widget.WidgetWithBounds;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class SoulEssenceStaffWidget extends WidgetWithBounds {

    //Widget requires the bounds of the recipe display and soul essence cost
    private Rectangle bounds;
    private Map<SoulType, Integer> soulCost;

    public SoulEssenceStaffWidget(Rectangle bounds, Map<SoulType, Integer> soulCost) {
        this.bounds = bounds;
        this.soulCost = soulCost;
    }

    //Render ("display") for the widget
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        //Draw the soul essence staff icon
        minecraft.getTextureManager().bindTexture(SoulMagicPlugin.getDisplayTexture());
        drawTexture(matrices, bounds.x, bounds.y, 200, 0, bounds.width, bounds.height);
        //If the widget has the mouse hovered over it
        if(this.containsMouse(mouseX, mouseY)) {
            //Create a list of text to represent the required soul essence cost
            List<Text> tooltips = new ArrayList<>();
            tooltips.add(new TranslatableText("soulmagic.rei.required"));
            //Loop through the soul types and their amount required
            for(Entry<SoulType, Integer> e : this.soulCost.entrySet()) {
                tooltips.add(new TranslatableText("soulmagic.rei.soul_amount", e.getKey().getName(), e.getValue()).formatted(e.getKey().getTextColor()));
            }
            //Create a tooltip object at the mouse position and display it
            Tooltip tooltip = Tooltip.create(new Point(mouseX, mouseY), tooltips);
            tooltip.queue();
        }
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