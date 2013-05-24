package com.blstream.urbangame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.blstream.urbangame.session.LoginManager;

public class SplashScreenActivity extends Activity {
	private LoginManager loginManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loginManager = LoginManager.getInstance(SplashScreenActivity.this);
		startGamesActivity();
	}
	
	private void startGamesActivity() {
		startGamesListActivity();
		openMyGamesIfUserLoggedIn();
	}
	
	private void startGamesListActivity() {
		startSpecificActivity(GamesListActivity.class);
	}
	
	private void openMyGamesIfUserLoggedIn() {
		if (isUserLoggedIn()) {
			startMyGamesActivity();
		}
	}
	
	private boolean isUserLoggedIn() {
		return loginManager.isUserLoggedIn();
	}
	
	private void startMyGamesActivity() {
		startSpecificActivity(MyGamesActivity.class);
	}
	
	private void startSpecificActivity(Class<?> cls) {
		startActivity(new Intent(SplashScreenActivity.this, cls));
	}
}