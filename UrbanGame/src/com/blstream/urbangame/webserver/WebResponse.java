package com.blstream.urbangame.webserver;

import com.blstream.urbangame.webserver.WebServer.QueryType;

/**
 * WebResponse is an auxiliary class which contains information about response
 * from web server: what kind of query was send to web server and String message
 * returned from web server.
 */

//FIXME implementation needed
public class WebResponse {
	public final QueryType queryType;
	public final long gameID;
	public final long taskID;
	
	private String responseString;
	
	public WebResponse(QueryType queryType) {
		this.gameID = 0L;
		this.taskID = 0L;
		this.queryType = queryType;
	}
	
	public void setResponse(String responseString) {
		this.responseString = responseString;
	}
	
	public String getResponse() {
		return responseString;
	}
}