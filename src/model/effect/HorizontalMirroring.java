package model.effect;

import model.Effect;
import model.Image;

public class HorizontalMirroring extends Effect {

	public HorizontalMirroring() {
		super("Espelhamento Horizontal",
				"Espelha a imagem horizontalmente.",
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
		int i = 0; // Altura
		int j = img.getWidth() - 1; // Largura
		for(int[][] ii : image.getImage()) {
			for(int [] ji : ii) {
				int p = 0;
				for(int pi : ji) {
					img.setPixel(i, j, p, pi);
					p++;
				}
				j--;
				if (j == -1)
					j = image.getWidth() - 1;
			}
			i++;
		}
		return img;
	}

}
