	package com.rainbowluigi.soulmagic.client;

import com.rainbowluigi.soulmagic.block.entity.SoulInfuserBlockEntity;
import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;

public class BlockEntitySpecialRendererSoulInfuser extends BlockEntityRenderer<SoulInfuserBlockEntity> {

	public ModelPart topStone;
    public ModelPart leftStone;
    public ModelPart bottomRightStone;
    public ModelPart rightStone;
    public ModelPart centerStone;
    public ModelPart bottomStone;
    public ModelPart topRightStone;
    public ModelPart bottomLeftStone;
    public ModelPart topLeftStone;
    public ModelPart top1;
    public ModelPart top2;
    public ModelPart top3;
    public ModelPart top4;
    public ModelPart soulInfuserMiddleBase;
    public ModelPart soulInfuserBase;
    public ModelPart soulInfuserBottomBase;
    
    private int textureWidth = 64;
    private int textureHeight = 64;
    
    public static final SpriteIdentifier TEXTURE;
    
	public BlockEntitySpecialRendererSoulInfuser(BlockEntityRenderDispatcher blockEntityRenderDispatcher_1) {
		super(blockEntityRenderDispatcher_1);
		
		this.centerStone = new ModelPart(textureWidth, textureHeight, 0, 0);
        this.centerStone.mirror = true;
        this.centerStone.setPivot(0.0F, 0.0F, 0.0F);
        this.centerStone.addCuboid(-1.0F, 11.1F, -1.0F, 2, 1, 2, 0.0F);
        this.top3 = new ModelPart(textureWidth, textureHeight, 0, 3);
        this.centerStone.setPivot(0.0F, 0.0F, 0.0F);
        this.top3.addCuboid(4.0F, 8.0F, 4.0F, 4, 10, 4, 0.0F);
        this.topRightStone = new ModelPart(textureWidth, textureHeight, 0, 0);
        this.topRightStone.setPivot(0.0F, 0.0F, 0.0F);
        this.topRightStone.addCuboid(2.0F, 11.5F, 2.0F, 2, 1, 2, 0.0F);
        this.rightStone = new ModelPart(textureWidth, textureHeight, 0, 0);
        this.rightStone.mirror = true;
        this.rightStone.setPivot(0.0F, 0.0F, 0.0F);
        this.rightStone.addCuboid(3.5F, 11.3F, -1.0F, 2, 1, 2, 0.0F);
        this.leftStone = new ModelPart(textureWidth, textureHeight, 0, 0);
        this.leftStone.setPivot(0.0F, 0.0F, 0.0F);
        this.leftStone.addCuboid(-5.5F, 11.3F, -1.0F, 2, 1, 2, 0.0F);
        this.soulInfuserBottomBase = new ModelPart(textureWidth, textureHeight, 0, 27);
        this.soulInfuserBottomBase.setPivot(0.0F, 0.0F, 0.0F);
        this.soulInfuserBottomBase.addCuboid(-8.0F, 21.0F, -8.0F, 16, 3, 16, 0.0F);
        this.top4 = new ModelPart(textureWidth, textureHeight, 0, 3);
        this.top4.setPivot(0.0F, 0.0F, 0.0F);
        this.top4.addCuboid(4.0F, 8.0F, -8.0F, 4, 10, 4, 0.0F);
        this.soulInfuserMiddleBase = new ModelPart(textureWidth, textureHeight, 12, -8);
        this.soulInfuserMiddleBase.setPivot(0.0F, 0.0F, 0.0F);
        this.soulInfuserMiddleBase.addCuboid(-6.0F, 18.0F, -6.0F, 12, 3, 12, 0.0F);
        this.bottomLeftStone = new ModelPart(textureWidth, textureHeight, 0, 0);
        this.bottomLeftStone.mirror = true;
        this.bottomLeftStone.setPivot(0.0F, 0.0F, 0.0F);
        this.bottomLeftStone.addCuboid(-4.0F, 11.5F, -4.0F, 2, 1, 2, 0.0F);
        this.bottomStone = new ModelPart(textureWidth, textureHeight, 0, 0);
        this.bottomStone.setPivot(0.0F, 0.0F, 0.0F);
        this.bottomStone.addCuboid(-1.0F, 11.3F, -5.5F, 2, 1, 2, 0.0F);
        this.topLeftStone = new ModelPart(textureWidth, textureHeight, 0, 0);
        this.topLeftStone.setPivot(0.0F, 0.0F, 0.0F);
        this.topLeftStone.addCuboid(-4.0F, 11.5F, 2.0F, 2, 1, 2, 0.0F);
        this.top2 = new ModelPart(textureWidth, textureHeight, 0, 3);
        this.top2.setPivot(0.0F, 0.0F, 0.0F);
        this.top2.addCuboid(-8.0F, 8.0F, -8.0F, 4, 10, 4, 0.0F);
        this.bottomRightStone = new ModelPart(textureWidth, textureHeight, 0, 0);
        this.bottomRightStone.setPivot(0.0F, 0.0F, 0.0F);
        this.bottomRightStone.addCuboid(2.0F, 11.5F, -4.0F, 2, 1, 2, 0.0F);
        this.top1 = new ModelPart(textureWidth, textureHeight, 0, 3);
        this.top1.setPivot(0.0F, 0.0F, 0.0F);
        this.top1.addCuboid(-8.0F, 8.0F, 4.0F, 4, 10, 4, 0.0F);
        this.soulInfuserBase = new ModelPart(textureWidth, textureHeight, 2, 7);
        this.soulInfuserBase.setPivot(0.0F, 0.0F, 0.0F);
        this.soulInfuserBase.addCuboid(-7.0F, 12.0F, -7.0F, 14, 6, 14, 0.0F);
        this.topStone = new ModelPart(textureWidth, textureHeight, 0, 0);
        this.topStone.mirror = true;
        this.topStone.setPivot(0.0F, 0.0F, 0.0F);
        this.topStone.addCuboid(-1.0F, 11.3F, 3.5F, 2, 1, 2, 0.0F);
	}
	
	//EntityItem entityItem = new EntityItem(Minecraft.getMinecraft().theWorld, 0, 0, 0, new ItemStack(SoulMagicItems.soul_gem));

	@Override
	public void render(SoulInfuserBlockEntity tile, float x, MatrixStack matrix, VertexConsumerProvider vertexCP, int i1, int i2) {
		matrix.push();
		matrix.translate(0.5D, 1.5D, 0.5D);
		
		//GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		//System.out.println("taner");
		matrix.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));

		
		VertexConsumer vertex = TEXTURE.getVertexConsumer(vertexCP, RenderLayer::getEntitySolid);
		int i3 = i1;
		this.centerStone.render(matrix, vertex, i3, i2);
        this.top3.render(matrix, vertex, i3, i2);
        this.topRightStone.render(matrix, vertex, i3, i2);
        this.rightStone.render(matrix, vertex, i3, i2);
        this.leftStone.render(matrix, vertex, i3, i2);
        this.soulInfuserBottomBase.render(matrix, vertex, i3, i2);
        this.top4.render(matrix, vertex, i3, i2);
        this.soulInfuserMiddleBase.render(matrix, vertex, i3, i2);
        this.bottomLeftStone.render(matrix, vertex, i3, i2);
        this.bottomStone.render(matrix, vertex, i3, i2);
        this.topLeftStone.render(matrix, vertex, i3, i2);
        this.top2.render(matrix, vertex, i3, i2);
        this.bottomRightStone.render(matrix, vertex, i3, i2);
        this.top1.render(matrix, vertex, i3, i2);
        this.soulInfuserBase.render(matrix, vertex, i3, i2);
        this.topStone.render(matrix, vertex, i3, i2);

        
		
		/*if(tile != null) {
			matrix.push();
			if (!tile.getInvStack(0).isEmpty()) {
				//GL11.glTranslatef((float) x + 0.5F, (float) y + 0.775F, (float) z + 0.220F);
				GL11.glScalef(0.3F, 0.3F, 0.3F);
				
				//MinecraftClient.getInstance().getItemRenderer().renderGuiItem(tile.getInvStack(0), ModelTransformation.Type.GROUND);
			}

			if (!tile.getInvStack(1).isEmpty()) {

				//GL11.glTranslatef((float) x + 0.69F, (float) y + 0.765F, (float) z + 0.31F);
				GL11.glScalef(0.3F, 0.3F, 0.3F);

				//MinecraftClient.getInstance().getItemRenderer().renderItem(tile.getInvStack(1), ModelTransformation.Type.GROUND);
			}

			if (!tile.getInvStack(2).isEmpty()) {

				//GL11.glTranslatef((float) x + 0.78F, (float) y + 0.775F, (float) z + 0.50F);
				GL11.glScalef(0.3F, 0.3F, 0.3F);

				//MinecraftClient.getInstance().getItemRenderer().renderItem(tile.getInvStack(2), ModelTransformation.Type.GROUND);
			}

			if (!tile.getInvStack(3).isEmpty()) {

				//GL11.glTranslatef((float) x + 0.69F, (float) y + 0.765F, (float) z + 0.69F);
				GL11.glScalef(0.3F, 0.3F, 0.3F);

				//MinecraftClient.getInstance().getItemRenderer().renderItem(tile.getInvStack(3), ModelTransformation.Type.GROUND);
			}

			if (!tile.getInvStack(4).isEmpty()) {

				//GL11.glTranslatef((float) x + 0.5F, (float) y + 0.775F, (float) z + 0.78F);
				GL11.glScalef(0.3F, 0.3F, 0.3F);

				//MinecraftClient.getInstance().getItemRenderer().renderItem(tile.getInvStack(4), ModelTransformation.Type.GROUND);
			}

			if (!tile.getInvStack(5).isEmpty()) {

				//GL11.glTranslatef((float) x + 0.31F, (float) y + 0.765F, (float) z + 0.69F);
				GL11.glScalef(0.3F, 0.3F, 0.3F);

				//MinecraftClient.getInstance().getItemRenderer().renderItem(tile.getInvStack(5), ModelTransformation.Type.GROUND);
			}

			if (!tile.getInvStack(6).isEmpty()) {

				//GL11.glTranslatef((float) x + 0.220F, (float) y + 0.775F, (float) z + 0.50F);
				GL11.glScalef(0.3F, 0.3F, 0.3F);

				//MinecraftClient.getInstance().getItemRenderer().renderItem(tile.getInvStack(6), ModelTransformation.Type.GROUND);
			}

			if (!tile.getInvStack(7).isEmpty()) {

				//GL11.glTranslatef((float) x + 0.31F, (float) y + 0.765F, (float) z + 0.31F);
				GL11.glScalef(0.3F, 0.3F, 0.3F);

				//MinecraftClient.getInstance().getItemRenderer().renderItem(tile.getInvStack(7), ModelTransformation.Type.GROUND);
			}

			if (!tile.getInvStack(8).isEmpty()) {

				//GL11.glTranslatef((float) x + 0.5F, (float) y + 0.785F, (float) z + 0.50F);
				GL11.glScalef(0.3F, 0.3F, 0.3F);

				//MinecraftClient.getInstance().getItemRenderer().renderItem(tile.getInvStack(8), ModelTransformation.Type.GROUND);
			}
			matrix.pop();
		}*/
		
		matrix.pop();
	}

	static {
		TEXTURE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEX, new Identifier(Reference.MOD_ID, "blocks/soul_infuser"));
	}
}
