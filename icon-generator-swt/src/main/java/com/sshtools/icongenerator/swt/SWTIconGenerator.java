package com.sshtools.icongenerator.swt;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.sshtools.icongenerator.IconBuilder;
import com.sshtools.icongenerator.IconGenerator;

public class SWTIconGenerator implements IconGenerator<Image> {

	@Override
	public Class<Image> getIconClass() {
		return Image.class;
	}

	@Override
	public Image generate(IconBuilder builder, Object... args) {
		final Device display = args.length == 0 ? Display.getCurrent() : (Device) args[0];
		Image img = new Image(display, (int) builder.width(), (int) builder.height());
		new SWTIconCanvas(builder, display).draw(new GC(img));
		return img;
	}

	@Override
	public boolean isValid() {
		try {
			getClass().getClassLoader().loadClass("org.eclipse.swt.graphics.Image");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
