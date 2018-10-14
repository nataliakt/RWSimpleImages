package model.effect;

import model.Effect;
import model.Image;

public class Decompress extends Effect {
	
	public Decompress() {
		super("Descomprimir",
				"Transforma imagem comprimida em imagem normal se já não for.\n"
				+ "Atenção: Apenas altera o ID, a descompressão é executada no método de carregamento!",
				"PNC",
				"PN-C");
	}

	@Override
	public Image effect() {
		Image i = new Image();
		i.setFilePath(image.getFilePath().substring(0, image.getFilePath().length() - 1) + "m");
		i.setId(image.getId().replace("C", ""));
		i.setWidth(image.getWidth());
		i.setHeight(image.getHeight());
		i.setMax(image.getMax());
		i.setImage(image.getImage());
		return i;
	}

}
