package com.sshtools.icongenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Build all the characteristics of a generated icon. This then needs to be
 * passed to one of the toolkit specific generators.
 * <p>
 * To get the final icon, you can either use
 */
public class IconBuilder {

	public static final int AUTO_TEXT_COLOR = -3;
	public static final int AUTO_TEXT_COLOR_WHITE = -1;
	public static final int AUTO_TEXT_COLOR_BLACK = -2;

	/**
	 * Icon shape
	 *
	 */
	public enum IconShape {
		/**
		 * Rectangle
		 */
		RECTANGLE,
		/**
		 * Circle
		 */
		ROUND,
		/**
		 * Rounded rectangle
		 */
		ROUNDED
	}

	/**
	 * How (or if) to convert the case of any text content
	 */
	public enum TextCase {
		/**
		 * Convert to upper case
		 */
		UPPER,
		/**
		 * Convert to lower case
		 */
		LOWER, ORIGINAL
	}

	/**
	 * How (or if) to select a font awesome icon
	 */
	public enum AwesomeIconMode {
		/**
		 * Do not use a an {@link AwesomeIcon}. Will be set if {@link IconBuilder#icon(AwesomeIcon)}
		 * is called with <code>null</code>
		 */
		NONE,
		/**
		 * Use a specific icon. Will be set if {@link IconBuilder#icon(AwesomeIcon)}
		 * is called with anything other than <code>null</code>
		 */
		SPECIFIC, 
		/**
		 * Automatically select an awesome icon based on the text, but default to 
		 * picking one based on the hash code of the text if none can be found.
		 */ 
		AUTO_MATCH,
		/**
		 * Automatically select an awesome icon based on the text, but default to 
		 * picking one based on the hash code of the text if none can be found.
		 */
		AUTO_TEXT,
	}

	private IconShape shape = IconShape.RECTANGLE;
	private String text;
	private float borderThickness;
	private float height, width;
	private String fontName;
	private int textColor;
	private int fixedFontSize;
	private boolean isBold;
	private TextCase textCase;
	private float radius;
	private AwesomeIconMode awesomeIconMode = AwesomeIconMode.NONE;
	private int color;
	private AwesomeIcon icon;
	private Map<Class<?>, IconGenerator<?>> generators = new HashMap<Class<?>, IconGenerator<?>>();

	/**
	 * Constructor.
	 */
	public IconBuilder() {
		text = "";
		textColor = AUTO_TEXT_COLOR;
		borderThickness = 0;
		width = 64;
		height = 64;
		fontName = "sans-serif-light";
		fixedFontSize = -1;
		isBold = false;
		textCase = TextCase.ORIGINAL;

		/* Default generators */
		for (IconGenerator<?> gen : ServiceLoader.load(IconGenerator.class)) {
			if (gen.isValid())
				generators.put(gen.getIconClass(), gen);
		}

	}

	/**
	 * Add a new custom generator. Remove one by setting generate argument to null.
	 * New generators can be automatically added if you add a service definition for
	 * them. In your add-on module, create a file in
	 * META-INF/services/com.sshtools.icongenerator.IconGenerator and place the full
	 * class name of a class that implements {@link IconGenerator}.
	 * 
	 * @param clazz
	 *            class of generate icon
	 * @param generate
	 *            callable that can generate such an object given this icon builder
	 * @param <T>
	 *            type of generated object
	 */
	public <T extends Object> void generator(Class<T> clazz, IconGenerator<T> generate) {
		if (generate == null)
			generators.remove(clazz);
		else
			generators.put(clazz, generate);
	}

	/**
	 * Build the icon given it's native type. If the type is recognised, that type
	 * will be returned.
	 * 
	 * @param <T> native type of icon
	 * @param iconClass class of native icon to return
	 * @return icon
	 */
	@SuppressWarnings("unchecked")
	public <T> T build(Class<T> iconClass, Object... args) {
		IconGenerator<T> gen = (IconGenerator<T>) generators.get(iconClass);
		if (gen == null)
			throw new UnsupportedOperationException("Icon's of class " + iconClass + " are not supported.");
		else
			return gen.generate(this, args);
	}

	/**
	 * Get the icon to display on the background.
	 * 
	 * @return icon
	 */
	public AwesomeIcon icon() {
		return icon;
	}

	/**
	 * Set the icon to display on the background. If set to <code>null</code>, 
	 * {@link #awesomeIconMode()} will be set to {@link AwesomeIconMode#NONE}. 
	 * If set to anything other than null, {@link #awesomeIconMode()} will be set to {@link AwesomeIconMode#SPECIFIC}.
	 * 
	 * @param icon
	 * @return this instance for chaining
	 */
	public IconBuilder icon(AwesomeIcon icon) {
		this.icon = icon;
		awesomeIconMode = icon == null ? AwesomeIconMode.NONE : AwesomeIconMode.SPECIFIC;
		return this;
	}

	/**
	 * Get the width.
	 * 
	 * @return width
	 */
	public float width() {
		return width;
	}

	/**
	 * Set the width
	 * 
	 * @param width
	 *            with
	 * @return this instance for chaining
	 */
	public IconBuilder width(float width) {
		this.width = width;
		return this;
	}

	/**
	 * Set the height
	 * 
	 * @param height
	 * @return this instance for chaining
	 */
	public IconBuilder height(float height) {
		this.height = height;
		return this;
	}

	/**
	 * Get the height.
	 * 
	 * @return height
	 */
	public float height() {
		return height;
	}

	/**
	 * Set the text color
	 * 
	 * @param color
	 *            text color
	 * @return this instance for chaining
	 */
	public IconBuilder textColor(int color) {
		this.textColor = color;
		return this;
	}
	
	/**
	 * Set the mode of selecting the pictorial <code>Awesome Icon</code>. 
	 * 
	 * @param mode
	 *            awesome icon selection mode
	 * @return this instance for chaining
	 */
	public IconBuilder awesomeIconMode(AwesomeIconMode awesomeIconMode) {
		this.awesomeIconMode = awesomeIconMode;
		return this;
	}
	
	/**
	 * Get the mode of selecting the pictorial <code>Awesome Icon</code>. 
	 * 
	 * @return awesome icon selection mode
	 */
	public AwesomeIconMode awesomeIconMode() {
		return awesomeIconMode;
	}

	/**
	 * Automatically choose the text color based on the luminosity of the background
	 * 
	 * @return this instance for chaining
	 */
	public IconBuilder autoTextColor() {
		this.textColor = AUTO_TEXT_COLOR;
		return this;
	}

	/**
	 * Automatically choose the text color based on the luminosity of the background
	 * with a leaning towards white.
	 * 
	 * @return this instance for chaining
	 */
	public IconBuilder autoTextColorPreferWhite() {
		this.textColor = AUTO_TEXT_COLOR_WHITE;
		return this;
	}

	/**
	 * Automatically choose the text color based on the luminosity of the background
	 * with a leaning towards black.
	 * 
	 * @return this instance for chaining
	 */
	public IconBuilder autoTextColorPreferBlack() {
		this.textColor = AUTO_TEXT_COLOR_BLACK;
		return this;
	}

	/**
	 * Get the text color
	 * 
	 * @return text color
	 */
	public int textColor() {
		return textColor;
	}

	/**
	 * Set the border thickness
	 * 
	 * @param thickness
	 *            border thickness
	 * @return this instance for chaining
	 */
	public IconBuilder border(float thickness) {
		this.borderThickness = thickness;
		return this;
	}

	/**
	 * Get the border width.
	 * 
	 * @return border width
	 */
	public float border() {
		return borderThickness;
	}

	/**
	 * Set the font name used for text.
	 * 
	 * @param font
	 *            font name
	 * @return this instance for chaining
	 */
	public IconBuilder fontName(String font) {
		this.fontName = font;
		return this;
	}

	/**
	 * Get the font name used for text.
	 * 
	 * @return font name
	 */
	public String fontName() {
		return fontName;
	}

	/**
	 * Set the font size used for text.
	 * 
	 * @param size
	 *            font size
	 * @return this instance for chaining
	 */
	public IconBuilder fontSize(int size) {
		this.fixedFontSize = size;
		return this;
	}

	/**
	 * Get the font size used for text.
	 * 
	 * @return font size
	 */
	public int fontSize() {
		return fixedFontSize;
	}

	/**
	 * Set whether to make the font bold.
	 * 
	 * @param bold
	 *            bold
	 * @return this instance for chaining
	 */
	public IconBuilder bold(boolean bold) {
		this.isBold = bold;
		return this;
	}

	/**
	 * Get whether to make the font bold
	 * 
	 * @return bold
	 */
	public boolean bold() {
		return isBold;
	}

	/**
	 * Get the radius for rounded corners.
	 * 
	 * @return radius
	 */
	public float radius() {
		return radius;
	}

	/**
	 * Set how (or if) to convert the case of any text content.
	 * 
	 * @param textCase
	 *            text case
	 * @return this instance for chaining
	 */
	public IconBuilder textCase(TextCase textCase) {
		this.textCase = textCase;
		return this;
	}

	/**
	 * Get how (or if) to convert the case of any text content.
	 * 
	 * @return text case
	 */
	public TextCase textCase() {
		return textCase;
	}

	/**
	 * Set the shape of the background.
	 * 
	 * @param shape
	 *            shape
	 * @return this instance for chaining
	 */
	public IconBuilder shape(IconShape shape) {
		this.shape = shape;
		return this;
	}

	/**
	 * Get the shape of the background
	 * 
	 * @return shape
	 */
	public IconShape shape() {
		return shape;
	}

	/**
	 * Set the background shape to a rectangle.
	 * 
	 * @return this instance for chaining
	 */
	public IconBuilder rect() {
		this.shape = IconShape.RECTANGLE;
		return this;
	}

	/**
	 * Set the background shape to a circle.
	 * 
	 * @return this instance for chaining
	 */
	public IconBuilder round() {
		this.shape = IconShape.ROUND;
		return this;
	}

	/**
	 * Set the background shape to a rounded rectangle.
	 *
	 * @param radius
	 *            radius of rounded corners
	 * @return this instance for chaining
	 */
	public IconBuilder roundRect(int radius) {
		this.shape = IconShape.ROUNDED;
		this.radius = radius;
		return this;
	}

	/**
	 * Set the text to display on the background
	 * 
	 * @param text
	 *            text
	 * @return this instance for chaining
	 */
	public IconBuilder text(String text) {
		this.text = text;
		return this;
	}

	/**
	 * Get the text to display on the background
	 * 
	 * @return text
	 */
	public String text() {
		return text;
	}

	/**
	 * Get the color of the background
	 * 
	 * @return background color
	 */
	public int color() {
		return color;
	}

	/**
	 * Set the color of the background
	 * 
	 * @param color
	 *            color
	 * @return this instance for chaining
	 */
	public IconBuilder color(int color) {
		this.color = color;
		return this;
	}
}