package com.sshtools.icongenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ServiceLoader;

/**
 * Build all the characteristics of a generated icon. This then needs to be
 * passed to one of the toolkit specific generators.
 * <p>
 * To get the final icon, you use {@link #build(Class, Object...)}.
 */
public class IconBuilder {
	public static final int RANDOM_TEXT_COLOR = -4;
	public static final int AUTO_TEXT_COLOR = -3;
	public static final int AUTO_TEXT_COLOR_WHITE = -1;
	public static final int AUTO_TEXT_COLOR_BLACK = -2;
	public static final int AUTO_COLOR = -3;
	public static final int RANDOM_COLOR = -4;

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
		ROUNDED,
		/**
		 * Automatic shape (RECTANGLE, ROUND or ROUNDED) depending on the hash
		 * code of the text
		 */
		AUTOMATIC;

		public IconShape shapeForText(String text) {
			if(text == null|| this != IconShape.AUTOMATIC)
				return this == IconShape.AUTOMATIC ? IconShape.ROUND : this;
			else
				return IconShape.values()[Math.abs(text.hashCode() % IconShape.values().length - 1)];
		}
	}

	/**
	 * How (or if) to convert the case of any text case
	 */
	public enum TextCase {
		/**
		 * Convert to upper case
		 */
		UPPER,
		/**
		 * Convert to lower case
		 */
		LOWER, ORIGINAL;

		public final String textForCase(String text) {
			if (text == null)
				return null;
			switch (this) {
			case UPPER:
				return text.toUpperCase();
			case LOWER:
				return text.toUpperCase();
			default:
				return text;
			}
		}
	}

	/**
	 * How (or if) to convert the case of any text content
	 */
	public enum TextContent {
		/**
		 * Take the initials of every word
		 */
		INITIALS,
		/**
		 * Take the first word
		 */
		FIRST_WORD,
		/**
		 * Take the last word
		 */
		LAST_WORD,
		/**
		 * The original text
		 */
		ORIGINAL;

		public final String textForContent(String text) {
			if (text == null)
				return null;
			switch (this) {
			case INITIALS:
				String[] words = text.split("\\s+");
				StringBuilder b = new StringBuilder();
				for (String w : words) {
					b.append(w.charAt(0));
				}
				return b.toString();
			case FIRST_WORD:
				words = text.split("\\s+");
				return words[0];
			case LAST_WORD:
				words = text.split("\\s+");
				return words[words.length - 1];
			default:
				return text;
			}
		}
	}

	/**
	 * How (or if) to select a font awesome icon
	 */
	public enum AwesomeIconMode {
		/**
		 * Do not use a an {@link AwesomeIcon}. Will be set if
		 * {@link IconBuilder#icon(AwesomeIcon)} is called with
		 * <code>null</code>
		 */
		NONE,
		/**
		 * Use a specific icon. Will be set if
		 * {@link IconBuilder#icon(AwesomeIcon)} is called with anything other
		 * than <code>null</code>
		 */
		SPECIFIC,
		/**
		 * Automatically select an awesome icon based on the text, but default
		 * to picking one based on the hash code of the text if none can be
		 * found.
		 */
		AUTO_MATCH,
		/**
		 * Automatically select an awesome icon based on the text, but default
		 * to picking one based on the hash code of the text if none can be
		 * found.
		 */
		AUTO_TEXT,
	}

	private IconShape shape = IconShape.RECTANGLE;
	private String text;
	private float borderThickness;
	private float height, width;
	private String fontName;
	private int textColor;
	private int borderColor;
	private int fixedFontSize;
	private boolean isBold;
	private TextCase textCase;
	private TextContent textContent;
	private float radius;
	private AwesomeIconMode awesomeIconMode = AwesomeIconMode.NONE;
	private int color;
	private AwesomeIcon icon;
	private Map<Class<?>, IconGenerator<?>> generators = new HashMap<Class<?>, IconGenerator<?>>();
	private int maxTextLength = -1;
	private Colors theme;
	private int backgroundOpacity= 255;

	/**
	 * Constructor.
	 */
	public IconBuilder() {
		color = AUTO_COLOR;
		text = "";
		textColor = AUTO_TEXT_COLOR;
		borderThickness = 0;
		width = 64;
		height = 64;
		fontName = "sans-serif-light";
		fixedFontSize = -1;
		isBold = false;
		textCase = TextCase.ORIGINAL;
		textContent = TextContent.ORIGINAL;
		theme = null;
		/* Default generators */
		for (IconGenerator<?> gen : ServiceLoader.load(IconGenerator.class)) {
			if (gen.isValid())
				generators.put(gen.getIconClass(), gen);
		}
	}

	/**
	 * Add a new custom generator. Remove one by setting generate argument to
	 * null. New generators can be automatically added if you add a service
	 * definition for them. In your add-on module, create a file in
	 * META-INF/services/com.sshtools.icongenerator.IconGenerator and place the
	 * full class name of a class that implements {@link IconGenerator}.
	 * 
	 * @param clazz class of generate icon
	 * @param generate callable that can generate such an object given this icon
	 *            builder
	 * @param <T> type of generated object
	 */
	public <T extends Object> void generator(Class<T> clazz, IconGenerator<T> generate) {
		if (generate == null)
			generators.remove(clazz);
		else
			generators.put(clazz, generate);
	}

	/**
	 * Build the icon given it's native type. If the type is recognised, that
	 * type will be returned.
	 * 
	 * @param <T> native type of icon
	 * @param iconClass class of native icon to return
	 * @param args additional arguments to supply to generator
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
	 * If set to anything other than null, {@link #awesomeIconMode()} will be
	 * set to {@link AwesomeIconMode#SPECIFIC}.
	 * 
	 * @param icon specific icon to set
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
	 * @param width width
	 * @return this instance for chaining
	 */
	public IconBuilder width(float width) {
		this.width = width;
		return this;
	}

	/**
	 * Set the height
	 * 
	 * @param height height
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
	 * Set the maximum length of any text after other processing such as
	 * converting text according to the set {@link TextCase} or
	 * {@link TextContent}. Set to -1 for no maximum.
	 * 
	 * @param maxTextLength max text length
	 * @return this instance for chaining
	 */
	public IconBuilder maxTextLength(int maxTextLength) {
		this.maxTextLength = maxTextLength;
		return this;
	}

	/**
	 * Set the maximum length of any text after other processing such as
	 * converting text according to the set {@link TextCase} or
	 * {@link TextContent}.
	 * 
	 * @return max text length
	 */
	public int maxTextLength() {
		return maxTextLength;
	}

	/**
	 * Set the text color. 
	 * 
	 * @param r red
	 * @param g green
	 * @param b blue
	 * @return this instance for chaining
	 */
	public IconBuilder textColor(int r, int g, int b) {
		return textColor(encodeRGB(r, g, b));
	}

	/**
	 * Set the border color. This is either {@link #AUTO_COLOR},
	 * {@link #RANDOM_COLOR} or an RGB value encoded in least
	 * significant 3 bytes.
	 * 
	 * @param color text color
	 * @return this instance for chaining
	 */
	public IconBuilder borderColor(int color) {
		this.borderColor = color;
		return this;
	}

	/**
	 * Set the border color. 
	 * 
	 * @param r red
	 * @param g green
	 * @param b blue
	 * @return this instance for chaining
	 */
	public IconBuilder borderColor(int r, int g, int b) {
		return borderColor(encodeRGB(r, g, b));
	}

	/**
	 * Get the border color. This is either {@link #AUTO_COLOR},
	 * {@link #RANDOM_COLOR} or an RGB value encoded in least
	 * significant 3 bytes.
	 * 
	 * @return color text color
	 */
	public int borderColor() {
		return borderColor;
	}

	/**
	 * Set the text color. This is either {@link #AUTO_TEXT_COLOR},
	 * {@link #RANDOM_TEXT_COLOR}, {@link #AUTO_TEXT_COLOR_WHITE},
	 * {@link #AUTO_TEXT_COLOR_BLACK} or an RGB value encoded in least
	 * significant 3 bytes.
	 * 
	 * @param color text color
	 * @return this instance for chaining
	 */
	public IconBuilder textColor(int color) {
		this.textColor = color;
		return this;
	}

	/**
	 * Set the mode of selecting the pictorial <code>Awesome Icon</code>.
	 * 
	 * @param awesomeIconMode awesome icon selection mode
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
	 * Set a random text color
	 * 
	 * @return this instance for chaining
	 */
	public IconBuilder randomTextColor() {
		this.textColor = RANDOM_TEXT_COLOR;
		return this;
	}

	/**
	 * Automatically choose the text color based on the luminosity of the
	 * background
	 * 
	 * @return this instance for chaining
	 */
	public IconBuilder autoTextColor() {
		this.textColor = AUTO_TEXT_COLOR;
		return this;
	}

	/**
	 * Automatically choose the text color based on the luminosity of the
	 * background with a leaning towards white.
	 * 
	 * @return this instance for chaining
	 */
	public IconBuilder autoTextColorPreferWhite() {
		this.textColor = AUTO_TEXT_COLOR_WHITE;
		return this;
	}

	/**
	 * Automatically choose the text color based on the luminosity of the
	 * background with a leaning towards black.
	 * 
	 * @return this instance for chaining
	 */
	public IconBuilder autoTextColorPreferBlack() {
		this.textColor = AUTO_TEXT_COLOR_BLACK;
		return this;
	}

	/**
	 * Get the text color. This is either {@link #AUTO_TEXT_COLOR},
	 * {@link #RANDOM_TEXT_COLOR}, {@link #AUTO_TEXT_COLOR_WHITE},
	 * {@link #AUTO_TEXT_COLOR_BLACK} or an RGB value encoded in least
	 * significate 3 bytes.
	 * 
	 * @return text color
	 */
	public int textColor() {
		return textColor;
	}

	/**
	 * Set the border thickness
	 * 
	 * @param thickness border thickness
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
	 * @param font font name
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
	 * @param size font size
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
	 * @param bold bold
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
	 * @param textCase text case
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
	 * Set how (or if) to convert any text content.
	 * 
	 * @param textContent text content
	 * @return this instance for chaining
	 */
	public IconBuilder textContent(TextContent textContent) {
		this.textContent = textContent;
		return this;
	}

	/**
	 * Get how (or if) to convert the text content.
	 * 
	 * @return text case
	 */
	public TextContent textContent() {
		return textContent;
	}

	/**
	 * Set the shape of the background.
	 * 
	 * @param shape shape
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
	 * Set the background shape to be automatically determined based on the hash
	 * code of the text.
	 * 
	 * @return this instance for chaining
	 */
	public IconBuilder autoShape() {
		this.shape = IconShape.AUTOMATIC;
		return this;
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
	 * @param radius radius of rounded corners
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
	 * @param text text
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
	 * Get the {@link Colors} theme that will be used if {@link #color} is
	 * {@link #AUTO_COLOR}. This may be <code>null</code>, which indicates no
	 * theme will be used, and the color will be generated from the entire
	 * palette.
	 * 
	 * @return theme
	 */
	public Colors theme() {
		return theme;
	}

	/**
	 * Set the {@link Colors} theme that will be used if {@link #color} is
	 * {@link #AUTO_COLOR}. This may be <code>null</code>, which indicates no
	 * theme will be used, and the color will be generated from the entire
	 * palette.
	 * 
	 * @param theme theme
	 * @return this instance for chaining
	 */
	public IconBuilder theme(Colors theme) {
		this.theme = theme;
		return this;
	}

	/**
	 * Automatically choose the background color based on the text content
	 * 
	 * @return this instance for chaining
	 */
	public IconBuilder autoColor() {
		this.color = AUTO_COLOR;
		return this;
	}

	/**
	 * Choose a random background color based on the text content
	 * 
	 * @return this instance for chaining
	 */
	public IconBuilder randomColor() {
		this.color = RANDOM_COLOR;
		return this;
	}

	/**
	 * Get the opacity of the background. This is a value from 0 to
	 * 255, with 0 being fully transparent, and 255 being full opaque.
	 * 
	 * @return background opacity
	 */
	public int backgroundOpacity() {
		return backgroundOpacity;
	}

	/**
	 * Get the opacity of the background. This is a value from 0 to
	 * 255, with 0 being fully transparent, and 255 being full opaque. 
	 * 
	 * @param backgroundOpacity background opacity
	 * @return this instance for chaining
	 */
	public IconBuilder backgroundOpacity(int backgroundOpacity) {
		this.backgroundOpacity = backgroundOpacity;
		return this;
	}

	/**
	 * Get the color of the background. This is either {@link #AUTO_COLOR},
	 * {@link #RANDOM_COLOR} or an RGB value encoded in least significate 3
	 * bytes.
	 * 
	 * @return background color
	 */
	public int color() {
		return color;
	}

	/**
	 * Set the color of the background. 
	 * 
	 * @param r red
	 * @param g green
	 * @param b blue
	 * @return this instance for chaining
	 */
	public IconBuilder color(int r, int g, int b) {
		return color(encodeRGB(r, g, b));
	}

	/**
	 * Set the color of the background. This is either {@link #AUTO_COLOR},
	 * {@link #RANDOM_COLOR} or an RGB value encoded in least significant 3
	 * bytes.
	 * 
	 * @param color color
	 * @return this instance for chaining
	 */
	public IconBuilder color(int color) {
		this.color = color;
		return this;
	}

	/**
	 * Compute the text to use based on the current {@link TextCase},
	 * {@link TextContent} and {@link #maxTextLength}.
	 * 
	 * @return computed text
	 */
	public String computedText() {
		if (text == null)
			return "";
		String v = textCase.textForCase(textContent.textForContent(text));
		return maxTextLength != -1 && v.length() > maxTextLength ? v.substring(0, maxTextLength) : v;
	}

	/**
	 * Compute the shape to use based on the current {@link IconShape}.
	 * 
	 * @return computed shape
	 */
	public IconShape computedShape() {
		return shape.shapeForText(text);
	}

	/**
	 * Compute the color to use based on whether the {@link #color} is
	 * {@link #AUTO_COLOR} or {@link #RANDOM_COLOR}.
	 * 
	 * @return computed color
	 */
	public int computedColor() {
		if (color == AUTO_COLOR) {
			if (theme == null || ( text == null && icon == null ) )
				return text == null && icon == null ? 0 : ( text == null ? icon : text).hashCode() & 0xffffff;
			else {
				return theme.color(text == null ? icon : text);
			}
		} else if (color == RANDOM_COLOR) {
			if (theme == null)
				return new Random().nextInt() & 0xffffff;
			else {
				return theme.randomColor();
			}
		} else {
			return color;
		}
	}

	/**
	 * Compute the border color to use based on whether the {@link #color} is
	 * {@link #AUTO_COLOR} or {@link #RANDOM_COLOR}.
	 * 
	 * @return computed color
	 */
	public int computedBorderColor() {
		if (borderColor == AUTO_COLOR) {
			return computedColor();
		} else if (borderColor == RANDOM_COLOR) {
			if (theme == null)
				return new Random().nextInt() & 0xffffff;
			else {
				return theme.randomColor();
			}
		} else {
			return borderColor;
		}
	}

	/**
	 * Compute the text color to use based on whether the {@link #textColor} is
	 * {@link #AUTO_TEXT_COLOR}, {@link #RANDOM_TEXT_COLOR},
	 * {@link #AUTO_TEXT_COLOR_BLACK}, {@link #AUTO_TEXT_COLOR_WHITE}.
	 * 
	 * @param background background for auto mode calculation
	 * @return computed text color
	 */
	public int computedTextColor(int background) {
		if (textColor < 0) {
			if(textColor == RANDOM_TEXT_COLOR) {
				return new Random().nextInt() & 0xffffff;
			}
			/*
			 * Give the text either black or white depending on brightness of
			 * background
			 */
			int[] rgb = decodeRGB(background);
			float[] hsb = toHSB(rgb[0], rgb[1], rgb[2]);
			switch (textColor) {
			case IconBuilder.AUTO_TEXT_COLOR:
				if (hsb[2] > 0.9f) {
					return 0;
				} else {
					return 0xffffff;
				}
			case IconBuilder.AUTO_TEXT_COLOR_WHITE:
				if (hsb[1] < 0.1f && hsb[2] > 0.1f) {
					return 0;
				} else {
					return 0xffffff;
				}
			case IconBuilder.AUTO_TEXT_COLOR_BLACK:
				if (hsb[1] > 0.9f || hsb[2] > 0.9f) {
					return 0;
				} else {
					return 0xffffff;
				}
			}
		} 
		return textColor;
	}

	/**
	 * Compute the icon to use based on the {@link AwesomeIconMode} and
	 * {@link #text}.
	 * 
	 * @return computed color
	 */
	public AwesomeIcon computedIcon() {
		if (icon == null) {
			switch (awesomeIconMode) {
			case AUTO_MATCH:
				return AwesomeIcon.match(text);
			case AUTO_TEXT:
				return AwesomeIcon.icon(text);
			default:
				break;
			}
		}
		return icon;
	}

	public static float[] toHSB(int red, int green, int blue) {
		float h = 0;
		float s = 0;
		float b = 0;
		int max = (red > green) ? red : green;
		if (blue > max)
			max = blue;
		int cmin = (red < green) ? red : green;
		if (blue < cmin)
			cmin = blue;
		b = ((float) max) / 255.0f;
		if (max != 0)
			s = ((float) (max - cmin)) / ((float) max);
		else
			s = 0;
		if (s > 0) {
			float redc = ((float) (max - red)) / ((float) (max - cmin));
			float greenc = ((float) (max - green)) / ((float) (max - cmin));
			float bluec = ((float) (max - blue)) / ((float) (max - cmin));
			if (red == max) {
				h = bluec - greenc;
			} else if (green == max) {
				h = 2.0f + redc - bluec;
			} else {
				h = 4.0f + greenc - redc;
			}
			h = h / 6.0f;
			if (h < 0) {
				h = h + 1.0f;
			}
		}
		return new float[] { h, s, b };
	}
	
	public static int encodeRGB(int r, int g, int b) {
		return (r  &0xff)<< 16 | (g &0xff) << 8 | b &0xff;
	}
	
	public static int[] decodeRGB(int v) {
		int r = v >> 16 & 0x000000ff;
		int g = v >> 8 & 0x000000ff;
		int b = v & 0x000000ff;
		return new int[] {r,g,b};
	}
}