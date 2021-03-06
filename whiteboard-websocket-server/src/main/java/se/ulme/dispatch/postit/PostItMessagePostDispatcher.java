package se.ulme.dispatch.postit;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import se.ulme.dispatch.MessageOperator;
import se.ulme.dispatch.interfaces.Command;
import se.ulme.dispatch.interfaces.Post;
import se.ulme.hibernate.Color;
import se.ulme.hibernate.PostIt;
import se.ulme.hibernate.Whiteboard;

public class PostItMessagePostDispatcher implements Post, Command {
	private MessageOperator mo;

	public PostItMessagePostDispatcher(MessageOperator mo) {
		this.mo = mo;
	}

	// Used to insert a postit note
	@Override
	public void post(javax.websocket.Session userSession) throws IOException {
		Configuration conf = new Configuration().configure();
		ServiceRegistry sr = new StandardServiceRegistryBuilder()
				.applySettings(conf.getProperties()).build();
		SessionFactory sf = conf.buildSessionFactory(sr);
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();

		StringReader reader = new StringReader(mo.getNote());
		JsonReader jsonReader = Json.createReader(reader);
		JsonObject jsonObject = jsonReader.readObject();

		jsonReader.close();

		// Get whiteboard on name parameter here from hibernate class Whiteboard
		Query query = session
				.createQuery("SELECT w FROM Whiteboard w WHERE w.whiteboard="
						+ "'" + mo.getOperate() + "'");
		List<?> whiteboards = query.list();
		Whiteboard wb = null;

		for (int i = 0; i < whiteboards.size(); i++) {
			wb = (Whiteboard) session.get(Whiteboard.class,
					((Whiteboard) whiteboards.get(i)).getId());
		}

		String title = jsonObject.getString("title");
		String information = jsonObject.getString("information");

		boolean yellow = jsonObject.getJsonObject("color").getBoolean("yellow");
		boolean green = jsonObject.getJsonObject("color").getBoolean("green");
		boolean blue = jsonObject.getJsonObject("color").getBoolean("blue");
		boolean red = jsonObject.getJsonObject("color").getBoolean("red");

		PostIt p = new PostIt(title, information, wb);

		session.save(p);
		session.persist(p);

		Color c = new Color(yellow, green, blue, red, p.getId());

		session.save(c);

		tx.commit();
		sf.close();

		for (javax.websocket.Session s : userSession.getOpenSessions()) {
			s.getBasicRemote().sendText("refresh");
		}
	}

	@Override
	public void operate(javax.websocket.Session userSession) throws IOException {
		Post wmd = new PostItMessagePostDispatcher(mo);
		wmd.post(userSession);
	}
}
