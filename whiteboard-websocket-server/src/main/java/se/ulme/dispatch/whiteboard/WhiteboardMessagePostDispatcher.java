package se.ulme.dispatch.whiteboard;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import se.ulme.dispatch.MessageOperator;
import se.ulme.dispatch.interfaces.Command;
import se.ulme.dispatch.interfaces.Post;
import se.ulme.hibernate.Whiteboard;

public class WhiteboardMessagePostDispatcher implements Post, Command {
	private MessageOperator mo;

	public WhiteboardMessagePostDispatcher(MessageOperator mo) {
		this.mo = mo;
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

	@Override
	public void operate(javax.websocket.Session userSession) throws IOException {
		Post wmd = new WhiteboardMessagePostDispatcher(mo);
		wmd.post(userSession);
	}
}
