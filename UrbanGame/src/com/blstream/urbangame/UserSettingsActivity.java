package com.blstream.urbangame;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.fragments.UserSettingsFragment;
import com.blstream.urbangame.notifications.NotificationServer;

public class UserSettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	private final String TAG = UserSettingsActivity.class.getSimpleName();
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//  for honeycomb and newer versions
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			getFragmentManager().beginTransaction().replace(android.R.id.content, new UserSettingsFragment()).commit();
			Log.i(TAG, "honeycomb user settings");
		}
		else {
			addPreferencesFromResource(R.xml.user_settings);
			Log.i(TAG, "older user settings");
		}
		
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
		else if (key.equals(getString(R.string.key_database_encryption_on))) {
			Database.use_encryption_flag = sharedPreferences.getBoolean(getString(R.string.key_database_encryption_on),
				false);
			Log.i(TAG, "Setting: Database encryption " + Database.use_encryption_flag);
		}
	}
	
}