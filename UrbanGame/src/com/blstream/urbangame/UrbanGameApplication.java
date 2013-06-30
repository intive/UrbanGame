package com.blstream.urbangame;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * UrbanGameApplication class is accessible within whole application as a global
 * object to store common data for all of the activities and fragments.
 * */
public class UrbanGameApplication extends Application {
	private final static String TAG = "UrbanGameApplication";
	
	private boolean isAppInBackground;
	private int numberOfRunnigActivities = 0;
	
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
	
	/**************************************************
	 *************** Networking Manager ***************
	 **************************************************/
	private WifiManager wifiManager;
	private ConnectivityManager connectivityManager;
	
	private WifiManager getWifiManager() {
		if (wifiManager == null) {
			wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		}
		return wifiManager;
	}
	
	private ConnectivityManager getConnectivityManager() {
		if (connectivityManager == null) {
			connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		}
		return connectivityManager;
	}
	
	private boolean isWiFiEnabled() {
		return getWifiManager().isWifiEnabled() && getWifiNetworkInfo().isConnected();
	}

	protected NetworkInfo getWifiNetworkInfo() {
		return getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	}
	
	private boolean isMobileDataEnabled() {
		return getMobileNetworkInfo().isConnected();
	}

	protected NetworkInfo getMobileNetworkInfo() {
		return getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	}
	
	protected NetworkInfo getNetworkInfo(int networkType) {
		return getConnectivityManager().getNetworkInfo(networkType);
	}
	
	public boolean isConnectedToInternet() {
		return isWiFiEnabled() || isMobileDataEnabled();
	}
}