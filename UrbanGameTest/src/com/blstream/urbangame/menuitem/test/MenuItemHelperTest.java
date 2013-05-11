package com.blstream.urbangame.menuitem.test;

import android.app.Activity;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.blstream.urbangame.MainActivity;
import com.blstream.urbangame.R;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.Player;

public class MenuItemHelperTest extends ActivityInstrumentationTestCase2<MenuItemHelperMockActivity> {
	
	private final String TAG = "MenuItemHelperTest";
	private final long TIMEOUT = 5000;
	
	private DatabaseInterface database;
	private MenuItemHelperMockActivity mockActivity;
	private ActivityMonitor mainActivityMonitor;
	private boolean success;
	
	public MenuItemHelperTest() {
		super(MenuItemHelperMockActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		database = new Database(getInstrumentation().getTargetContext());
		success = database.insertUser(new Player("mockMail@mock.com", "mockPassword", "mockDisplayedName",
			"mockAvatarBase64"));
		assertTrue(success);
		Log.d(TAG, "setUp completed");
	}
	
	@Override
	protected void tearDown() throws Exception {
		success = database.deletePlayer("mockMail@mock.com");
		assertTrue(success);
		database.closeDatabase();
		super.tearDown();
		Log.d(TAG, "tearDown completed");
	}
	
	//
	// Tests
	//
	public void testMenuItemLogoutUserNotLogged() {
		
		// Set that no one is logged
		assertTrue(database.setNoOneLogged());
		
		// Now create activity; "Logout" menu item should be disabled.
		mockActivity = getActivity();
		assertFalse(mockActivity.getLogoutMenuItem().isEnabled());
		
		Log.d(TAG, "testMenuItemLogoutUserNotLogged completed");
	}
	
	public void testMenuItemLogoutUserLogged() {
		
		// Set that user is logged
		success = database.setLoggedPlayer("mockMail@mock.com");
		assertTrue(success);
		
		// monitor MainActivity
		mainActivityMonitor = new ActivityMonitor(MainActivity.class.getName(), null, false);
		getInstrumentation().addMonitor(mainActivityMonitor);
		
		// Now create activity; "Logout" menu item should be enabled.
		mockActivity = getActivity();
		assertTrue(mockActivity.getLogoutMenuItem().isEnabled());
		
		// Simulate click on "Logout" menu item. 
		mockActivity.onMenuItemSelected(R.id.menu_logout, mockActivity.getLogoutMenuItem());
		
		// Check if information about logged player is not set now
		assertNull(database.getLoggedPlayerID());
		
		// Check if MainActivity was started
		Activity activity = mainActivityMonitor.waitForActivityWithTimeout(TIMEOUT);
		assertTrue(activity instanceof MainActivity);
		activity.finish();
		Log.d(TAG, "testMenuItemLogoutUserLogged completed");
	}
	
}