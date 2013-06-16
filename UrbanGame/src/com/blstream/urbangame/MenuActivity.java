package com.blstream.urbangame;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.blstream.urbangame.notifications.NotificationServer;
import com.blstream.urbangame.session.LoginManager;

//formatter:off
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
<<<<<<< HEAD
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

=======
*  
>>>>>>> 8b040ff18476ea6f4160d75d45563096e138a1a1
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
//formatter:on

public class MenuActivity extends SherlockFragmentActivity {
	private UrbanGameApplication urbanGameApplication;
	private LoginManager loginManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.urbanGameApplication = (UrbanGameApplication) getApplication();
		this.loginManager = LoginManager.getInstance(MenuActivity.this);
		getSupportActionBar().setBackgroundDrawable(
			new ColorDrawable(getResources().getColor(R.color.action_bar_background)));
		urbanGameApplication.incremenetNumberOfRunningActivities();
		// start notification server
		NotificationServer.getInstance(this);
		NotificationServer.getInstance(this).setApplication(urbanGameApplication);
		
	}
	
	/** onResume() and onPause() from {@link UrbanGameApplication} methods are
	 * invoked to set flag if application is running in background or not */
	@Override
	protected void onResume() {
		super.onResume();
		urbanGameApplication.onResume();
		NotificationServer.getInstance(this).updateContext(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		urbanGameApplication.onPause();
		loginManager = LoginManager.getInstance(MenuActivity.this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		urbanGameApplication.decrementNumberOfRunningActivities();
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