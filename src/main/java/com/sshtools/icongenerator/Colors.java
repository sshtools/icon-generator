package com.sshtools.icongenerator;

import java.awt.Color;
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
	public static Colors MATERIAL = new Colors(Arrays.asList(Color.decode("#FFEB3B").getRGB(),
			Color.decode("#FFC107").getRGB(), Color.decode("#FF9800").getRGB(), Color.decode("#FF5722").getRGB(),
			Color.decode("#795548").getRGB(), Color.decode("#9E9E9E").getRGB(), Color.decode("#607D8B").getRGB(),
			Color.decode("#000000").getRGB(), Color.decode("#F44336").getRGB(), Color.decode("#9C27B0").getRGB(),
			Color.decode("#E91E63").getRGB(), Color.decode("#673AB7").getRGB(), Color.decode("#3F51B5").getRGB(),
			Color.decode("#2196F3").getRGB(), Color.decode("#03A9F4").getRGB(), Color.decode("#00BCD4").getRGB(),
			Color.decode("#009688").getRGB(), Color.decode("#4CAF50").getRGB(), Color.decode("#8BC34A").getRGB(),
			Color.decode("#CDDC39").getRGB()));

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
