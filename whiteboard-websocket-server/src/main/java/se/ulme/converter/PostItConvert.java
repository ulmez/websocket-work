package se.ulme.converter;

import se.ulme.hibernate.Color;

public class PostItConvert {
	private int id;
	private String title;
	private String information;
	private Color color;
	
	public PostItConvert() {
		
	}
	
	public PostItConvert(String title, String information, Color color) {
		this.id = 0;
		this.title = title;
		this.information = information;
		this.color = color;
	}
	
	public PostItConvert(int id, String title, String information, Color color) {
		this.id = id;
		this.title = title;
		this.information = information;
		this.color = color;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
