package com.blstream.urbangame.session;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.blstream.urbangame.GamesListActivity;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.Player;

//formatter:off
/**
* This class manages user session.
* 
* It provides the following functionalities:
* 	- adding users to DB
* 
* Due to this class we can easily connect with server
* to check and compare local and online data.
*/
//formatter:on
public abstract class SessionManager {
	protected DatabaseInterface database;
	protected Context context;
	
	protected SessionManager(Context context) {
		this.context = context;
		this.database = new Database(context);
	}
	
	public boolean doesPlayerExist(String email) {
		return getPlayerFromDB(email) != null;
	}
	
	protected Player getPlayerFromDB(String email) {
		// FIXME connect with server and download user
		return database.getPlayer(email);
	}
	
	protected boolean doesPlayerExist(Player player) {
		return player != null;
	}
	
	protected void startMainActivity() {
		Intent intent = new Intent(context, GamesListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}
	
	protected void addUserToDB(String email, String password, String displayName, Drawable avatar) {
		Player player = new Player(email, password, displayName, avatar);
		database.insertUser(player);
		// FIXME connect with server and add user
	}
}