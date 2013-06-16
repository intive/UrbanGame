package com.blstream.urbangame;

import android.app.Application;
import android.util.Log;

/** UrbanGameApplication class is accessible within whole application as a global
 * object to store common data for all of the activities and fragments. */
public class UrbanGameApplication extends Application {
	private final static String TAG = "UrbanGameApplication";
	
	private boolean isAppInBackground;
	private static int numberOfRunnigActivities = 0;
	
	@Override
	public void onCreate() {
		super.onCreate();
		isAppInBackground = true;
		Log.i(TAG, "numberOfRunnigActivitiesC " + numberOfRunnigActivities);
	}
	
	public void incremenetNumberOfRunningActivities() {
		++numberOfRunnigActivities;
		Log.i(TAG, "incremenetNumberOfRunningActivities() " + numberOfRunnigActivities);
	}
	
	public void decrementNumberOfRunningActivities() {
		--numberOfRunnigActivities;
		Log.i(TAG, "decrementNumberOfRunningActivities() " + numberOfRunnigActivities);
	}
	
	public boolean isApplicationRunning() {
		Log.i(TAG, "numberOfRunnigActivities " + numberOfRunnigActivities);
		if (numberOfRunnigActivities > 0) return true;
		return false;
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