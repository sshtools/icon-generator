package com.sshtools.icongenerator.java2d;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sshtools.icongenerator.IconGenerator;
import com.sshtools.icongenerator.TestBuilder;

@SuppressWarnings("serial")
public class SwingTest extends JFrame {

	SwingTest() {
		super("IconGen");
		setLayout(new GridLayout(8, 8, 4, 4));
		for (int i = 0; i < 64; i++) {
			TestBuilder builder = new TestBuilder();
			builder.fontSize(48);
			add(new JLabel(IconGenerator.icon(builder, Icon.class)));
		}
	}

	public static void main(String[] args) {
		SwingTest st = new SwingTest();
		st.pack();
		st.setVisible(true);
		st.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});
	}
}
