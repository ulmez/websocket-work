package se.ulme.hibernate;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "whiteboard")
public class Whiteboard {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "whiteboard")
	private String whiteboard;

	// bi-directional many-to-one association to PostIt
	@JsonIgnore
	@OneToMany(mappedBy = "whiteboard")
	private List<PostIt> db;

	public Whiteboard() {
	}

	public Whiteboard(String whiteboard) {
		this.whiteboard = whiteboard;
	}

	public Whiteboard(String whiteboard, List<PostIt> db) {
		this.whiteboard = whiteboard;
		this.db = db;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWhiteboard() {
		return this.whiteboard;
	}

	public void setWhiteboard(String whiteboard) {
		this.whiteboard = whiteboard;
	}

	public List<PostIt> getDb() {
		return this.db;
	}

	public void setDb(List<PostIt> db) {
		this.db = db;
	}

	public PostIt addPostit(PostIt postit) {
		getDb().add(postit);
		postit.setWhiteboard(this);

		return postit;
	}

	public PostIt removePostit(PostIt postit) {
		getDb().remove(postit);
		postit.setWhiteboard(null);

		return postit;
	}
}
