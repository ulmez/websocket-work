package se.ulme.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "postit")
public class PostIt  {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Lob
	@Column(name = "information")
	private String information;
	
	@Column(name = "title")
	private String title;

	//bi-directional one-to-one association to Color
	@JsonIgnore
	@OneToOne(mappedBy="postit")
	private Color color;

	//bi-directional many-to-one association to Whiteboard
	@JsonIgnore
	@ManyToOne
	private Whiteboard whiteboard;

	public PostIt() {
	}
	
	public PostIt(String title, String information, Color color) {
		this.title = title;
		this.information = information;
		this.color = color;
	}
	
	public PostIt(String title, String information, Whiteboard whiteboard) {
		this.title = title;
		this.information = information;
		this.whiteboard = whiteboard;
	}
	
	public PostIt(String title, String information) {
		this.title = title;
		this.information = information;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInformation() {
		return this.information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Color getColor() {
		return this.color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Whiteboard getWhiteboard() {
		return this.whiteboard;
	}

	public void setWhiteboard(Whiteboard whiteboard) {
		this.whiteboard = whiteboard;
	}
}
