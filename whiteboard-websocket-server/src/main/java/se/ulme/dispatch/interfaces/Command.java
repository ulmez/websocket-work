package se.ulme.dispatch.interfaces;

import java.io.IOException;

public interface Command {
	public void operate(javax.websocket.Session userSession) throws IOException;
}
