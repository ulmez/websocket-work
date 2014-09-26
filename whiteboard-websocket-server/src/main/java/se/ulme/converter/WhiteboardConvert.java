package se.ulme.converter;

import java.util.List;

public class WhiteboardConvert {
	private int id;
	private String whiteboard;
	private List<PostItConvert> db;
	
	public WhiteboardConvert() {
		
	}
	
	public WhiteboardConvert(String whiteboard, List<PostItConvert> db, int id) {
		this.whiteboard = whiteboard;
		this.db = db;
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWhiteboard() {
		return whiteboard;
	}

	public void setWhiteboard(String whiteboard) {
		this.whiteboard = whiteboard;
	}

	public List<PostItConvert> getDb() {
		return db;
	}

	public void setDb(List<PostItConvert> db) {
		this.db = db;
	}
}
