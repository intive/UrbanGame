package com.blstream.urbangame.menuitem.test;

import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.blstream.urbangame.R;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.menuitem.MenuItemHelper;

public class MenuItemHelperMockActivity extends SherlockFragmentActivity {
	
	private final String TAG = "MenuItemHelperMockActivity";
	private MenuItem logoutMenuItem;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.top_bar_menu_more, menu);
		MenuItemHelper.initLogoutMenuItem(this, menu);
		
		logoutMenuItem = menu.findItem(R.id.menu_logout);
		Log.d(TAG, "onCreateOptionsMenu completed");
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
			case R.id.menu_logout:
				MenuItemHelper.invokeActionLogoutMenuItem(this);
				break;
		}
		return true;
	}
	
	public MenuItem getLogoutMenuItem() {
		return logoutMenuItem;
	}
	
}