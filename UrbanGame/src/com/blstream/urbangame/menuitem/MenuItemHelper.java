package com.blstream.urbangame.menuitem;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.actionbarsherlock.view.Menu;
import com.blstream.urbangame.MainActivity;
import com.blstream.urbangame.R;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.Player;

/* MenuItemHelper is the auxiliary class which you can use to perform actions on
 * menu items. */

public class MenuItemHelper {
	private final static String TAG = "MenuItemHelper";
	
	//
	// Public methods
	//
	public static void initLogoutMenuItem(Activity activity, Menu menu) {
		// This method hides "Logout" menu item in Menu if user is not logged.
		// If user is logged "Logout" menu item is visible in Menu.

		DatabaseInterface database = new Database(activity);
		
		if (database.getLoggedPlayerID() == null) menu.findItem(R.id.menu_logout).setVisible(false);
		else  menu.findItem(R.id.menu_logout).setVisible(true);
		
		database.closeDatabase();
		Log.i(TAG, "initLogoutMenuItem completed in: " + activity.getClass().getSimpleName());
	}
	
	public static void invokeActionLogoutMenuItem(Activity activity) {
		// This method erases information that user is logged in
		// and shows MainActivitiy to the user
		DatabaseInterface database = new Database(activity);
		if (database.getLoggedPlayerID() != null) {
			database.setNoOneLogged();
			Intent intent = new Intent(activity, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			activity.startActivity(intent);
		}
		database.closeDatabase();
		
		Log.i(TAG, "invokeActionLogoutMenuItem completed in: " + activity.getClass().getSimpleName());
	}
	
}