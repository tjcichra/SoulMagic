package com.rainbowluigi.soulmagic.util;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.Map;
import java.util.Map.Entry;

public class SoulUtils {

    public static Map<SoulType, Integer> deserializeSoulMap(JsonObject json) {
        Map<SoulType, Integer> soulMap = Maps.newHashMap();

        for (Entry<String, JsonElement> entry : json.entrySet()) {
            SoulType s = ModSoulTypes.SOUL_TYPE.get(new Identifier(entry.getKey()));
            int d = entry.getValue().getAsInt();
            soulMap.put(s, d);
        }
        return soulMap;
    }

    public static Map<SoulType, int[]> deserializeSoulMap2(JsonArray array) {
        Map<SoulType, int[]> soulMap = Maps.newHashMap();

        for (int i = 0; i < array.size(); ++i) {
            JsonElement ele = array.get(i);

            JsonObject jo = ele.getAsJsonObject();

            SoulType s = ModSoulTypes.SOUL_TYPE.get(new Identifier(JsonHelper.getString(jo, "soul")));
            int d[] = new int[]{JsonHelper.getInt(jo, "min"), JsonHelper.getInt(jo, "max")};
            soulMap.put(s, d);
        }
        return soulMap;
    }
}
