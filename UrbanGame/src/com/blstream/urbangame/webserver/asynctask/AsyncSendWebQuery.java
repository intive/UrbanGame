package com.blstream.urbangame.webserver.asynctask;

import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.blstream.urbangame.webserver.deserialization.JsonResponse;
import com.blstream.urbangame.webserver.helper.WebResource;
import com.blstream.urbangame.webserver.helper.WebResponse;
import com.blstream.urbangame.webserver.helper.WebResponse.QueryType;
import com.blstream.urbangame.webserver.helper.WebServerHelper.WebServerResponseInterface;
import com.blstream.urbangame.webserver.mock.MockWebServer;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/* AsyncSendWebQuery is a class which is responsible for obtaining information
 * from web server by issuing HTTP GET query. Pass appropriate "queryType" as
 * constructor argument to issue query which you want. */

public class AsyncSendWebQuery extends AsyncTask<Void, Void, WebResponse> {
	private final String TAG = "AsyncSendWebQuery";
	
	private WebResponse webResponse;
	private WebResource webResource;
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
	
	public AsyncSendWebQuery(WebServerResponseInterface _webServerResponseListener, QueryType queryType, long gid,
		long tid) {
		this.gid = gid;
		this.tid = tid;
		init(_webServerResponseListener, queryType);
	}
	
	public AsyncSendWebQuery(WebServerResponseInterface webServerResponseListener, WebResponse webResponse,
		WebResource webResource, long gid, long tid) {
		
		this.gid = gid;
		this.tid = tid;
		this.webServerResponseListener = webServerResponseListener;
		this.webResource = webResource;
		this.webResponse = webResponse;
	}
	
	//
	// Private methods
	//
	private void init(WebServerResponseInterface _webServerResponseListener, QueryType queryType) {
		webResource = new WebResource(queryType);
		webResponse = new WebResponse(queryType);
		webServerResponseListener = _webServerResponseListener;
	}
	
	private JsonResponse getJsonResponse(String jsonString) {
		// try to parse response JSON string to JsonResponse
		Gson gson = new Gson();
		try {
			JsonResponse jsonResponse = gson.fromJson(jsonString, JsonResponse.class);
			
			return jsonResponse;
		}
		catch (JsonSyntaxException e) {
			Log.e(TAG, "getJsonResponse exception " + e.toString());
		}
		return null;
		
	}
	
	private String getUrlForNextResource(String nextResource) {
		String url = WebResource.ROOT_URL;
		if (!nextResource.equals(WebResource.RESOURCE_ROOT)) {
			
			if (nextResource == WebResource.RESOURCE_SELF) {
				
				if (webResponse.getQueryType() == QueryType.GetTask && webResource.isLastResource()) {
					List<Task> tasks = webResponse.getTaskList();
					
					for (Task task : tasks) {
						if (task.getId() == tid) {
							url = task.getLinkFromResource(WebResource.RESOURCE_SELF);
							break;
						}
					}
				}
				else if (webResponse.getQueryType() == QueryType.GetTask && !webResource.isLastResource()
					|| webResponse.getQueryType() == QueryType.GetTaskList && !webResource.isLastResource()
					|| webResponse.getQueryType() == QueryType.GetUrbanGameDetails && webResource.isLastResource()) {
					List<UrbanGameShortInfo> games = webResponse.getUrbanGameShortInfoList();
					
					for (UrbanGameShortInfo urbanGameShortInfo : games) {
						if (urbanGameShortInfo.getID() == gid) {
							url = urbanGameShortInfo.getLinkFromResource(WebResource.RESOURCE_SELF);
							break;
						}
					}
				}
				
			}
			else {
				JsonResponse jsonResponse = getJsonResponse(webResponse.getJsonString());
				if (jsonResponse != null) {
					url = jsonResponse.getLinkFromResource(nextResource);
				}
			}
		}
		
		return url;
	}
	
	//
	// Implementation of the base methods
	//
	@Override
	protected WebResponse doInBackground(Void... params) {
		
		MockWebServer mockWebServer = new MockWebServer();
		
		String nextResource = webResource.getNextResource();
		String url = getUrlForNextResource(nextResource);
		
		Log.i(TAG, "url: " + url);
		Log.i(TAG, "nextResource: " + nextResource);
		Log.i(TAG, "queryType: " + webResponse.getQueryType());
		
		// FIXME send query to a real server instead of mock
		String responseString = mockWebServer.getResponse(url, nextResource, webResponse.getQueryType(), gid, tid);
		
		Log.i(TAG, "response: " + responseString);
		
		if (responseString == null) return null;
		
		webResponse.setJsonString(responseString);
		return webResponse;
	}
	
	@Override
	protected void onPostExecute(WebResponse result) {
		
		if (webResource.isLastResource()) {
			if (webResponse.getQueryType() != QueryType.GetUrbanGameBaseList) {
				webResponse.setGameId(gid);
			}
			webServerResponseListener.onWebServerResponse(result);
		}
		else {
			new AsyncSendWebQuery(webServerResponseListener, webResponse, webResource, gid, tid).execute();
			
		}
	}
}