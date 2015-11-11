package com.sshtools.icongenerator;

import com.sshtools.icongenerator.java2d.Java2DIcon;
import com.sshtools.icongenerator.swt.SWTIconImageWrapper;

public class IconGenerator {

	@SuppressWarnings("unchecked")
	public static <T> T icon(IconBuilder builder, Class<T> iconClass) {
		if(iconClass.getName().equals("javax.swing.Icon")) {
			return (T) new Java2DIcon(builder);
		}
		else if(iconClass.getName().equals("org.eclipse.swt.graphics.Image")) {
			return (T) new SWTIconImageWrapper(builder).getImage();
		}
		throw new UnsupportedOperationException("Icon's of class " + iconClass + " are not supported.");
	}
}
