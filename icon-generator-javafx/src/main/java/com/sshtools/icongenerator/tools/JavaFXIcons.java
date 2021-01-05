package com.sshtools.icongenerator.tools;

import java.util.Random;

import com.sshtools.icongenerator.AwesomeIcon;
import com.sshtools.icongenerator.Colors;
import com.sshtools.icongenerator.IconBuilder;
import com.sshtools.icongenerator.IconBuilder.IconShape;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Very simple icon test. Just shows a grid of random icons. Remove the zero
 * seed from {@link JavaFXIcons#r} if you want truly random every time.
 */
public class JavaFXIcons extends Application {
	private Random r = new Random(0);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("JavaFXIcons");

		GridPane grid = new GridPane();

		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				IconBuilder ib = new IconBuilder();
				ib.theme(Colors.MATERIAL);
				ib.autoColor();
				if (r.nextFloat() > 0.5)
					ib.bold(true);
				if (r.nextFloat() > 0.5)
					ib.border((int) (r.nextFloat() * 4f));
				ib.shape(IconShape.values()[(int) (IconShape.values().length * r.nextFloat())]);
				ib.width(128);
				ib.height(128);
				ib.backgroundOpacity(128);
				if(r.nextFloat() > 0.5) {
					ib.border((int)( 1 + ( r.nextFloat() * 3 ) ));
					if(r.nextFloat() > 0.5) {
						ib.backgroundOpacity(0);
						ib.textColor(0);
					}
				}
				if (r.nextFloat() > 0.5) {
					ib.icon(AwesomeIcon.values()[(int) (AwesomeIcon.values().length * r.nextFloat())]);
					if (r.nextFloat() > 0.5)
						ib.text(randWord());
				} else
					ib.text(randWord());
				ib.fontName("Sans");
				grid.add(ib.build(Canvas.class), x, y);
			}
		}

		stage.setScene(new Scene(grid));
		stage.show();
	}

	String randWord() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 1 + (int) (r.nextFloat() * 3); i++) {
			if (r.nextFloat() > 0.5)
				b.append((char) ('a' + (int) (r.nextFloat() * 26f)));
			else
				b.append((char) ('A' + (int) (r.nextFloat() * 26f)));
		}
		return b.toString();
	}

}
