package com.blstream.urbangame.webserver.json;

import java.lang.reflect.Type;

import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class TaskDeserializer implements JsonDeserializer<Task> {
	
	@Override
	public Task deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
		throws JsonParseException {
		
		JsonObject jsonTaskObject = json.getAsJsonObject();
		
		String category = jsonTaskObject.get("category").getAsString();
		
		Task taskResult;
		
		if (category.equals("ABCTask")) {
			taskResult = new ABCDTask();
			taskResult.setType(Task.TASK_TYPE_ABCD);
			JsonArray jsonOptions = jsonTaskObject.get("choices").getAsJsonArray();
			
			String[] answers = new String[jsonOptions.size()];
			
			for (int i = 0; i < jsonOptions.size(); i++) {
				JsonObject jsonOption = jsonOptions.get(i).getAsJsonObject();
				
				answers[i] = jsonOption.get("answer").getAsString();
			}
			((ABCDTask) taskResult).setAnswers(answers);
			//FIXME there is no question in JSON 
			
		}
		else {
			taskResult = new LocationTask();
			taskResult.setType(Task.TASK_TYPE_LOCATION);
		}
		
		taskResult.setId(jsonTaskObject.get("tid").getAsLong());
		taskResult.setDescription(jsonTaskObject.get("description").getAsString());
		taskResult.setTitle(jsonTaskObject.get("name").getAsString());
		taskResult.setMaxPoints(jsonTaskObject.get("maxpoints").getAsInt());
		
		//TODO We don't remember max attempts and it is in JSON
		int maxAttempts = jsonTaskObject.get("maxattempts").getAsInt();
		taskResult.setIsRepetable(maxAttempts > 1);
		
		return taskResult;
	}
	
}
