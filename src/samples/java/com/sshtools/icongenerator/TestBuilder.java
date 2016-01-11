package com.sshtools.icongenerator;

import com.sshtools.icongenerator.AwesomeIcon;
import com.sshtools.icongenerator.Colors;
import com.sshtools.icongenerator.IconBuilder;

public class TestBuilder extends IconBuilder {

	public TestBuilder() {

		int r = (int) (Math.random() * 3f);
		switch (r) {
		case 0:
			rect();
			break;
		case 1:
			round();
			break;
		case 2:
			roundRect((int) (Math.random() * 12f));
			break;
		}

		String text = "";
		for(int i = 0 ; i < 1 + (int)( Math.random() * 3); i++) {
			char ca = (char) ((short) 'a' + (short) (Math.random() * 26));
			text += String.valueOf(ca);
		}

		if (Math.random() > 0.5)
			textCase(TextCase.UPPER);

		if (Math.random() > 0.5)
			bold();

		border((int) (Math.random() * 5));

		if (Math.random() > 0.5)
			text(text);
		else
			icon(AwesomeIcon.values()[(int) (Math.random() * AwesomeIcon
					.values().length)]);

		color(Colors.DEFAULT.color(text));
	}
}
