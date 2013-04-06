package com.example.webserverinterface.interfaces;

import android.util.Log;

import com.example.webserverinterface.asynctasks.AsyncGetGameInfo;
import com.example.webserverinterface.helpers.UrbanGame;

/*
   WebServerInterface is an intermediary class which issues HTTP requests
   to web server.
   
   Remember to register appropriate listener before using
   WebServerInterface methods, for instance:
   1) firstly call "registerGetUrbanGameInfoListener()" to register your 
      listener.
   2) then you can use "getUrbanGameInfo()" to obtain information
      about a particular game from web server. After a query to a
      web server has been completed then WebServerInterface will call
      listener, which you provided in step 1) as an argument to
      "registerGetUrbanGameInfoListener()" method.
      
      If data has been successfully collected from web server,
      then your listener will receive not null object.
      Otherwise it will receive null object.
*/

public class WebServerInterface
{
	public final static String scheme = "http";
	public final static String authority = "blstream.com";
	public final static String gamePath = "/urbangame/games";
	
	private IGetUrbanGameInfo getUrbanGameInfoListener;
	
	
	// Declarations of the interfaces
    public interface IGetUrbanGameInfo
        { public void onGetUrbanGameInfo(UrbanGame urbanGame); }
    
    
    

    
	// Public methods of the "WebServerInterface" class
    public void getUrbanGameInfo(long gid)
    {	
    	/*
          This method is issuing HTTP request to server to collect game
          data for particular GID number.
          If there's no Internet connection or HTTP timeout occurred, 
          then Exception is logged and null is returned.
        */
    	
    	if(getUrbanGameInfoListener != null)
    	{
        	new AsyncGetGameInfo(gid, getUrbanGameInfoListener)
                                 .execute();    	
        	Log.i("WebServerInterface", "getUrbanGameInfo success");
    	}
    	else
    		Log.e("WebServerInterface", "getUrbanGameInfo listener not set");
    }
    
    public void registerGetUrbanGameInfoListener
                                              (IGetUrbanGameInfo listener)
    {
    	Log.i("WebServerInterface", "registergetUrbanGameInfoListener");
        try 
           { getUrbanGameInfoListener = listener; } 
        catch (ClassCastException e) 
        {
           throw new ClassCastException(listener.toString() + 
      	                    " must implement getUrbanGameInfoInterface");
        }
    }
    
    
}