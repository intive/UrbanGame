package com.blstream.urbangame.web;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.example.DemoData;
import com.blstream.urbangame.fragments.ABCDTaskAnswerFragment.ServerResponseToSendedAnswers;
import com.blstream.urbangame.webserver.mock.MockWebServer;

/**
 * For now it's mock-y but as time will go by, every method will be implemented
 * as it should work in real conditions.
 */
public class WebHighLevel implements WebHighLevelInterface {
	
	Context context;
	String TAG = WebHighLevel.class.getSimpleName();
	
	public WebHighLevel(Context context) {
		this.context = context;
	}
	
	@Override
	public synchronized void downloadGameList() {
		new DemoData(context).insertDataIntoDatabase();
	}
	
	@Override
	public synchronized void downloadUsersGames() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public synchronized void downloadGameDetails(Long selectedGameID) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public synchronized void joinCurrentPlayerToTheGame(Long selectedGameID) {
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
	public synchronized void leaveCurrentPlayerFromTheGame(Long selectedGameID) {
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
	public synchronized Player loginUser(String email, String password) {
		// TODO Auto-generated method stub
		return new Player(email, password, null, (String) null);
	}
	
	@Override
	public synchronized Player registerPlayer(String email, String displayName, String password) {
		// TODO Auto-generated method stub
		return new Player(email, password, displayName, (String) null);
	}
	
	@Override
	public synchronized void downloadTasksForGame(long gameID) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public ServerResponseToSendedAnswers sendAnswersForABCDTask(ABCDTask task, ArrayList<String> answers) {
		
		ServerResponseToSendedAnswers serverResponse = new ServerResponseToSendedAnswers();
		
		ArrayList<String> correctAnswers = null;
		
		correctAnswers = DemoData.getCorrectAnswers();
		
		if (correctAnswers != null) {
			
			int maxPoints = task.getMaxPoints();
			int points = 0;
			int numberOfCorrectAnswers = 0;
			
			for (String element : correctAnswers) {
				if (answers.contains(element)) {
					numberOfCorrectAnswers++;
				}
			}
			
			if (numberOfCorrectAnswers == correctAnswers.size()) {
				points = maxPoints;
			}
			else {
				points = maxPoints / correctAnswers.size() * numberOfCorrectAnswers;
			}
			
			serverResponse.correctAnswers = correctAnswers;
			serverResponse.points = points;
		}
		else {
			serverResponse.noInternetConnection = true;
		}
		
		return serverResponse;
	}
	
	@Override
	public int sendAnswerForLocationTask(Task task, Location location) {
		MockWebServer mockWebServer = new MockWebServer();
		return mockWebServer.sendGPSLocation(context, task, location);
	}
	
	@Override
	public Location getCorrectAnswerForGpsTask(Task task) {
		return new MockWebServer().getCorrectGpsLocation(task);
	}
}
