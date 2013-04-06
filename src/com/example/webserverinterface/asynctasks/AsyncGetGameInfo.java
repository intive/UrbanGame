package com.example.webserverinterface.asynctasks;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.webserverinterface.helpers.UrbanGame;
import com.example.webserverinterface.interfaces.WebServerInterface;
import com.example.webserverinterface.interfaces.WebServerInterface.IGetUrbanGameInfo;
import com.example.webserverinterface.mocks.MockWebServer;
import com.google.gson.Gson;

/*
   AsyncGetGameInfo is a class which is responsible for obtaining information
   from web server about a single UrbanGame by issuing HTTP GET query.
*/

public class AsyncGetGameInfo extends 
                              AsyncTask<Void, Void, UrbanGame> 
{
	private long gid;
	private IGetUrbanGameInfo getUrbanGameInfoListener;
	
	
	
	// Constructor
	public AsyncGetGameInfo(long _gid,
			                IGetUrbanGameInfo _getUrbanGameInfoListener)
	{
		gid = _gid;
		getUrbanGameInfoListener = _getUrbanGameInfoListener;
	}
	
	
	
	// Implementation of the base methods
	@Override
	protected UrbanGame doInBackground(Void... params)
	{	
		Uri.Builder uriBuilder = new Uri.Builder();
		
		uriBuilder.scheme(WebServerInterface.scheme);
		uriBuilder.authority(WebServerInterface.authority);
		uriBuilder.path(WebServerInterface.gamePath);
		uriBuilder.appendQueryParameter("gid", String.valueOf(gid));
		
		HttpGet get = new HttpGet( uriBuilder.toString() );	    	
		HttpResponse response;	
		HttpClient client = new DefaultHttpClient();
		
		try
		{
			// Normally this method should issue HTTP GET query
			// response = client.execute(get);
			// String responseString = 
			//		        EntityUtils.toString(response.getEntity()); 
			
			// Mock version uses "MockWebServer" class instead
			MockWebServer  mockWebServer = new MockWebServer();
			String responseString = mockWebServer.getGameInfo(gid);
			
			Log.i("WebServerInterface", "response: " + responseString);
			
			// try to parse response JSON string to UrbanGame
			Gson gson = new Gson();
			UrbanGame urbanGame = gson.fromJson(responseString, 
			                                    UrbanGame.class);
			
			return urbanGame;
		}
		catch(Exception e)
		{
			Log.e("WebServerInterface", "Response Exception " + e.toString());
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(UrbanGame result) 
	{
		getUrbanGameInfoListener.onGetUrbanGameInfo(result);
	} 
}