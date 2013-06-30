package com.blstream.urbangame.session;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.web.WebHighLevel;
import com.blstream.urbangame.webserver.ServerResponseHandler;
import com.blstream.urbangame.webserver.WebServerNotificationListener;

public class RegistrationManager extends SessionManager implements WebServerNotificationListener {
	private final static String TAG = "RegistrationManager";
	private static RegistrationManager instance;
	private boolean registrationStatus;
	
	private RegistrationManager(Context context) {
		super(context);
		this.handler = new ServerResponseHandler(this);
		this.web = new WebHighLevel(handler, context);
	}
	
	public static RegistrationManager getInstance(Context context) {
		if (instance == null) {
			instance = new RegistrationManager(context);
		}
		return instance;
	}
	
	public synchronized boolean register(String email, String displayName, String password) {
		web.registerPlayer(email, displayName, password);
		waitForServerResponse();
		
		return registrationStatus;
	}
	
	protected void waitForServerResponse() {
		try {
			wait();
		}
		catch (InterruptedException e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	@Override
	public synchronized void onWebServerResponse(Message message) {
		// TODO obtain player from server response
		Player player = null;
		registrationStatus = player != null;
		
		if (registrationStatus) {
			addUserToDB(player);
		}
		notify();
	}
}