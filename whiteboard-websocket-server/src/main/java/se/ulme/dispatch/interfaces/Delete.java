package se.ulme.dispatch.interfaces;

import java.io.IOException;

public interface Delete {
	public void delete(javax.websocket.Session userSession) throws IOException;
}
