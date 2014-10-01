package se.ulme.dispatch;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import se.ulme.converter.PostItConvert;
import se.ulme.converter.WhiteboardConvert;
import se.ulme.hibernate.Whiteboard;
import se.ulme.util.Data;

/**
 * Taking care of CRUD with whiteboards
 */
public class WhiteboardMessageDispatcher implements Get, Post, Delete, Put {
	private MessageOperator mo;

	public WhiteboardMessageDispatcher(MessageOperator mo) {
		this.mo = mo;
	}

	// Get all whiteboards or only a specific whiteboard and generate a string
	// in json format
	@Override
	public String get() {
		String json;
		if (mo.getOperate().equals("")) {
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

			for (WhiteboardConvert l : Data.convertedAllWhiteboards()) {
				JsonObjectBuilder builder = Json.createObjectBuilder();
				builder.add("id", l.getId());
				builder.add("whiteboard", l.getWhiteboard());
				builder.add("db", Json.createArrayBuilder());
				arrayBuilder.add(builder);
			}

			JsonArray whiteboards = arrayBuilder.build();

			StringWriter sw = new StringWriter();
			JsonWriter jw = Json.createWriter(sw);
			jw.writeArray(whiteboards);
			jw.close();

			json = sw.toString();
		} else {
			JsonObjectBuilder builder = Json.createObjectBuilder();
			JsonObjectBuilder builderPostIt = Json.createObjectBuilder();
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

			WhiteboardConvert wc = Data.convertedWhiteboard(mo.getOperate());

			JsonObjectBuilder colorBuilder = Json.createObjectBuilder();
			for (PostItConvert pc : wc.getDb()) {
				JsonObject c = colorBuilder.add("id", pc.getId())
						.add("yellow", pc.getColor().getYellow())
						.add("green", pc.getColor().getGreen())
						.add("blue", pc.getColor().getBlue())
						.add("red", pc.getColor().getRed()).build();

				arrayBuilder.add(builderPostIt.add("id", pc.getId())
						.add("title", pc.getTitle())
						.add("information", pc.getInformation())
						.add("color", c));
			}

			JsonArray postIts = arrayBuilder.build();

			builder.add("id", wc.getId());
			builder.add("whiteboard", wc.getWhiteboard());
			builder.add("db", postIts);

			JsonObject whiteboard = builder.build();

			StringWriter sw = new StringWriter();
			JsonWriter jw = Json.createWriter(sw);
			jw.writeObject(whiteboard);
			jw.close();

			json = sw.toString();
		}

		return json;
	}

	// Post a whiteboard to the database with hibernate class Whiteboard
	@Override
	public void post(javax.websocket.Session userSession) throws IOException {
		Configuration conf = new Configuration().configure();
		ServiceRegistry sr = new StandardServiceRegistryBuilder()
				.applySettings(conf.getProperties()).build();
		SessionFactory sf = conf.buildSessionFactory(sr);
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();

		Whiteboard wb = new Whiteboard();
		wb.setWhiteboard(mo.getOperate());

		session.save(wb);

		tx.commit();
		sf.close();

		for (javax.websocket.Session s : userSession.getOpenSessions()) {
			s.getBasicRemote().sendText("refresh");
		}
	}

	// Delete a whiteboard from the database with hibernate class Whiteboard on
	// whiteboard name as parameter
	@Override
	public void delete(javax.websocket.Session userSession) throws IOException {
		Configuration conf = new Configuration().configure();
		ServiceRegistry sr = new StandardServiceRegistryBuilder()
				.applySettings(conf.getProperties()).build();
		SessionFactory sf = conf.buildSessionFactory(sr);
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();

		Query query = session
				.createQuery("SELECT w FROM Whiteboard w WHERE w.whiteboard="
						+ "'" + mo.getOperate() + "'");
		List<?> whiteboards = query.list();
		Whiteboard wb = null;

		for (int i = 0; i < whiteboards.size(); i++) {
			wb = (Whiteboard) session.get(Whiteboard.class,
					((Whiteboard) whiteboards.get(i)).getId());
		}

		session.delete(wb);

		tx.commit();
		sf.close();

		for (javax.websocket.Session s : userSession.getOpenSessions()) {
			s.getBasicRemote().sendText("refresh");
		}
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
}
