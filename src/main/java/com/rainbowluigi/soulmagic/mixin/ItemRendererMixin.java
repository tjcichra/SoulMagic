package com.rainbowluigi.soulmagic.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Invoker("renderGuiQuad")
    public abstract void invokeRenderGuiQuad(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha);

    @Inject(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    public void addSoulEssenceBar(TextRenderer textRenderer, ItemStack stack, int x, int y, String s, CallbackInfo cbi) {
        if (stack.getItem() instanceof SoulEssenceStaff staff) {
            MinecraftClient client = MinecraftClient.getInstance();

            int total = 0;
            for (SoulType type : ModSoulTypes.SOUL_TYPE) {
                if (staff.getSoul(stack, client.world, type) > 0) {
                    total += staff.getMaxSoul(stack, client.world, type);
                }
            }

            if (total > 0) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tessellator.getBuffer();

                this.invokeRenderGuiQuad(bufferBuilder, x + 2, y + 13, 13, 2, 0, 0, 0, 255);

                double start = 0;
                for (SoulType type : ModSoulTypes.SOUL_TYPE) {
                    if (staff.getSoul(stack, client.world, type) > 0) {
                        int color = type.getColor();

                        double j = (double) staff.getSoul(stack, client.world, type) / total * 13;
                        this.renderGuiQuadWithDoubles(bufferBuilder, x + 2 + start, y + 13, j, 1, color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, 255);

                        start += j;
                    }
                }

                RenderSystem.enableBlend();
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }
        }
    }

    private void renderGuiQuadWithDoubles(BufferBuilder buffer, double x, double y, double width, double height, int red, int green, int blue, int alpha) {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(x, y, 0.0).color(red, green, blue, alpha).next();
        buffer.vertex(x, y + height, 0.0).color(red, green, blue, alpha).next();
        buffer.vertex(x + width, y + height, 0.0).color(red, green, blue, alpha).next();
        buffer.vertex(x + width, y, 0.0).color(red, green, blue, alpha).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }
}
