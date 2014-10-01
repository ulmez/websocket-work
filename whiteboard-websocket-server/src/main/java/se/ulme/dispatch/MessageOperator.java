package se.ulme.dispatch;

/**
 * Used to direct what happens in CrudChooser class
 */
public class MessageOperator {
	private String type;
	private String operate;
	private String note;
	
	public MessageOperator() {
		
	}
	
	public MessageOperator(String type, String operate) {
		this.type = type;
		this.operate = operate;
		this.note = "";
	}
	
	public MessageOperator(String type, String operate, String note) {
		this.type = type;
		this.operate = operate;
		this.note = note;
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
