package com.example.webserverhelper.tests;

import android.test.AndroidTestCase;

import com.example.webserverhelper.helpers.UrbanGame;

public class UrbanGameTest extends AndroidTestCase
{
	UrbanGame urbanGame;

	//
	// Test methods
	//
	public void setUp()
	{
		urbanGame = new UrbanGame();
	}

	public void testGetGidAndSetGid()
	{
		long gid = 100;
		urbanGame.setGid(gid);
		assertTrue(gid == urbanGame.getGid());
	}
}