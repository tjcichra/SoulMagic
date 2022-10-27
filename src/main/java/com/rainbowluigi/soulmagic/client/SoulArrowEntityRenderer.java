package com.rainbowluigi.soulmagic.client;

import com.rainbowluigi.soulmagic.entity.SoulArrowEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class SoulArrowEntityRenderer extends EntityRenderer<SoulArrowEntity> {
	public static final Identifier SKIN = new Identifier("soulmagic:textures/entities/soul_arrow.png");
	public static final Identifier TIPPED_SKIN = new Identifier("textures/entity/projectiles/tipped_arrow.png");

	public SoulArrowEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}
	
	public void render(SoulArrowEntity arrow, float float_1, float float_2, MatrixStack matrixStack_1, VertexConsumerProvider vertexConsumerProvider_1, int int_1) {
		matrixStack_1.push();
		matrixStack_1.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(float_2, arrow.prevYaw, arrow.getYaw()) - 90.0F));
		matrixStack_1.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(float_2, arrow.prevPitch, arrow.getPitch())));
		
		float float_12 = (float) arrow.shake - float_2;
		if (float_12 > 0.0F) {
			float float_13 = -MathHelper.sin(float_12 * 3.0F) * float_12;
			matrixStack_1.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(float_13));
		}
		
		int red;
		int green;
		int blue;
		
		if(arrow.getSoulType() != null) {
			int color = arrow.getSoulType().getColor();
			red = color >>> 16;
			green = color >>> 8 & 255;
			blue = color & 255;
		} else {
			red = 255;
			green = 255;
			blue = 255;
		}
		
		
		matrixStack_1.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45.0F));
		matrixStack_1.scale(0.05625F, 0.05625F, 0.05625F);
		matrixStack_1.translate(-4.0D, 0.0D, 0.0D);
		VertexConsumer vertexConsumer_1 = vertexConsumerProvider_1.getBuffer(RenderLayer.getEntityCutout(this.getTexture(arrow)));
		MatrixStack.Entry matrixStack$Entry_1 = matrixStack_1.peek();
		Matrix4f matrix4f_1 = matrixStack$Entry_1.getPositionMatrix();
		Matrix3f matrix3f_1 = matrixStack$Entry_1.getNormalMatrix();
		this.method_23153(matrix4f_1, matrix3f_1, vertexConsumer_1, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, int_1, red, green, blue);
		this.method_23153(matrix4f_1, matrix3f_1, vertexConsumer_1, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, int_1, red, green, blue);
		this.method_23153(matrix4f_1, matrix3f_1, vertexConsumer_1, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, int_1, red, green, blue);
		this.method_23153(matrix4f_1, matrix3f_1, vertexConsumer_1, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, int_1, red, green, blue);
		this.method_23153(matrix4f_1, matrix3f_1, vertexConsumer_1, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, int_1, red, green, blue);
		this.method_23153(matrix4f_1, matrix3f_1, vertexConsumer_1, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, int_1, red, green, blue);
		this.method_23153(matrix4f_1, matrix3f_1, vertexConsumer_1, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, int_1, red, green, blue);
		this.method_23153(matrix4f_1, matrix3f_1, vertexConsumer_1, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, int_1, red, green, blue);

		for (int int_3 = 0; int_3 < 4; ++int_3) {
			matrixStack_1.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
			this.method_23153(matrix4f_1, matrix3f_1, vertexConsumer_1, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, int_1, red, green, blue);
			this.method_23153(matrix4f_1, matrix3f_1, vertexConsumer_1, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, int_1, red, green, blue);
			this.method_23153(matrix4f_1, matrix3f_1, vertexConsumer_1, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, int_1, red, green, blue);
			this.method_23153(matrix4f_1, matrix3f_1, vertexConsumer_1, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, int_1, red, green, blue);
		}

		matrixStack_1.pop();
		super.render(arrow, float_1, float_2, matrixStack_1, vertexConsumerProvider_1, int_1);
	}
	
	public void method_23153(Matrix4f matrix4f_1, Matrix3f matrix3f_1, VertexConsumer vertexConsumer_1, int int_1, int int_2, int int_3, float float_1, float float_2, int int_4, int int_5, int int_6, int int_7, int r, int g, int b) {
		vertexConsumer_1.vertex(matrix4f_1, (float) int_1, (float) int_2, (float) int_3).color(r, g, b, 230).texture(float_1, float_2).overlay(OverlayTexture.DEFAULT_UV).light(int_7).normal(matrix3f_1, (float) int_4, (float) int_6, (float) int_5).next();
	}

	public Identifier getTexture(SoulArrowEntity arrowEntity_1) {
		return SKIN;
	}
}
