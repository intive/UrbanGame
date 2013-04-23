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
	private ArrayList<UrbanGame> mockSingleUrbanGames;
	
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
		
		mockSingleUrbanGames = new ArrayList<UrbanGame>();
		
		mockSingleUrbanGames.add(new UrbanGame(Long.valueOf(1), 1.0, "MyGameTitle1", "MyOperatorName1",
			"MyWinningStrategy1", 1, 1, startDate, endDate, 1, true, "MyPrizesInfo1", "MyDescription1",
			"MyGameLogoBase641", "MyOperatorLogoBase641", "MyComments1", "MyLocation1", "MyDetaisLink1"));
		
		mockSingleUrbanGames.add(new UrbanGame(Long.valueOf(2), 2.0, "MyGameTitle2", "MyOperatorName2",
			"MyWinningStrategy2", 2, 2, startDate, endDate, 2, true, "MyPrizesInfo2", "MyDescription2",
			"MyGameLogoBase642", "MyOperatorLogoBase642", "MyComments2", "MyLocation2", "MyDetaisLink2"));
		
		mockSingleUrbanGames.add(new UrbanGame(Long.valueOf(3), 3.0, "MyGameTitle3", "MyOperatorName3",
			"MyWinningStrategy3", 3, 3, startDate, endDate, 3, true, "MyPrizesInfo3", "MyDescription3",
			"MyGameLogoBase643", "MyOperatorLogoBase642", "MyComments3", "MyLocation3", "MyDetaisLink3"));
		
		mockSingleUrbanGames.add(new UrbanGame(Long.valueOf(4), 4.0, "MyGameTitle4", "MyOperatorName4",
			"MyWinningStrategy4", 4, 4, startDate, endDate, 4, true, "MyPrizesInfo4", "MyDescription4",
			"MyGameLogoBase644", "MyOperatorLogoBase644", "MyComments4", "MyLocation4", "MyDetaisLink4"));
		
		mockSingleUrbanGames.add(new UrbanGame(Long.valueOf(5), 5.0, "MyGameTitle5", "MyOperatorName5",
			"MyWinningStrategy5", 5, 5, startDate, endDate, 5, true, "MyPrizesInfo5", "MyDescription5",
			"MyGameLogoBase645", "MyOperatorLogoBase645", "MyComments5", "MyLocation5", "MyDetaisLink5"));
		
		mockAllUrbanGames = new ArrayList<UrbanGameShortInfo>();
		
		mockAllUrbanGames.add(new UrbanGameShortInfo(Long.valueOf(1), "MyGameTitle1", "MyOperatorName1", 1, 1,
			startDate, endDate, true, "MyLocation1", "MyGamelogoBase641", "MyOperatorlogoBase641", "MyDetailsLink1"));
		
		mockAllUrbanGames.add(new UrbanGameShortInfo(Long.valueOf(2), "MyGameTitle2", "MyOperatorName2", 2, 2,
			startDate, endDate, true, "MyLocation2", "MyGamelogoBase642", "MyOperatorlogoBase642", "MyDetailsLink2"));
		
		mockAllUrbanGames.add(new UrbanGameShortInfo(Long.valueOf(3), "MyGameTitle3", "MyOperatorName3", 3, 3,
			startDate, endDate, true, "MyLocation3", "MyGamelogoBase643", "MyOperatorlogoBase643", "MyDetailsLink3"));
		
		mockAllUrbanGames.add(new UrbanGameShortInfo(Long.valueOf(4), "MyGameTitle4", "MyOperatorName4", 4, 4,
			startDate, endDate, true, "MyLocation4", "MyGamelogoBase644", "MyOperatorlogoBase644", "MyDetailsLink4"));
		
		mockAllUrbanGames
			.add(new UrbanGameShortInfo(Long.valueOf(5), "MyGameTitle5", "MyOperatorName5", 5, 5, startDate,
				endDate, true, "MyLocation5", "MyGamelogoBase645", "MyOperatorlogoBase645", "MyDetailsLink5"));
		
	}
	
	//
	// Public methods
	//
	public ArrayList<UrbanGameShortInfo> getMockAllUrbanGames() {
		return mockAllUrbanGames;
	}
	
	public UrbanGame getMockSingleUrbanGame(Long gid) {
		return mockSingleUrbanGames.get(gid.intValue());
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
		
		return gson.toJson(mockSingleUrbanGames.get(Integer.valueOf(gidString)));
		
	}
	
}