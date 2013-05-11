package com.blstream.urbangame;

import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.blstream.urbangame.fragments.TabManager;
import com.blstream.urbangame.fragments.TaskAnswerFragment;
import com.blstream.urbangame.fragments.TaskDescriptionFragment;
import com.blstream.urbangame.menuitem.MenuItemHelper;

public class ActiveTaskActivity extends SherlockFragmentActivity {
	private final String TAG = ActiveTaskActivity .class.getSimpleName();
	
	public static final String TASK_ID = "task_id";
	public static final String TAG_TAB_DESCRIPTION = "fragment_description";
	public static final String TAG_TAB_ANSWER = "fragment_answer";
	
	private final String TAB_LAST_SELECTED = "tab";
	
	private TabHost tabHost;
	private TabManager tabManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost_layout);
		
		configureActionBar();
		setUpTabHost(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    this.supportInvalidateOptionsMenu();
		Log.i(TAG, "onResume completed");
	}
	
	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}
	
	private void setUpTabHost(Bundle savedInstanceState) {
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		fillTabHost();
		
		if (savedInstanceState != null) {
			String lastSelectedTabTag = savedInstanceState.getString(TAB_LAST_SELECTED);
			tabHost.setCurrentTabByTag(lastSelectedTabTag);
		}
	}
	
	private void fillTabHost() {
		tabManager = new TabManager(this, tabHost, R.id.realtabcontent);
		Bundle extras = getIntent().getExtras();
		
		String tagTaskDescription = getString(R.string.tab_task_description);
		TabSpec tabDescription = tabHost.newTabSpec(TAG_TAB_DESCRIPTION).setIndicator(tagTaskDescription);
		tabManager.addTab(tabDescription, TaskDescriptionFragment.class, extras);
		
		String tagTasksAnswer = getString(R.string.tab_task_answer);
		TabSpec tabAnswer = tabHost.newTabSpec(TAG_TAB_ANSWER).setIndicator(tagTasksAnswer);
		tabManager.addTab(tabAnswer, TaskAnswerFragment.class, extras);
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
		MenuItemHelper.initLogoutMenuItem(this, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
			case android.R.id.home:
				finish();
				break;
			case R.id.menu_logout:
				MenuItemHelper.invokeActionLogoutMenuItem(this);
				break;
		}
		return true;
	}
}