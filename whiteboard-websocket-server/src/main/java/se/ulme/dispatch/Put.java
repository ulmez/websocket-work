package se.ulme.dispatch;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface Put {
	public void put(javax.websocket.Session userSession) throws JsonParseException, JsonMappingException, IOException;
}
