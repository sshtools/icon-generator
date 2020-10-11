module com.sshtools.icongenerator.swing {
	requires transitive com.sshtools.icongenerator.common;
	requires transitive java.desktop;
	requires transitive org.eclipse.swt.gtk.linux.x86_64;
	exports com.sshtools.icongenerator.tools;

	provides com.sshtools.icongenerator.IconGenerator with com.sshtools.icongenerator.swt.SWTIconGenerator;
}