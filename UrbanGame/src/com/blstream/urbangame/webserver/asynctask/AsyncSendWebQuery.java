package com.blstream.urbangame.webserver.asynctask;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.blstream.urbangame.webserver.helper.WebResponse;
import com.blstream.urbangame.webserver.helper.WebResponse.QueryType;
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
	private long gid;
	private long tid;
	
	//
	// Constructors
	//
	public AsyncSendWebQuery(WebServerResponseInterface _webServerResponseListener, QueryType queryType) {
		init(_webServerResponseListener, queryType);
	}
	
	public AsyncSendWebQuery(WebServerResponseInterface _webServerResponseListener, QueryType queryType, long gid) {
		this.gid = gid;
		init(_webServerResponseListener, queryType);
	}
	
	public AsyncSendWebQuery(WebServerResponseInterface _webServerResponseListener, QueryType queryType, long gid, long tid) {
		this.gid = gid;
		this.tid = tid;
		init(_webServerResponseListener, queryType);
	}
	
	//
	// Private methods
	//
	private void init(WebServerResponseInterface _webServerResponseListener, QueryType queryType) {
		webQuery = new Uri.Builder();
		webServerResponseListener = _webServerResponseListener;
		webResponse = new WebResponse(queryType);
		
		webQuery.scheme(WebServerHelper.scheme);
		webQuery.authority(WebServerHelper.authority);
		webQuery.path(WebServerHelper.basePath);
		
		
		switch (queryType) {
			case GetUrbanGameBaseList:
				webQuery.appendPath(WebServerHelper.gameSubPath);
				break;
			
			case GetUrbanGameDetails:
				webQuery.appendPath(WebServerHelper.gameSubPath);
				webQuery.appendPath(String.valueOf(gid));
				break;
			
			case GetTaskList:
				createGetTaskListQuery();
				webResponse.setGameId(gid);
				break;
			
			case GetTask:
				createGetTaskListQuery();
				webQuery.appendPath(String.valueOf(tid));
				webResponse.setGameId(gid);
				break;
			default:
				Log.e(TAG, "Incorrect queryType " + queryType.toString());
				break;	
		}
		
		
	}
	
	private void createGetTaskListQuery() {
		webQuery.appendPath(WebServerHelper.gameSubPath);
		webQuery.appendPath(String.valueOf(gid));
		webQuery.appendPath(WebServerHelper.taskSubpath);
	}
	
	//
	// Implementation of the base methods
	//
	@Override
	protected WebResponse doInBackground(Void... params) {
		
		MockWebServer mockWebServer = new MockWebServer();
		String responseString = mockWebServer.getResponse(webQuery.toString(), webResponse.getQueryType(), gid, tid);
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