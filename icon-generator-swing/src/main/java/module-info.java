module com.sshtools.icongenerator.swing {
	requires transitive com.sshtools.icongenerator.common;
	requires transitive javafx.graphics;
	requires transitive java.desktop;

	exports com.sshtools.icongenerator.java2d;
	exports com.sshtools.icongenerator.tools;

	provides com.sshtools.icongenerator.IconGenerator with com.sshtools.icongenerator.java2d.Java2DIconGenerator,
			com.sshtools.icongenerator.java2d.Java2DBufferedImageGenerator;
}