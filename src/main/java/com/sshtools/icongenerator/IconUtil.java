package com.sshtools.icongenerator;

public class IconUtil {

	/**
	 * Convert a pixel amount to a point size. This assumes a PPI of 72pt (or 96 pixels)
	 * 
	 * @param pixels
	 * @return points
	 */
	public static int pixelsToPoints(int pixels) {
		return (int)((float)pixels * 72f / 96f);
	}
}
