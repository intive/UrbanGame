package com.blstream.urbangame.webserver.helper;

import java.util.List;

import android.util.Log;

import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.blstream.urbangame.webserver.deserialization.GamesResponse;
import com.blstream.urbangame.webserver.deserialization.GsonTaskAdapter;
import com.blstream.urbangame.webserver.deserialization.TasksResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/* WebResponse is an auxiliary class which contains information about response
 * from web server: what kind of query was send to web server and JSON message
 * returned from web server. */

public class WebResponse {
	private final String TAG = "WebResponse";
	
	public enum QueryType {
		GetUrbanGameDetails, GetUrbanGameBaseList, GetTask, GetTaskList
	};
	
	private final QueryType queryType;
	private String jsonString;
	private Long gameID;
	
	//
	// Constructor
	//
	public WebResponse(QueryType _queryType) {
		queryType = _queryType;
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
	public void setGameId(Long gameID) {
		this.gameID = gameID;
	}
	
	public Long getGameId() {
		return gameID;
	}
	
	public QueryType getQueryType() {
		return queryType;
	}
	
	public String getJsonString() {
		return jsonString;
	}
	
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public UrbanGame getUrbanGame() {
		// try to parse response JSON string to UrbanGame
		Gson gson = new Gson();
		
		try {
			UrbanGame urbanGame = gson.fromJson(jsonString, UrbanGame.class);
			return urbanGame;
		}
		catch (JsonSyntaxException e) {
			Log.e(TAG, "parseResponseToUrbanGame() exception " + e.toString());
		}
		
		return null;
	}
	
	public List<UrbanGameShortInfo> getUrbanGameShortInfoList() {
		// try to parse response JSON string to GamesResponse
		Gson gson = new Gson();
		
		try {
			GamesResponse gamesResponse = gson.fromJson(jsonString, GamesResponse.class);
			return gamesResponse.getUrbanGameShortInfoList();
		}
		catch (JsonSyntaxException e) {
			Log.e(TAG, "parseResponseToUrbanGameList() exception " + e.toString());
		}
		
		return null;
	}
	
	public List<Task> getTaskList() {
		// try to parse response JSON string to TasksResponse
		Gson gson = initalizeGsonTaskAdapter();
		
		try {
			TasksResponse tasksResponse = gson.fromJson(jsonString, TasksResponse.class);
			return tasksResponse.getTaskList();
		}
		catch (JsonSyntaxException e) {
			Log.e(TAG, "parseResponseToUrbanGameList() exception " + e.toString());
		}
		return null;
	}
	
	public Task getTask() {
		// try to parse response JSON string to a single Task object
		Gson gson = initalizeGsonTaskAdapter();
		
		try {
			Task task = gson.fromJson(jsonString, Task.class);
			return task;
		}
		catch (JsonSyntaxException e) {
			Log.e(TAG, "parseResponseToTask() exception " + e.toString());
		}
		
		return null;
	}
	
}
