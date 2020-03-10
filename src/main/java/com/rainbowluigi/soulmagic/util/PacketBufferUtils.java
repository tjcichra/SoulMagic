package com.rainbowluigi.soulmagic.util;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketBufferUtils {

	public static BlockEntity getBlockEntity(PacketByteBuf pb, PlayerEntity player) {
		BlockPos bp = pb.readBlockPos();
		
		World w = player.world;
		
		if(w.isChunkLoaded(bp)) {
			BlockEntity bentity = w.getBlockEntity(bp);
			
			if(bentity != null) {
				return bentity;
			}
		}
		return null;
	}
}
