package com.blstream.urbangame;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.blstream.urbangame.session.LoginManager;

public class MenuListActivity extends SherlockListActivity {
	private LoginManager loginManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loginManager = LoginManager.getInstance(MenuListActivity.this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.top_bar_menu_more, menu);
		configureLogoutMenuItem(menu);
		return true;
	}
	
	private void configureLogoutMenuItem(Menu menu) {
		if (loginManager.isUserLoggedIn()) {
			menu.findItem(R.id.menu_logout).setVisible(true);
		}
		else {
			menu.findItem(R.id.menu_logout).setVisible(false);
		}
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
			case R.id.menu_logout:
				loginManager.logoutUser();
				break;
			case android.R.id.home:
				finish();
				break;
		}
		return true;
	}
}