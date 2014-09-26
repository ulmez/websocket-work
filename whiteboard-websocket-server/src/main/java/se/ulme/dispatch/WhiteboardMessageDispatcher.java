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

import se.ulme.converter.WhiteboardConvert;
import se.ulme.database.Data;
import se.ulme.hibernate.Whiteboard;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class WhiteboardMessageDispatcher implements Get, Post, Delete, Put {
	private MessageOperator mo;
	
	public WhiteboardMessageDispatcher(MessageOperator mo) {
		this.mo = mo;
	}
	
	@Override
	public String get() throws JsonProcessingException {
		String json;
		if(mo.getOperate().equals("")) {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			json = ow.writeValueAsString(Data.convertedAllWhiteboards());
		} else {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			json = ow.writeValueAsString(Data.convertedWhiteboard(mo.getOperate()));
		}
		
		return json;
	}
	
	@Override
	public void post(javax.websocket.Session userSession) throws IOException {
		Configuration conf = new Configuration().configure();
		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		SessionFactory sf = conf.buildSessionFactory(sr);
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		
		Whiteboard wb = new Whiteboard();
		wb.setWhiteboard(mo.getOperate());
		
		session.save(wb);
		
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
		
		Query query = session.createQuery("SELECT w FROM Whiteboard w WHERE w.whiteboard=" + "'" + mo.getOperate() + "'");
		List<?> whiteboards = query.list();
		Whiteboard wb = null;
		
		for(int i = 0; i < whiteboards.size(); i++) {
			wb = (Whiteboard) session.get(Whiteboard.class, ((Whiteboard) whiteboards.get(i)).getId());
		}
		
		session.delete(wb);
		
		tx.commit();
        sf.close();
        
        for(javax.websocket.Session s : userSession.getOpenSessions()) {
			s.getBasicRemote().sendText("refresh");
		}
	}
	
	@Override
	public void put(javax.websocket.Session userSession) throws JsonParseException, JsonMappingException, IOException {
		Configuration conf = new Configuration().configure();
		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		SessionFactory sf = conf.buildSessionFactory(sr);
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(mo.getOperate());
		json = json.substring(1, json.lastIndexOf(",")) + "}";
		json = json.replace("\\", "");
		
		ObjectMapper mapper = new ObjectMapper();
		WhiteboardConvert wbc = mapper.readValue(json, WhiteboardConvert.class);
		Whiteboard wb = (Whiteboard) session.get(Whiteboard.class, wbc.getId());

		wb.setWhiteboard(wbc.getWhiteboard());
		session.update(wb);
		
		tx.commit();
        sf.close();
        
        for(javax.websocket.Session s : userSession.getOpenSessions()) {
			s.getBasicRemote().sendText("refresh");
		}
	}
}
