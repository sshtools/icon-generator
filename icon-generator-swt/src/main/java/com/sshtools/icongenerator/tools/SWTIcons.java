package com.sshtools.icongenerator.tools;

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.sshtools.icongenerator.AwesomeIcon;
import com.sshtools.icongenerator.Colors;
import com.sshtools.icongenerator.IconBuilder;
import com.sshtools.icongenerator.IconBuilder.IconShape;

/**
 * Very simple icon test. Just shows a grid of random icons. Remove the zero
 * seed from {@link SWTIcons#r} if you want truly random every time.
 */
public class SWTIcons {

	private Random r = new Random(0);

	SWTIcons(Shell shell) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 10;
		gridLayout.makeColumnsEqualWidth = true;

		shell.setLayout(gridLayout);

		for (int i = 0; i < 100; i++) {
			IconBuilder ib = new IconBuilder();
			if (r.nextFloat() > 0.5)
				ib.bold(true);
			if (r.nextFloat() > 0.5)
				ib.border((int) (r.nextFloat() * 4f));
			ib.shape(IconShape.values()[(int) (IconShape.values().length * r.nextFloat())]);
			ib.width(48);
			ib.height(48);
			if (r.nextFloat() > 0.5) {
				ib.icon(AwesomeIcon.values()[(int) (AwesomeIcon.values().length * r.nextFloat())]);
				if (r.nextFloat() > 0.5)
					ib.text(randWord());
			} else
				ib.text(randWord());
			ib.fontName("Sans");
			ib.color(Colors.MATERIAL.color(ib.text()));
			final Label label = new Label(shell, SWT.NONE);
			label.setImage(ib.build(Image.class));
		}
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

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);

		new SWTIcons(shell);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
