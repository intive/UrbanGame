package com.blstream.urbangame.session;

import android.content.Context;
import android.util.Log;

import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.web.server.WebServerNotificationListener;

public class RegistrationManager extends SessionManager {
	private static final String NAME = RegistrationManager.class.getSimpleName();
	
	public RegistrationManager(Context context, WebServerNotificationListener listener) {
		super(context, listener);
	}
	
	public synchronized void register(String email, String displayName, String password) {
		Log.d(TAG, NAME + " register()");
		
		web.registerPlayer(email, displayName, password);
	}
	
	public void storeUserInDB(Player player) {
		Log.d(TAG, NAME + " storeUserInDB()");
		
		boolean registrationStatus = player != null;
		if (registrationStatus) {
			addUserToDB(player);
		}
	}
}