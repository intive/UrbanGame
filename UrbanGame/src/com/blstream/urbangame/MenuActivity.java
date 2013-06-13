package com.blstream.urbangame;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.blstream.urbangame.session.LoginManager;

// formatter:off
/* EXAMPLE OF USE:
 * 
 * When you want to add menu to your activity like:
 *  | Refresh
 *  | Settings
 *  | Help
 *  | Send feedback
 *  | [Logout]
 *  
 *  just override this class.
 *  When you want to add your own menu items and handle
 *  menu items selection just override also this two methods
 *  
 ****************************************************
 * onMenuItemSelected():
 *
 *  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    int itemId = item.getItemId();
    switch (itemId) {
      case R.id.some_id:
        doSomething();
        break;
    }
    return super.onMenuItemSelected(featureId, item);  // handling menu "more" items
  }
 **************************************************** */
// formatter:on

public class MenuActivity extends SherlockFragmentActivity {
	private LoginManager loginManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loginManager = LoginManager.getInstance(MenuActivity.this);
		getSupportActionBar().setBackgroundDrawable(
			new ColorDrawable(getResources().getColor(R.color.action_bar_background)));
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
				finish();
				break;
			case android.R.id.home:
				finish();
				break;
		}
		return true;
	}
}