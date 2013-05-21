package com.blstream.urbangame.webserver.helper;

import java.util.List;

import android.util.Log;

import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/* WebResponse is an auxiliary class which contains information about response
 * from web server: what kind of query was send to web server and JSON message
 * returned from web server. */

public class WebResponse {
	private final String TAG = "WebResponse";
	
	public final static int queryTypeGetUrbanGameDetails = 1;
	public final static int queryTypeGetUrbanGameBaseList = 2;
	public final static int queryTypeGetTask = 3;
	public final static int queryTypeGetTaskList = 4;
	
	private int queryType;
	private String jsonResponse;
	
	//
	// Constructor
	//
	public WebResponse(int _responseType) {
		queryType = _responseType;
	}
	
	//
	// Private methods
	//
	private Gson initalizeGsonTaskAdapter() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Task.class, new GsonTaskAdapter());
		return gsonBuilder.create();
	}
	
	//
	// Public methods
	//
	public int getQueryType() {
		return queryType;
	}
	
	public void setJsonResponse(String _jsonResponse) {
		jsonResponse = _jsonResponse;
	}
	
	public UrbanGame getUrbanGame() {
		// try to parse response JSON string to UrbanGame
		Gson gson = new Gson();
		
		try {
			UrbanGame urbanGame = gson.fromJson(jsonResponse, UrbanGame.class);
			return urbanGame;
		}
		catch (JsonSyntaxException e) {
			Log.e(TAG, "parseResponseToUrbanGame() exception " + e.toString());
		}
		return null;
	}
	
	public List<UrbanGameShortInfo> getUrbanGameShortInfoList() {
		// try to parse response JSON string to list of UrbanGame objects
		Gson gson = new Gson();
		
		try {
			List<UrbanGameShortInfo> urbanGames = gson.fromJson(jsonResponse,
				new TypeToken<List<UrbanGameShortInfo>>() {}.getType());
			return urbanGames;
		}
		catch (JsonSyntaxException e) {
			Log.e(TAG, "parseResponseToUrbanGameList() exception " + e.toString());
		}
		
		return null;
	}
	
	public List<Task> getTaskList() {
		// try to parse response JSON string to list of Task objects
		Gson gson = initalizeGsonTaskAdapter();
		
		try {
			List<Task> taskList = gson.fromJson(jsonResponse, new TypeToken<List<Task>>() {}.getType());
			return taskList;
		}
		catch (JsonSyntaxException e) {
			Log.e(TAG, "parseResponseToTaskList() exception " + e.toString());
		}
		
		return null;
	}
	
	public Task getTask() {
		// try to parse response JSON string to a single Task object
		Gson gson = initalizeGsonTaskAdapter();
		
		try {
			Task task = gson.fromJson(jsonResponse, Task.class);
			return task;
		}
		catch (JsonSyntaxException e) {
			Log.e(TAG, "parseResponseToTask() exception " + e.toString());
		}
		
		return null;
	}
	
}
