package com.rainbowluigi.soulmagic.client;

import com.rainbowluigi.soulmagic.util.Reference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BlankRender extends EntityRenderer<Entity> {

    private static final Identifier TEXTURE = new Identifier(Reference.MOD_ID, "textures/entities/magic_fireball.png");

    protected BlankRender(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(Entity entity, float f1, float f2, MatrixStack matrix, VertexConsumerProvider vertexCP, int i1) {
        super.render(entity, f1, f2, matrix, vertexCP, i1);
    }

    @Override
    public Identifier getTexture(Entity var1) {
        return TEXTURE;
    }
}
