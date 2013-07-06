package com.blstream.urbangame.webserver.json;

import java.lang.reflect.Type;

import com.blstream.urbangame.database.entity.Task;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TaskListDeserializer implements JsonDeserializer<Task> {
	
	@Override
	public Task deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) {
		
		JsonObject jsonObject = json.getAsJsonObject();
		
		//TODO - I need JSON example  to do this
		
		return null;
	}
}
