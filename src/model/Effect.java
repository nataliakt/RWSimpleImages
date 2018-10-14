package model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Classe generica para efeitos
 * 
 * @author Natalia Kelim Thiel
 *
 */
public abstract class Effect {

	protected Image image;
	private String name;
	private String description;
	private String from;
	private String to;
	private boolean parameterized = false;
	private List<Parameter> parameters = new ArrayList<Parameter>();

	public Effect(String name, String description, String from, String to) {
		super();
		this.name = name;
		this.description = description;
		this.from = from;
		this.to = to;
	}

	/**
	 * Efeito a ser implementado
	 * 
	 * @return Image com efeito
	 */
	public abstract Image effect();

	/**
	 * Inicia um requerimento de parametro
	 * 
	 * @param requestParam
	 *            Mensagem exibida para o usuário
	 * @param defaultParam
	 *            Parametro padrao
	 */
	public void setRequest(Parameter p) {
		parameterized = true;
		boolean exists = false;
		for (Parameter par : parameters) {
			if (par.getName().equals(p.getName())) {
				exists = true;
			}
		}
		if (!exists) {
			parameters.add(p);
		}
	}

	public Parameter getParameter(int index) {
		return parameters.get(index);
	}
	
	public Parameter getParameter(String name) {
		Parameter param = null;
		for (Parameter p : parameters) {
			if (p.getName().equals(name)) {
				param = p;
			}
		}
		return param;
	}

	public StringProperty getNameProperty() {
		return new SimpleStringProperty(name);
	}

	public StringProperty getToProperty() {
		return new SimpleStringProperty(to);
	}

	public void setParameter(int index, String value) {
		this.parameters.get(index).setValue(value);
	}

	public void setParameter(int index, Image value) {
		this.parameters.get(index).setValue(value);
	}

	public void setParameter(int index, int value) {
		this.parameters.get(index).setValue(value);
	}

	public void setImage(Image image) {
		this.image = new Image();
		this.image.setFilePath(image.getFilePath());
		this.image.setId(image.getId());
		this.image.setWidth(image.getWidth());
		this.image.setHeight(image.getHeight());
		this.image.setMax(image.getMax());
		this.image.setImage(image.getImage());
	}

	protected void setFrom(String from) {
		this.from = from;
	}

	protected void setTo(String to) {
		this.to = to;
	}

	public Image getImage() {
		return image;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public boolean isParameterized() {
		return parameterized;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

}
