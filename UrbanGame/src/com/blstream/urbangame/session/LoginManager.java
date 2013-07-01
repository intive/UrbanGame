package com.blstream.urbangame.session;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.web.WebHighLevel;
import com.blstream.urbangame.webserver.ServerResponseHandler;
import com.blstream.urbangame.webserver.WebServerNotificationListener;

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
public class LoginManager extends SessionManager implements WebServerNotificationListener {
	private final static String TAG = "LoginManager";
	private boolean loginResult;
	private String email;
	
	public LoginManager(Context context) {
		super(context);
		this.handler = new ServerResponseHandler(this);
		this.web = new WebHighLevel(handler, context);
	}
	
	public boolean isUserLoggedIn() {
		return isUserIdStoredInDB() && !isUserIdEmpty();
	}
	
	private boolean isUserIdStoredInDB() {
		return database.getLoggedPlayerID() != null;
	}
	
	private boolean isUserIdEmpty() {
		return database.getLoggedPlayerID().length() == 0;
	}
	
	public boolean loginUser(String email) {
		this.email = email;
		Log.i(TAG, email + " logging in");
		
		return database.setLoggedPlayer(email);
	}
	
	public void isLoginDataValid(String email, String password) {
		web.loginUser(email, password);
		waitForServerResponse();
	}
	
	public void logoutUser() {
		Log.i(TAG, database.getLoggedPlayerID() + " logging out");
		
		if (database.setNoOneLogged()) {
			startMainActivity();
		}
	}
	
	@Override
	public synchronized void onWebServerResponse(Message message) {
		hasResult = true;
		
		// TODO obtain player from server response
		Player fromWeb = null;
		
		loginResult = fromWeb != null;
		
		if (loginResult) {
			if (!doesPlayerExist(email)) {
				addUserToDB(fromWeb);
			}
			else {
				updatePlayerInDB(fromWeb);
			}
		}
	}
}