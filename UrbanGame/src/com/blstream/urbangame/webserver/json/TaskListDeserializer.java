package com.blstream.urbangame.webserver.json;

import java.lang.reflect.Type;
import java.util.LinkedList;

import com.blstream.urbangame.database.entity.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TaskListDeserializer implements JsonDeserializer<TaskContainer> {
	
	@Override
	public TaskContainer deserialize(final JsonElement json, final Type typeOfT,
		final JsonDeserializationContext context) {
		
		JsonObject jsonObject = json.getAsJsonObject();
		
		JsonArray jsonTaskArray = jsonObject.get("_embedded").getAsJsonArray();
		
		LinkedList<Task> resultList = new LinkedList<Task>();
		
		for (int i = 0; i < jsonTaskArray.size(); i++) {
			JsonObject jsonTask = jsonTaskArray.get(i).getAsJsonObject();
			Task task = new Task() {}; //TODO I don't know what is the task type. I think it shouldn't be like this because we have to now what icon shoud be displayed
			
			task.setId(jsonTask.get("tid").getAsLong());
			task.setTitle(jsonTask.get("name").getAsString());
			resultList.add(task);
		}
		
		return new TaskContainer(resultList);
	}
}
