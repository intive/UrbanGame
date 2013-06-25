package com.blstream.urbangame.web;

import android.content.Context;
import android.util.Log;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;
import com.blstream.urbangame.example.DemoData;

/**
 * For now it's mock-y but as time will pass, every method will be implemented
 * as it should work in real conditions.
 */
public class WebHighLevel implements WebHighLevelInterface {
	
	Context context;
	String TAG = WebHighLevel.class.getSimpleName();
	
	public WebHighLevel(Context context) {
		this.context = context;
	}
	
	@Override
	public void downloadGameList() {
		new DemoData(context).insertDataIntoDatabase();
	}
	
	@Override
	public void downloadUsersGames() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void downloadGameDetails(Long selectedGameID) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void joinCurrentPlayerToTheGame(Long selectedGameID) {
		// FIXME invocation to server should occur here
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
	public void leaveCurrentPlayerToTheGame(Long selectedGameID) {
		// FIXME invocation to server should occur here
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
	public Player checkUsersCredinetials(String email, String password) {
		// TODO Auto-generated method stub
		return new Player(email, password, null, (String) null);
	}
	
	@Override
	public Player registerPlayer(String email, String displayName, String password) {
		// TODO Auto-generated method stub
		return new Player(email, password, displayName, (String) null);
	}
	
	@Override
	public void downloadTasksForGame(long gameID) {
		// TODO Auto-generated method stub
	}
	
}
