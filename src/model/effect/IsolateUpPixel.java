package model.effect;

import model.Effect;
import model.Image;
import model.Parameter;

public class IsolateUpPixel extends Effect{

	public IsolateUpPixel() {
		super("Isolar Maximo",
				"Isola a cor conforme o número informado e maximiza os outros canais.\n"
				+ "Exemplo: RGB(100, 232, 123) aplicado parâmetro R = RGB(100, 255, 255).",
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
		img.setFilePath(image.getFilePath());
		img.setId(image.getId());
		img.setWidth(image.getWidth());
		img.setHeight(image.getHeight());
		img.setMax(image.getMax());
		img.setImage(new int[img.getHeight()][img.getWidth()][3]);
		int i = 0;
		int j = 0;
		for (int[][] ii : image.getImage()) {
			for (int[] ij : ii) {
				img.setPixel(i, j, 0, img.getMax());
				img.setPixel(i, j, 1, img.getMax());
				img.setPixel(i, j, 2, img.getMax());
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
