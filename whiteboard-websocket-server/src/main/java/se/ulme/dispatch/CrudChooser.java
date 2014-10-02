package se.ulme.dispatch;

import java.io.IOException;
import javax.websocket.Session;

import se.ulme.util.Data;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A switch class that directs what will happen depending on message from the
 * client
 */
public class CrudChooser {
	private ObjectMapper mapper = new ObjectMapper();
	private MessageOperator mo;
	private Session userSession;

	public CrudChooser(String message, Session userSession)
			throws JsonParseException, JsonMappingException, IOException {
		this.userSession = userSession;
		mo = mapper.readValue(message, MessageOperator.class);
	}

	public void operate() throws IOException {
		Data.operate(userSession, mo);
	}
}
