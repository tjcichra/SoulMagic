package com.rainbowluigi.soulmagic.client;

import com.rainbowluigi.soulmagic.util.Reference;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModParticleTypes {

	public static final DefaultParticleType SOUL_ESSENCE = new SoulParticleType(false);
	
	public static void registerParticles() {
		registerParticleType(SOUL_ESSENCE, "soul_essence");
    }

    private static void registerParticleType(ParticleType<?> type, String name) {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(Reference.MOD_ID, name), type);
    }
}
