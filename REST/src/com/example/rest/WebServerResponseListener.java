package com.example.rest;

public interface WebServerResponseListener {
	// This method will be called after a query to web server has completed.
	// If the query was successful webResponse is not null.
	// If the query failed webResponse is null.
	public void onWebServerResponse(WebResponse webResponse);
}