package com.sshtools.icongenerator.java2d;

import javax.swing.Icon;

import com.sshtools.icongenerator.IconBuilder;
import com.sshtools.icongenerator.IconGenerator;

public class Java2DIconGenerator implements IconGenerator<Icon> {

	@Override
	public Class<Icon> getIconClass() {
		return Icon.class;
	}

	@Override
	public Icon generate(IconBuilder builder, Object... args) {
		return new Java2DIcon(builder);
	}

	@Override
	public boolean isValid() {
		/* TODO maybe need to implement this for modular Java when AWT can be missinig ?*/
		return true;
	}

}
