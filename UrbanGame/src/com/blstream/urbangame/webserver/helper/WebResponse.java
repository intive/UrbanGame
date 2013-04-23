package com.blstream.urbangame.webserver.helper;

import java.util.List;

import android.util.Log;

import com.blstream.urbangame.database.entity.UrbanGame;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/*WebResponse is an auxiliary class which contains information about response
 * from web server: what kind of query was send to web server and JSON message
 * returned from web server. */

public class WebResponse {
	private final String TAG = "WebResponse";
	
	public final static int queryTypeGetUrbanGameDetails = 1;
	public final static int queryTypeGetUrbanGameBaseList = 2;
	
	private int queryType;
	private String jsonResponse;
	
	//
	// Constructor
	//
	public WebResponse(int _responseType) {
		queryType = _responseType;
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
	
	public UrbanGame parseResponseToUrbanGame() {
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
	
	public List<UrbanGame> parseResponseToUrbanGameList() {
		// try to parse response JSON string to list of UrbanGame objects
		Gson gson = new Gson();
		
		try {
			List<UrbanGame> urbanGames = gson.fromJson(jsonResponse, new TypeToken<List<UrbanGame>>() {}.getType());
			return urbanGames;
		}
		catch (JsonSyntaxException e) {
			Log.e(TAG, "parseResponseToUrbanGameList() exception " + e.toString());
		}
		
		return null;
	}
	
}
