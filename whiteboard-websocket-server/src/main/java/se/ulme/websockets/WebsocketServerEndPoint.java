package se.ulme.websockets;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import se.ulme.dispatch.CrudChooser;

@ServerEndpoint("/serverendpoint")
public class WebsocketServerEndPoint {
	static Set<Session> chatroomUsers = Collections
			.synchronizedSet(new HashSet<Session>());

	@OnOpen
	public void handleOpen(Session userSession) throws IOException {

	}

	@OnMessage
	public void handleMessage(String message, Session userSession)
			throws IOException {
		CrudChooser cc = new CrudChooser(message, userSession);
		cc.operate();
	}

	@OnClose
	public void handleClose(Session userSession) {

	}

	@OnError
	public void handleError(Throwable t) {
		t.printStackTrace();
	}
}
