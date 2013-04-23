package com.example.webserverhelper.asynctasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.webserverhelper.helpers.WebResponse;
import com.example.webserverhelper.helpers.WebServerHelper;
import com.example.webserverhelper.helpers.WebServerHelper.WebServerResponseInterface;
import com.example.webserverhelper.mocks.MockWebServer;

/*
 AsyncSendWebQuery is a class which is responsible for obtaining information
 from web server about a single UrbanGame by issuing HTTP GET query.

 Remember to create appropriate webQuery before doing:
 AsyncSendWebQuery.execute(). For instance if you want to get
 information about UrbanGame use this class as follows:

 AsyncSendWebQuery asyncSendWebQuery  = 
 new AsyncSendWebQuery(webServerResponseListener,
 webResponse.QueryTypeGetUrbanGameArray);
 asyncSendWebQuery.createGetUrbanGameQuery(gid); // create webQuery
 asyncSendWebQuery.execute();                    // execute query
 */

public class AsyncSendWebQuery extends AsyncTask<Void, Void, WebResponse>
{
	private final String TAG = "AsyncSendWebQuery";

	private WebResponse webResponse;
	private Uri.Builder webQuery;
	private WebServerResponseInterface webServerResponseListener;

	//
	// Constructor
	//
	public AsyncSendWebQuery(
			WebServerResponseInterface _webServerResponseListener,
			int responseType)
	{
		webQuery = new Uri.Builder();
		webResponse = new WebResponse(responseType);
		webServerResponseListener = _webServerResponseListener;
	}

	//
	// Public methods
	//
	public void createGetUrbanGameArrayQuery()
	{
		// Query to get list of all available games
		webQuery.scheme(WebServerHelper.scheme);
		webQuery.authority(WebServerHelper.authority);
		webQuery.path(WebServerHelper.gamePath);
	}

	public void createGetUrbanGameQuery(long gid)
	{
		// Query to get an information about a particular game
		createGetUrbanGameArrayQuery();
		webQuery.appendQueryParameter("gid", String.valueOf(gid));
	}

	//
	// Implementation of the base methods
	//
	@Override
	protected WebResponse doInBackground(Void... params)
	{
		MockWebServer mockWebServer = new MockWebServer();
		String responseString = mockWebServer.getResponse(webQuery.toString());

		Log.i(TAG, "response: " + responseString);

		if (responseString == null)
			return null;

		webResponse.setJsonResponse(responseString);
		return webResponse;

	}

	@Override
	protected void onPostExecute(WebResponse result)
	{
		webServerResponseListener.onWebServerResponse(result);
	}
}