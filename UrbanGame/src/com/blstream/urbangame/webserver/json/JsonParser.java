package com.blstream.urbangame.webserver.json;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonParser {
	
	public static String URBANGAME_URL = "http://urbangame.patronage.blstream.com/";
	
	public UrbanGame getUrbanGameFromJSON(String urbanGaneInJSON) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UrbanGame.class, new UrbanGameDeserializer());
		Gson gson = gsonBuilder.create();
		
		// Parse JSON to Java
		return gson.fromJson(urbanGaneInJSON, UrbanGame.class);
		
	}
	
	public List<UrbanGame> getUrbanGameListFromJSON(String urbanGameListInJSON) {
		// Configure GSON
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UrbanGameContainer.class, new UrbanGameListDeserializer());
		Gson gson = gsonBuilder.create();
		
		// Parse JSON to Java
		UrbanGameContainer container = gson.fromJson(urbanGameListInJSON, UrbanGameContainer.class);
		
		return container.getGameList();
	}
	
	public List<Task> getTaskListFromJSON(String taskListInJSON) {
		
		// Configure GSON
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TaskContainer.class, new TaskListDeserializer());
		Gson gson = gsonBuilder.create();
		
		// Parse JSON to Java
		TaskContainer container = gson.fromJson(taskListInJSON, TaskContainer.class);
		
		return container.getTaskList();
		
	}
	
	public Task getTaskFromJSON(String taskInJSON) {
		// Configure GSON
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Task.class, new TaskDeserializer());
		Gson gson = gsonBuilder.create();
		
		// Parse JSON to Java
		return gson.fromJson(taskInJSON, Task.class);
	}
	
	//deserializer helper methods
	//translates difficulty from string to int
	public static int parseDifficulty(String difficulty) {
		int result = 0;
		if (difficulty.equals("very easy")) {
			result = 0;
		}
		else if (difficulty.equals("easy")) {
			result = 1;
		}
		else if (difficulty.equals("medium")) {
			result = 2;
		}
		else if (difficulty.equals("hard")) {
			result = 3;
		}
		else if (difficulty.equals("very hard")) {
			result = 4;
		}
		else if (difficulty.equals("extremely hard")) {
			result = 5;
		}
		return result;
	}
	
	public static Drawable drawableFromUrl(String url) throws IOException {
		Bitmap x;
		
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.connect();
		InputStream input = connection.getInputStream();
		
		x = BitmapFactory.decodeStream(input);
		return new BitmapDrawable(x);
	}
	
}
