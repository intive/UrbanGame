package com.blstream.urbangame.webserver.helper;

import android.util.Log;

import com.blstream.urbangame.webserver.asynctask.AsyncSendWebQuery;
import com.blstream.urbangame.webserver.helper.WebResponse.QueryType;

/* WebServerHelper is an intermediary class which issues HTTP requests to web
 * server. After query to web server has been completed listener which you
 * provided as first argument to one of WebServerHelper query methods (for
 * example to "getUrbanGameDetails()" method) will be called. If there was not
 * correct response from web server (for example because of timeout) then you
 * will get as an answer null WebResponse object in your listener. */

public class WebServerHelper {
	private final static String TAG = "WebServerHelper";
	
	public final static String scheme = "http";
	public final static String authority = "blstream.com";
	public final static String basePath = "urbangame";
	public final static String gameSubPath = "games";
	public final static String taskSubpath = "tasks";
	
	//
	// Declarations of the interfaces
	//
	public interface WebServerResponseInterface {
		// This method will be called after a query to web server has completed.
		// If the query was successful webResponse is not null.
		// If the query failed webResponse is null.
		public void onWebServerResponse(WebResponse webResponse);
	}
	
	//
	// Public methods
	//
	public static void getUrbanGameDetails(WebServerResponseInterface webServerResponseInterface, long gid) {
		// This method issues HTTP request to server to collect game
		// data for particular GID number.
		
		if (webServerResponseInterface != null) {
			new AsyncSendWebQuery(webServerResponseInterface, QueryType.GetUrbanGameDetails, gid).execute();
			
			Log.i(TAG, "getUrbanGameDetails success");
		}
		else {
			Log.e(TAG, "getUrbanGameDetails listener not set");
		}
	}
	
	public static void getUrbanGameBaseList(WebServerResponseInterface webServerResponseInterface) {
		// This method issues HTTP request to server to collect list
		// of all available games;
		
		if (webServerResponseInterface != null) {
			new AsyncSendWebQuery(webServerResponseInterface, QueryType.GetUrbanGameBaseList).execute();
			
			Log.i(TAG, "getUrbanGameBaseList success");
		}
		else {
			Log.e(TAG, "getUrbanGameBaseList listener not set");
		}
	}
	
	public static void getTaskList(WebServerResponseInterface webServerResponseInterface, long gid) {
		// This method issues HTTP request to server to collect Task list
		// for particular game.
		
		if (webServerResponseInterface != null) {
			new AsyncSendWebQuery(webServerResponseInterface, QueryType.GetTaskList, gid).execute();
			
			Log.i(TAG, "getTaskList success");
		}
		else {
			Log.e(TAG, "getTaskList listener not set");
		}
	}
	
	public static void getTask(WebServerResponseInterface webServerResponseInterface, long gid, long tid) {
		// This method issues HTTP request to server to collect information
		// about particular Task that belongs to a specified game.
		
		if (webServerResponseInterface != null) {
			new AsyncSendWebQuery(webServerResponseInterface, QueryType.GetTask, gid, tid).execute();
			
			Log.i(TAG, "getTask success");
		}
		else {
			Log.e(TAG, "getTask listener not set");
		}
	}
	
}