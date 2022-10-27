package com.rainbowluigi.soulmagic.util;

public class ColorUtils {

	public static int[] colorToInts(int color) {
		int red = color >>> 16;
		int green = color >>> 8 & 255;
		int blue = color & 255;
		return new int[] {red, green, blue};
	}
	
	public static float[] colorToFloats(int color) {
		int[] ia = colorToInts(color);
		
		float red = ia[0] / 255f;
		float green = ia[1] / 255f;
		float blue = ia[2] /255f;
		return new float[] {red, green, blue};
	}
	
	public static float[] colorsToFloats(int... colors) {
		int[][] ia = new int[colors.length][3];
		
		for(int i = 0; i < colors.length; i++) {
			ia[i] = colorToInts(colors[i]);
		}
		
		int total = 0;
		int sum = 0;
		
		for(int i = 0; i < colors.length; i++) {
			total += ia[i][0];
			sum += 1;
		}
		
		float red = ((float) total / sum) / 255;
		
		total = 0;
		sum = 0;
		
		for(int i = 0; i < colors.length; i++) {
			total += ia[i][1];
			sum += 1;
		}
		
		float green = ((float) total / sum) / 255;
		
		total = 0;
		sum = 0;
		
		for(int i = 0; i < colors.length; i++) {
			total += ia[i][2];
			sum += 1;
		}
		
		float blue = ((float) total / sum) / 255;
		
		return new float[] {red, green, blue};
	}

	public static int intsToColor(int red, int green, int blue) {
		int rgb = red;
		rgb = (rgb << 8) + green;
		return (rgb << 8) + blue;
	}

	public static int getRedFromColor(int color) {
		return (color >> 16) & 0xFF;
	}

	public static int getGreenFromColor(int color) {
		return (color >> 8) & 0xFF;
	}

	public static int getBlueFromColor(int color) {
		return (color) & 0xFF;
	}

	public static int getColorBetweenTwoColors(double ratio, int firstColor, int secondColor) {
		return (int) Math.abs((ratio * secondColor) + ((1 - ratio) * firstColor));
	}
}
