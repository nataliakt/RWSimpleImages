package model.effect;

import model.Effect;
import model.Image;

public class GrayToPixel extends Effect{
	
	public GrayToPixel() {
		super("Colorido",
				"Transforma escala de cinza para colorido.\nNote que a imagem terá a mesma aparência de antes, apenas alterando a estrutura interna.",
				"P2",
				"P3");
	}
	
	@Override
	public Image effect() {
		Image img = new Image();
		img.setId(getTo());
		img.setFilePath(image.getPath() + image.getName() + img.getExtension());
		img.setWidth(image.getWidth());
		img.setHeight(image.getHeight());
		img.setMax(image.getMax());
		img.setImage(new int[img.getHeight()][img.getWidth()][3]);
		int ii = 0;
		int ij = 0;
		for(int[][] i : image.getImage()) {
			for(int [] j : i) {
				img.setPixel(ii, ij, 0, j[0]);
				img.setPixel(ii, ij, 1, j[0]);
				img.setPixel(ii, ij, 2, j[0]);
				
				ij++;
				if (ij == img.getWidth())
					ij = 0;
			}
			ii++;
		}
		return img;
	}
}
