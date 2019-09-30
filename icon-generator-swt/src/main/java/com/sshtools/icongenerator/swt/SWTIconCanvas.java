package com.sshtools.icongenerator.swt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.sshtools.icongenerator.IconBuilder;
import com.sshtools.icongenerator.IconBuilder.IconShape;

/**
 * Helper for drawing SWT icons given an {@link IconBuilder}.
 */
public class SWTIconCanvas {
	private static final float SHADE_FACTORY = 0.9f;

	private static FontData iconFont;

	private Color textPaint;
	private String text;
	private Rectangle bounds;
	private Font font;
	private int fixedFontSize;
	private boolean bold;

	private Device display;
	private Color paint;

	private IconShape shape;

	private int radius;

	private int border;

	public SWTIconCanvas(IconBuilder builder, Device display) {
		this.display = display;

		bounds = new Rectangle(0, 0, (int) builder.width(), (int) builder.height());
		radius = (int) builder.radius();
		shape = builder.shape();
		paint = new Color(display, getColor(builder.color()));
		border = (int) builder.border();
		fixedFontSize = builder.fontSize();

		// Text
		int fontSize = fixedFontSize == -1 ? (int) (Math.min(bounds.width, bounds.height) / 2.5) : fixedFontSize;
		if (builder.icon() != null) {
			text = builder.icon().toString();
			FontData iconFont = getIconFont();
			FontData fontData = new FontData(iconFont.name, fontSize, bold ? SWT.BOLD : SWT.NONE);
			font = new Font(display, fontData);

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
			FontData iconFont = getIconFont();
			FontData fontData = new FontData(iconFont.name, fontSize, bold ? SWT.BOLD : SWT.NONE);
			font = new Font(display, fontData);
		}
		bold = builder.bold();
		textPaint = new Color(display, getColor(builder.textColor()));

	}

	public void draw(GC canvas) {
		Rectangle actualBounds = new Rectangle(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);
		try {
			canvas.setBackground(paint);
			switch (shape) {
			case ROUNDED:
				canvas.fillRoundRectangle(actualBounds.x, actualBounds.y, actualBounds.width, actualBounds.height,
						radius, radius);
				break;
			case ROUND:
				canvas.fillOval(actualBounds.x, actualBounds.y, actualBounds.width, actualBounds.height);
				break;
			default:
				canvas.fillRectangle(actualBounds);
				break;
			}

			if (border > 0) {
				canvas.setLineWidth(border);
				canvas.setForeground(new Color(display, getDarker(paint.getRGB())));
				actualBounds.x += border / 2;
				actualBounds.y += border / 2;
				actualBounds.width -= border;
				actualBounds.height -= border;
				switch (shape) {
				case ROUNDED:
					canvas.drawRoundRectangle(actualBounds.x, actualBounds.y, actualBounds.width, actualBounds.height,
							radius, radius);
					break;
				case ROUND:
					canvas.drawOval(actualBounds.x, actualBounds.y, actualBounds.width, actualBounds.height);
					break;
				default:
					canvas.drawRectangle(actualBounds);
					break;
				}
			}

			canvas.setFont(font);
			canvas.setForeground(textPaint);
			Point textLen = canvas.stringExtent(text);
			int tx = (bounds.width - textLen.x) / 2;
			int ty = (bounds.height - textLen.y) / 2;
			canvas.drawText(text, tx, ty, true);

		} finally {
			canvas.dispose();
		}

	}

	private static RGB getColor(int rgb) {
		int r = rgb >> 16 & 0x000000ff;
		int g = rgb >> 8 & 0x000000ff;
		int b = rgb & 0x000000ff;
		return new RGB(r, g, b);
	}

	private static FontData getIconFont() {
		if (iconFont == null) {
			try {
				final URL resource = SWTIconCanvas.class.getResource("/fontawesome-webfont.ttf");

				/*
				 * SWT can't always load fonts from the classpath, and provides no way of using
				 * an input stream, so we write it to a temporary file
				 */

				File tmpFont = File.createTempFile("igen", ".fnt");
				tmpFont.deleteOnExit();

				InputStream in = resource == null ? null : resource.openStream();
				if (in == null) {
					throw new RuntimeException(
							"Could not find FontAwesome font. Make sure fontaweseome-webfont.ttf is on the CLASSPATH");
				}
				try {
					OutputStream out = new FileOutputStream(tmpFont);
					byte[] buf = new byte[8096];
					try {
						int r;
						while ((r = in.read(buf, 0, buf.length)) != -1) {
							out.write(buf, 0, r);
						}
						out.flush();
					} finally {
						out.close();
					}
				} finally {
					in.close();
				}

				if (!Display.getDefault().loadFont(tmpFont.getAbsolutePath())) {
					throw new IOException("SWT refused to load font " + tmpFont);
				}

				FontData[] fd = Display.getCurrent().getFontList(null, true);
				FontData fontdata = null;
				for (int i = 0; i < fd.length; i++) {
					System.out.println(fd[i].getName());
					if (fd[i].getName().equals("FontAwesome")) {
						fontdata = fd[i];
						break;
					}
				}
				if (fontdata == null) {
					throw new RuntimeException("Could not find icon font.");
				}

				iconFont = fontdata;

			} catch (IOException ioe) {
				throw new RuntimeException("Failed to load icon font.", ioe);
			}
		}
		return iconFont;
	}

	private static RGB getDarker(RGB color) {
		return new RGB((int) (SHADE_FACTORY * color.red), (int) (SHADE_FACTORY * color.green),
				(int) (SHADE_FACTORY * color.blue));
	}
}