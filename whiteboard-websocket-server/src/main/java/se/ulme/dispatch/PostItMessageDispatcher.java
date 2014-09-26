package se.ulme.dispatch;

import java.io.IOException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import se.ulme.converter.PostItConvert;
import se.ulme.hibernate.Color;
import se.ulme.hibernate.PostIt;
import se.ulme.hibernate.Whiteboard;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PostItMessageDispatcher implements Get, Post, Delete, Put {
	private MessageOperator mo;
	
	public PostItMessageDispatcher(MessageOperator mo) {
		this.mo = mo;
	}
	
	@Override
	public void put(javax.websocket.Session userSession) throws JsonParseException, JsonMappingException, IOException {
		Configuration conf = new Configuration().configure();
		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		SessionFactory sf = conf.buildSessionFactory(sr);
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		
		String json = mo.getOperate().substring(0, mo.getOperate().lastIndexOf(",")) + "}";
		ObjectMapper mapper = new ObjectMapper();
		PostItConvert pc = mapper.readValue(json, PostItConvert.class);
		PostIt p = (PostIt) session.get(PostIt.class, pc.getId());
		Color c = (Color) session.get(Color.class, pc.getId());
		
		c.setYellow(pc.getColor().getYellow());
		c.setGreen(pc.getColor().getGreen());
		c.setBlue(pc.getColor().getBlue());
		c.setRed(pc.getColor().getRed());
		
		p.setTitle(pc.getTitle());
		p.setInformation(pc.getInformation());
		p.setColor(c);
		
		session.update(p);
		
		tx.commit();
        sf.close();
        
        for(javax.websocket.Session s : userSession.getOpenSessions()) {
			s.getBasicRemote().sendText("refresh");
		}
	}

	@Override
	public void delete(javax.websocket.Session userSession) throws IOException {
		Configuration conf = new Configuration().configure();
		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		SessionFactory sf = conf.buildSessionFactory(sr);
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		
		String json = mo.getOperate().substring(0, mo.getOperate().lastIndexOf(",")) + "}";
		
		ObjectMapper mapper = new ObjectMapper();
		PostItConvert pc = mapper.readValue(json, PostItConvert.class);
		PostIt p = (PostIt) session.get(PostIt.class, pc.getId());
		
		session.delete(p);
		
		tx.commit();
        sf.close();
        
        for(javax.websocket.Session s : userSession.getOpenSessions()) {
			s.getBasicRemote().sendText("refresh");
		}
	}

	@Override
	public void post(javax.websocket.Session userSession) throws IOException {
		Configuration conf = new Configuration().configure();
		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		SessionFactory sf = conf.buildSessionFactory(sr);
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		
		Query query = session.createQuery("SELECT w FROM Whiteboard w WHERE w.whiteboard=" + "'" + mo.getOperate() + "'");
		List<?> whiteboards = query.list();
		Whiteboard wb = null;
		
		for(int i = 0; i < whiteboards.size(); i++) {
			wb = (Whiteboard) session.get(Whiteboard.class, ((Whiteboard) whiteboards.get(i)).getId());
		}
		
		ObjectMapper mapper = new ObjectMapper();
		PostItConvert pc = mapper.readValue(mo.getNote(), PostItConvert.class);
		PostIt p = new PostIt(pc.getTitle(), pc.getInformation(), wb);
		
		session.save(p);
		session.persist(p);
		
		boolean yellow = pc.getColor().getYellow();
		boolean green = pc.getColor().getGreen();
		boolean blue = pc.getColor().getBlue();
		boolean red = pc.getColor().getRed();
		Color c = new Color(yellow, green, blue, red, p.getId());
		
		session.save(c);
		
		tx.commit();
        sf.close();
        
        for(javax.websocket.Session s : userSession.getOpenSessions()) {
			s.getBasicRemote().sendText("refresh");
		}
	}

	@Override
	public String get() throws JsonProcessingException {
		// TODO Auto-generated method stub
		return null;
	}

}
