package com.blstream.urbangame.webserver;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.blstream.urbangame.webserver.WebServer.QueryType;

/**
 * WebResponse is an auxiliary class which contains information about response
 * from web server: what kind of query was send to web server and String message
 * returned from web server.
 */

// TODO extend by information which will be needed
public class WebResponse {
	public final QueryType queryType;
	private String responseString;
	
	private final static int USER_ALREADY_EXISTS = 409;
	private final static int WRONG_LOGIN_DATA = 401;
	
	/*
	 * Optional data to be placed in response 
	 */
	public long gameID;
	public long taskID;
	public int points;
	public ArrayList<String> correctAnswers;
	
	public WebResponse(QueryType queryType) {
		this.queryType = queryType;
	}
	
	public void setResponse(String responseString) {
		this.responseString = responseString;
	}
	
	public String getResponse() {
		return responseString;
	}
	
	public boolean isValid() {
		if (WebDownloader.EMPTY_JSON.equals(responseString)) return false;
		JSONObject jsonObject = null;
		boolean valid = true;
		try {
			jsonObject = new JSONObject(responseString);
		}
		catch (JSONException e) {
			valid = false;
		}
		try {
			if (jsonObject != null) {
				switch (Integer.valueOf(jsonObject.getString("code"))) {
					case USER_ALREADY_EXISTS:
					case WRONG_LOGIN_DATA:
						valid = false;
						break;
					default:
						break;
				}
			}
		}
		catch (JSONException e) {}
		return valid;
	}
}