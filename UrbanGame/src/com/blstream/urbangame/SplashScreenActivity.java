package com.blstream.urbangame;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.blstream.urbangame.session.LoginManager;
import com.blstream.urbangame.web.WebHighLevel;
import com.blstream.urbangame.web.WebHighLevelInterface;

/**
 * SplashScreen for application, which is not kept in history stack.
 */
public class SplashScreenActivity extends SherlockFragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WebHighLevelInterface web = new WebHighLevel(this);
		web.downloadGameList();
		
		startGamesActivity();
	}
	
	private void startGamesActivity() {
		if (isUserLoggedIn()) {
			startMyGamesActivity();
		}
		else {
			startGamesListActivity();
		}
	}
	
	private void startMyGamesActivity() {
		startSpecificActivity(MyGamesActivity.class);
	}
	
	private void startGamesListActivity() {
		startSpecificActivity(GamesListActivity.class);
	}
	
	private boolean isUserLoggedIn() {
		LoginManager loginManager = LoginManager.getInstance(SplashScreenActivity.this);
		return loginManager.isUserLoggedIn();
	}
	
	private void startSpecificActivity(Class<?> cls) {
		startActivity(new Intent(SplashScreenActivity.this, cls));
	}
}