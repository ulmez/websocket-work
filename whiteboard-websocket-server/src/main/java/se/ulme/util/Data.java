package se.ulme.util;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import se.ulme.converter.PostItConvert;
import se.ulme.converter.WhiteboardConvert;
import se.ulme.hibernate.Color;
import se.ulme.hibernate.Whiteboard;

/**
 * Is used to convert the format from my hibernate classes to fit with my
 * AngularJS client
 */
public class Data {
	// Gives one specific whiteboardConvert from my Whiteboard hibernate class
	public static WhiteboardConvert convertedWhiteboard(Whiteboard w) {
		List<PostItConvert> pits1 = new ArrayList<>();

		for (int i = 0; i < w.getDb().size(); i++) {
			int id = w.getDb().get(i).getId();
			String title = w.getDb().get(i).getTitle();
			String information = w.getDb().get(i).getInformation();
			Color color = w.getDb().get(i).getColor();
			PostItConvert pit1 = new PostItConvert(id, title, information,
					color);

			pits1.add(pit1);
		}

		WhiteboardConvert wbt1 = new WhiteboardConvert(w.getWhiteboard(),
				pits1, w.getId());

		return wbt1;
	}

	// Gives all the Whiteboards in WhiteboardConvert format from the
	// Whiteboards in hibernate format
	public static List<WhiteboardConvert> convertedAllWhiteboards() {
		Configuration conf = new Configuration().configure();
		ServiceRegistry sr = new StandardServiceRegistryBuilder()
				.applySettings(conf.getProperties()).build();
		SessionFactory sf = conf.buildSessionFactory(sr);
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();

		Query query = session.createQuery("SELECT w FROM Whiteboard w");
		List<?> whiteboards = query.list();
		List<WhiteboardConvert> wbts = new ArrayList<>();

		for (int i = 0; i < whiteboards.size(); i++) {
			Whiteboard wb1 = (Whiteboard) session.get(Whiteboard.class,
					((Whiteboard) whiteboards.get(i)).getId());
			WhiteboardConvert wbt1 = convertedWhiteboard(wb1);
			wbts.add(wbt1);
		}

		tx.commit();
		sf.close();

		return wbts;
	}

	// Gives one converted Whiteboard from whiteboard name
	public static WhiteboardConvert convertedWhiteboard(String whiteboardName) {
		Configuration conf = new Configuration().configure();
		ServiceRegistry sr = new StandardServiceRegistryBuilder()
				.applySettings(conf.getProperties()).build();
		SessionFactory sf = conf.buildSessionFactory(sr);
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();

		Query query = session
				.createQuery("SELECT w FROM Whiteboard w WHERE w.whiteboard="
						+ "'" + whiteboardName + "'");
		List<?> whiteboards = query.list();
		WhiteboardConvert wbc = null;

		for (int i = 0; i < whiteboards.size(); i++) {
			Whiteboard wb1 = (Whiteboard) session.get(Whiteboard.class,
					((Whiteboard) whiteboards.get(i)).getId());
			wbc = convertedWhiteboard(wb1);
		}

		tx.commit();
		sf.close();

		return wbc;
	}
}
