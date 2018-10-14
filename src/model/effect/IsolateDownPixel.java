package model.effect;

import model.Effect;
import model.Image;
import model.Parameter;

public class IsolateDownPixel extends Effect{

	public IsolateDownPixel() {
		super("Isolar Mínimo",
				"Isola a cor conforme o número informado e zera os outros canais.\n"
				+ "Exemplo: RGB(100, 232, 123) aplicado parâmetro R = RGB(100, 0, 0).",
				"P3",
				"P3");
	}

	@Override
	public void setImage(Image image) {
		super.setImage(image);
		Parameter channel = new Parameter("channel", "Informe R, G ou B", "R");
		setRequest(channel);
	}
	
	@Override
	public Image effect() {
		String channel = getParameter("channel").getString().toUpperCase();
		if (!channel.equals("R") && !channel.equals("G") && !channel.equals("B"))
			return null;
		Image img = new Image();
		img.setId(getTo());
		img.setFilePath(image.getPath() + image.getName() + img.getExtension());
		img.setWidth(image.getWidth());
		img.setHeight(image.getHeight());
		img.setMax(image.getMax());
		img.setImage(new int[img.getHeight()][img.getWidth()][3]);
		int i = 0;
		int j = 0;
		for (int[][] ii : image.getImage()) {
			for (int[] ij : ii) {
				img.setPixel(i, j, 0, 0);
				img.setPixel(i, j, 1, 0);
				img.setPixel(i, j, 2, 0);
				if (channel.equals("R"))
					img.setPixel(i, j, 0, ij[0]);
				if (channel.equals("G"))
					img.setPixel(i, j, 1, ij[1]);
				if (channel.equals("B"))
					img.setPixel(i, j, 2, ij[2]);
				j++;
				if (j == img.getWidth())
					j = 0;
			}
			i++;
		}
		return img;
	}

}
