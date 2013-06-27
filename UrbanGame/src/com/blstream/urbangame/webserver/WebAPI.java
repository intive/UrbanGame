package com.blstream.urbangame.webserver;

import android.net.Uri;
import android.net.Uri.Builder;

/**
 * This class is used to obtain specific path for required resource in WebServer
 */
//FIXME implementation needed
public class WebAPI {
	private final static String base = "http://urbangame.patronage.blstream.com/api";
	private final static String games = "games";
	private final static String tasks = "tasks";
	private final static String user = "my";
	
	private static Builder path;
	
	protected static Builder getBasicPath() {
		Uri baseUri = Uri.parse(base);
		return baseUri.buildUpon();
	}
	
	public static Uri getAllGamesUri() {
		path = getBasicPath();
		path.appendPath(games);
		return path.build();
	}
	
	public static Uri getGameDetailUri(int gameID) {
		path = getAllGamesUri().buildUpon();
		path.appendPath(String.valueOf(gameID));
		return path.build();
	}
	
	public static Uri getAllTasksUri(int gameID) {
		path = getGameDetailUri(gameID).buildUpon();
		path.appendPath(tasks);
		return path.build();
	}
	
	public static Uri getTaskDetailUri(int gameID, int taskID) {
		path = getGameDetailUri(gameID).buildUpon();
		path.appendPath(String.valueOf(taskID));
		return path.build();
	}
	
	public static Uri getUserGameDetailsUri(int gameID) {
		path = getBasicPath();
		path.appendPath(user);
		path.appendPath(getGameDetailUri(gameID).toString());
		return path.build();
	}
	
	public static Uri getUserTaskDetailsUri(int gameID, int taskID) {
		path = getUserGameDetailsUri(gameID).buildUpon();
		return path.build();
	}
	
	/// and so on ...
}