package com.blstream.urbangame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.blstream.urbangame.login.LoginSession;

public class SplashScreenActivity extends Activity {
	private LoginSession loginSession;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loginSession = LoginSession.getInstance(SplashScreenActivity.this);
		startGamesActivity();
	}
	
	private void startGamesActivity() {
		if (loginSession.isUserLoggedIn()) {
			startMyGamesActivity();
		}
		else {
			startGamesListActivity();
		}
	}
	
	private void startGamesListActivity() {
		startSpecificActivity(GamesListActivity.class);
	}
	
	private void startMyGamesActivity() {
		startSpecificActivity(MyGamesActivity.class);
	}
	
	private void startSpecificActivity(Class<?> cls) {
		startActivity(new Intent(SplashScreenActivity.this, cls));
	}
}