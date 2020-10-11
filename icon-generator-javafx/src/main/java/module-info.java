
module com.sshtools.icongenerator.javafx {
	requires transitive com.sshtools.icongenerator.common;
	requires transitive javafx.graphics;
    exports com.sshtools.icongenerator.javafx;
    exports com.sshtools.icongenerator.tools;
    provides com.sshtools.icongenerator.IconGenerator with com.sshtools.icongenerator.javafx.JavaFXCanvasGenerator;
}