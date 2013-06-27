package com.blstream.urbangame.webserver;

import android.app.IntentService;
import android.content.Intent;

// FIXME implementation needed
public class WebCyclicService extends IntentService implements WebServerResponseListener {
	public final static String VERSION_TYPE = "version_type";
	public final static String VERSION_TASK = "version_game";
	public final static String VERSION_GAME = "version_task";
	public final static String VERSION = "version";
	
	public final static String ID_TASK = "id_task";
	public final static String ID_GAME = "id_game";
	
	public WebCyclicService() {
		super(WebCyclicService.class.getSimpleName());
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {}
	
	@Override
	public void onWebServerResponse(WebResponse webResponse) {
		
	}
}