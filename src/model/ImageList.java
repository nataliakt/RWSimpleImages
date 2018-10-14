package model;

import java.util.ArrayList;

public class ImageList {
	
	private ArrayList<Image> images;
	private ArrayList<Image> filter;
	private Image selected;
	private static ImageList instance = null;
	
	public static ImageList getInstance() {
		if (instance == null) {
			instance = new ImageList();
		}
		return instance;
	}

	public ImageList() {
		super();
		images = new ArrayList<Image>();
	}
	
	public boolean hasFileName(String name) {
		for (Image i : images) {
			if ((i.getName() + i.getExtension()).equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void clear() {
		images.clear();
	}
	
	public void clearFilter() {
		filter = null;
	}
	
	public void filter(String name) {
		filter = (ArrayList<Image>) images.clone();
		for (Image i : images) {
			if (!i.getName().toLowerCase().contains(name.toLowerCase())) {
				filter.remove(i);
			}
		}
	}
	
	public int addImage(Image image) {
		boolean add = false;
		for (int i = 0; i < images.size(); i++) {
			if (!add && images.get(i).getName().compareTo(image.getName()) >= 0) {
				add = true;
				images.add(i, image);
			}
		}
		if (!add) {
			images.add(image);
		}
		return images.indexOf(image);
	}
	
	public void remove(Image image) {
		images.remove(image);
	}

	public ArrayList<Image> getImages() {
		if (filter == null || filter.size() == images.size())
			return images;
		else
			return filter;
	}	

	public Image getImage(int index) {
		if (filter == null) {
			return images.get(index);
		}
		return filter.get(index);
	}
	
	public Image getImage(String internalName) {
		Image image = null;
		for (Image i : images) {
			if ((i.getName() + i.getExtension()).equals(internalName)) {
				image = i;
			}
		}
		return image;
	}

	public Image getSelected() {
		return selected;
	}

	public void setSelected(Image selected) {
		if (!selected.isLoad()) {
			selected = ImageFile.load(selected.getFilePath());
			selected.setLoad(true);
		}
		this.selected = selected;
	}
	

}
