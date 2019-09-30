package com.sshtools.icongenerator.java2d;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

import com.sshtools.icongenerator.IconBuilder;

/**
 * A {@link Icon} implementation created from an {@link IconBuilder}.
 */
public class Java2DIcon implements Icon {

	private BufferedImage image;

	/**
	 * Construct.
	 * 
	 * @param builder builder
	 */
	public Java2DIcon(IconBuilder builder) {
		image = new BufferedImage((int)builder.width(), (int)builder.height(), BufferedImage.TYPE_INT_ARGB);
		new Java2DIconCanvas(builder).draw((Graphics2D) image.getGraphics());
	} 

	/* (non-Javadoc)
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.drawImage(image, x, y, null);		
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconWidth()
	 */
	public int getIconWidth() {
		return image.getWidth();
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconHeight()
	 */
	public int getIconHeight() {
		return image.getHeight();
	}

}
