package model.effect;

import model.Effect;
import model.Image;
import model.Parameter;

public class SimpleResize extends Effect {

	public SimpleResize() {
		super("Redimencionamento Simples", "Redimenciona a imagem pelo tamanho selecionado, não faz média de pixels.",
				"PN", "PN");
	}

	@Override
	public void setImage(Image image) {
		super.setImage(image);
		Parameter width = new Parameter("width", "Informe a largura", 480);
		Parameter height = new Parameter("height", "Informe a altura", 320);
		setRequest(width);
		setRequest(height);
	}

	@Override
	public Image effect() {
		int width = getParameter("width").getInteger();
		int height = getParameter("height").getInteger();
//		switch (getParam()) {
//		case 1:
//			width = 480;
//			height = 320;
//			break;
//		case 2:
//			width = 720;
//			height = 480;
//			break;
//		case 3:
//			width = 1280;
//			height = 720;
//			break;
//		case 4:
//			width = 1920;
//			height = 1080;
//			break;
//		case 5:
//			width = 4096;
//			height = 2160;
//			break;
//		case 6:
//			width = 7680;
//			height = 4320;
//			break;
//		}
		Image img = new Image();
		img.setFilePath(image.getFilePath());
		img.setId(image.getId());
		img.setWidth(image.getWidth());
		img.setHeight(image.getHeight());
		img.setMax(image.getMax());
		img.setImage(image.getImage());

		resizeHeight(img, height);
		resizeWidth(img, width);

		return img;
	}

	public void resizeHeight(Image image, int height) {
		int[][][] arrayHeight = new int[height][image.getWidth()][image.getImage()[0][0].length];

		if (height > image.getHeight()) {
			int iProp = height / image.getHeight() + 1;
			for (int j = 0; j < image.getWidth(); j++) {
				int iNew = 0;
				for (int i = 0; i < image.getHeight(); i++) {
					int pixel[] = new int[image.getImage()[i][j].length];
					for (int ip = 0; ip < image.getImage()[i][j].length; ip++) {
						pixel[ip] = image.getImage()[i][j][ip];
					}
					for (int x = 0; x < iProp; x++) {
						if (iNew < height) {
							for (int p = 0; p < image.getImage()[0][0].length; p++) {
								arrayHeight[iNew][j][p] = pixel[p];
							}
							iNew++;
						}
					}
				}
			}
			image.setImage(arrayHeight);
			image.setHeight(height);
		} else if (height < image.getHeight()) {
			int iProp = image.getHeight() / height;
			for (int j = 0; j < image.getWidth(); j++) {
				int iOld = 0;
				for (int i = 0; i < height; i++) {
					if (iOld < image.getHeight()) {
						for (int p = 0; p < image.getImage()[0][0].length; p++) {
							arrayHeight[i][j][p] = image.getImage()[iOld][j][p];
						}
						iOld += iProp;
					}
				}
			}
			image.setImage(arrayHeight);
			image.setHeight(height);
		}
	}

	public void resizeWidth(Image image, int width) {
		int[][][] arrayWidth = new int[image.getHeight()][width][image.getImage()[0][0].length];

		if (width > image.getWidth()) {
			int jProp = width / image.getWidth() + 1;
			for (int i = 0; i < image.getHeight(); i++) {
				int jNew = 0;
				for (int j = 0; j < image.getWidth(); j++) {
					int pixel[] = new int[image.getImage()[i][j].length];
					for (int ip = 0; ip < image.getImage()[i][j].length; ip++) {
						pixel[ip] = image.getImage()[i][j][ip];
					}
					for (int x = 0; x < jProp; x++) {
						if (jNew < width) {
							for (int p = 0; p < image.getImage()[0][0].length; p++) {
								arrayWidth[i][jNew][p] = pixel[p];
							}
							jNew++;
						}
					}
				}
			}
			image.setImage(arrayWidth);
			image.setWidth(width);
		} else if (width < image.getWidth()) {
			int jProp = image.getWidth() / width;
			for (int i = 0; i < image.getHeight(); i++) {
				int jOld = 0;
				for (int j = 0; j < width; j++) {
					if (jOld < image.getWidth()) {
						for (int p = 0; p < image.getImage()[0][0].length; p++) {
							arrayWidth[i][j][p] = image.getImage()[i][jOld][p];
						}
						jOld += jProp;
					}
				}
			}
			image.setImage(arrayWidth);
			image.setWidth(width);
		}
	}

}
