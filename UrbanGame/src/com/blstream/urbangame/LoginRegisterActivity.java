package com.blstream.urbangame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.blstream.urbangame.fragments.LoginRegisterPageAdapter;
import com.blstream.urbangame.session.SessionManager;

public class LoginRegisterActivity extends AbstractMenuActivity {
	private static final String NAME = LoginRegisterActivity.class.getSimpleName();
	
	public final static String ACTION_RETURN_LOGIN_RESULT = "intent_open_games";
	public final static String ACTION_AFTER_LOGIN = "intent_open";
	public final static int LOGIN_REQUEST_CODE = 0x1;
	
	private LoginRegisterPageAdapter sectionsPagerAdapter;
	private ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSupportProgressBarVisibility(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_login_register);
		
		sectionsPagerAdapter = new LoginRegisterPageAdapter(getSupportFragmentManager(), LoginRegisterActivity.this);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(sectionsPagerAdapter);
	}
	
	public void setLoggedUserInDB(String email) {
		Log.d(SessionManager.TAG, NAME + " setLoggedUserInDB() " + email);
		
		boolean loginResult = loginManager.setLoggedUserInDB(email);
		handleLoginResult(loginResult);
	}
	
	private void handleLoginResult(boolean loginResult) {
		Intent intent = getIntent();
		String action = intent.getStringExtra(ACTION_AFTER_LOGIN);
		
		if (ACTION_RETURN_LOGIN_RESULT.equals(action)) {
			finishAndGoBackToGame(loginResult);
		}
		else {
			finishAndOpenMyGamesAcitivty();
		}
	}
	
	private void finishAndOpenMyGamesAcitivty() {
		Intent openMyGamesActivity = new Intent(LoginRegisterActivity.this,
			com.blstream.urbangame.MyGamesActivity.class);
		openMyGamesActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(openMyGamesActivity);
		this.finish();
	}
	
	private void finishAndGoBackToGame(boolean loginResult) {
		setResult(loginResult ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
		this.finish();
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(LoginRegisterActivity.this, GamesListActivity.class);
		startActivity(intent);
		super.onBackPressed();
	}
}