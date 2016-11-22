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
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.sshtools.icongenerator.IconBuilder;
import com.sshtools.icongenerator.IconUtil;

public class Java2DIconCanvas {
	private static final float SHADE_FACTOR = 0.9f;

	/*
	 * How much to scale the 'available' space by to leave a margin around the
	 * text/icon. For example, to leave a margin that 12.5% of the total width
	 * (1/8th) around the icon, use a shrink factor of 0.75 (meaning the
	 * text/icon will be 75% of the total width).
	 * 
	 * NOTE: As a special case, the circle shape will apply a further factor of
	 * 0.75 to this.
	 */
	private static float DEFAULT_SHRINK_FACTOR = 0.75f;
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
	private float border;
	private float shrinkFactor = DEFAULT_SHRINK_FACTOR;

	public Java2DIconCanvas(IconBuilder builder) {
		bounds = new Rectangle2D.Float(0, 0, builder.width(), builder.height());

		// Configure the shape
		switch (builder.shape()) {
		case ROUNDED:
			shape = new RoundRectangle2D.Float();
			((RoundRectangle2D) shape).setRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, builder.radius(),
					builder.radius());
			break;
		case ROUND:
			shape = new Ellipse2D.Float();
			shape.setFrame(bounds);
			shrinkFactor *= 0.85f;
			break;
		default:
			shape = new Rectangle2D.Float();
			shape.setFrame(bounds);
			break;
		}
		final Color color = new Color(builder.color());
		paint = color;

		// Border
		border = builder.border();
		if (border > 0) {
			Rectangle2D.Float rect = new Rectangle2D.Float(bounds.x, bounds.y, bounds.width, bounds.height);
			rect.x += border / 2;
			rect.y += border / 2;
			rect.width -= border;
			rect.height -= border;

			// Configure the shape and create the border shape
			if (shape instanceof RoundRectangle2D) {
				borderShape = new RoundRectangle2D.Float(rect.x, rect.y, rect.width, rect.height, builder.radius(),
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
		double hack = 0;
		if (builder.icon() != null) {
			text = builder.icon().toString();
			font = getIconFont().deriveFont(builder.bold() ? Font.BOLD : Font.PLAIN,
					fixedFontSize == -1 ? (int) bounds.width : fixedFontSize);
//			hack = (bounds.height / 7D);
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

			font = new Font(builder.fontName(), builder.bold() ? Font.BOLD : Font.PLAIN, fixedFontSize == -1
					? IconUtil.pixelsToPoints((int) Math.min(bounds.width, bounds.height)) : fixedFontSize);
		}
		textStroke = new BasicStroke(Math.max(1, border));
		textPaint = new Color(builder.textColor());

		bounds.setRect(bounds.x + border, bounds.y + border, bounds.width - (border * 2), bounds.height - (border * 2) - hack);
	}

	public void draw(Graphics2D canvas) {
		canvas = (Graphics2D) canvas.create();
		try {
			canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// The background
			canvas.setPaint(paint);
			canvas.fill(shape);

			// The border
			if (borderPaint != null) {
				drawBorder(canvas);
			}

			// Translate by the border size
			canvas.translate(bounds.x, bounds.y);

			/* Create the text as glyphs and get their nature bounds */
			GlyphVector gv = font.createGlyphVector(canvas.getFontRenderContext(), text);
			Rectangle2D textBounds = gv.getPixelBounds(canvas.getFontRenderContext(), 0, 0);

			/*
			 * Calculate how much to scale by to make the text fit inside the
			 * bounding box
			 */
			float availableWidth = bounds.width * shrinkFactor;
			float availableHeight = bounds.height * shrinkFactor;
			float scaleX = 1;
			float scaleY = 1;
			if (textBounds.getWidth() > availableWidth) {
				scaleX = availableWidth / (float) textBounds.getWidth();
			}
			if (textBounds.getHeight() > availableHeight) {
				scaleY = availableHeight / (float) textBounds.getHeight();
			}
			float scale = Math.min(scaleX, scaleY);

			/*
			 * Center within the bounds using the width/height as it will be
			 * after scaling
			 */
			canvas.translate((bounds.width - (textBounds.getWidth() * scale)) / 2f,
					(bounds.height - (textBounds.getHeight() * scale)) / 2f);

			canvas.setPaint(textPaint);
			canvas.setStroke(textStroke);

			/*
			 * Use an AffineTransform rather the GC.scale to prevent having to
			 * calculate a scaled translation
			 */
			AffineTransform t = AffineTransform.getScaleInstance(scale, scale);
			AffineTransform saveAT = canvas.getTransform();
			canvas.transform(t);
			/*
			 * The text's origin is not at the bottom left of it's bounding box,
			 * so translate it so it is. This makes the above calculation
			 * clearer, if you think of the text as a box when centering /
			 * scaling it
			 */
			canvas.translate(-textBounds.getX(), textBounds.getHeight());
			canvas.fill(gv.getOutline());
			canvas.setTransform(saveAT);

		} finally {
			canvas.dispose();
		}

	}

	private static Font getIconFont() {
		if (iconFont == null) {
			try {
				final URL resource = Java2DIconCanvas.class.getResource("/fontawesome-webfont.ttf");
				InputStream in = resource == null ? null : resource.openStream();
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
		return new Color((int) (SHADE_FACTOR * color.getRed()), (int) (SHADE_FACTOR * color.getGreen()),
				(int) (SHADE_FACTOR * color.getBlue()));
	}
}