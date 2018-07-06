# icon-generator
A library for generating custom icons in Java.

![](src/web/images/sample.png)

Generates icons image automatically from text or icons with customisable shapes, colour
and styles.

## Configuring the Icon

For this, you use [IconBuilder](src/main/java/com/sshtools/icongenerator/IconBuilder.java). 

### Create The Builder 

```java
IconBuilder builder = new IconBuilder();
```

### Sizes

```java
IconBuilder builder = new IconBuilder();
builder.width(64);
builder.height(64);
```

### Text Colours

```java

builder.textColor(0xff0000); // red

// or

build.textColor(IconBuilder.AUTO_TEXT_COLOR);

// or

build.textColor(IconBuilder.AUTO_TEXT_COLOR_WHITE);

// or

build.textColor(IconBuilder.AUTO_TEXT_COLOR_BLACK);

// and the background colour

builder.color(0x00ff00); // green
```


### Shape

```java

builder.rect(); // rectangle

// or 

builder.round(); // round
// or 

builder.rectRound(8); // rounded rectangle with 8px radius
```

### Text And Fonts

```java

builder.text("AB");
builder.font("Monospaced");
builder.fontSize(12); 
builder.bold(true);

```

### Font Awesome Icon

```java
builder.icon(AwesomeIcon.ADDRESS_BOOK);
```

## Generate The Icon

Now you can use the IconBuilder.build() for the toolkit you are using.
Currently only the Swing toolkit is supported (java.awt.BufferedImage
and javax.swing.Icon), but other toolkit implementations will be available
in later releases (SWT, JavaFX and HTML are planned).

### Generate an ImageIcon for use in Swing

```
JLabel l = new JLabel("A label with an icon");
l.setIcon(builder.build(Icon.class));
```

### Generate a Java2D BufferedImage for use in a servlet

Something like this :- 

```java
response.setContentType("image/jpeg");
BufferedImage bi = builder.build(BufferedImage.class);
OutputStream out = response.getOutputStream();
ImageIO.write(bi, "jpg", out);
out.close();

```