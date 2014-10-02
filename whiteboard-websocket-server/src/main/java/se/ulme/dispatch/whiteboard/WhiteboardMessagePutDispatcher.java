package se.ulme.dispatch.whiteboard;

import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import se.ulme.dispatch.MessageOperator;
import se.ulme.dispatch.interfaces.Command;
import se.ulme.dispatch.interfaces.Put;
import se.ulme.hibernate.Whiteboard;

public class WhiteboardMessagePutDispatcher implements Put, Command {
	private MessageOperator mo;

	public WhiteboardMessagePutDispatcher(MessageOperator mo) {
		this.mo = mo;
	}

	// Change name of a whiteboard in database with hibernate class Whiteboard
	@Override
	public void put(javax.websocket.Session userSession) throws IOException {
		Configuration conf = new Configuration().configure();
		ServiceRegistry sr = new StandardServiceRegistryBuilder()
				.applySettings(conf.getProperties()).build();
		SessionFactory sf = conf.buildSessionFactory(sr);
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();

		StringReader reader = new StringReader(mo.getOperate());
		JsonReader jsonReader = Json.createReader(reader);
		JsonObject jsonObject = jsonReader.readObject();

		jsonReader.close();

		int id = jsonObject.getInt("id");
		String whiteboard = jsonObject.getString("whiteboard");
		Whiteboard wb = (Whiteboard) session.get(Whiteboard.class, id);

		wb.setWhiteboard(whiteboard);

		session.update(wb);

		tx.commit();
		sf.close();

		for (javax.websocket.Session s : userSession.getOpenSessions()) {
			s.getBasicRemote().sendText("refresh");
		}
	}

	@Override
	public void operate(javax.websocket.Session userSession) throws IOException {
		Put wmd = new WhiteboardMessagePutDispatcher(mo);
		wmd.put(userSession);
	}
}
