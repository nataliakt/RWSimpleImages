package model.effect;

import model.Effect;
import model.Image;
import model.Parameter;

public class Rotate extends Effect {

	public Rotate() {
		super("Rotacionar",
				"Rotaciona a imagem 90º vezes o número informado.\nExemplo: 90º x 2 = 180º.",
				"PN",
				"PN");
	}
	
	@Override
	public void setImage(Image image) {
		super.setImage(image);
		Parameter rotate = new Parameter("rotate", "Informe o número de rotações a 90º", 1);
		setRequest(rotate);
	}
	
	@Override
	public Image effect() {
		Image image = new Image();
		image.setFilePath(this.image.getFilePath());
		image.setId(this.image.getId());
		image.setWidth(this.image.getWidth());
		image.setHeight(this.image.getHeight());
		image.setMax(this.image.getMax());
		image.setImage(this.image.getImage());
		if (getParameter("rotate").getInteger() < 0)
			setParameter(0, getParameter("rotate").getInteger() * -3);
		for (int e = 0; e < getParameter("rotate").getInteger(); e++) {
			Image img = new Image();
			img.setFilePath(image.getPath() + image.getName() + image.getExtension());
			img.setId(image.getId());
			img.setWidth(image.getHeight());
			img.setHeight(image.getWidth());
			img.setMax(image.getMax());
			img.setImage(new int[img.getHeight()][img.getWidth()][image.getImage()[0][0].length]);
			int i = img.getWidth() - 1; // Altura
			int j = 0; // Largura
			for(int[][] ii : image.getImage()) {
				for(int [] ji : ii) {
					int p = 0;
					for(int pi : ji) {
						img.setPixel(j, i, p, pi);
						p++;
					}
					j++;
					if (j == image.getWidth())
						j = 0;
				}
				i--;
			}
			image = img;
		}
		return image;
	}

	
}
