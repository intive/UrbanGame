package com.blstream.urbangame.web;

import java.util.ArrayList;

import android.location.Location;

import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.web.server.WebServer;

/**
 * WebHighLevelInterface is interface provided for user to perform specific
 * actions on {@link WebServer}. All of methods don't return anything because
 * user get server response only in onWebServerResponse() method required by
 * handler which is used to create {@link WebServer} object.
 */
public interface WebHighLevelInterface {
	
	/**
	 * it downloads games basic info and inserts it into database
	 */
	void downloadGamesList();
	
	/**
	 * it downloads all games that user is participating and inserts them into
	 * database
	 */
	void downloadUsersGames();
	
	/**
	 * it downloads game's details and inserts into database
	 * 
	 * @param game's ID to be downloaded if not already in database.
	 */
	void downloadGameDetails(Long selectedGameID);
	
	/**
	 * it joins player to the provided game
	 * 
	 * @param selectedGameID - game's ID to be joined by current user
	 */
	void joinCurrentPlayerToTheGame(Long selectedGameID);
	
	/**
	 * it dis-joins player from the game
	 * 
	 * @param selectedGameID - game's ID to be leaved by current user
	 */
	void leaveCurrentPlayerFromTheGame(Long selectedGameID);
	
	/**
	 * it check if user can "log in" (REST is stateless so no session - check if
	 * can get some resource | consult with web)
	 * 
	 * @param email - user's email
	 * @param password - provided password
	 */
	void loginUser(String email, String password);
	
	/**
	 * if registers user in web server
	 * 
	 * @param email
	 * @param displayName
	 * @param password
	 */
	void registerPlayer(String email, String displayName, String password);
	
	/**
	 * it downloads all tasks for game with data for current user if they are
	 * not already in database and inserts it into database.
	 * 
	 * @param gameID - game of which tasks should be downloaded
	 */
	void downloadTasksForGame(long gameID);
	
	/**
	 * it sends answers for ABCD task to server in order to verify
	 * 
	 * @param task
	 * @param answers
	 */
	void sendAnswersForABCDTask(ABCDTask task, ArrayList<String> answers);
	
	/**
	 * it sends answer for location task to server in order to verify
	 * 
	 * @param task
	 * @param location
	 */
	void sendAnswerForLocationTask(Task task, Location location);
	
	/**
	 * it asks server for correct answer location
	 * 
	 * @param task
	 * @return
	 */
	void getCorrectAnswerForGpsTask(Task task);
}