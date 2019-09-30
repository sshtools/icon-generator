package com.sshtools.icongenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Colour theme for colours that are automatically picked based on text content.
 */
public class Colors {

	/**
	 * Default theme
	 */
	public static Colors DEFAULT = new Colors(Arrays.asList(0xfff16364, 0xfff58559, 0xfff9a43e, 0xffe4c62e, 0xff67bf74,
			0xff59a2be, 0xff2093cd, 0xffad62a7, 0xff805781));

	/**
	 * Material theme
	 */
	public static Colors MATERIAL = new Colors(0xFFFFEB3B, 0xFFFFC107, 0xFFFF9800, 0xFFFF5722, 0xFF795548, 0xFF9E9E9E,
			0xFF607D8B, 0xFF000000, 0xFFF44336, 0xFF9C27B0, 0xFFE91E63, 0xFF673AB7, 0xFF3F51B5, 0xFF2196F3, 0xFF03A9F4,
			0xFF00BCD4, 0xFF009688, 0xFF4CAF50, 0xFF8BC34A, 0xFFCDDC39);

	private final List<Integer> colors;
	private final Random rnd;

	/**
	 * Constructor for creating your own themes.
	 * 
	 * @param colorList list of colours
	 */
	public Colors(List<Integer> colorList) {
		colors = colorList;
		rnd = new Random(System.currentTimeMillis());
	}

	/**
	 * Constructor for creating your own themes.
	 * 
	 * @param colorList list of colours
	 */
	public Colors(Integer... colorList) {
		colors = Arrays.asList(colorList);
		rnd = new Random(System.currentTimeMillis());
	}

	/**
	 * Picked a random colour.
	 * 
	 * @return random colour
	 */
	public int randomColor() {
		return colors.get(rnd.nextInt(colors.size()));
	}

	/**
	 * Get a colour from the theme based on it's hash code.
	 * 
	 * @param key key to base colour on
	 * @return colour
	 */
	public int color(Object key) {
		return colors.get(Math.abs(key.hashCode()) % colors.size());
	}
}
