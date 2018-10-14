package model.effect;

import model.Effect;
import model.Image;

public class Compress extends Effect {
	
	public Compress() {
		super("Comprimir",
				"Transforma imagem normal em imagem comprimida se já não for.\n"
				+ "Atenção: Apenas altera o ID, a compressão é executada no método de salvamento!",
				"PN-C",
				"PNC");
	}

	@Override
	public Image effect() {
		Image i = new Image();
		i.setFilePath(image.getFilePath().substring(0, image.getFilePath().length() - 1) + "c");
		i.setId(image.getId().replace("C", "") + "C");
		i.setWidth(image.getWidth());
		i.setHeight(image.getHeight());
		i.setMax(image.getMax());
		i.setImage(image.getImage());
		return i;
	}

}
