package model.effect;

import model.Effect;
import model.Image;

public class VerticalMirroring extends Effect {

	public VerticalMirroring() {
		super("Espelhamento Vertical",
				"Espelha a imagem verticalmente.",
				"PN",
				"PN");
	}

	@Override
	public Image effect() {
		Image img = new Image();
		img.setFilePath(image.getPath() + image.getName() + image.getExtension());
		img.setId(image.getId());
		img.setWidth(image.getWidth());
		img.setHeight(image.getHeight());
		img.setMax(image.getMax());
		img.setImage(new int[img.getHeight()][img.getWidth()][image.getImage()[0][0].length]);
		int i = img.getHeight() - 1; // Altura
		int j = 0; // Largura
		for(int[][] ii : image.getImage()) {
			for(int [] ji : ii) {
				int p = 0;
				for(int pi : ji) {
					img.setPixel(i, j, p, pi);
					p++;
				}
				j++;
				if (j == image.getWidth())
					j = 0;
			}
			i--;
		}
		return img;
	}

}
