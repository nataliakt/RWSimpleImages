package model.effect;

import model.Effect;
import model.Image;
import model.ImageList;
import model.Parameter;

public class ImageAddition extends Effect{
	
	public ImageAddition() {
		super("Adição de Imagem",
				"Adiciona uma imagem por cima da imagem atual.",
				"PN",
				"PN");
	}
	
	@Override
	public void setImage(Image image) {
		super.setImage(image);
		Parameter imageUp = new Parameter("image", "Selecione a imagem", ImageList.getInstance().getImage(0));
		setRequest(imageUp);
		Parameter top = new Parameter("top", "Informe a distância do topo", 0);
		Parameter left = new Parameter("left", "Informe a distância da esquerda", 0);
		setRequest(top);
		setRequest(left);
	}

	@Override
	public Image effect() {
		Image img = new Image();
		img.setFilePath(this.image.getFilePath());
		img.setId(this.image.getId());
		img.setWidth(this.image.getWidth());
		img.setHeight(this.image.getHeight());
		img.setMax(this.image.getMax());
		img.setImage(this.image.getImage());
		Image image = getParameter("image").getImage();
		int top = getParameter("top").getInteger();
		int left = getParameter("left").getInteger();
		int i = 0;
		int j = 0;

		for (int ii = top; ii < img.getImage().length; ii++) {
			if (i < image.getHeight()) {
				for (int ij = left; ij < img.getImage()[0].length; ij++) {
					if (j < image.getWidth()) {
						for (int p = 0; p < img.getImage()[0][0].length; p++) {
							img.setPixel(ii, ij, p, image.getPixel(i, j, p));
						}
						j++;
					}
				}
				i++;
				j = 0;
			}
		}
		
		return img;

	}

}
