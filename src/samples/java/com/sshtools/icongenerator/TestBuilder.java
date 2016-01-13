package com.sshtools.icongenerator;

import java.util.Random;

import com.sshtools.icongenerator.AwesomeIcon;
import com.sshtools.icongenerator.Colors;
import com.sshtools.icongenerator.IconBuilder;

public class TestBuilder extends IconBuilder {
	
	static Random rnd = new Random(1);

	public TestBuilder() {
		
		

		int r = (int) (rnd.nextFloat() * 3f);
		switch (r) {
		case 0:
			rect();
			break;
		case 1:
			round();
			break;
		case 2:
			roundRect((int) (rnd.nextFloat() * 12f));
			break;
		}

		String text = "";
		for(int i = 0 ; i < 1 + (int)( rnd.nextFloat() * 3); i++) {
			char ca = (char) ((short) 'a' + (short) (rnd.nextFloat() * 26));
			text += String.valueOf(ca);
		}

		if (rnd.nextFloat() > 0.5)
			textCase(TextCase.UPPER);

		if (rnd.nextFloat() > 0.5)
			bold();

		border((int) (rnd.nextFloat() * 5));

		if (rnd.nextFloat() > 0.5)
			text(text);
		else
			icon(AwesomeIcon.values()[(int) (rnd.nextFloat() * AwesomeIcon
					.values().length)]);

		color(Colors.DEFAULT.color(text));
	}
}
