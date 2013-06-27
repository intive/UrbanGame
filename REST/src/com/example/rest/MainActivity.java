package com.example.rest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements WebServerNotificationListener {
	private ServerResponseHandler handler;
	private WebServer webServer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.handler = new ServerResponseHandler(this);
		this.webServer = new WebServer(handler);
	}
	
	public void onClick(View w) {
		webServer.doWhatever();
	}
	
	@Override
	public void onWebServerResponse(Message message) {
		WebResponse webResponse = (WebResponse) message.obj;
		Bundle bundle = message.getData();
		Toast.makeText(this, webResponse.getResponse(), Toast.LENGTH_LONG).show();
	}
}