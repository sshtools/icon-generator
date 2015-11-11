package com.sshtools.icongenerator.java2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.sshtools.icongenerator.IconBuilder;

public class Java2DIconCanvas {
	private static final float SHADE_FACTORY = 0.9f;

	private static Font iconFont;

	private Paint textPaint;
	private Paint borderPaint;
	private String text;
	private RectangularShape shape;
	private RectangularShape borderShape;
	private Rectangle2D.Float bounds;
	private Font font;
	private Stroke borderStroke;
	private Stroke textStroke;
	private int fixedFontSize;
	private Paint paint;

	public Java2DIconCanvas(IconBuilder builder) {
		bounds = new Rectangle2D.Float(0, 0, builder.width(), builder.height());

		// Configure the shape
		switch (builder.shape()) {
		case ROUNDED_RECTANGLE:
			shape = new RoundRectangle2D.Float();
			((RoundRectangle2D) shape).setRoundRect(bounds.x, bounds.y,
					bounds.width, bounds.height, builder.radius(),
					builder.radius());
			break;
		case ROUND:
			shape = new Ellipse2D.Float();
			shape.setFrame(bounds);
			break;
		default:
			shape = new Rectangle2D.Float();
			shape.setFrame(bounds);
			break;
		}
		final Color color = new Color(builder.color());
		paint = color;

		// Border
		final float border = builder.border();
		if (border > 0) {
			Rectangle2D.Float rect = new Rectangle2D.Float(bounds.x, bounds.y,
					bounds.width, bounds.height);
			rect.x += border / 2;
			rect.y += border / 2;
			rect.width -= border;
			rect.height -= border;

			// Configure the shape and create the border shape
			if (shape instanceof RoundRectangle2D) {
				borderShape = new RoundRectangle2D.Float(rect.x, rect.y,
						rect.width, rect.height, builder.radius(),
						builder.radius());
			} else if (shape instanceof Ellipse2D) {
				borderShape = new Ellipse2D.Float();
				borderShape.setFrame(rect);
			} else {
				borderShape = new Rectangle2D.Float();
				borderShape.setFrame(rect);
			}

			borderPaint = getDarker(color);
			borderStroke = new BasicStroke(border);
		}
		fixedFontSize = builder.fontSize();

		// Text
		if (builder.icon() != null) {
			text = builder.icon().toString();
			font = getIconFont().deriveFont(
					builder.bold() ? Font.BOLD : Font.PLAIN,
					(float) (fixedFontSize == -1 ? 12 : fixedFontSize));
		} else {
			text = builder.text();
			switch (builder.textCase()) {
			case LOWER:
				text = text.toLowerCase();
				break;
			case UPPER:
				text = text.toUpperCase();
				break;
			default:
				break;
			}

			font = new Font(builder.fontName(), builder.bold() ? Font.BOLD
					: Font.PLAIN, fixedFontSize == -1 ? 12 : fixedFontSize);
		}
		textStroke = new BasicStroke(Math.max(1, border));
		textPaint = new Color(builder.textColor());

	}

	public void draw(Graphics2D canvas) {
		canvas = (Graphics2D) canvas.create();
		try {
			canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			// The background
			canvas.setPaint(paint);
			canvas.fill(shape);

			// The border
			if (borderPaint != null) {
				drawBorder(canvas);
			}

			// The text
			Font actualFont = this.fixedFontSize == -1 ? new Font(
					font.getName(), font.getStyle(), (int) (Math.min(
							bounds.width, bounds.height) / 2)) : font;

			GlyphVector gv = actualFont.createGlyphVector(
					canvas.getFontRenderContext(), text);
			Rectangle2D textBounds = gv.getVisualBounds();
			canvas.setPaint(textPaint);
			canvas.setStroke(textStroke);
			canvas.translate((bounds.width - textBounds.getWidth()) / 2f,
					((bounds.height) / 2f) + (textBounds.getHeight() / 2f));
			canvas.fill(gv.getOutline());

		} finally {
			canvas.dispose();
		}

	}

	private static Font getIconFont() {
		if (iconFont == null) {
			try {
				final URL resource = Java2DIconCanvas.class
						.getResource("/fontawesome-webfont.ttf");
				InputStream in = resource == null ? null : resource
						.openStream();
				if (in == null) {
					throw new RuntimeException(
							"Could not find FontAwesome font. Make sure fontaweseome-webfont.ttf is on the CLASSPATH");
				}
				try {
					iconFont = Font.createFont(Font.TRUETYPE_FONT, in);
				} finally {
					in.close();
				}
			} catch (IOException ioe) {
				throw new RuntimeException("Failed to load icon font.", ioe);
			} catch (FontFormatException ffe) {
				throw new RuntimeException("Failed to load icon font.", ffe);
			}
		}
		return iconFont;
	}

	private void drawBorder(Graphics2D canvas) {
		Paint p = canvas.getPaint();
		Stroke s = canvas.getStroke();
		canvas.setPaint(borderPaint);
		canvas.setStroke(borderStroke);
		canvas.draw(borderShape);
		canvas.setPaint(p);
		canvas.setStroke(s);
	}

	private static Color getDarker(Color color) {
		return new Color((int) (SHADE_FACTORY * color.getRed()),
				(int) (SHADE_FACTORY * color.getGreen()),
				(int) (SHADE_FACTORY * color.getBlue()));
	}
}