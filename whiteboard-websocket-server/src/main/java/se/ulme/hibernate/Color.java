package se.ulme.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "color")
public class Color {

	@Id
	@Column(name = "id")
	private int id;

	@Column(nullable = false, columnDefinition = "BOOLEAN")
	private boolean yellow;

	@Column(nullable = false, columnDefinition = "BOOLEAN")
	private boolean green;

	@Column(nullable = false, columnDefinition = "BOOLEAN")
	private boolean blue;

	@Column(nullable = false, columnDefinition = "BOOLEAN")
	private boolean red;

	// bi-directional one-to-one association to PostIt
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "id")
	private PostIt postit;

	public Color() {
	}

	public Color(boolean yellow, boolean green, boolean blue, boolean red) {
		this.yellow = yellow;
		this.green = green;
		this.blue = blue;
		this.red = red;
		this.id = 0;
	}

	public Color(boolean yellow, boolean green, boolean blue, boolean red,
			int id) {
		this.yellow = yellow;
		this.green = green;
		this.blue = blue;
		this.red = red;
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getBlue() {
		return this.blue;
	}

	public void setBlue(boolean blue) {
		this.blue = blue;
	}

	public boolean getGreen() {
		return this.green;
	}

	public void setGreen(boolean green) {
		this.green = green;
	}

	public boolean getRed() {
		return this.red;
	}

	public void setRed(boolean red) {
		this.red = red;
	}

	public boolean getYellow() {
		return this.yellow;
	}

	public void setYellow(boolean yellow) {
		this.yellow = yellow;
	}

	public PostIt getPostit() {
		return this.postit;
	}

	public void setPostit(PostIt postit) {
		this.postit = postit;
	}
}
