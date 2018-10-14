package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ImageFile {

	public static final String ROOT = System.getProperty("user.dir").replace('\\', '/') + "/src/images/";
	public static final String TEMPFILE = System.getProperty("user.dir").replace('\\', '/')
			+ "/src/images/temp/temp.pnm";

	public static void saveTemp(Image image) {
		Image i = new Image();
		i.setFilePath(image.getFilePath());
		i.setId(image.getId().replace("C", ""));
		i.setWidth(image.getWidth());
		i.setHeight(image.getHeight());
		i.setMax(image.getMax());
		i.setImage(image.getImage());
		save(i, TEMPFILE);
	}

	public static void save(Image image) {
		save(image, ROOT + image.getName() + image.getExtension());
	}

	public static void save(Image image, String path) {
		if (image.getId().contains("C")) {
			saveCompress(image, path);
		} else {
			try {
				FileWriter fw = new FileWriter(path, false);
				try {
					fw.write(image.getId() + "\n#Gerado por RW Bit Images de Natalia Kelim Thiel\n"
							+ Integer.toString(image.getWidth()) + " " + Integer.toString(image.getHeight()) + "\n");
					if (!image.getId().contains("P1")) {
						fw.write(Integer.toString(image.getMax()) + "\n");
					}
					for (int[][] i : image.getImage()) {
						for (int[] j : i) {
							for (int p : j) {
								fw.write(Integer.toString(p) + " ");
							}
						}
					}
				} catch (Exception e) {
					System.out.println("Erro ao salvar!");
				}
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void saveCompress(Image image, String path) {
		path = path.substring(0, path.length() - 1) + "c";
		try {
			FileWriter fw = new FileWriter(path, false);
			try {
				fw.write(image.getId() + "\n#Gerado e comprimido por RW Bit Images de Natalia Kelim Thiel\n"
						+ Integer.toString(image.getWidth()) + " " + Integer.toString(image.getHeight()) + "\n");
				if (!image.getId().contains("P1")) {
					fw.write(Integer.toString(image.getMax()) + "\n");
				}

				int pLenght = image.getImage()[0][0].length - 1;

				int[] times = { 1, 1, 1 };
				int[] diffs = { 1, 1, 1 };
				ArrayList<String> builder = new ArrayList<String>();
				for (int i = image.getHeight() - 1; i >= 0; i--) {
					for (int j = image.getWidth() - 1; j >= 0; j--) {
						for (int c = pLenght; c >= 0; c--) {
							int p = image.getPixel(i, j, c);
							int pOld = -1;
							int jOld = j - 1;
							int iOld = i;
							if (jOld < 0) {
								jOld = image.getImage()[0].length - 1;
								iOld--;
							}
							try {
								pOld = image.getPixel(iOld, jOld, c);
							} catch (Exception e) {

							}
							if (p == pOld) {
								times[c]++;
								diffs[c] = 1;
							} else {
								builder.add(p + " ");
								if (times[c] > 1) {
									builder.add("*" + times[c] + " ");
								}
								times[c] = 1;
								diffs[c]++;
							}
						}
					}
				}
				for (int i = builder.size() - 1; i >= 0; i--) {
					fw.write(builder.get(i));
				}
			} catch (Exception e) {
				System.out.println("Erro ao salvar!");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public static boolean remove(Image image) {
		File f = new File(image.getFilePath());
		return f.delete();
	}

	public static void load() {
		ImageList.getInstance().clear();
		File dirImages = new File(ROOT);
		for (File file : dirImages.listFiles()) {
			if (file.isFile()) {
				Image i = loadSmall(file.getAbsolutePath());
				ImageList.getInstance().addImage(i);
			}
		}
	}

	public static Image loadSmall(String path) {
		return load(path, 0);
	}

	public static Image load(String path) {
		return load(path, 1);
	}

	private static Image load(String path, int format) {
		if (path.substring(path.length() - 1).equals("c") && format != 0) {
			return loadCompress(path);
		} else {
			try {
				Image image = new Image();
				image.setFilePath(path);

				int status = 0;
				int i = 0;
				int j = 0;
				int p = 0;
				String info = "";
				boolean load = false;
				boolean comment = false;
				boolean run = true;
				char c;

				BufferedReader br = new BufferedReader(new FileReader(path));

				while (br.ready() && run) {
					c = (char) br.read();
					if (c == '#' || (c != '\n' && comment)) {
						comment = true;
					} else {
						comment = false;
						switch (status) {
						// ID
						case 0:
							if (!Character.isWhitespace(c)) {
								load = true;
								info += c;
							} else {
								if (load) {
									load = false;
									image.setId(info);
									info = "";
									status++;
								}
							}
							break;
						// Width
						case 1:
							if (!Character.isWhitespace(c)) {
								load = true;
								info += c;
							} else {
								if (load) {
									load = false;
									image.setWidth(Integer.parseInt(info));
									info = "";
									status++;
								}
							}
							break;
						// Height
						case 2:
							if (!Character.isWhitespace(c)) {
								load = true;
								info += c;
							} else {
								if (load) {
									load = false;
									image.setHeight(Integer.parseInt(info));
									if (image.getId().contains("P3")) {
										image.setImage(new int[image.getHeight()][image.getWidth()][3]);
									} else {
										image.setImage(new int[image.getHeight()][image.getWidth()][1]);
									}
									info = "";
									status++;
								}
							}
							break;
						// Max
						case 3:
							if (image.getId().contains("P1")) {
								image.setMax(1);
								status++;
							} else {
								if (!Character.isWhitespace(c)) {
									load = true;
									info += c;
								} else {
									if (load) {
										load = false;
										image.setMax(Integer.parseInt(info));
										info = "";
										status++;
									}
								}
								break;
							}
							// Array
						case 4:
							if (format == 0) {
								run = false;
								break;
							}
							if (!Character.isWhitespace(c)) {
								load = true;
								info += c;
							} else {
								if (load) {
									load = false;
									if (j == image.getWidth()) {
										i++;
										j = 0;
									}
									if (image.getId().contains("P3")) {
										image.setPixel(i, j, p, Integer.parseInt(info));
										p++;
										if (p == 3) {
											p = 0;
											j++;
										}
									} else {
										image.setPixel(i, j, Integer.parseInt(info));
										j++;
									}
									info = "";
								}
							}
							break;
						}
					}
				}
				br.close();
				image.setFilePath(ROOT + image.getName() + image.getExtension());
				return image;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	private static Image loadCompress(String path) {
		try {
			Image image = new Image();
			image.setFilePath(path);

			int status = 0;
			int i[] = { 0, 0, 0 };
			int j[] = { 0, 0, 0 };
			int p = 0;
			String info = "";
			boolean load = false;
			boolean comment = false;
			boolean run = true;
			char c;
			boolean[] waitTimes = { false, false, false };
			int[] times = { 1, 1, 1 };
			int[] timesRest = { 1, 1, 1 };

			BufferedReader br = new BufferedReader(new FileReader(path));

			while (br.ready() && run) {
				c = (char) br.read();
				if (c == '#' || (c != '\n' && comment)) {
					comment = true;
				} else {
					comment = false;
					switch (status) {
					// ID
					case 0:
						if (!Character.isWhitespace(c)) {
							load = true;
							info += c;
						} else {
							if (load) {
								load = false;
								image.setId(info);
								info = "";
								status++;
							}
						}
						break;
					// Width
					case 1:
						if (!Character.isWhitespace(c)) {
							load = true;
							info += c;
						} else {
							if (load) {
								load = false;
								image.setWidth(Integer.parseInt(info));
								info = "";
								status++;
							}
						}
						break;
					// Height
					case 2:
						if (!Character.isWhitespace(c)) {
							load = true;
							info += c;
						} else {
							if (load) {
								load = false;
								image.setHeight(Integer.parseInt(info));
								if (image.getId().contains("P3")) {
									image.setImage(new int[image.getHeight()][image.getWidth()][3]);
								} else {
									image.setImage(new int[image.getHeight()][image.getWidth()][1]);
								}
								info = "";
								status++;
							}
						}
						break;
					// Max
					case 3:
						if (image.getId().contains("P1")) {
							image.setMax(1);
							status++;
						} else {
							if (!Character.isWhitespace(c)) {
								load = true;
								info += c;
							} else {
								if (load) {
									load = false;
									image.setMax(Integer.parseInt(info));
									info = "";
									status++;
								}
							}
							break;
						}
						// Array
					case 4:
						if (!Character.isWhitespace(c)) {
							load = true;
							if (info.isEmpty() && c == '*') {
								waitTimes[p] = true;
							} else {
								info += c;
							}
						} else {
							if (load) {
								load = false;
								if (waitTimes[p]) {
									times[p] = Integer.parseInt(info);
									timesRest[p] = Integer.parseInt(info);
									waitTimes[p] = false;
								} else {
									for (int t = 0; t < times[p]; t++) {
										if (j[p] == image.getWidth()) {
											i[p]++;
											j[p] = 0;
										}
										if (image.getId().contains("P3")) {
											image.setPixel(i[p], j[p], p, Integer.parseInt(info));
										} else {
											image.setPixel(i[p], j[p], Integer.parseInt(info));
										}
										j[p]++;
									}
									times[p] = 1;
									do {
										p++;
										if (p == image.getImage()[0][0].length) {
											p = 0;
										}
										timesRest[p]--;
									} while (timesRest[p] > 0);
									if (timesRest[p] == 0) {
										timesRest[p] = 1;
									}
								}
								info = "";
							}
						}
						break;
					}
				}
			}
			br.close();
			image.setFilePath(ROOT + image.getName() + image.getExtension());
			return image;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
