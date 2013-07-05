package com.blstream.urbangame.web;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.blstream.urbangame.UrbanGameApplication;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.dialogs.InternetAlertDialog;
import com.blstream.urbangame.example.DemoData;
import com.blstream.urbangame.webserver.ServerResponseHandler;
import com.blstream.urbangame.webserver.WebServer;

/**
 * For now it's mock-y but as time will go by, every method will be implemented
 * as it should work in real conditions.
 */
public class WebHighLevel implements WebHighLevelInterface {
	private final static String TAG = WebHighLevel.class.getSimpleName();
	private final WebServer webServer;
	private final Context context;
	private final InternetAlertDialog internetAlertDialog;
	
	public WebHighLevel(ServerResponseHandler handler, Context context) {
		this.webServer = new WebServer(context, handler);
		this.context = context;
		this.internetAlertDialog = new InternetAlertDialog(context);
	}
	
	@Override
	public void downloadGamesList() {
		if (isOnline()) {
			webServer.downloadGamesList();
		}
		new DemoData(context).insertDataIntoDatabase();
	}
	
	@Override
	public void downloadUsersGames() {
		if (isOnline()) {
			webServer.downloadUsersGames();
		}
	}
	
	@Override
	public void downloadGameDetails(Long selectedGameID) {
		if (isOnline()) {
			webServer.downloadGameDetails(selectedGameID);
		}
	}
	
	@Override
	public void joinCurrentPlayerToTheGame(Long selectedGameID) {
		if (isOnline()) {
			webServer.joinCurrentPlayerToTheGame(selectedGameID);
		}
		//******************//
		//					//
		//	   M O C K		//
		//					//
		//******************//
		DatabaseInterface db = new Database(context);
		String playerEmail = db.getLoggedPlayerID();
		Long gameID = selectedGameID;
		Log.i(TAG, "Sending information to server that player " + playerEmail + " want to join the game " + gameID);
		
		// FIXME code below should be invoked after positive response from server [move that code when appropriate class is ready]
		int rankFromServer = 10;
		PlayerGameSpecific playerGameInfo = new PlayerGameSpecific(rankFromServer, playerEmail, gameID, null, false);
		playerGameInfo.setState(PlayerGameSpecific.GAME_ACTIVE);
		PlayerGameSpecific actual = db.getUserGameSpecific(playerEmail, gameID);
		if (actual == null) {
			db.insertUserGameSpecific(playerGameInfo);
		}
		else {
			db.updateUserGameSpecific(playerGameInfo);
		}
		db.closeDatabase();
		//******************//
		//					//
		//	M O C K  E N D	//
		//					//
		//******************//
	}
	
	@Override
	public void leaveCurrentPlayerFromTheGame(Long selectedGameID) {
		if (isOnline()) {
			webServer.leaveCurrentPlayerFromTheGame(selectedGameID);
		}
		//******************//
		//					//
		//	   M O C K		//
		//					//
		//******************//
		DatabaseInterface db = new Database(context);
		String playerEmail = db.getLoggedPlayerID();
		Long gameID = selectedGameID;
		Log.i(TAG, "Sending information to server that player " + playerEmail + " leaved the game " + gameID);
		
		// FIXME code below should be invoked after positive response from server [move that code when appropriate class is ready]
		db.deleteUserGameSpecific(playerEmail, gameID);
		db.closeDatabase();
		//******************//
		//					//
		//	M O C K  E N D	//
		//					//
		//******************//
	}
	
	@Override
	public void loginUser(String email, String password) {
		if (isOnline()) {
			webServer.loginUser(email, password);
		}
	}
	
	@Override
	public void registerPlayer(String email, String displayName, String password) {
		if (isOnline()) {
			webServer.registerPlayer(email, displayName, password);
		}
	}
	
	@Override
	public void downloadTasksForGame(long gameID) {
		if (isOnline()) {
			webServer.downloadTasksForGame(gameID);
		}
	}
	
	@Override
	public void sendAnswersForABCDTask(ABCDTask task, ArrayList<String> answers) {
		if (isOnline()) {
			webServer.sendAnswersForABCDTask(task, answers);
		}
	}
	
	@Override
	public void sendAnswerForLocationTask(Task task, Location location) {
		if (isOnline()) {
			webServer.sendAnswerForLocationTask(task, location);
		}
	}
	
	@Override
	public void getCorrectAnswerForGpsTask(Task task) {
		if (isOnline()) {
			webServer.getCorrectAnswerForGpsTask(task);
		}
	}
	
	public boolean isOnline() {
		UrbanGameApplication uga = (UrbanGameApplication) context.getApplicationContext();
		if (uga.isConnectedToInternet()) return true;
		else {
			internetAlertDialog.showDialog();
			return false;
		}
	}
}