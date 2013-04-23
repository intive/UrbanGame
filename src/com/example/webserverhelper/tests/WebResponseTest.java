package com.example.webserverhelper.tests;

import android.test.AndroidTestCase;

import com.example.webserverhelper.helpers.UrbanGame;
import com.example.webserverhelper.helpers.WebResponse;

public class WebResponseTest extends AndroidTestCase
{
	WebResponse webResponse;

	public void testParseResponseToUrbanGame()
	{
		WebResponse webResponse =
				new WebResponse(WebResponse.queryTypeGetUrbanGame);
		webResponse.setJsonResponse("{gid:1}");

		UrbanGame urbanGame = webResponse.parseResponseToUrbanGame();

		assertTrue(urbanGame.getGid() == 1);
	}

	public void testParseResponseToUrbanGamesArray()
	{
		WebResponse webResponse =
				new WebResponse(WebResponse.queryTypeGetUrbanGame);

		webResponse.setJsonResponse("[{\"gid\":1},{\"gid\":10},{\"gid\":100}]");

		UrbanGame[] urbanGames = webResponse.parseResponseToUrbanGamesArray();

		assertTrue(urbanGames[0].getGid() == 1);
		assertTrue(urbanGames[1].getGid() == 10);
		assertTrue(urbanGames[2].getGid() == 100);
	}

}
