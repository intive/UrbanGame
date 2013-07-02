package com.blstream.urbangame.webserver;

import java.util.ArrayList;

import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;

import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.webserver.WebServer.QueryType;

/**
 * This class represents simple asynchronous background HTTP request for
 * specific resource on web server and passes response back to server
 */
//FIXME implementation needed
public class WebRequest extends AsyncTask<Void, Integer, WebResponse> {
	private WebResponse webResponse;
	private WebDownloader webDownloader;
	private QueryType queryType;
	private WebServer webServer;
	
	/* Data to send */
	private long gameID;
	private long taskID;
	private String email;
	private String password;
	private String displayName;
	private Location location;
	private ArrayList<String> answers;
	private Task task;
	private ABCDTask abcdTask;
	
	public WebRequest(WebServer webServer, QueryType queryType) {
		this(webServer, queryType, 0, 0);
	}
	
	public WebRequest(WebServer webServer, QueryType queryType, long gameID) {
		this(webServer, queryType, gameID, 0);
	}
	
	public WebRequest(WebServer webServer, QueryType queryType, long gameID, long taskID) {
		this.gameID = gameID;
		this.taskID = taskID;
		
		this.queryType = queryType;
		this.webServer = webServer;
		this.webResponse = new WebResponse(queryType);
	}
	
	public WebRequest(WebServer webServer, QueryType queryType, String email, String password) {
		this(webServer, queryType, email, password, "");
	}
	
	public WebRequest(WebServer webServer, QueryType queryType, String email, String password, String displayName) {
		this.email = email;
		this.password = password;
		this.displayName = displayName;
		
		this.queryType = queryType;
		this.webServer = webServer;
		this.webResponse = new WebResponse(queryType);
	}
	
	public WebRequest(WebServer webServer, QueryType queryType, Task task) {
		this(webServer, queryType, task, null);
	}
	
	public WebRequest(WebServer webServer, QueryType queryType, Task task, Location location) {
		this.task = task;
		this.location = location;
		
		this.queryType = queryType;
		this.webServer = webServer;
		this.webResponse = new WebResponse(queryType);
	}
	
	public WebRequest(WebServer webServer, QueryType queryType, ABCDTask abcdTask, ArrayList<String> answers) {
		this.abcdTask = abcdTask;
		this.answers = answers;
		
		this.queryType = queryType;
		this.webServer = webServer;
		this.webResponse = new WebResponse(queryType);
	}
	
	@Override
	protected WebResponse doInBackground(Void... args) {
		Uri requestUri = downloadRequestUri();
		if (requestUri != null) {
			String responseString = webDownloader.executeRequest(requestUri);
			webResponse.setResponse(responseString);
		}
		return webResponse;
	}
	
	/**
	 * Specific {@link WebDownloader} and request URI is configured here
	 * regarding to query type.
	 */
	private Uri downloadRequestUri() {
		WebAPI webAPI;
		
		switch (queryType) {
			case DownloadGamesList:
				this.webDownloader = new WebDownloaderGET();
				webAPI = new WebAPI(webDownloader);
				return webAPI.getAllGamesUri();
			case DownloadUsersGames:
				this.webDownloader = new WebDownloaderGET();
				webAPI = new WebAPI(webDownloader);
				return null;
			case DownloadGameDetails:
				this.webDownloader = new WebDownloaderGET();
				webAPI = new WebAPI(webDownloader);
				return null;
			case DownloadTasksForGame:
				this.webDownloader = new WebDownloaderGET();
				webAPI = new WebAPI(webDownloader);
				return null;
			case DownloadTaskDetails:
				this.webDownloader = new WebDownloaderGET();
				webAPI = new WebAPI(webDownloader);
				break;
			case LoginUser:
				this.webDownloader = new WebDownloaderPOST();
				webAPI = new WebAPI(webDownloader);
				return null;
			case RegisterPlayer:
				this.webDownloader = new WebDownloaderPOST();
				webAPI = new WebAPI(webDownloader);
				return null;
			case JoinCurrentPlayerToTheGame:
				this.webDownloader = new WebDownloaderPOST();
				webAPI = new WebAPI(webDownloader);
				return null;
			case LeaveCurrentPlayerFromTheGame:
				this.webDownloader = new WebDownloaderDELETE();
				webAPI = new WebAPI(webDownloader);
				return null;
			case SendAnswersForABCDTask:
				this.webDownloader = new WebDownloaderPUT();
				webAPI = new WebAPI(webDownloader);
				return null;
			case SendAnswerForLocationTask:
				this.webDownloader = new WebDownloaderPUT();
				webAPI = new WebAPI(webDownloader);
				return null;
			case GetCorrectAnswerForGpsTask:
				this.webDownloader = new WebDownloaderGET();
				webAPI = new WebAPI(webDownloader);
				return null;
			default:
				return null;
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(WebResponse result) {
		webServer.onWebServerResponse(result);
	}
}