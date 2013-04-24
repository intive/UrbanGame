package com.blstream.urbangame.webserver.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.blstream.urbangame.webserver.helper.WebResponse;
import com.blstream.urbangame.webserver.helper.WebServerHelper;
import com.blstream.urbangame.webserver.mock.MockWebServer;

public class WebServerHelperTest extends InstrumentationTestCase {
	private final String TAG = "WebServerHelperTest";
	private final int TIMEOUT = 30;
	private CountDownLatch signal = new CountDownLatch(1);
	private long gameIndex;
	
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
						UrbanGame urbanGame = webResponse.getUrbanGame();
						
						assertNotNull(urbanGame);
						
						MockWebServer mockWebServer = new MockWebServer();
						checkUrbanGamesEqual(urbanGame, mockWebServer.getMockSingleUrbanGame(gameIndex));
						Log.d(TAG, "checkUrbanGamesEqual singleGame equal");
						
						signal.countDown();
						break;
					}
					// if query returned array of all available games
					case (WebResponse.queryTypeGetUrbanGameBaseList): {
						
						List<UrbanGameShortInfo> urbanGames = webResponse.getUrbanGameShortInfoList();
						
						assertNotNull(urbanGames);
						
						MockWebServer mockWebServer = new MockWebServer();
						ArrayList<UrbanGameShortInfo> mockAllUrbanGames = mockWebServer.getMockAllUrbanGames();
						
						for (int i = 0; i < mockAllUrbanGames.size(); ++i) {
							checkUrbanGameShortInfoEqual(mockAllUrbanGames.get(i), urbanGames.get(i));
							Log.d(TAG, "checkUrbanGameShortInfoEqual index i: " + i);
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
		public void issueGetUrbanGameDetails(long _gameIndex) {
			gameIndex = _gameIndex;
			WebServerHelper.getUrbanGameDetails(this, gameIndex);
		}
		
		public void issueGetUrbanGameBaseList() {
			WebServerHelper.getUrbanGameBaseList(this);
		}
		
		public void checkUrbanGamesEqual(UrbanGame expected, UrbanGame actual) {
			
			checkUrbanGameShortInfoEqual(expected.getPrimaryInfo(), actual.getPrimaryInfo());
			
			assertEquals(expected.getComments(), actual.getComments());
			assertEquals(expected.getDescription(), actual.getDescription());
			assertEquals(expected.getDifficulty(), actual.getDifficulty());
			assertEquals(expected.getGameVersion(), actual.getGameVersion());
			assertEquals(expected.getPrizesInfo(), actual.getPrizesInfo());
			assertEquals(expected.getWinningStrategy(), actual.getWinningStrategy());
		}
		
		public void checkUrbanGameShortInfoEqual(UrbanGameShortInfo expected, UrbanGameShortInfo actual) {
			assertEquals(expected.getDetailsLink(), actual.getDetailsLink());
			assertEquals(expected.getEndDate(), actual.getEndDate());
			assertEquals(expected.getGameLogoBase64(), actual.getGameLogoBase64());
			assertEquals(expected.getID(), actual.getID());
			assertEquals(expected.getLocation(), actual.getLocation());
			assertEquals(expected.getMaxPlayers(), actual.getMaxPlayers());
			assertEquals(expected.getOperatorLogoBase64(), actual.getOperatorLogoBase64());
			assertEquals(expected.getOperatorName(), actual.getOperatorName());
			assertEquals(expected.getPlayers(), actual.getPlayers());
			assertEquals(expected.getReward(), actual.getReward());
			assertEquals(expected.getStartDate(), actual.getStartDate());
			assertEquals(expected.getTitle(), actual.getTitle());
		}
		
	}
	
	//
	// Test methods
	//
	public void setUp() {
		mockUserClass = new MockUserClass();
	}
	
	public void testGetUrbanGameDetails() throws Throwable {
		
		for (long i = 0; i < 5; ++i) {
			final long index = i;
			runTestOnUiThread(new Runnable() {
				@Override
				public void run() {
					mockUserClass.issueGetUrbanGameDetails(index);
				}
			});
			
			signal.await(TIMEOUT, TimeUnit.SECONDS);
			signal = new CountDownLatch(1);
			Log.d(TAG, "testGetUrbanGameDetails() completed");
		}
		
	}
	
	public void testGetUrbanGameBaseList() throws Throwable {
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				mockUserClass.issueGetUrbanGameBaseList();
			}
		});
		
		signal.await(TIMEOUT, TimeUnit.SECONDS);
		Log.d(TAG, "testGetUrbanGameBaseList() completed");
	}
	
}