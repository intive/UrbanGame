package com.blstream.urbangame.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.blstream.urbangame.R;

@SuppressLint("NewApi")
public class UserSettingsFragment extends PreferenceFragment {
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the preferences from a XML resource
		addPreferencesFromResource(R.xml.user_settings);
	}
}
