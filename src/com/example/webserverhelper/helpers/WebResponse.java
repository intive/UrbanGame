package com.example.webserverhelper.helpers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/*
 WebResponse is an auxiliary class which contains information about 
 response from web server: what kind of query was send to web server and
 JSON message returned from web server.
 */

public class WebResponse
{
	private final String TAG = "WebResponse";

	public final static int queryTypeGetUrbanGame = 1;
	public final static int queryTypeGetUrbanGameArray = 2;

	private int responseType;
	private String jsonResponse;

	//
	// Constructor
	//
	public WebResponse(int _responseType)
	{
		responseType = _responseType;
	}

	//
	// Public methods
	//
	public int getResponseType()
	{
		return responseType;
	}

	public void setJsonResponse(String _jsonResponse)
	{
		jsonResponse = _jsonResponse;
	}

	public UrbanGame parseResponseToUrbanGame()
	{
		// try to parse response JSON string to UrbanGame
		Gson gson = new Gson();

		try
		{
			UrbanGame urbanGame = gson.fromJson(jsonResponse, UrbanGame.class);
			return urbanGame;
		}
		catch (JsonSyntaxException e)
		{
			Log.e(TAG, "parseResponseToUrbanGame exception " + e.toString());
		}
		return null;
	}

	public UrbanGame[] parseResponseToUrbanGamesArray()
	{
		// try to parse response JSON string to array of UrbanGame
		Gson gson = new Gson();

		try
		{
			UrbanGame[] urbanGames =
					gson.fromJson(jsonResponse, UrbanGame[].class);
			return urbanGames;
		}
		catch (JsonSyntaxException e)
		{
			Log.e(TAG,
					"parseResponseToUrbanGamesArray exception " + e.toString());
		}

		return null;
	}

}