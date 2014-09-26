package se.ulme.dispatch;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface Get {
	public String get() throws JsonProcessingException;
}
