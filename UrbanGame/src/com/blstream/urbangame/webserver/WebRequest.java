package com.blstream.urbangame.webserver;

import android.net.Uri;
import android.os.AsyncTask;

import com.blstream.urbangame.webserver.WebServer.QueryType;

/**
 * This class represents simple asynchronous background HTTP request for
 * specific resource on web server and passes response to registered listener
 */
//FIXME implementation needed
public class WebRequest extends AsyncTask<Void, Integer, WebResponse> {
	private Uri requestUri;
	private WebResponse webResponse;
	private WebDownloader webDownloader;
	
	private WebServerResponseListener webServerResponseListener;
	private int gameID;
	private int taskID;
	
	public WebRequest(WebServerResponseListener webServerResponseListener, QueryType queryType) {
		this(webServerResponseListener, queryType, 0, 0);
	}
	
	public WebRequest(WebServerResponseListener webServerResponseListener, QueryType queryType, int gameID) {
		this(webServerResponseListener, queryType, gameID, 0);
	}
	
	public WebRequest(WebServerResponseListener webServerResponseListener, QueryType queryType, int gameID, int taskID) {
		this.gameID = gameID;
		this.taskID = taskID;
		this.webServerResponseListener = webServerResponseListener;
		configureRequestUri(queryType);
	}
	
	private void configureRequestUri(QueryType queryType) {
		this.webResponse = new WebResponse(queryType);
		
		switch (queryType) {
			case GetGamesList:
				this.requestUri = WebAPI.getAllGamesUri();
				this.webDownloader = new WebDownloaderGET();
				break;
			
			case GetGameDetails:
				this.requestUri = WebAPI.getGameDetailUri(gameID);
				this.webDownloader = new WebDownloaderGET();
				break;
			
			case GetTasksList:
				this.requestUri = WebAPI.getAllTasksUri(gameID);
				this.webDownloader = new WebDownloaderGET();
				break;
			
			case GetTaskDetails:
				this.requestUri = WebAPI.getTaskDetailUri(gameID, taskID);
				this.webDownloader = new WebDownloaderGET();
				break;
			
			/*
			 *  and so on ...
			 *  Combination of requestUri and webDownloader will be used
			 */
			
			default:
				break;
		}
	}
	
	@Override
	protected WebResponse doInBackground(Void... args) {
		String responseString = webDownloader.executeRequest(requestUri);
		webResponse.setResponse(responseString);
		return webResponse;
	}
	
	@Override
	protected void onPostExecute(WebResponse result) {
		webServerResponseListener.onWebServerResponse(result);
	}
}