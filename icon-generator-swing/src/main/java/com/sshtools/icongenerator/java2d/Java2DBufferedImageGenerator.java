package com.sshtools.icongenerator.java2d;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.sshtools.icongenerator.IconBuilder;
import com.sshtools.icongenerator.IconGenerator;

/**
 * Generator for {@link BufferedImage}.
 */
public class Java2DBufferedImageGenerator implements IconGenerator<BufferedImage> {

	@Override
	public Class<BufferedImage> getIconClass() {
		return BufferedImage.class;
	}

	@Override
	public BufferedImage generate(IconBuilder builder, Object... args) {
		BufferedImage bim = new BufferedImage((int)builder.width(),(int)builder.height(), args.length == 0 ? BufferedImage.TYPE_INT_ARGB : (Integer)args[0]);
		Java2DIconCanvas c = new Java2DIconCanvas(builder);
		c.draw((Graphics2D)bim.getGraphics());
		return bim;
	}

	@Override
	public boolean isValid() {
		try {
			getClass().getClassLoader().loadClass("java.awt.image.BufferedImage");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
