package com.example.webserverhelper.helpers;

import android.util.Log;

import com.example.webserverhelper.asynctasks.AsyncSendWebQuery;

/*
 WebServerHelper is an intermediary class which issues HTTP requests
 to web server.

 After query to web server has been completed listener which you
 provided as first argument to one of WebServerHelper query methods
 (for example to "getUrbanGame" method) will be called.
 */

public class WebServerHelper
{
	private final String TAG = "WebServerHelper";

	public final static String scheme = "http";
	public final static String authority = "blstream.com";
	public final static String gamePath = "/urbangame/games";

	//
	// Declarations of the interfaces
	//
	public interface WebServerResponseInterface
	{
		public void onWebServerResponse(WebResponse webResponse);
	}

	//
	// Public methods
	//
	public void getUrbanGame(
			WebServerResponseInterface webServerResponseInterface, long gid)
	{
		// This method is issuing HTTP request to server to collect game
		// data for particular GID number.

		if (webServerResponseInterface != null)
		{
			AsyncSendWebQuery asyncSendWebQuery =
					new AsyncSendWebQuery(webServerResponseInterface,
							WebResponse.queryTypeGetUrbanGame);
			asyncSendWebQuery.createGetUrbanGameQuery(gid);
			asyncSendWebQuery.execute();

			Log.i(TAG, "getUrbanGame success");
		}
		else
			Log.e(TAG, "getUrbanGame listener not set");
	}

	public void getUrbanGameArray(
			WebServerResponseInterface webServerResponseInterface)
	{

		// This method is issuing HTTP request to server to collect array
		// of all available Games;

		if (webServerResponseInterface != null)
		{
			AsyncSendWebQuery asyncSendWebQuery =
					new AsyncSendWebQuery(webServerResponseInterface,
							WebResponse.queryTypeGetUrbanGameArray);
			asyncSendWebQuery.createGetUrbanGameArrayQuery();
			asyncSendWebQuery.execute();

			Log.i(TAG, "getUrbanGameArray success");
		}
		else
			Log.e(TAG, "getUrbanGameArray listener not set");
	}

}