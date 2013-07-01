package com.blstream.urbangame.session;

import android.content.Context;
import android.os.Message;

import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.web.WebHighLevel;
import com.blstream.urbangame.webserver.ServerResponseHandler;
import com.blstream.urbangame.webserver.WebServerNotificationListener;

public class RegistrationManager extends SessionManager implements WebServerNotificationListener {
	private boolean registrationStatus;
	
	public RegistrationManager(Context context) {
		super(context);
		this.handler = new ServerResponseHandler(this);
		this.web = new WebHighLevel(handler, context);
	}
	
	public synchronized boolean register(String email, String displayName, String password) {
		web.registerPlayer(email, displayName, password);
		
		return registrationStatus;
	}
	
	@Override
	public synchronized void onWebServerResponse(Message message) {
		hasResult = true;
		// TODO obtain player from server response
		Player player = null;
		registrationStatus = player != null;
		
		if (registrationStatus) {
			addUserToDB(player);
		}
	}
}