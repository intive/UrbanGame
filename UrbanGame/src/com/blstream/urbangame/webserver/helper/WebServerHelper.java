package com.blstream.urbangame.webserver.helper;

import android.util.Log;

import com.blstream.urbangame.webserver.asynctask.AsyncSendWebQuery;

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
	public final static String gamePath = "/urbangame/games";
	
	//
	// Declarations of the interfaces
	//
	public interface WebServerResponseInterface {
		public void onWebServerResponse(WebResponse webResponse);
	}
	
	//
	// Public methods
	//
	public static void getUrbanGameDetails(WebServerResponseInterface webServerResponseInterface, long gid) {
		// This method is issuing HTTP request to server to collect game
		// data for particular GID number.
		
		if (webServerResponseInterface != null) {
			new AsyncSendWebQuery(webServerResponseInterface, WebResponse.queryTypeGetUrbanGameDetails, gid).execute();
			
			Log.i(TAG, "getUrbanGameDetails success");
		}
		else Log.e(TAG, "getUrbanGameDetails listener not set");
	}
	
	public static void getUrbanGameBaseList(WebServerResponseInterface webServerResponseInterface) {
		// This method is issuing HTTP request to server to collect array
		// of all available Games;
		
		if (webServerResponseInterface != null) {
			new AsyncSendWebQuery(webServerResponseInterface, WebResponse.queryTypeGetUrbanGameBaseList).execute();
			
			Log.i(TAG, "getUrbanGameBaseList success");
		}
		else Log.e(TAG, "getUrbanGameBaseList listener not set");
	}
	
}