package com.sshtools.icongenerator;

import com.sshtools.icongenerator.java2d.Java2DIcon;

public class IconGenerator {

	@SuppressWarnings("unchecked")
	public static <T> T icon(IconBuilder builder, Class<T> iconClass) {
		if(iconClass.getName().equals("javax.swing.Icon")) {
			return (T) new Java2DIcon(builder);
		}
		throw new UnsupportedOperationException("Icon's of class " + iconClass + " are not supported.");
	}
}
