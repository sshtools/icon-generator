package com.sshtools.icongenerator.javafx;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.sshtools.icongenerator.AwesomeIcon;
import com.sshtools.icongenerator.IconBuilder;
import com.sshtools.icongenerator.IconBuilder.IconShape;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class JavaFXIconCanvas {

	/**
	 * TODO this is a work around to the fact that JavaFX has no Font Metrics and
	 *      and accurate text placement seems impossible. 
	 */
	private static final double ADJUST_FACTOR = 0.125f;
	
	private static Font iconFont;

	private Color textPaint;
	private String text;
	private BoundingBox bounds;
	private Font font;
	private int fixedFontSize;
	private boolean bold;
	private Color paint;
	private Color borderPaint;
	private IconShape shape;
	private int radius;
	private int border;
	private Color backgroundPaint;

	public JavaFXIconCanvas(IconBuilder builder) {

		bounds = new BoundingBox(0, 0, (int) builder.width(), (int) builder.height());
		radius = (int) builder.radius();
		shape = builder.computedShape();
		int bg = builder.computedColor();
		paint = getColor(bg);
		borderPaint = getColor(builder.computedBorderColor());
		backgroundPaint = paint;
		if (builder.backgroundOpacity() != 255) {
			backgroundPaint = new Color(paint.getRed(), paint.getGreen(), paint.getBlue(),
					(float) builder.backgroundOpacity() / 255.0);
		}

		border = (int) builder.border();
		fixedFontSize = builder.fontSize();

		text = builder.computedText();

		// Icon
		AwesomeIcon awesomeIcon = builder.computedIcon();

		// Text
		double fontSize = fixedFontSize == -1 ? (Math.min(bounds.getWidth(), bounds.getHeight()) / 2.25)
				: fixedFontSize;
		if (awesomeIcon != null) {
			text = awesomeIcon.toString();
			font = getIconFont();
			if (bold)
				font = Font.font(font.getFamily(), FontWeight.BOLD, fontSize);
			else
				font = Font.font(font.getFamily(), FontWeight.NORMAL, fontSize);
		} else {
			if (builder.fontName() != null && !builder.fontName().equals(""))
				font = Font.font(builder.fontName(), bold ? FontWeight.BOLD : FontWeight.NORMAL, fontSize);
			else {
				font = Font.getDefault();
				if (bold)
					font = Font.font(font.getFamily(), FontWeight.BOLD, fontSize);
				else
					font = Font.font(font.getFamily(), FontWeight.NORMAL, fontSize);
			}
		}
		bold = builder.bold();
		textPaint = getColor(builder.computedTextColor(bg));

	}

	public void draw(GraphicsContext canvas) {
		BoundingBox actualBounds = new BoundingBox(this.bounds.getMinX(), this.bounds.getMinY(), this.bounds.getWidth(),
				this.bounds.getHeight());

		// The background
		canvas.setFill(backgroundPaint);

		switch (shape) {
		case ROUNDED:
			canvas.fillRoundRect(actualBounds.getMinX(), actualBounds.getMinY(), actualBounds.getWidth() - border,
					actualBounds.getHeight(), radius, radius);
			break;
		case ROUND:
			canvas.fillOval(actualBounds.getMinX(), actualBounds.getMinY(), actualBounds.getWidth() - border,
					actualBounds.getHeight());
			break;
		default:
			canvas.fillRect(actualBounds.getMinX(), actualBounds.getMinY(), actualBounds.getWidth() - border,
					actualBounds.getHeight());
			break;
		}

		if (border > 0) {
			canvas.setLineWidth(border);
			canvas.setStroke(borderPaint);
			BoundingBox borderBounds = new BoundingBox(actualBounds.getMinX() + (border / 2),
					actualBounds.getMinY() + (border / 2), actualBounds.getWidth() - border,
					actualBounds.getHeight() - border);
			switch (shape) {
			case ROUNDED:
				canvas.strokeRoundRect(borderBounds.getMinX(), borderBounds.getMinY(), borderBounds.getWidth(),
						borderBounds.getHeight(), radius, radius);
				break;
			case ROUND:
				canvas.strokeOval(borderBounds.getMinX(), borderBounds.getMinY(), borderBounds.getWidth(),
						borderBounds.getHeight());
				break;
			default:
				canvas.rect(borderBounds.getMinX(), borderBounds.getMinY(), borderBounds.getWidth(),
						borderBounds.getHeight());
				break;
			}
		}

		canvas.setFont(font);
		canvas.setFill(textPaint);
		Bounds b = reportSize(text, font);

		canvas.fillText(text, (int) ((bounds.getWidth() - b.getWidth()) / 2d),
				(int) ((bounds.getHeight() / 2d) + (font.getSize() / 2f) - (font.getSize() * ADJUST_FACTOR)));

	}

	public Bounds reportSize(String s, Font myFont) {
		double tw = computeTextWidth(myFont, s, Short.MAX_VALUE);
		double th = computeTextHeight(myFont, s, Short.MAX_VALUE);
		return new Rectangle(0, 0, tw, th).getBoundsInLocal();
	}

	static Text helper = new Text();

	static double computeTextWidth(Font font, String text, double wrappingWidth) {
		helper.setText(text);
		helper.setFont(font);
		helper.setWrappingWidth(0);
		double w = Math.min(helper.prefWidth(-1), wrappingWidth);
		helper.setWrappingWidth((int) Math.ceil(w));
		return Math.ceil(helper.getLayoutBounds().getWidth());
	}

	static double computeTextHeight(Font font, String text, double wrappingWidth) {
		helper.setText(text);
		helper.setFont(font);
		helper.setWrappingWidth((int) wrappingWidth);
		return helper.getLayoutBounds().getHeight();

	}

	private static Color getColor(int rgb) {
		int red = (rgb >> 16) & 0x000000FF;
		int green = (rgb >> 8) & 0x000000FF;
		int blue = (rgb) & 0x000000FF;
		return new Color((double) red / 255d, (double) green / 255d, (double) blue / 255d, 1d);
	}

	private static Font getIconFont() {
		if (iconFont == null) {
			try {
				final URL resource = AwesomeIcon.class.getResource("fontawesome-webfont.ttf");
				InputStream in = resource == null ? null : resource.openStream();
				if (in == null) {
					throw new RuntimeException(
							"Could not find FontAwesome font. Make sure fontaweseome-webfont.ttf is on the CLASSPATH");
				}
				try {
					iconFont = Font.loadFont(in, 12);
					if (iconFont == null)
						throw new IOException("Font could not be loaded.");
				} finally {
					in.close();
				}
			} catch (IOException ioe) {
				throw new RuntimeException("Failed to load icon font.", ioe);
			}
		}
		return iconFont;
	}

}