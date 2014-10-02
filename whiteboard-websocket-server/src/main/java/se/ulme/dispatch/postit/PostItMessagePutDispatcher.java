package se.ulme.dispatch.postit;

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
import se.ulme.hibernate.Color;
import se.ulme.hibernate.PostIt;

public class PostItMessagePutDispatcher implements Put, Command {
	private MessageOperator mo;

	public PostItMessagePutDispatcher(MessageOperator mo) {
		this.mo = mo;
	}

	// Used to update title, information and color in the postit note
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

		String title = jsonObject.getString("title");
		String information = jsonObject.getString("information");

		int id = jsonObject.getInt("id");
		boolean yellow = jsonObject.getJsonObject("color").getBoolean("yellow");
		boolean green = jsonObject.getJsonObject("color").getBoolean("green");
		boolean blue = jsonObject.getJsonObject("color").getBoolean("blue");
		boolean red = jsonObject.getJsonObject("color").getBoolean("red");

		PostIt p = (PostIt) session.get(PostIt.class, id);
		Color c = (Color) session.get(Color.class, id);

		c.setYellow(yellow);
		c.setGreen(green);
		c.setBlue(blue);
		c.setRed(red);

		p.setTitle(title);
		p.setInformation(information);
		p.setColor(c);

		session.update(p);

		tx.commit();
		sf.close();

		for (javax.websocket.Session s : userSession.getOpenSessions()) {
			s.getBasicRemote().sendText("refresh");
		}
	}

	@Override
	public void operate(javax.websocket.Session userSession) throws IOException {
		Put wmd = new PostItMessagePutDispatcher(mo);
		wmd.put(userSession);
	}
}
