package model.effect;

import model.Effect;
import model.Image;

public class HistogramEnhancement extends Effect {

	public HistogramEnhancement() {
		super("Realce por Manipulação de Histograma",
				"Aplica realce por manipulação de histograma utilizando a fórmula Y = A*X + B.", "PN", "PN");
	}

	@Override
	public Image effect() {
		Image img = new Image();
		img.setFilePath(image.getFilePath());
		img.setId(image.getId());
		img.setWidth(image.getWidth());
		img.setHeight(image.getHeight());
		img.setMax(image.getMax());
		img.setImage(image.getImage());

		for (int p = 0; p < img.getImage()[0][0].length; p++) {
			int A = img.getMax() / (image.getHistogram().getxMax()[p] - image.getHistogram().getxMin()[p]);
			int B = (A * -1) * image.getHistogram().getxMin()[p];

			for (int i = 0; i < image.getImage().length; i++) {
				for (int j = 0; j < image.getImage()[0].length; j++) {

					for (int c = 0; c < image.getImage()[0][0].length; c++) {
						int Y = A * image.getPixel(i, j, c) + B;
						if (Y < 0) {
							Y = 0;
						} else if (Y > img.getMax()) {
							Y = img.getMax();
						}
						img.setPixel(i, j, c, Y);
					}

				}
			}
		}

		return img;
	}

}
