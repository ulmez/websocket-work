package se.ulme.dispatch;

import java.io.IOException;

public interface Post {
	public void post(javax.websocket.Session userSession) throws IOException;
}
