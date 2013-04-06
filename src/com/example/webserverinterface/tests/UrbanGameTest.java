package com.example.webserverinterface.tests;

import android.test.AndroidTestCase;

import com.example.webserverinterface.helpers.UrbanGame;

public class UrbanGameTest extends AndroidTestCase
{
    UrbanGame urbanGame;
	
    
    
	public void setUp()
	{
		urbanGame = new UrbanGame();
	}
	
	public void testGetGidAndSetGid() 
	{
		long gid = 100;
		urbanGame.setGid(gid);
		assertTrue( gid == urbanGame.getGid() );
	}
}