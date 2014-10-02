package se.ulme.dispatch.interfaces;

import java.io.IOException;

public interface Put {
	public void put(javax.websocket.Session userSession) throws IOException;
}
