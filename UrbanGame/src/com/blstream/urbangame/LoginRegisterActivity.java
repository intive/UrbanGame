package com.blstream.urbangame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.blstream.urbangame.fragments.LoginRegisterPageAdapter;

public class LoginRegisterActivity extends MenuActivity {
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
	
	public void finishAndOpenProfileAcitivty() {
		Intent openProfileActivity = new Intent(LoginRegisterActivity.this,
			com.blstream.urbangame.ProfileActivity.class);
		startActivity(openProfileActivity);
		this.finish();
	}
}