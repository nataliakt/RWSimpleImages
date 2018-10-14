package model.effect;

import model.Effect;
import model.Image;
import model.Parameter;

public class ComplexResize extends Effect {

	public ComplexResize() {
		super("Redimencionamento Complexo", "Redimenciona a imagem pelo tamanho selecionado, faz média dos pixels.",
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
					int pixel[] = new int[image.getImage()[0][0].length];
					int nextPixel[] = new int[image.getImage()[0][0].length];
					if (i + 1 < image.getHeight()) {
						for (int ip = 0; ip < image.getImage()[0][0].length; ip++) {
							pixel[ip] = image.getImage()[i][j][ip];
							nextPixel[ip] = image.getImage()[i + 1][j][ip];
							nextPixel[ip] = (pixel[ip] - nextPixel[ip]) / iProp;
						}
					} else {
						for (int ip = 0; ip < image.getImage()[0][0].length; ip++) {
							pixel[ip] = image.getImage()[i][j][ip];
							nextPixel[ip] = image.getImage()[i - 1][j][ip];
							nextPixel[ip] = (pixel[ip] - nextPixel[ip]) / iProp;
						}
					}
					for (int x = 0; x < iProp; x++) {
						if (iNew < height) {
							for (int p = 0; p < image.getImage()[0][0].length; p++) {
								arrayHeight[iNew][j][p] = pixel[p];
								pixel[p] -= nextPixel[p];
							}
							iNew++;
						}
					}
				}
			}
			image.setImage(arrayHeight);
			image.setHeight(height);
		} else if (height < image.getHeight()){
			int iProp = image.getHeight() / height;
			for (int j = 0; j < image.getWidth(); j++) {
				int iOld = 0;
				for (int i = 0; i < height; i++) {
					int pixel[] = new int[image.getImage()[0][0].length];
					int count = 0;
					for (int y = 0; y < iProp; y++) {
						if (iOld < image.getHeight()) {
							for (int p = 0; p < image.getImage()[0][0].length; p++) {
								pixel[p] += image.getImage()[iOld][j][p];
							}
							count++;
							iOld++;
						}
					}
					if (count != 0) {
						for (int p = 0; p < image.getImage()[0][0].length; p++) {
							arrayHeight[i][j][p] = pixel[p] / count;
						}
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
					int pixel[] = new int[image.getImage()[0][0].length];
					int nextPixel[] = new int[image.getImage()[0][0].length];
					if (j + 1 < image.getWidth()) {
						for (int ip = 0; ip < image.getImage()[0][0].length; ip++) {
							pixel[ip] = image.getImage()[i][j][ip];
							nextPixel[ip] = image.getImage()[i][j + 1][ip];
							nextPixel[ip] = (pixel[ip] - nextPixel[ip]) / jProp;
						}
					} else {
						for (int ip = 0; ip < image.getImage()[0][0].length; ip++) {
							pixel[ip] = image.getImage()[i][j][ip];
							nextPixel[ip] = image.getImage()[i][j - 1][ip];
							nextPixel[ip] = (pixel[ip] - nextPixel[ip]) / jProp;
						}
					}
					for (int x = 0; x < jProp; x++) {
						if (jNew < width) {
							for (int p = 0; p < image.getImage()[0][0].length; p++) {
								arrayWidth[i][jNew][p] = pixel[p];
								pixel[p] -= nextPixel[p];
							}
							jNew++;
						}
					}
				}
			}
			image.setImage(arrayWidth);
			image.setWidth(width);
		} else if (width < image.getWidth()){
			int jProp = image.getWidth() / width;
			for (int i = 0; i < image.getHeight(); i++) {
				int jOld = 0;
				for (int j = 0; j < width; j++) {
					int pixel[] = new int[image.getImage()[0][0].length];
					int count = 0;
					for (int x = 0; x < jProp; x++) {
						if (jOld < image.getWidth()) {
							for (int p = 0; p < image.getImage()[0][0].length; p++) {
								pixel[p] += image.getImage()[i][jOld][p];
							}
							count++;
							jOld++;
						}
					}
					if (count != 0) {
						for (int p = 0; p < image.getImage()[0][0].length; p++) {
							arrayWidth[i][j][p] = pixel[p] / count;
						}
					}
				}
			}
			image.setImage(arrayWidth);
			image.setWidth(width);
		}
	}

}
