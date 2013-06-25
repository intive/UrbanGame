package com.blstream.urbangame.session;

import android.content.Context;
import android.util.Log;

import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.web.WebHighLevel;
import com.blstream.urbangame.web.WebHighLevelInterface;

// formatter:off
/**
 * This class manages user login session.
 * 
 * It provides the following functionalities: - keeping information about logged
 * user - login and logout user - validating user data (login and password)
 * 
 * Due to this class we can easily manages logged users, write and read their
 * data from DB, and connect with server to update information about session.
 */
//formatter:on
public class LoginManager extends SessionManager {
	private final static String TAG = "LoginManager";
	private static LoginManager instance;
	
	private LoginManager(Context context) {
		super(context);
	}
	
	public static LoginManager getInstance(Context context) {
		if (instance == null) {
			instance = new LoginManager(context);
		}
		return instance;
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
		Log.i(TAG, email + " logging in");
		
		return database.setLoggedPlayer(email);
	}
	
	public boolean isLoginDataValid(String email, String password) {
		
		WebHighLevelInterface web = new WebHighLevel(super.context);
		Player fromWeb = web.checkUsersCredinetials(email, password);
		boolean isOK = fromWeb != null;
		
		if (isOK) {
			if (!doesPlayerExist(email)) {
				addUserToDB(fromWeb);
			}
			else {
				updatePlayerInDB(fromWeb);
			}
		}
		
		return isOK;
	}
	
	public void logoutUser() {
		Log.i(TAG, database.getLoggedPlayerID() + " logging out");
		
		if (database.setNoOneLogged()) {
			startMainActivity();
		}
	}
}