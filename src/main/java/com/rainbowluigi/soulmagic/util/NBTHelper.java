package com.rainbowluigi.soulmagic.util;

import net.minecraft.nbt.NbtCompound;

public class NBTHelper {

    public static int getIntFromNbt(NbtCompound compound, String key, int defaultValue) {
        if(compound != null && compound.contains(key)) {
            return compound.getInt(key);
        }

        return defaultValue;
    }

    public static boolean getBooleanFromNbt(NbtCompound compound, String key, boolean defaultValue) {
        if(compound != null && compound.contains(key)) {
            return compound.getBoolean(key);
        }

        return defaultValue;
    }
}
