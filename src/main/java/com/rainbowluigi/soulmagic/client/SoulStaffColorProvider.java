package com.rainbowluigi.soulmagic.client;

import com.rainbowluigi.soulmagic.item.soulessence.SoulEssenceStaff;
import com.rainbowluigi.soulmagic.soultype.ModSoulTypes;
import com.rainbowluigi.soulmagic.soultype.SoulType;
import com.rainbowluigi.soulmagic.util.ColorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Environment(value= EnvType.CLIENT)
public class SoulStaffColorProvider {
    private static final int NO_COLOR = 0xFFFFFF;
    private static final int EMPTY_STAFF_COLOR = 0x5b442a;

    public static int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex != 1) {
            return NO_COLOR;
        }

        List<Integer> colorsToDisplay = new ArrayList<>();
        SoulEssenceStaff staff = (SoulEssenceStaff) stack.getItem();

        // Get a list of colors for each soul type in the staff
        for (SoulType soulType : ModSoulTypes.SOUL_TYPE) {
            if (staff.getSoul(stack, MinecraftClient.getInstance().world, soulType) > 0) {
                colorsToDisplay.add(soulType.getColor());
            }
        }

        if (colorsToDisplay.isEmpty()) {
            return EMPTY_STAFF_COLOR;
        }

        long time = 3000L;

        //if colorsToDisplay.size() is 3, then milliseconds would be 0-2999
        long milliseconds = System.currentTimeMillis() % (time * colorsToDisplay.size() - 1);

        //if colorsToDisplay.size() is 3, then color would be 0-2
        int color = (int) (milliseconds / time);
        //if colorsToDisplay.size() is 3, then colorRatio would be 0-0.999
        double colorRatio = (milliseconds % time) / ((double) time);

        int currentColor = colorsToDisplay.get(color);
        int nextColor = colorsToDisplay.get(color >= colorsToDisplay.size() - 1 ? 0 : color + 1);

        int currentRed = ColorUtils.getRedFromColor(currentColor);
        int currentGreen = ColorUtils.getGreenFromColor(currentColor);
        int currentBlue = ColorUtils.getBlueFromColor(currentColor);

        int nextRed = ColorUtils.getRedFromColor(nextColor);
        int nextGreen = ColorUtils.getGreenFromColor(nextColor);
        int nextBlue = ColorUtils.getBlueFromColor(nextColor);

        int red = ColorUtils.getColorBetweenTwoColors(colorRatio, currentRed, nextRed);
        int green = ColorUtils.getColorBetweenTwoColors(colorRatio, currentGreen, nextGreen);
        int blue = ColorUtils.getColorBetweenTwoColors(colorRatio, currentBlue, nextBlue);

        return ColorUtils.intsToColor(red, green, blue);
    }
}
