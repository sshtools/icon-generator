package com.sshtools.icongenerator.javafx;

import com.sshtools.icongenerator.IconBuilder;
import com.sshtools.icongenerator.IconGenerator;

import javafx.scene.canvas.Canvas;

/**
 * Icon generator for Java. Produces a {@link Canvas}
 */
public class JavaFXCanvasGenerator implements IconGenerator<Canvas> {

	@Override
	public Class<Canvas> getIconClass() {
		return Canvas.class;
	}

	@Override
	public Canvas generate(IconBuilder builder, Object... args) {
		JavaFXIconCanvas iconCanvas = new JavaFXIconCanvas(builder);
		Canvas canvas = new Canvas(builder.width(), builder.height());
		iconCanvas.draw(canvas.getGraphicsContext2D());
		return canvas;
	}

}
