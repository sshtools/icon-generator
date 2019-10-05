package com.sshtools.icongenerator;

/**
 * Utilities.
 */
public class IconUtil {
	/**
	 * Convert a pixel amount to a point size. This assumes a PPI of 72pt (or 96
	 * pixels)
	 * 
	 * @param pixels pixels
	 * @return points
	 */
	public static int pixelsToPoints(int pixels) {
		return (int) ((float) pixels * 72f / 96f);
	}

	/**
	 * Strips everything from a string exception alphabetic characters, digits
	 * or whitespace
	 * 
	 * @param text string to process
	 * @return processed string
	 */
	public static String alphaOnly(String text) {
		StringBuilder b = new StringBuilder();
		for (char c : text.toCharArray()) {
			if (Character.isAlphabetic(c) || Character.isDigit(c) || c == ' ' || c == '\t')
				b.append(c);
		}
		return b.toString();
	}
}
