package com.sshtools.icongenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Colors {

	public static Colors DEFAULT = new Colors(Arrays.asList(0xfff16364,
			0xfff58559, 0xfff9a43e, 0xffe4c62e, 0xff67bf74, 0xff59a2be,
			0xff2093cd, 0xffad62a7, 0xff805781));
	
	public static Colors MATERIAL = new Colors(Arrays.asList(0xffe57373,
			0xfff06292, 0xffba68c8, 0xff9575cd, 0xff7986cb, 0xff64b5f6,
			0xff4fc3f7, 0xff4dd0e1, 0xff4db6ac, 0xff81c784, 0xffaed581,
			0xffff8a65, 0xffd4e157, 0xffffd54f, 0xffffb74d, 0xffa1887f,
			0xff90a4ae));

	private final List<Integer> colors;
	private final Random rnd;

	public Colors(List<Integer> colorList) {
		colors = colorList;
		rnd = new Random(System.currentTimeMillis());
	}

	public int randomColor() {
		return colors.get(rnd.nextInt(colors.size()));
	}

	public int color(Object key) {
		final int fct = Math.abs(key.hashCode()) % colors.size();
		System.out.println("HASH OF " + key + " " + key.hashCode() + " = " + fct + " " + colors.size());
		return colors.get(fct);
	}
}
