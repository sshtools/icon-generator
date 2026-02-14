package com.sshtools.icongenerator.javafx;

import com.sshtools.icongenerator.IconBuilder;
import com.sshtools.icongenerator.IconGenerator;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

/**
 * Icon generator for Java. Produces a {@link Canvas}
 */
public class JavaFXImageGenerator implements IconGenerator<Image> {

	@Override
	public Class<Image> getIconClass() {
		return Image.class;
	}

	@Override
	public Image generate(IconBuilder builder, Object... args) {
		JavaFXIconCanvas iconCanvas = new JavaFXIconCanvas(builder);
		Canvas canvas = new Canvas(builder.width(), builder.height());
		iconCanvas.draw(canvas.getGraphicsContext2D());
		return canvas.snapshot(null, null);
	}

	@Override
	public boolean isValid() {
		try {
			getClass().getClassLoader().loadClass("javafx.scene.image.Image");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
