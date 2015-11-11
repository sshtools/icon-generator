package com.sshtools.icongenerator.swt;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.sshtools.icongenerator.IconBuilder;


public class SWTIconImageWrapper {

	private Image image;

	public SWTIconImageWrapper(IconBuilder builder) {
		image = new Image(Display.getDefault(), (int)builder.width(), (int)builder.height());
		new SWTIconCanvas(builder, Display.getDefault()).draw(new GC(image));
	}
	
	public Image getImage() {
		return image;
	}
}
