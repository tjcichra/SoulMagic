package com.rainbowluigi.soulmagic.client;

import com.rainbowluigi.soulmagic.entity.MagicFireballEntity;
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
public class MagicFireballRender extends EntityRenderer<MagicFireballEntity> {

	private static final Identifier TEXTURE = new Identifier(Reference.MOD_ID, "textures/entities/magic_fireball.png");
	
	protected MagicFireballRender(EntityRenderDispatcher entityRenderDispatcher_1) {
		super(entityRenderDispatcher_1);
	}
	
    @Override
    public void render(MagicFireballEntity entity, float f1, float f2, MatrixStack matrix, VertexConsumerProvider vertexCP, int i1) {
		matrix.push();
    	matrix.multiply(this.dispatcher.getRotation());
    	matrix.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
    	
    	float size = entity.ticks < 100 ? 0.75f : -0.0375f * entity.ticks + 4.5f;
    	matrix.scale(size, size, size);
        
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
        
    	/*
    	//if (!this.renderOutlines && MinecraftClient.getInstance().getEntityRenderManager().gameOptions != null) {
    		//System.out.println("hetero");
    		matrix.push();
            //this.bindEntityTexture(entity);
            
            //matrix.translate((float)x, (float)y + 0.25f, (float)z);
            //matrix.enableRescaleNormal();
           //matrix.enableBlend();
            
            RenderSystem.blendFunc(class_4493.class_4535.SRC_ALPHA, class_4493.class_4534.ONE_MINUS_SRC_ALPHA);
            float size = entity.ticks < 100 ? 0.75f : -0.0375f * entity.ticks + 4.5f;
            RenderSystem.scalef(size, size, size);
            
            RenderSystem.color4f(1F, 1F, 1F, 0.95f);
            
            Tessellator tessellator_1 = Tessellator.getInstance();
            BufferBuilder bufferBuilder_1 = tessellator_1.getBufferBuilder();
            
            RenderSystem.rotatef(180.0F - this.renderManager.cameraYaw, 0.0F, 1.0F, 0.0F);
            RenderSystem.rotatef((float)(this.renderManager.gameOptions.perspective == 2 ? -1 : 1) * -this.renderManager.cameraPitch, 1.0F, 0.0F, 0.0F);
            
            //if (this.renderOutlines) {
            //    RenderSystem.enableColorMaterial();
            //    RenderSystem.setupSolidRenderingTextureCombine(this.getOutlineColor(entity));
             //}
            
            bufferBuilder_1.begin(7, VertexFormats.POSITION_UV_NORMAL);
            vertexCP.vertex(-0.5D, -0.25D, 0.0D).texture(0, 1).normal(0.0F, 1.0F, 0.0F).next();
            vertexCP.vertex(0.5D, -0.25D, 0.0D).texture(1, 1).normal(0.0F, 1.0F, 0.0F).next();
            vertexCP.vertex(0.5D, 0.75D, 0.0D).texture(1, 0).normal(0.0F, 1.0F, 0.0F).next();
            vertexCP.vertex(-0.5D, 0.75D, 0.0D).texture(0, 0).normal(0.0F, 1.0F, 0.0F).next();
            
            tessellator_1.draw();
            
            //if (this.renderOutlines) {
           //     RenderSystem.tearDownSolidRenderingTextureCombine();
            //    RenderSystem.disableColorMaterial();
             //}
            
            //matrix.disableBlend();
            //matrix.disableRescaleNormal();
            matrix.pop();
            super.render(entity, f1, f2, matrix, vertexCP, i1);
    	//}*/
    }
    
    private static void renderHelp(VertexConsumer vertexConsumer_1, Matrix4f matrix4f_1, Matrix3f matrix3f_1, int int_1, float float_1, int int_2, int int_3, int int_4) {
        vertexConsumer_1.vertex(matrix4f_1, float_1 - 0.5F, (float)int_2 - 0.25F, 0.0F).color(255, 255, 255, 255).texture((float)int_3, (float)int_4).overlay(OverlayTexture.DEFAULT_UV).light(int_1).normal(matrix3f_1, 0.0F, 1.0F, 0.0F).next();
     }

	@Override
	public Identifier getTexture(MagicFireballEntity var1) {
		return TEXTURE;
	}
}
