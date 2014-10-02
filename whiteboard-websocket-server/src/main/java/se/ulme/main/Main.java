package se.ulme.main;

import se.ulme.dispatch.MessageOperator;
import se.ulme.dispatch.interfaces.Get;
import se.ulme.dispatch.whiteboard.WhiteboardMessageGetDispatcher;

public class Main {
	public static void main(String[] args) {
		MessageOperator mo = new MessageOperator("get", "");
		Get md = new WhiteboardMessageGetDispatcher(mo);
		System.out.println(md.get());
	}
}
