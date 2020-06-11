package com.rainbowluigi.soulmagic.client;

import com.rainbowluigi.soulmagic.entity.MagicFireballEntity;
import com.rainbowluigi.soulmagic.entity.SpiritFlameEntity;
import com.rainbowluigi.soulmagic.util.Reference;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class SpiritFlameRender extends EntityRenderer<SpiritFlameEntity> {

	private static final Identifier TEXTURE = new Identifier(Reference.MOD_ID, "textures/entities/spirit_flame.png");
	
	protected SpiritFlameRender(EntityRenderDispatcher entityRenderDispatcher_1) {
		super(entityRenderDispatcher_1);
	}
	
    @Override
    public void render(SpiritFlameEntity entity, float f1, float f2, MatrixStack matrix, VertexConsumerProvider vertexCP, int i1) {
		matrix.push();
    	matrix.multiply(this.dispatcher.getRotation());
    	matrix.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
        
    	MatrixStack.Entry matrixStack$Entry_1 = matrix.peek();
        Matrix4f matrix4f_1 = matrixStack$Entry_1.getModel();
        Matrix3f matrix3f_1 = matrixStack$Entry_1.getNormal();
        VertexConsumer vertexConsumer_1 = vertexCP.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
        renderHelp(vertexConsumer_1, matrix4f_1, matrix3f_1, i1, 0.0F, 0, 0, 1);
        renderHelp(vertexConsumer_1, matrix4f_1, matrix3f_1, i1, 1.0F, 0, 1, 1);
        renderHelp(vertexConsumer_1, matrix4f_1, matrix3f_1, i1, 1.0F, 1, 1, 0);
        renderHelp(vertexConsumer_1, matrix4f_1, matrix3f_1, i1, 0.0F, 1, 0, 0);
        
        matrix.pop();
        super.render(entity, f1, f2, matrix, vertexCP, i1);
    }
    
    private static void renderHelp(VertexConsumer vertexConsumer_1, Matrix4f matrix4f_1, Matrix3f matrix3f_1, int int_1, float float_1, int int_2, int int_3, int int_4) {
        vertexConsumer_1.vertex(matrix4f_1, float_1 - 0.5F, (float)int_2 - 0.25F, 0.0F).color(255, 255, 255, 255).texture((float)int_3, (float)int_4).overlay(OverlayTexture.DEFAULT_UV).light(int_1).normal(matrix3f_1, 0.0F, 1.0F, 0.0F).next();
     }

	@Override
	public Identifier getTexture(SpiritFlameEntity var1) {
		return TEXTURE;
	}
}
