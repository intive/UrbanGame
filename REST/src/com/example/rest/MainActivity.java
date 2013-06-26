package com.example.rest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Toast.makeText(MainActivity.this, webServer.getWebResponse().getResponse(), Toast.LENGTH_SHORT).show();
		}
	};
	
	private WebServer webServer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.webServer = new WebServer(handler);
	}
	
	public void onClick(View w) {
		webServer.doWhatever();
	}
}