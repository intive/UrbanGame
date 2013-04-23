package com.example.webserverhelper.mocks;

import java.util.ArrayList;

import android.net.Uri;
import android.util.Log;

import com.example.webserverhelper.helpers.UrbanGame;

/*
 MockWebServer class is simulating web server behavior.
 */

public class MockWebServer
{
	private final String TAG = "MockWebServer";

	private ArrayList<UrbanGame> availableGames;

	//
	// Constructor
	//
	public MockWebServer()
	{
		availableGames = new ArrayList<UrbanGame>();
		availableGames.add(new UrbanGame(1));
		availableGames.add(new UrbanGame(10));
		availableGames.add(new UrbanGame(100));
	}

	//
	// Public methods
	//
	public ArrayList<UrbanGame> getAvailableGames()
	{
		return availableGames;
	}

	public String getResponse(String queryString)
	{
		// Method returns JSON string which is a server response for
		// a queryString

		Log.i(TAG, "queryString " + queryString);

		Uri uri = Uri.parse(queryString);
		String gidString = uri.getQueryParameter("gid");

		// If queryString doesn't contain GameID parameter MockWebServer
		// will return an array of all available games
		if (gidString == null)
		{
			String returnString = "[";

			int i;
			for (i = 0; i < availableGames.size() - 1; ++i)
				returnString =
						returnString + "{\"gid\":"
								+ availableGames.get(i).getGid() + "},";

			returnString =
					returnString + "{\"gid\":" + availableGames.get(i).getGid()
							+ "}]";

			return returnString;
		}

		return "{gid:" + String.valueOf(gidString) + "}";
	}
}