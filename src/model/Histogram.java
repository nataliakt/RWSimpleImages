package model;

public class Histogram {

	private int[] xMax;
	private int[] xMin;
	private int[][] histogram;

	public Histogram(Image image) {
		if (!image.isLoad()) {
			image = ImageFile.load(image.getFilePath());
			image.setLoad(true);
		}
		xMax = new int[image.getImage()[0][0].length];
		xMin = new int[image.getImage()[0][0].length];
		for (int i = 0; i < image.getImage()[0][0].length; i++) {
			xMax[i] = 0;
			xMin[i] = image.getMax();
		}
		histogram = new int[image.getMax() + 1][image.getImage()[0][0].length];
		for (int i = 0; i < histogram.length; i++) {
			for (int j = 0; j < histogram[0].length; j++) {
				histogram[i][j] = 0;
			}
		}
		for (int[][] i : image.getImage()) {
			for (int[] j : i) {
				for (int p = 0; p < image.getImage()[0][0].length; p++) {
					histogram[j[p]][p]++;
					if (xMax[p] < j[p]) {
						xMax[p] = j[p];
					}
					if (xMin[p] > j[p]) {
						xMin[p] = j[p];
					}
				}
			}
		}
	}

	public String getChannel(String channel) {
		int p = 0;
		if (channel.equals("G")) {
			p = 1;
		} else if (channel.equals("B")) {
			p = 2;
		}
		return getChannel(p);
	}

	public String getChannel(int channel) {
		String h = "";
		for (int i = 0; i < histogram.length; i++) {
			h += i + ";" + histogram[i][channel] + "\n";
		}
		return h;
	}

	public int[] getxMax() {
		return xMax;
	}

	public int[] getxMin() {
		return xMin;
	}

	public int[][] getHistogram() {
		return histogram;
	}

}
