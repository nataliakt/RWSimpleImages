package model.effect;

import model.Effect;
import model.Image;

public class PixelToGrayPixel extends Effect{
	
	public PixelToGrayPixel() {
		super("Escala de Cinza Colorido",
				"Transforma colorido para escala de cinza depois colorido novamente.",
				"P3",
				"P3");
	}
	
	@Override
	public Image effect() {
		PixelToGray ptg = new PixelToGray();
		ptg.setImage(image);
		
		GrayToPixel gtp = new GrayToPixel();
		gtp.setImage(ptg.effect());
		
		return gtp.effect();
	}
}
