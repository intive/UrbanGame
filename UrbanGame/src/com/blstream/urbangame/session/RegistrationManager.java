package com.blstream.urbangame.session;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class RegistrationManager extends SessionManager {
	private final static String TAG = "RegistrationManager";
	private static RegistrationManager instance;
	
	private RegistrationManager(Context context) {
		super(context);
	}
	
	public static RegistrationManager getInstance(Context context) {
		if (instance == null) {
			instance = new RegistrationManager(context);
		}
		return instance;
	}
	
	public void registerUser(String email, String password, String displayName, Drawable avatar) {
		Log.d(TAG, "Registering user " + email);
		
		addUserToDB(email, password, displayName, avatar);
		// FIXME connect with server to add user data
	}
}