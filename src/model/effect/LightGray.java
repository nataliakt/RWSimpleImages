package model.effect;

import model.Effect;
import model.Image;
import model.Parameter;

public class LightGray extends Effect {

	public LightGray() {
		super("Clarear ou Escurecer",
				"Clareia/escurece escala de cinza de acordo com a porcentagem infomada.\n"
				+ "Para escurecer informar porcentagem negativa.",
				"P2",
				"P2");
	}

	@Override
	public void setImage(Image image) {
		super.setImage(image);
		Parameter percent = new Parameter("percent", "Informe o a porcentagem, negativa para escurecer", 20);
		setRequest(percent);
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
				float prop = (float) getParameter("percent").getInteger() / 100;
				
				bit = bit + bit * prop;
				
				if (bit < 0)
					bit  = 0;
				if (bit > img.getMax())
					bit = img.getMax();
				
				img.setPixel(ii, ij, (int) bit);
				
				ij++;
				if (ij == img.getWidth())
					ij = 0;
			}
			ii++;
		}
		return img;

	}

}
