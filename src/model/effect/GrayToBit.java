package model.effect;

import model.Effect;
import model.Image;
import model.Parameter;

public class GrayToBit extends Effect {

	public GrayToBit() {
		super("Binário",
				"Transforma escala de cinza para binário com base no limite informado.",
				"P2",
				"P1");
	}
	
	@Override
	public void setImage(Image image) {
		super.setImage(image);
		Parameter limit = new Parameter("limit", "Informe o limite", image.getMax()/2);
		setRequest(limit);
	}

	@Override
	public Image effect() {
		Image img = new Image();
		img.setId(getTo());
		img.setFilePath(image.getPath() + image.getName() + img.getExtension());
		img.setWidth(image.getWidth());
		img.setHeight(image.getHeight());
		img.setImage(new int[img.getHeight()][img.getWidth()][1]);
		int ii = 0;
		int ij = 0;
		for (int[][] i : image.getImage()) {
			for (int[] j : i) {
				int pixel = 0;
				if (j[0] < getParameter("limit").getInteger()) {
					pixel = 1;
				}
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
