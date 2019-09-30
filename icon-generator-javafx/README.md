# icon-generator-javafx
An add-on for the [icon-generator](https://github.com/sshtools/icon-generator) library for generating icons using JavaFX.


## Configuring your project

This add-on is available in Maven Central :-

```xml
<dependencies>
	<dependency>
		<groupId>com.sshtools</groupId>
		<artifactId>icon-generator-javafx</artifactId>
		<version>1.1.0</version>
	</dependency>
</dependencies>
```
# Generating an Icon

See the [icon-generator](https://github.com/sshtools/icon-generator) for general information on building an icon.
Then when you have an IconBuilder instance, use the following to generate
a `javafx.scene.canvas.Canvas`. 

```java
Canvas iconCanvas  = builder.build(Canvas.class);
// Do something with iconCanvas 

```
