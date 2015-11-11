package com.sshtools.icongenerator.java2d;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

import com.sshtools.icongenerator.IconBuilder;

public class Java2DIcon implements Icon {

	private BufferedImage image;

	public Java2DIcon(IconBuilder builder) {
		image = new BufferedImage((int)builder.width(), (int)builder.height(), BufferedImage.TYPE_4BYTE_ABGR);
		new Java2DIconCanvas(builder).draw((Graphics2D) image.getGraphics());
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.drawImage(image, x, y, null);		
	}

	public int getIconWidth() {
		return image.getWidth();
	}

	public int getIconHeight() {
		return image.getHeight();
	}

}
