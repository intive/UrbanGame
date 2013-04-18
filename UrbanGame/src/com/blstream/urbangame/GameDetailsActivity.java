package com.blstream.urbangame;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class GameDetailsActivity extends SherlockActivity {
	
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getMockData();
	}
	
	private void getMockData() {
		Intent intent = getIntent();
		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				// TODO fill view
			}
		}
	}
	
	@Override public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
			case android.R.id.home:
				finish();
				break;
		}
		return true;
	}
	
	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.top_bar_menu_more, menu);
		return true;
	}
}