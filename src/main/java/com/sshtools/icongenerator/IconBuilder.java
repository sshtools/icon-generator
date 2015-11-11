package com.sshtools.icongenerator;

public class IconBuilder {

	public enum IconShape {
		RECTANGLE, ROUND, ROUNDED_RECTANGLE
	}

	public enum TextCase {
		UPPER, LOWER, ORIGINAL
	}

	private IconShape shape = IconShape.RECTANGLE;
	private String text;
	private float borderThickness;
	private float height, width;
	private String fontName;
	private int textColor;
	private int fixedFontSize;
	private boolean isBold;
	private TextCase textCase;
	private float radius;
	private int color;
	private AwesomeIcon icon;

	public IconBuilder() {
		text = "";
		textColor = 0x00ffffff;
		borderThickness = 0;
		width = 64;
		height = 64;
		fontName = "sans-serif-light";
		fixedFontSize = -1;
		isBold = false;
		textCase = TextCase.ORIGINAL;
	}
	
	public AwesomeIcon icon() {
		return icon;
	}
	
	public IconBuilder icon(AwesomeIcon icon) {
		this.icon = icon;
		return this;
	}

	public float width() {
		return width;
	}

	public IconBuilder width(float width) {
		this.width = width;
		return this;
	}

	public IconBuilder height(float height) {
		this.height = height;
		return this;
	}

	public float height() {
		return height;
	}

	public IconBuilder textColor(int color) {
		this.textColor = color;
		return this;
	}

	public int textColor() {
		return textColor;
	}

	public IconBuilder border(float thickness) {
		this.borderThickness = thickness;
		return this;
	}

	public float border() {
		return borderThickness;
	}

	public IconBuilder fontName(String font) {
		this.fontName = font;
		return this;
	}

	public String fontName() {
		return fontName;
	}

	public IconBuilder fontSize(int size) {
		this.fixedFontSize = size;
		return this;
	}

	public int fontSize() {
		return fixedFontSize;
	}

	public IconBuilder bold(boolean bold) {
		this.isBold = bold;
		return this;
	}

	public boolean bold() {
		return isBold;
	}

	public float radius() {
		return radius;
	}

	public IconBuilder textCase(TextCase textCase) {
		this.textCase = textCase;
		return this;
	}

	public TextCase textCase() {
		return textCase;
	}

	public IconBuilder shape(IconShape shape) {
		this.shape = shape;
		return this;
	}

	public IconShape shape() {
		return shape;
	}

	public IconBuilder rect() {
		this.shape = IconShape.RECTANGLE;
		return this;
	}

	public IconBuilder round() {
		this.shape = IconShape.ROUND;
		return this;
	}

	public IconBuilder roundRect(int radius) {
		this.shape = IconShape.ROUNDED_RECTANGLE;
		this.radius = radius;
		return this;
	}

	public IconBuilder text(String text) {
		this.text = text;
		return this;
	}

	public String text() {
		return text;
	}

	public int color() {
		return color;
	}

	public IconBuilder color(int color) {
		this.color = color;
		return this;
	}

}