package com.blstream.urbangame.webserver.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.webserver.helper.WebResponse;
import com.blstream.urbangame.webserver.helper.WebServerHelper;
import com.blstream.urbangame.webserver.mock.MockWebServer;

public class WebServerHelperTest extends InstrumentationTestCase {
	private final CountDownLatch signal = new CountDownLatch(1);
	private final String TAG = "WebServerHelperTest";
	private final int TIMEOUT = 30;
	
	private MockUserClass mockUserClass;
	
	private class MockUserClass implements WebServerHelper.WebServerResponseInterface {
		//
		// Implementation of the interfaces
		//
		public void onWebServerResponse(WebResponse webResponse) {
			// If there was correct response from web server
			if (webResponse != null) {
				switch (webResponse.getQueryType()) {
				
				// If query returned information about single UrbanGame
					case (WebResponse.queryTypeGetUrbanGameDetails): {
						UrbanGame urbanGame = webResponse.parseResponseToUrbanGame();
						
						assertNotNull(urbanGame);
						
						MockWebServer mockWebServer = new MockWebServer();
						checkUrbanGamesEqual(urbanGame, mockWebServer.getMockSingleUrbanGame());
						Log.d(TAG, "checkUrbanGamesEqual singleGame equal");
						
						signal.countDown();
						break;
					}
					// if query returned array of all available games
					case (WebResponse.queryTypeGetUrbanGameBaseList): {
						List<UrbanGame> urbanGames = webResponse.parseResponseToUrbanGameList();
						
						assertNotNull(urbanGames);
						
						MockWebServer mockWebServer = new MockWebServer();
						ArrayList<UrbanGame> mockAllUrbanGames = mockWebServer.getMockAllUrbanGames();
						
						for (int i = 0; i < mockAllUrbanGames.size(); ++i) {
							checkUrbanGamesEqual(mockAllUrbanGames.get(i), urbanGames.get(i));
							Log.d(TAG, "checkUrbanGamesEqual index i: " + i);
						}
						
						signal.countDown();
						break;
					}
					
				}
			}
		}
		
		//
		// Public methods
		//
		public void issueGetUrbanGameRequest() {
			WebServerHelper.getUrbanGameDetails(this, 0);
		}
		
		public void issueGetUrbanGameArray() {
			WebServerHelper.getUrbanGameBaseList(this);
		}
		
		public void checkUrbanGamesEqual(UrbanGame expected, UrbanGame actual) {
			assertEquals(expected.getComments(), actual.getComments());
			assertEquals(expected.getDescription(), actual.getDescription());
			assertEquals(expected.getDetailsLink(), actual.getDetailsLink());
			assertEquals(expected.getDifficulty(), actual.getDifficulty());
			assertEquals(expected.getEndDate(), actual.getEndDate());
			assertEquals(expected.getGameLogoBase64(), actual.getGameLogoBase64());
			assertEquals(expected.getGameVersion(), actual.getGameVersion());
			assertEquals(expected.getID(), actual.getID());
			assertEquals(expected.getLocation(), actual.getLocation());
			assertEquals(expected.getMaxPlayers(), actual.getMaxPlayers());
			assertEquals(expected.getOperatorLogoBase64(), actual.getOperatorLogoBase64());
			assertEquals(expected.getOperatorName(), actual.getOperatorName());
			assertEquals(expected.getPlayers(), actual.getPlayers());
			assertEquals(expected.getPrizesInfo(), actual.getPrizesInfo());
			assertEquals(expected.getReward(), actual.getReward());
			assertEquals(expected.getStartDate(), actual.getStartDate());
			assertEquals(expected.getTitle(), actual.getTitle());
			assertEquals(expected.getWinningStrategy(), actual.getWinningStrategy());
			
		}
		
	}
	
	//
	// Test methods
	//
	public void setUp() {
		mockUserClass = new MockUserClass();
	}
	
	public void testGetUrbanGameDetails() throws Throwable {
		
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				mockUserClass.issueGetUrbanGameRequest();
			}
		});
		
		signal.await(TIMEOUT, TimeUnit.SECONDS);
		Log.d(TAG, "testGetUrbanGameDetails() completed");
	}
	
	public void testGetUrbanGameBaseList() throws Throwable {
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				mockUserClass.issueGetUrbanGameArray();
			}
		});
		
		signal.await(TIMEOUT, TimeUnit.SECONDS);
		Log.d(TAG, "testGetUrbanGameBaseList() completed");
	}
	
}