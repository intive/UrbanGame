package com.example.rest;

import android.os.Handler;

public class WebServer implements WebServerResponseListener {
	public final static int MESSAGE_UPDATED = 0x128;
	private WebResponse webResponse;
	private Handler handler;
	
	public WebServer(Handler handler) {
		this.handler = handler;
	}
	
	public enum QueryType {
		GetGamesList, GetGameDetails, GetTasksList, GetTaskDetails
	}
	
	public void doWhatever() {
		new WebRequest(this, QueryType.GetGamesList).execute();
	}
	
	@Override
	public void onWebServerResponse(WebResponse webResponse) {
		this.webResponse = webResponse;
		handler.sendEmptyMessage(MESSAGE_UPDATED);
	}
	
	public WebResponse getWebResponse() {
		return webResponse;
	}
}