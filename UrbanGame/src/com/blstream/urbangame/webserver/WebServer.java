package com.blstream.urbangame.webserver;

import android.os.Bundle;
import android.os.Message;

/**
 * Basic class for downloading and updating data from web server.
 */
// formatter:off
/*
 * Example of use
  
  class A extends Activity implements WebServerNotificationListener {
		private ServerResponseHandler handler;
		private WebServer webServer;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			...
		
			this.handler = new ServerResponseHandler(this);
			this.webServer = new WebServer(handler);
			
			...
		}
		
		public void doWhatever() {
			webServer.doWhatever();
		}
		
		@Override
		public void onWebServerResponse(Message message) {
			WebResponse webResponse = (WebResponse) message.obj;
			Bundle bundle = message.getData();
			...
		}
  }
 */
// formatter:on
// TODO add required query types and corresponding methods
public class WebServer {
	private ServerResponseHandler handler;
	
	public WebServer(ServerResponseHandler handler) {
		this.handler = handler;
	}
	
	public enum QueryType {
		GetGamesList, GetGameDetails, GetTasksList, GetTaskDetails
		/// and so on...
	}
	
	public void getAllGames() {
		new WebRequest(this, QueryType.GetGamesList).execute();
	}
	
	/// and so on...
	
	public void onWebServerResponse(WebResponse webResponse) {
		Message message = getMessageWithResponse(webResponse);
		configureMessageBundle(message);
		
		handler.sendMessage(message);
	}
	
	protected Message getMessageWithResponse(WebResponse webResponse) {
		Message message = new Message();
		message.obj = webResponse;
		return message;
	}
	
	protected void configureMessageBundle(Message message) {
		Bundle bundle = new Bundle();
		
		// TODO pack required data in bundle
		/**
		 * Here we can configure message as we wish. We can add here a bunch of
		 * pairs <key, value> describing content of response for our client that
		 * it'll have sufficient information about notification response
		 */
		message.setData(bundle);
	}
	
	public void doWhatever() {
		new WebRequest(this, QueryType.GetGamesList).execute();
	}
}