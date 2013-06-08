package com.blstream.urbangame;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.blstream.urbangame.notifications.NotificationsManager;
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
 * onCreateOptionsMenu():
 * 
 * 	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);	// adding menu "more"
		
		// HERE COMES OWN CODE
		
		return true;
	}
	
 ****************************************************
 * onMenuItemSelected():
 *
 *	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
			case R.id.some_id:
				doSomething();
				break;
		}
		return super.onMenuItemSelected(featureId, item);	// handling menu "more" items
	}
 ****************************************************
 */
// formatter:on

public class MenuActivity extends SherlockFragmentActivity {
	private LoginManager loginManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loginManager = LoginManager.getInstance(MenuActivity.this);
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
		
		//******** MOCK ********
			case R.id.menu_more:
				NotificationsManager notificationsManager = new NotificationsManager(MenuActivity.this);
				notificationsManager.notifyGameChanged("KRASNOLUDKI", null, -1L);
				break;
			//****** END MOCK ******
			
			case R.id.menu_logout:
				loginManager.logoutUser();
				finishAndStartGamesListActivity();
				break;
			case android.R.id.home:
				finish();
				break;
		}
		return true;
	}
	
	private void finishAndStartGamesListActivity() {
		Intent gamesListActivity = new Intent(MenuActivity.this, GamesListActivity.class);
		gamesListActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(gamesListActivity);
		finish();
	}
}