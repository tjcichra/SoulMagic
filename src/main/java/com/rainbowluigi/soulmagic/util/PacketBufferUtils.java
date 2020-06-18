package com.rainbowluigi.soulmagic.util;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketBufferUtils {

	public static <T extends BlockEntity> T getBlockEntity(PacketByteBuf pb, PlayerEntity player) {
		BlockPos bp = pb.readBlockPos();
		
		World w = player.world;
		
		if(w.isChunkLoaded(bp)) {
			@SuppressWarnings("unchecked")
			T be = (T) w.getBlockEntity(bp);
			
			if(be != null) {
				return be;
			}
		}
		return null;
	}
}
