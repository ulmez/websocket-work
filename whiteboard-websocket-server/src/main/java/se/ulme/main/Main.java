package se.ulme.main;

import se.ulme.dispatch.Get;
import se.ulme.dispatch.WhiteboardMessageDispatcher;
import se.ulme.dispatch.MessageOperator;

import com.fasterxml.jackson.core.JsonProcessingException;

public class Main {
	public static void main(String[] args) throws JsonProcessingException {
		MessageOperator mo = new MessageOperator("get", "");
		Get md = new WhiteboardMessageDispatcher(mo);
		System.out.println(md.get());
	}
}
