package com.example.webserverhelper.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.webserverhelper.R;
import com.example.webserverhelper.helpers.UrbanGame;
import com.example.webserverhelper.helpers.WebResponse;
import com.example.webserverhelper.helpers.WebServerHelper;

/*
 MainActivity is a sample class which can be used to present
 how WebServerHelper works within activity. 
 */

public class MainActivity extends FragmentActivity implements
		WebServerHelper.WebServerResponseInterface
{
	private final long GAMEID = 100;
	private final String TAG = "MyActivity";

	private WebServerHelper webServerHelper;
	private TextView txtGameID;
	private TextView txtGamesIDs;

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

					txtGameID.setText(getResources().getString(
							R.string.textViewGameID)
							+ String.valueOf(urbanGame.getGid()));

					Log.i("MainActivity", "onWebServerResponse"
							+ " UrbanGame completed");
					break;
				}
				// if query returned array of all available games
				case (WebResponse.queryTypeGetUrbanGameArray):
				{
					UrbanGame[] urbanGames =
							webResponse.parseResponseToUrbanGamesArray();

					String gameNumbers = "";
					int i;

					for (i = 0; i < urbanGames.length - 1; ++i)
						gameNumbers =
								gameNumbers
										+ String.valueOf(urbanGames[i].getGid())
										+ ", ";

					gameNumbers =
							gameNumbers
									+ String.valueOf(urbanGames[i].getGid());

					txtGamesIDs.setText(getResources().getString(
							R.string.textViewGamesIDs)
							+ gameNumbers);

					Log.i(TAG, "onWebServerResponse"
							+ " UrbanGameArray completed");
					break;
				}
			}
		}
	}

	//
	// Implementation of the base methods
	//
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtGameID = (TextView) findViewById(R.id.textViewGameID);
		txtGamesIDs = (TextView) findViewById(R.id.textViewGamesIDs);
		webServerHelper = new WebServerHelper();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	//
	// Callbacks
	//
	public void ActivityMainLayout_GetButtonCallback(View view)
	{
		Log.i("MainActivity", "ActivityMainLayout_GetButtonCallback");

		webServerHelper.getUrbanGame(this, GAMEID);
	}

	public void ActivityMainLayout_GetListButtonCallback(View view)
	{
		Log.i("MainActivity", "ActivityMainLayout_GetListButtonCallback");

		webServerHelper.getUrbanGameArray(this);
	}

	public void ActivityMainLayout_ClearInfoCallback(View view)
	{
		Log.i("MainActivity", "ActivityMainLayout_ClearInfoCallback");

		txtGameID.setText(getResources().getString(R.string.textViewGameID));

		txtGamesIDs
				.setText(getResources().getString(R.string.textViewGamesIDs));
	}

}