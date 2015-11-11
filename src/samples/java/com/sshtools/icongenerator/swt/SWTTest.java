package com.sshtools.icongenerator.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.sshtools.icongenerator.IconGenerator;
import com.sshtools.icongenerator.TestBuilder;

public class SWTTest {
	Display display = new Display();
	Shell shell = new Shell(display);

	public SWTTest() {

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 8;
		gridLayout.verticalSpacing = 8;
		gridLayout.horizontalSpacing = 8;
		gridLayout.makeColumnsEqualWidth = true;

		shell.setLayout(gridLayout);

		for (int i = 0; i < 64; i++) {
			final Canvas canvas = new Canvas(shell, SWT.NONE);
			canvas.addPaintListener(new PaintListener() {
				public void paintControl(PaintEvent e) {
					e.gc.drawImage(IconGenerator.icon(new TestBuilder(), Image.class), 0, 0);
				}
			});
			canvas.setSize(64, 64);
		}

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// If no more entries in event queue
				display.sleep();
			}
		}
		display.dispose();
	}

	public static void main(String[] args) {
		new SWTTest();
	}
}