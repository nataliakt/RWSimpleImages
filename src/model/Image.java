package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Image {

	private String path;
	private String name;
	private String id;
	private int width;
	private int height;
	private int max;
	private int[][][] image;
	private Histogram histogram;
	private boolean load = false;

	public Image() {
		super();
	}

	public Histogram getHistogram() {
		if (histogram == null) {
			histogram = new Histogram(this);
		}
		return histogram;
	}

	public String getExtension() {
		switch (id) {
		case "P1":
			return ".pbm";
		case "P2":
			return ".pgm";
		case "P3":
			return ".ppm";
		case "P1C":
			return ".pbc";
		case "P2C":
			return ".pgc";
		case "P3C":
			return ".ppc";
		default:
			return ".pnm";
		}
	}

	public String getFilePath() {
		return path + name + getExtension();
	}

	public void setFilePath(String path) {
		String name = path.split("/")[path.split("/").length - 1];
		this.path = path.substring(0, path.length() - name.length());
		this.name = name.substring(0, name.length() - 4);
	}

	public StringProperty getNameProperty() {
		return new SimpleStringProperty(getName());
	}

	public StringProperty getIdProperty() {
		return new SimpleStringProperty(id);
	}

	public StringProperty getWidthProperty() {
		return new SimpleStringProperty(Integer.toString(width));
	}

	public StringProperty getHeightProperty() {
		return new SimpleStringProperty(Integer.toString(height));
	}

	public String getPath() {
		return path;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getMax() {
		return max;
	}

	public int[][][] getImage() {
		return image;
	}

	public int getPixel(int i, int j) {
		return image[i][j][0];
	}

	public int getPixel(int i, int j, int c) {
		return image[i][j][c];
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void setImage(int[][][] image) {
		this.image = image;
	}

	public void setPixel(int i, int j, int pixel) {
		this.image[i][j][0] = pixel;
	}

	public void setPixel(int i, int j, int c, int pixel) {
		this.image[i][j][c] = pixel;
	}

	public boolean isLoad() {
		return load;
	}

	public void setLoad(boolean load) {
		this.load = load;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Nome: ");
		builder.append(name);
		builder.append("\nFormato: ");
		builder.append(id);
		builder.append("\nLargura: ");
		builder.append(width);
		builder.append("\nAltura: ");
		builder.append(height);
		builder.append("\nMáximo: ");
		builder.append(max);
		builder.append("\nBits: \n");
		for (int[][] i : image) {
			for (int[] j : i) {
				if (!id.contains("P3")) {
					builder.append(j[0]);
					builder.append(" ");
				} else {
					for (int p : j) {
						builder.append(p);
						builder.append(" ");
					}
					builder.append("  ");
				}
			}
			builder.append("\n");
		}
		return builder.toString();
	}

}
