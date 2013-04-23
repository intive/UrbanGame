package com.blstream.urbangame.webserver.asynctask;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.blstream.urbangame.webserver.helper.WebResponse;
import com.blstream.urbangame.webserver.helper.WebServerHelper;
import com.blstream.urbangame.webserver.helper.WebServerHelper.WebServerResponseInterface;
import com.blstream.urbangame.webserver.mock.MockWebServer;

/* AsyncSendWebQuery is a class which is responsible for obtaining information
 * from web server by issuing HTTP GET query. Pass appropriate "queryType" as
 * constructor argument to issue query which you want. */

public class AsyncSendWebQuery extends AsyncTask<Void, Void, WebResponse> {
	private final String TAG = "AsyncSendWebQuery";
	
	private WebResponse webResponse;
	private Uri.Builder webQuery;
	private WebServerResponseInterface webServerResponseListener;
	
	//
	// Constructors
	//
	public AsyncSendWebQuery(WebServerResponseInterface _webServerResponseListener, int queryType) {
		init(_webServerResponseListener, queryType);
		createGetUrbanGameBaseListQuery();
		
	}
	
	public AsyncSendWebQuery(WebServerResponseInterface _webServerResponseListener, int queryType, long gid) {
		init(_webServerResponseListener, queryType);
		createGetUrbanGameDetailsQuery(gid);
	}
	
	//
	// Private methods
	//
	private void init(WebServerResponseInterface _webServerResponseListener, int queryType) {
		webQuery = new Uri.Builder();
		webServerResponseListener = _webServerResponseListener;
		webResponse = new WebResponse(queryType);
	}
	
	private void createGetUrbanGameBaseListQuery() {
		// Query to get list of all available games
		webQuery.scheme(WebServerHelper.scheme);
		webQuery.authority(WebServerHelper.authority);
		webQuery.path(WebServerHelper.gamePath);
	}
	
	private void createGetUrbanGameDetailsQuery(long gid) {
		// Query to get an information about a particular game
		createGetUrbanGameBaseListQuery();
		webQuery.appendQueryParameter("gid", String.valueOf(gid));
	}
	
	//
	// Implementation of the base methods
	//
	@Override
	protected WebResponse doInBackground(Void... params) {
		
		MockWebServer mockWebServer = new MockWebServer();
		String responseString = mockWebServer.getResponse(webQuery.toString());
		Log.i(TAG, "response: " + responseString);
		
		if (responseString == null) return null;
		
		webResponse.setJsonResponse(responseString);
		return webResponse;
	}
	
	@Override
	protected void onPostExecute(WebResponse result) {
		webServerResponseListener.onWebServerResponse(result);
	}
}