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
	
	/**
	 * @param  webServerResponseInterface - listener to be called after
	 *         a query completed
	 * @param  gid - id of a game
	 * @return on failure listener which you provided as the first argument
	 *         will receive null WebResponse object. Otherwise WebResponse will
	 *         not be null.
	 */
	public static void getUrbanGameDetails(WebServerResponseInterface webServerResponseInterface, long gid) {
		if (webServerResponseInterface != null) {
			new AsyncSendWebQuery(webServerResponseInterface, QueryType.GetUrbanGameDetails, gid).execute();
			
			Log.i(TAG, "getUrbanGameDetails success");
		}
		else {
			Log.e(TAG, "getUrbanGameDetails listener not set");
		}
	}
	
	/**
	 * @param  webServerResponseInterface - listener to be called after
	 *         a query completed
	 * @return on failure listener which you provided as the first argument
	 *         will receive null WebResponse object. Otherwise WebResponse will
	 *         not be null.
	 */
	public static void getUrbanGameBaseList(WebServerResponseInterface webServerResponseInterface) {
		if (webServerResponseInterface != null) {
			new AsyncSendWebQuery(webServerResponseInterface, QueryType.GetUrbanGameBaseList).execute();
			
			Log.i(TAG, "getUrbanGameBaseList success");
		}
		else {
			Log.e(TAG, "getUrbanGameBaseList listener not set");
		}
	}
	
	/**
	 * @param  webServerResponseInterface - listener to be called after
	 *         a query completed
	 * @param  gid - id of a game
	 * @return on failure listener which you provided as the first argument
	 *         will receive null WebResponse object. Otherwise WebResponse will
	 *         not be null.
	 */
	public static void getTaskList(WebServerResponseInterface webServerResponseInterface, long gid) {
		
		if (webServerResponseInterface != null) {
			new AsyncSendWebQuery(webServerResponseInterface, QueryType.GetTaskList, gid).execute();
			
			Log.i(TAG, "getTaskList success");
		}
		else {
			Log.e(TAG, "getTaskList listener not set");
		}
	}
	
	/**
	 * @param webServerResponseInterface - listener to be called after
	 *         a query completed
	 * @param  gid - id of a game
	 * @param  tid - id of a task
	 * @return on failure listener which you provided as the first argument
	 *         will receive null WebResponse object. Otherwise WebResponse will
	 *         not be null.
	 */
	public static void getTask(WebServerResponseInterface webServerResponseInterface, long gid, long tid) {
		if (webServerResponseInterface != null) {
			new AsyncSendWebQuery(webServerResponseInterface, QueryType.GetTask, gid, tid).execute();
			
			Log.i(TAG, "getTask success");
		}
		else {
			Log.e(TAG, "getTask listener not set");
		}
	}
	
}