package com.blstream.urbangame;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.blstream.urbangame.session.LoginManager;

/**
 * SplashScreen for application, which is not kept in history stack.
 */
public class SplashScreenActivity extends SherlockFragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
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
		LoginManager loginManager = new LoginManager(SplashScreenActivity.this);
		return loginManager.isUserLoggedIn();
	}
	
	private void startSpecificActivity(Class<?> cls) {
		startActivity(new Intent(SplashScreenActivity.this, cls));
	}
}