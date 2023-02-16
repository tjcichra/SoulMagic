package com.rainbowluigi.soulmagic.util;

import com.rainbowluigi.soulmagic.soultype.SoulType;

import java.util.HashMap;
import java.util.Map;

public abstract class SoulEssenceHelper {

    public static Map<SoulType, Integer> createMap(Object... o) {
        Map<SoulType, Integer> sMap = new HashMap<>();
        for (int i = 0; i < o.length; i += 2) {
            sMap.put((SoulType) o[i], (Integer) o[i + 1]);
        }
        return sMap;
    }
}