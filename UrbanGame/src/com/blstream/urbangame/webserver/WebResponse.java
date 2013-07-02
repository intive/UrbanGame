package com.blstream.urbangame.webserver;

import java.util.ArrayList;

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
		return !WebDownloader.EMPTY_JSON.equals(responseString);
	}
}