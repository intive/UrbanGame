package com.blstream.urbangame;

import android.os.Bundle;
import android.widget.TabHost;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.blstream.urbangame.fragments.TabManager;
import com.blstream.urbangame.fragments.TaskAnswerFragment;
import com.blstream.urbangame.fragments.TaskDescriptionFragment;

public class ActiveTaskActivity extends SherlockFragmentActivity {
	private final String TAB_LAST_SELECTED = "tab";
	
	private TabHost tabHost;
	private TabManager tabManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		setUpTabHost();
		
		if (savedInstanceState != null) {
			tabHost.setCurrentTabByTag(savedInstanceState.getString(TAB_LAST_SELECTED));
		}
	}
	
	private void setUpTabHost() {
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		fillTabHost();
	}
	
	private void fillTabHost() {
		tabManager = new TabManager(this, tabHost, R.id.realtabcontent);
		
		String tagTaskDescription = getString(R.string.tab_task_description);
		tabManager.addTab(tabHost.newTabSpec(tagTaskDescription).setIndicator(tagTaskDescription),
			TaskDescriptionFragment.class);
		
		String tagTasksAnswer = getString(R.string.tab_task_answer);
		tabManager.addTab(tabHost.newTabSpec(tagTasksAnswer).setIndicator(tagTasksAnswer), TaskAnswerFragment.class);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(TAB_LAST_SELECTED, tabHost.getCurrentTabTag());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.top_bar_alert, menu);
		menuInflater.inflate(R.menu.top_bar_message, menu);
		menuInflater.inflate(R.menu.top_bar_menu_more, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
			case android.R.id.home:
				finish();
				break;
		}
		return true;
	}
}