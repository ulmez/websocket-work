package se.ulme.dispatch;

import java.io.IOException;

import javax.websocket.Session;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CrudChooser {
	private ObjectMapper mapper = new ObjectMapper();
	private MessageOperator mo;
	private Session userSession;
	
	public CrudChooser(String message, Session userSession) throws JsonParseException, JsonMappingException, IOException {
		this.userSession = userSession;
		mo = mapper.readValue(message, MessageOperator.class);
	}
	
	public void operate() throws IOException {
		if(mo.getType().equals("get")) {
			Get wmd = new WhiteboardMessageDispatcher(mo);
			userSession.getBasicRemote().sendText(wmd.get());
		} else if(mo.getType().equals("post")) {
			Post wmd = new WhiteboardMessageDispatcher(mo);
			wmd.post(userSession);
		} else if(mo.getType().equals("delete")) {
			Delete wmd = new WhiteboardMessageDispatcher(mo);
			wmd.delete(userSession);
		} else if(mo.getType().equals("put")) {
			Put wmd = new WhiteboardMessageDispatcher(mo);
			wmd.put(userSession);
		} else if(mo.getType().equals("postnote")) {
			Post pmd = new PostItMessageDispatcher(mo);
			pmd.post(userSession);
		} else if(mo.getType().equals("putnote")) {
			Put pmd = new PostItMessageDispatcher(mo);
			pmd.put(userSession);
		} else if(mo.getType().equals("deletenote")) {
			Delete pmd = new PostItMessageDispatcher(mo);
			pmd.delete(userSession);
		}
	}
}
