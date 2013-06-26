package com.blstream.urbangame;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.blstream.urbangame.notifications.NotificationServer;

public class UserSettingsActivity extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener {
	
	private final String TAG = UserSettingsActivity.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String themeName = sharedPrefs
			.getString(getResources().getString(R.string.key_theme), "UrbanGameTheme.Default");
		int themeID = this.getResources().getIdentifier(themeName, "style", this.getPackageName());
		setTheme(themeID);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.user_settings);
	}
	
	@Override
	protected void onResume() {
		super.onDestroy();
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.i(TAG, "key " + key);
		if (key.equals(getString(R.string.key_notifications_on))) {
			if (sharedPreferences.getBoolean(getString(R.string.key_notifications_on), true)) {
				NotificationServer.getInstance(this).turnOnNotifications();
				Log.i(TAG, "Setting: Notifications On");
			}
			else {
				NotificationServer.getInstance(this).turnOffNotifications();
				Log.i(TAG, "Setting: Notifications Off");
			}
		}
	}
}