# icon-generator-swt
An add-on for the [icon-generator](https://github.com/sshtools/icon-generator) library for generating icons using Java Swing.


## Configuring your project

This add-on is available in Maven Central :-

```xml
<dependencies>
	<dependency>
		<groupId>com.sshtools</groupId>
		<artifactId>icon-generator-swt</artifactId>
		<version>1.4.4</version>
	</dependency>
</dependencies>
```
# Generating an Icon

See the [icon-generator](https://github.com/sshtools/icon-generator) for general information on building an icon.
Then when you have an IconBuilder instance, use the following to generate
an SWT image. 

```java
// You need to pass in an instance of Display as an argument for SWT 
Image swtImage  = builder.build(Image.class, Display.getDefault());
// Do something with swtImage 

```
