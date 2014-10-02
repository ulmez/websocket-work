package se.ulme.dispatch.whiteboard;

import java.io.IOException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import se.ulme.dispatch.MessageOperator;
import se.ulme.dispatch.interfaces.Command;
import se.ulme.dispatch.interfaces.Delete;
import se.ulme.hibernate.Whiteboard;

public class WhiteboardMessageDeleteDispatcher implements Delete, Command {
	private MessageOperator mo;

	public WhiteboardMessageDeleteDispatcher(MessageOperator mo) {
		this.mo = mo;
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

	@Override
	public void operate(javax.websocket.Session userSession) throws IOException {
		Delete wmd = new WhiteboardMessageDeleteDispatcher(mo);
		wmd.delete(userSession);
	}
}
