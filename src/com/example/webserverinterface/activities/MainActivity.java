package com.example.webserverinterface.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.webserverinterface.R;
import com.example.webserverinterface.helpers.UrbanGame;
import com.example.webserverinterface.interfaces.WebServerInterface;

/*
   MainActivity is a sample class which can be used to present
   how WebServerInterface works within activity. 
*/

public class MainActivity extends FragmentActivity implements 
                                  WebServerInterface.IGetUrbanGameInfo
{
	private final long GAMEID = 100;
	
	
	private WebServerInterface webServerInterface;
	
    private TextView txtGameID;
   
    
   
    // Implementation of the interfaces
    public void onGetUrbanGameInfo(UrbanGame urbanGame)
    {
    	if(urbanGame != null)
    	{
    		 txtGameID.setText(getResources().getString
                                 (R.string.textViewGameID) + 
                                 String.valueOf(urbanGame.getGid()));
    		 
	         Log.i("MainActivity", "onGetUrbanGameInfo succes");
    	}
   	}

    
   
    // Implementation of the base methods
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		txtGameID = (TextView) findViewById(R.id.textViewGameID);

		webServerInterface = new WebServerInterface();
		webServerInterface.registerGetUrbanGameInfoListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	
	

	
	// Callbacks
    public void ActivityMainLayout_GetButtonCallback(View view) 
    {
    	Log.i("MainActivity", "ActivityMainLayout_GetButtonCallback");

    	webServerInterface.getUrbanGameInfo(GAMEID);
    }
    
    public void ActivityMainLayout_ClearInfoCallback(View view) 
    {
    	Log.i("MainActivity", "ActivityMainLayout_ClearInfoCallback");
    	
    	txtGameID.setText(getResources().getString
    			                          (R.string.textViewGameID));
    }   
    
}