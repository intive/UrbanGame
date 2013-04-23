package com.blstream.urbangame.webserver.mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.net.Uri;
import android.util.Log;

import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.google.gson.Gson;

/* MockWebServer class is simulating web server behavior. */

public class MockWebServer {
	private final String TAG = "MockWebServer";
	
	private ArrayList<UrbanGameShortInfo> mockAllUrbanGames;
	private UrbanGame mockSingleUrbanGame;
	
	//
	// Constructor
	//
	public MockWebServer() {
		
		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = curFormater.parse("04/05/2010");
			endDate = curFormater.parse("04/06/2010");
		}
		catch (ParseException e) {
			Log.e(TAG, "ParseException " + e.toString());
		}
		
		mockSingleUrbanGame = new UrbanGame(Long.valueOf(100), 15.5, "MyGameTitle", "MyOperatorName",
			"MyWinningStrategy", 34, 50, startDate, endDate, 2, true, "MyPrizesInfo", "MyDescription",
			"MyGameLogoBase64", "MyOperatorLogoBase64", "MyComments", "MyLocation", "MyDetaisLink");
		
		mockAllUrbanGames = new ArrayList<UrbanGameShortInfo>();
		
		for (int i = 0; i < 3; ++i)
			mockAllUrbanGames.add(new UrbanGameShortInfo(3L, "MyGameTitle", "MyOperatorName", 2, 50, startDate,
				endDate, true, "MyLocation", "MyGamelogoBase64", "MyOperatorlogoBase64", "MyDetailsLink"));
	}
		
	//
	// Public methods
	//
	public ArrayList<UrbanGameShortInfo> getMockAllUrbanGames() {
		return mockAllUrbanGames;
	}
	
	public UrbanGame getMockSingleUrbanGame() {
		return mockSingleUrbanGame;
	}
	
	public String getResponse(String queryString) {
		// Method returns JSON string which is a server response for
		// a queryString
		
		Log.i(TAG, "queryString " + queryString);
		
		Uri uri = Uri.parse(queryString);
		String gidString = uri.getQueryParameter("gid");
		Gson gson = new Gson();
		
		// If queryString doesn't contain GameID parameter MockWebServer
		// will return an array of all available games
		if (gidString == null) {
			String returnString = "[";
			
			int i;
			for (i = 0; i < mockAllUrbanGames.size() - 1; ++i)
				returnString = returnString + gson.toJson(mockAllUrbanGames.get(i)) + ",";
			
			returnString = returnString + gson.toJson(mockAllUrbanGames.get(i)) + "]";
			
			return returnString;
		}
		
		return gson.toJson(mockSingleUrbanGame);
		
	}
	
}