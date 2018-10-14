package model.effect;

import model.Effect;
import model.Image;
import model.Parameter;

public class InvertGray extends Effect {

	public InvertGray() {
		super("Inverter Cores",
				"Inverte escala de cinza com base no limite infomado.",
				"P2",
				"P2");
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
		img.setMax(image.getMax());
		img.setImage(new int[img.getHeight()][img.getWidth()][1]);
		int ii = 0;
		int ij = 0;
		for (int[][] i : image.getImage()) {
			for (int[] j : i) {
				float bit = j[0];
				if (j[0] < getParameter("limit").getInteger()) {
					float prop = bit / getParameter("limit").getInteger(); // Proporção atual com base no limiar
					prop = 1 - prop; // Proporção inversa
					int pixel = getParameter("limit").getInteger() + (int) (prop * (img.getMax() - getParameter("limit").getInteger())); // Cria a cor de mesma proporção
					img.setPixel(ii, ij, pixel);
				} else if (j[0] > getParameter("limit").getInteger()) {
					float prop = (bit - getParameter("limit").getInteger()) / (img.getMax() - getParameter("limit").getInteger()); // Proporção atual com base no limiar
					prop = 1 - prop; // Proporção inversa
					int pixel = (int) (prop * getParameter("limit").getInteger()); // Cria a cor de mesma proporção
					img.setPixel(ii, ij, pixel);
				} else {
					img.setPixel(ii, ij, j[0]);
				}
				
				ij++;
				if (ij == img.getWidth())
					ij = 0;
			}
			ii++;
		}
		return img;

	}

}
