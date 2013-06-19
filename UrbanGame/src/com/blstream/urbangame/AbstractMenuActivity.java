package com.blstream.urbangame;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.actionbarsherlock.app.ActionBar;
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

/**
 * AbstractMenuActivity is abstract super class for all of the activities in
 * application. It provides the following features:
 * 		a) For the user there is overflow menu which
 * 		   allows to perform simple actions like:
 * 			- refresh data
 * 			- open settings
 * 			- send feedback
 * 			- read help
 * 			- logout from application
 * 		b) For subactivities it provides {@link UrbanGameApplication} and {@link LoginManager}
 * 		   objects
 * 		c) Subclasses are also capable to finish themselves after receiving LOGOUT_ACTION intent
 */
//formatter:on
public abstract class AbstractMenuActivity extends SherlockFragmentActivity {
	protected final static String LOGOUT_ACTION = "com.blstream.urbangame.LOGOUT";
	protected UrbanGameApplication urbanGameApplication;
	protected LoginManager loginManager;
	private LocalBroadcastManager localBroadcastManager;
	private NotificationServer notificationServer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.urbanGameApplication = (UrbanGameApplication) getApplication();
		this.localBroadcastManager = LocalBroadcastManager.getInstance(AbstractMenuActivity.this);
		
		setUpBroadcastReceiver();
		setStyleToActionBar();
		urbanGameApplication.incremenetNumberOfRunningActivities();
		notificationServer = NotificationServer.getInstance(this);
		notificationServer.setApplication(urbanGameApplication);
	}
	
	/**
	 * {@link LocalBroadcastManager} is used to exchange local data within
	 * application and here it is used for closing all of the opened activities
	 * in case of logout.
	 */
	private void setUpBroadcastReceiver() {
		BroadcastReceiver finishBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		};
		
		IntentFilter finishIntentFilter = new IntentFilter(LOGOUT_ACTION);
		
		localBroadcastManager.registerReceiver(finishBroadcastReceiver, finishIntentFilter);
	}
	
	private void setStyleToActionBar() {
		Resources resources = getResources();
		int actionBarColor = resources.getColor(R.color.action_bar_background);
		ColorDrawable actionBarColorDrawable = new ColorDrawable(actionBarColor);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(actionBarColorDrawable);
	}
	
	/**
	 * onResume() and onPause() from {@link UrbanGameApplication} methods are
	 * invoked to set flag if application is running in background or not
	 */
	@Override
	protected void onResume() {
		super.onResume();
		urbanGameApplication.onResume();
		loginManager = LoginManager.getInstance(AbstractMenuActivity.this);
		notificationServer.updateContext(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		urbanGameApplication.onPause();
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
		if (isUserLoggedIn()) {
			menu.findItem(R.id.menu_logout).setVisible(true);
		}
		else {
			menu.findItem(R.id.menu_logout).setVisible(false);
		}
	}

	protected boolean isUserLoggedIn() {
		return loginManager.isUserLoggedIn();
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
	
	@SuppressLint("NewApi")
	private void finishAndStartGamesListActivity() {
		Intent gamesListActivity = getLogoutIntent();
		
		// In android version starting from API 17 finishAffinity clears activities stack
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			finishAffinity();
		}
		// In android pre JB there's need to close all of the activities explicitly in case of logout
		else {
			localBroadcastManager.sendBroadcast(new Intent(LOGOUT_ACTION));
		}
		
		startActivity(gamesListActivity);
	}
	
	private Intent getLogoutIntent() {
		Intent gamesListActivity = new Intent(AbstractMenuActivity.this, GamesListActivity.class);
		gamesListActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		gamesListActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return gamesListActivity;
	}
}