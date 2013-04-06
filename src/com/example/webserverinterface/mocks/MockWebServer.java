package com.example.webserverinterface.mocks;

/*
   MockWebServer class is simulating web server behavior.
*/

public class MockWebServer 
{

	public String getGameInfo(long gid)
	{	
		// Method returns JSON string containing information about a single game
		return "{gid:" + String.valueOf(gid) + "}";
	}
	
}