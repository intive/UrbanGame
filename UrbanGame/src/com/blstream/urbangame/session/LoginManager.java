package com.blstream.urbangame.session;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.web.server.WebServerNotificationListener;

// formatter:off
/**
 * This class manages user login session.
 * 
 * It provides the following functionalities: 
 * 		- keeping information about logged user 
 * 		- login and logout user 
 * 		- validating user data (login and password)
 * 
 * Due to this class we can easily manages logged users, write and read their
 * data from DB, and connect with server to update information about session.
 */
//formatter:on
public class LoginManager extends SessionManager {
	private static final String NAME = LoginManager.class.getSimpleName();
	
	private String email;
	
	public LoginManager(Context context, WebServerNotificationListener listener) {
		super(context, listener);
	}
	
	public LoginManager(Context context) {
		super(context);
	}
	
	public boolean isUserLoggedIn() {
		return !isUserIdEmpty() && isUserIdStoredInDB();
	}
	
	private boolean isUserIdStoredInDB() {
		return database.getLoggedPlayerID() != null;
	}
	
	private boolean isUserIdEmpty() {
		String loggedPlayerID = database.getLoggedPlayerID();
		return database.getLoggedPlayerID() != null && TextUtils.isEmpty(loggedPlayerID);
	}
	
	public boolean setLoggedUserInDB(String email) {
		Log.d(TAG, NAME + " setLoggedUserInDB() " + email);
		
		return database.setLoggedPlayer(email);
	}
	
	public void loginUser(String email, String password) {
		Log.d(TAG, NAME + " loginUser() " + email + " with password " + password);
		
		web.loginUser(email, password);
	}
	
	public void logoutUser() {
		Log.d(TAG, NAME + " " + database.getLoggedPlayerID() + " logging out");
		
		if (database.setNoOneLogged()) {
			startMainActivity();
		}
	}
	
	public void storeUserInDB(Player player) {
		Log.d(TAG, NAME + " storeUserInDB()");
		
		boolean loginResult = player != null;
		if (loginResult) {
			if (!doesPlayerExist(email)) {
				addUserToDB(player);
			}
			else {
				updatePlayerInDB(player);
			}
		}
	}
}