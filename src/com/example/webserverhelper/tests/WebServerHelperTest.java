package com.example.webserverhelper.tests;

import java.util.ArrayList;

import android.test.AndroidTestCase;

import com.example.webserverhelper.helpers.UrbanGame;
import com.example.webserverhelper.helpers.WebResponse;
import com.example.webserverhelper.helpers.WebServerHelper;
import com.example.webserverhelper.mocks.MockWebServer;

public class WebServerHelperTest extends AndroidTestCase
{
	private MockUserClass mockUserClass;

	private class MockUserClass implements
			WebServerHelper.WebServerResponseInterface
	{
		private long expectedGid;
		private WebServerHelper webServerHelper;

		//
		// Constructor
		//
		public MockUserClass()
		{
			webServerHelper = new WebServerHelper();
		}

		//
		// Implementation of the interfaces
		//
		public void onWebServerResponse(WebResponse webResponse)
		{
			// If there was response from web server
			if (webResponse != null)
			{
				switch (webResponse.getResponseType())
				{
				// If query returned information about single UrbanGame
					case (WebResponse.queryTypeGetUrbanGame):
					{
						UrbanGame urbanGame =
								webResponse.parseResponseToUrbanGame();

						assertTrue(urbanGame.getGid() == expectedGid);
						break;
					}
					// if query returned array of all available games
					case (WebResponse.queryTypeGetUrbanGameArray):
					{
						UrbanGame[] urbanGames =
								webResponse.parseResponseToUrbanGamesArray();

						MockWebServer mockWebServer = new MockWebServer();
						ArrayList<UrbanGame> availableGames =
								mockWebServer.getAvailableGames();

						for (int i = 0; i < availableGames.size(); ++i)
							assertTrue(availableGames.get(i).getGid() == urbanGames[i]
									.getGid());

						break;
					}
				}
			}
		}

		//
		// Public methods
		//
		public void issueGetUrbanGameRequest(long gid)
		{
			webServerHelper.getUrbanGame(this, gid);
		}

		public void issueGetUrbanGameArray()
		{
			webServerHelper.getUrbanGameArray(this);
		}

		public void setExpectedGid(long _expectedGID)
		{
			expectedGid = _expectedGID;
		}

	}

	//
	// Test methods
	//
	public void setUp()
	{
		mockUserClass = new MockUserClass();
	}

	public void testGetUrbanGame()
	{
		long gid = 10;
		mockUserClass.setExpectedGid(gid);
		mockUserClass.issueGetUrbanGameRequest(gid);
	}

	public void testGetUrbanGameArray()
	{
		mockUserClass.issueGetUrbanGameArray();
	}

}