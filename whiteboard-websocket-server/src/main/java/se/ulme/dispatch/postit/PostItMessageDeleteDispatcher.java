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
import se.ulme.dispatch.interfaces.Delete;
import se.ulme.hibernate.PostIt;

public class PostItMessageDeleteDispatcher implements Delete, Command {
	private MessageOperator mo;

	public PostItMessageDeleteDispatcher(MessageOperator mo) {
		this.mo = mo;
	}

	// Used to delete postit note
	@Override
	public void delete(javax.websocket.Session userSession) throws IOException {
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
		PostIt p = (PostIt) session.get(PostIt.class, id);

		session.delete(p);

		tx.commit();
		sf.close();

		for (javax.websocket.Session s : userSession.getOpenSessions()) {
			s.getBasicRemote().sendText("refresh");
		}
	}

	@Override
	public void operate(javax.websocket.Session userSession) throws IOException {
		Delete wmd = new PostItMessageDeleteDispatcher(mo);
		wmd.delete(userSession);
	}
}
