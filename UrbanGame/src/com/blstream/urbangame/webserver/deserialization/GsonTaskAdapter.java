package com.blstream.urbangame.webserver.deserialization;

import java.lang.reflect.Type;

import android.util.Log;

import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.Task;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

/* GsonTaskAdapter is custom GSON adapter which allows to deserialize JSON
 * string that describes object of the abstract class "Task". */

public class GsonTaskAdapter implements JsonDeserializer<Task> {
	private final String TAG = "GsonTaskAdapter";
	private final String TASK_TYPE_FIELD_NAME = "type";
	
	public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		throws JsonParseException {
		Gson gson = new Gson();
		JsonObject jsonObject = json.getAsJsonObject();
		int type = jsonObject.get(TASK_TYPE_FIELD_NAME).getAsInt();
		Task task = null;
		
		try {
			if (type == Task.TASK_TYPE_ABCD) {
				task = gson.fromJson(json, ABCDTask.class);
			}
			else {
				task = gson.fromJson(json, LocationTask.class);
			}
		}
		
		catch (JsonSyntaxException e) {
			Log.e(TAG, "GsonTaskAdapter deserialize exception " + e.toString());
		}
		
		return task;
		
	}
}