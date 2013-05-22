package com.blstream.urbangame.session;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blstream.urbangame.GamesListActivity;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;

public class LoginSession {
	private final static String TAG = "LoginSession";
	private static LoginSession instance;
	
	private Context context;
	private DatabaseInterface database;
	
	private LoginSession(Context context) {
		this.context = context;
		database = new Database(context);
	}
	
	public static LoginSession getInstance(Context context) {
		if (instance == null) {
			instance = new LoginSession(context);
		}
		return instance;
	}
	
	public boolean isUserLoggedIn() {
		return isUserIdStored() && !isUserIdEmpty();
	}
	
	private boolean isUserIdStored() {
		return database.getLoggedPlayerID() != null;
	}
	
	private boolean isUserIdEmpty() {
		return database.getLoggedPlayerID().length() != 0;
	}
	
	public void loginUser(String email) {
		Log.i(TAG, email + " logging in");
		
		database.setLoggedPlayer(email);
	}
	
	public void logoutUser() {
		Log.i(TAG, database.getLoggedPlayerID() + " logging out");
		
		database.setNoOneLogged();
		startMainActivity();
	}
	
	private void startMainActivity() {
		Intent intent = new Intent(context, GamesListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}
}