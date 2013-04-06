package com.example.webserverinterface.tests;

import android.test.AndroidTestCase;

import com.example.webserverinterface.helpers.UrbanGame;
import com.example.webserverinterface.interfaces.WebServerInterface;

public class WebServerInterfaceTest extends AndroidTestCase
{
	private MockUserClass mockUserClass;


	private class MockUserClass implements
	                            WebServerInterface.IGetUrbanGameInfo
	{
        private long expectedGid;
        private WebServerInterface webServerInterface;
        
        
        
        // Constructor
        public MockUserClass()
           { webServerInterface = new WebServerInterface(); }
        


		// Implementation of the interfaces
		public void onGetUrbanGameInfo(UrbanGame urbanGame)
		{
		   if(urbanGame != null)
			   assertTrue( urbanGame.getGid() == expectedGid );			   
		}	
        
		
		
		// public methods
		public void issueGetUrbanGameInfoRequest(long gid)
		   { webServerInterface.getUrbanGameInfo(gid); }
		
        public void registerGetUrbanGameInfoListener() 
           { webServerInterface.registerGetUrbanGameInfoListener(this); }
		
		public void setExpectedGid(long _expectedGID)
		   { expectedGid = _expectedGID; }
        
	}
	
	
	
	

	public void setUp()
	{
		mockUserClass = new MockUserClass();
	}
	

	public void testGetUrbanGameInfo() 
	{
		long gid = 10;
		mockUserClass.setExpectedGid(gid);
		mockUserClass.registerGetUrbanGameInfoListener();
		mockUserClass.issueGetUrbanGameInfoRequest(gid);
	}

	public void testRegisterGetUrbanGameInfoListener() 
	{
		mockUserClass.registerGetUrbanGameInfoListener();
	    // assertTrue(true); ?
	}
}