package se.ulme.dispatch.whiteboard;

import java.io.IOException;
import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import se.ulme.converter.PostItConvert;
import se.ulme.converter.WhiteboardConvert;
import se.ulme.dispatch.MessageOperator;
import se.ulme.dispatch.interfaces.Command;
import se.ulme.dispatch.interfaces.Get;
import se.ulme.util.Data;

public class WhiteboardMessageGetDispatcher implements Get, Command {
	private MessageOperator mo;

	public WhiteboardMessageGetDispatcher(MessageOperator mo) {
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

	@Override
	public void operate(javax.websocket.Session userSession) throws IOException {
		Get wmd = new WhiteboardMessageGetDispatcher(mo);
		userSession.getBasicRemote().sendText(wmd.get());
	}
}
