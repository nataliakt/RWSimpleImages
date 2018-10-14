package model.effect;

import model.Effect;
import model.Image;

public class PixelToGray extends Effect{
	
	public PixelToGray() {
		super("Escala de Cinza",
				"Transforma colorido para escala de cinza.",
				"P3",
				"P2");
	}
	
	@Override
	public Image effect() {
		Image img = new Image();
		img.setId(getTo());
		img.setFilePath(image.getPath() + image.getName() + img.getExtension());
		img.setWidth(image.getWidth());
		img.setHeight(image.getHeight());
		img.setMax(image.getMax());
		img.setImage(new int[img.getHeight()][img.getWidth()][1]);
		int ii = 0;
		int ij = 0;
		for(int[][] i : image.getImage()) {
			for(int [] j : i) {
				int pixel = 0;
				for(int p : j) {
					pixel += p;
				}
				pixel /= 3;
				img.setPixel(ii, ij, pixel);
				ij++;
				if (ij == img.getWidth())
					ij = 0;
			}
			ii++;
		}
		return img;
	}

}
