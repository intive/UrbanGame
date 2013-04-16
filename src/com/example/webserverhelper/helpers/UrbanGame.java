package com.example.webserverhelper.helpers;

/*
 UrbanGame is auxiliary class, which is an object representation of a
 single game.
 */

public class UrbanGame
{
	private long gid;

	//
	// Constructors
	//
	public UrbanGame()
	{

	}

	public UrbanGame(long _gid)
	{
		gid = _gid;
	}

	//
	// Public methods
	//
	public long getGid()
	{
		return gid;
	}

	public void setGid(long _gid)
	{
		gid = _gid;
	}

}