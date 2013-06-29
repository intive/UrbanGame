package com.blstream.urbangame.session;

import android.content.Context;

import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.web.WebHighLevel;
import com.blstream.urbangame.web.WebHighLevelInterface;

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
	
	public boolean register(String email, String displayName, String password) {
		
		WebHighLevelInterface web = new WebHighLevel(context);
		Player player = web.registerPlayer(email, displayName, password);
		boolean isOK = player != null;
		
		if (isOK) {
			addUserToDB(player);
		}
		
		return isOK;
	}
}