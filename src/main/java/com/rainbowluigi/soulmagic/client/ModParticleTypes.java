package com.rainbowluigi.soulmagic.client;

import com.rainbowluigi.soulmagic.util.Reference;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticleTypes {

    public static final DefaultParticleType SOUL_ESSENCE_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        registerParticleType(SOUL_ESSENCE_PARTICLE, "soul_essence");
    }

    private static void registerParticleType(ParticleType<?> type, String name) {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(Reference.MOD_ID, name), type);
    }
}
