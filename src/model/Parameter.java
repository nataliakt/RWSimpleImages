package model;

public class Parameter {

	private String name;
	
	private String type; // image, int, string
	private String request;

	private int integer;
	private String string;
	private Image image;

	private int integerDefault;
	private String stringDefault;
	private Image imageDefault;

	public Parameter(String name, String request, int integerDefault) {
		super();
		this.name = name;
		this.type = "integer";
		this.request = request;
		this.integerDefault = integerDefault;
	}

	public Parameter(String name, String request, String stringDefault) {
		super();
		this.name = name;
		this.type = "string";
		this.request = request;
		this.stringDefault = stringDefault;
	}

	public Parameter(String name, String request, Image imageDefault) {
		super();
		this.name = name;
		this.type = "image";
		this.request = request;
		this.imageDefault = imageDefault;
	}

	public String getType() {
		return type;
	}

	public String getRequest() {
		return request;
	}

	public int getInteger() {
		return integer;
	}

	public String getString() {
		return string;
	}

	public Image getImage() {
		return image;
	}

	public int getIntegerDefault() {
		return integerDefault;
	}

	public String getStringDefault() {
		return stringDefault;
	}

	public Image getImageDefault() {
		return imageDefault;
	}

	public String getValue() {
		switch (type) {
			case "image":
				return image.getName() + image.getExtension();
			case "string":
				return string;
			case "integer":
				return Integer.toString(integer);
			default:
				return "";
		}
	}

	public String getDefaultValue() {
		switch (type) {
			case "image":
				return imageDefault.getName() + imageDefault.getExtension();
			case "string":
				return stringDefault;
			case "integer":
				return Integer.toString(integerDefault);
			default:
				return "";
		}
	}
	
	public void setValue(int integer) {
		this.integer = integer;
	}

	public void setValue(String string) {
		this.string = string;
	}

	public void setValue(Image image) {
		this.image = image;
	}

	public void setDefault(int integerDefault) {
		this.integerDefault = integerDefault;
	}

	public void setDefault(String stringDefault) {
		this.stringDefault = stringDefault;
	}

	public void setDefault(Image imageDefault) {
		this.imageDefault = imageDefault;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
