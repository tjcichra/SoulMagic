package com.rainbowluigi.soulmagic.client;

import com.rainbowluigi.soulmagic.block.ModBlocks;
import com.rainbowluigi.soulmagic.block.entity.SoulEssenceInfuserBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

@Environment(value= EnvType.CLIENT)
public class BlockEntitySpecialRendererSoulInfuser implements BlockEntityRenderer<SoulEssenceInfuserBlockEntity> {
    private double[][] points = {{1, 0.5}, {1.37, 0.63}, {1.5, 1}, {1.37, 1.37}, {1, 1.5}, {0.63, 1.37}, {0.5, 1}, {0.63, 0.63}};

    public BlockEntitySpecialRendererSoulInfuser(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(SoulEssenceInfuserBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        boolean worldExists = entity.getWorld() != null;
        BlockState blockState = worldExists ? entity.getCachedState() : ModBlocks.SOUL_ESSENCE_INFUSER.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.SOUTH);
        Direction direction = blockState.get(Properties.HORIZONTAL_FACING);

        int horizontal = direction.getHorizontal();

        matrices.push();



//        double offset = Math.sin((entity.getWorld().getTime() + tickDelta) / 8.0) / 4.0;

//        matrices.translate(0.5, 1.25, 0.5);
        matrices.scale(0.5f, 0.5f, 0.5f);

        double y = 1.7;
//        System.out.println(horizontal * 2);
        int startingPoint = horizontal * 2;

        for (int i = 0; i < 8; i++) {
            int j = (i + startingPoint) % 8;

            matrices.push();
            matrices.translate(points[j][0], y, points[j][1]);
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(i), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
            matrices.pop();
        }

        ItemStack centerItemStack = entity.getStack(8);
        if (!centerItemStack.isEmpty()) {
            matrices.push();
            matrices.translate(1, y, 1);

            float quaternion = direction.asRotation();
//            System.out.println(quaternion);
//            quaternion.getY();

            matrices.multiply(new Quaternion(Vec3f.NEGATIVE_Y, -quaternion, true));
            MinecraftClient.getInstance().getItemRenderer().renderItem(centerItemStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
            matrices.pop();
        }

        matrices.pop();
    }
}
