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
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class JavaFXIconCanvas {

	private static Font iconFont;

	private Color textPaint;
	private String text;
	private BoundingBox bounds;
	private Font font;
	private int fixedFontSize;
	private boolean bold;
	private Color paint;
	private IconShape shape;
	private int radius;
	private int border;

	public JavaFXIconCanvas(IconBuilder builder) {

		bounds = new BoundingBox(0, 0, (int) builder.width(), (int) builder.height());
		radius = (int) builder.radius();
		shape = builder.computedShape();
		int bg = builder.computedColor();
		paint = getColor(bg);
		border = (int) builder.border();
		fixedFontSize = builder.fontSize();
		
		text = builder.computedText();

		// Icon
		AwesomeIcon awesomeIcon = builder.computedIcon();

		// Text
		double fontSize = fixedFontSize == -1 ? (Math.min(bounds.getWidth(), bounds.getHeight()) / 2.5) : fixedFontSize;
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
		canvas.setFill(paint);

		switch (shape) {
		case ROUNDED:
			canvas.fillRoundRect(actualBounds.getMinX(), actualBounds.getMinY(), actualBounds.getWidth(),
					actualBounds.getHeight(), radius, radius);
			break;
		case ROUND:
			canvas.fillOval(actualBounds.getMinX(), actualBounds.getMinY(), actualBounds.getWidth(),
					actualBounds.getHeight());
			break;
		default:
			canvas.fillRect(actualBounds.getMinX(), actualBounds.getMinY(), actualBounds.getWidth(),
					actualBounds.getHeight());
			break;
		}

		if (border > 0) {
			canvas.setLineWidth(border);
			canvas.setStroke(paint.darker());
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

		canvas.fillText(text, (bounds.getWidth() - b.getWidth()) / 2d, (bounds.getHeight() + b.getHeight()) / 2d);

	}

	public Bounds reportSize(String s, Font myFont) {
		Text text = new Text(s);
		text.setFont(myFont);
		Bounds tb = text.getBoundsInLocal();
		Rectangle stencil = new Rectangle(tb.getMinX(), tb.getMinY(), tb.getWidth(), tb.getHeight());

		Shape intersection = Shape.intersect(text, stencil);

		Bounds ib = intersection.getBoundsInLocal();
		return ib;
	}

	private static Color getColor(int rgb) {
		int red = (rgb >> 16) & 0x000000FF;
		int green = (rgb >>8 ) & 0x000000FF;
		int blue = (rgb) & 0x000000FF;
		return new Color((double)red / 255d, (double)green / 255d, (double)blue / 255d , 1d);
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