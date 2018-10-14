package model.effect;

import model.Effect;
import model.Image;
import model.Parameter;

public class SimpleResizePercent extends Effect {
	
	public SimpleResizePercent() {
		super("Redimencionamento Simples Proporcionalmente", "Redimenciona a imagem pela porcentagem informada, não faz média de pixels.\n"
				+ "Porcentagem positiva para aumentar e negativa para diminuir", "PN", "PN");
	}

	@Override
	public void setImage(Image image) {
		super.setImage(image);
		Parameter percent = new Parameter("percent", "Informe porcentagem positiva para aumentar e negativa para diminuir", -10);
		setRequest(percent);
	}

	@Override
	public Image effect() {
		Image img = new Image();
		img.setFilePath(image.getFilePath());
		img.setId(image.getId());
		img.setWidth(image.getWidth());
		img.setHeight(image.getHeight());
		img.setMax(image.getMax());
		img.setImage(image.getImage());

		SimpleResize r = new SimpleResize();
		r.resizeWidth(img, img.getWidth() + img.getWidth() * getParameter("percent").getInteger() / 100);
		r.resizeHeight(img, img.getHeight() + img.getHeight() * getParameter("percent").getInteger() / 100);
		
		return img;
	}

}
