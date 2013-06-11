package com.blstream.urbangame;

import android.app.Application;
import android.util.Log;

/**
 * UrbanGameApplication class is accessible within whole application as a global
 * object to store common data for all of the activities and fragments.
 */
public class UrbanGameApplication extends Application {
	private final static String TAG = "UrbanGameApplication";
	
	private boolean isAppInBackground;
	
	@Override
	public void onCreate() {
		super.onCreate();
		isAppInBackground = true;
	}
	
	public boolean isAppInBackground() {
		return isAppInBackground;
	}
	
	public void onResume() {
		isAppInBackground = false;
		Log.d(TAG, "Application is in foreground");
	}
	
	public void onPause() {
		isAppInBackground = true;
		Log.d(TAG, "Application is in background");
	}
}