package com.rainbowluigi.soulmagic.mixin;

import java.util.OptionalInt;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.rainbowluigi.soulmagic.block.entity.SoulInfuserBlockEntity;
import com.rainbowluigi.soulmagic.block.entity.SoulSeparatorBlockEntity;
import com.rainbowluigi.soulmagic.inventory.ModContainerFactories;

import net.fabricmc.fabric.impl.container.ContainerProviderImpl;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.NameableContainerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

	@Redirect(method = "interactBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;openContainer(Lnet/minecraft/container/NameableContainerFactory;)Ljava/util/OptionalInt;"))
	private OptionalInt openContainer(PlayerEntity player, NameableContainerFactory container, PlayerEntity og, World world, ItemStack itemStack_1, Hand hand_1, BlockHitResult blockHitResult_1) {
		BlockEntity blockEntity = world.getBlockEntity(blockHitResult_1.getBlockPos());
		
		if (blockEntity instanceof SoulInfuserBlockEntity) {
			ContainerProviderImpl.INSTANCE.openContainer(ModContainerFactories.SOUL_INFUSER_FACTORY, player, buf -> {
				buf.writeBlockPos(blockHitResult_1.getBlockPos());
			});
		} else if(blockEntity instanceof SoulSeparatorBlockEntity) {
			ContainerProviderImpl.INSTANCE.openContainer(ModContainerFactories.SOUL_SEPARATOR, player, buf -> {
				buf.writeBlockPos(blockHitResult_1.getBlockPos());
			});
		} else {
			player.openContainer(container);
		}
		
		return OptionalInt.empty();
	}
}
