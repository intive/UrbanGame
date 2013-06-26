package com.blstream.urbangame.webserver;

import android.os.Handler;

/**
 * Basic class for downloading and updating data from web server.
 */
// formatter:off
/*
 * Example of use
  
  class A extends Activity {
  		private final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				// download/update has been performed
			}
		};
		
		private WebServer webServer;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			...
			this.webServer = new WebServer(handler);
			...
		}
		
		public void doWhatever() {
			webServer.doWhatever();
		}
  }
 */
// formatter:on
//FIXME implementation needed
public class WebServer implements WebServerResponseListener {
	public final static int MESSAGE_UPDATED = 0x128;
	private WebResponse webResponse;
	private Handler handler;
	
	public WebServer(Handler handler) {
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
	
	@Override
	public void onWebServerResponse(WebResponse webResponse) {
		this.webResponse = webResponse;
		handler.sendEmptyMessage(MESSAGE_UPDATED);
	}
	
	public WebResponse getWebResponse() {
		return webResponse;
	}
}