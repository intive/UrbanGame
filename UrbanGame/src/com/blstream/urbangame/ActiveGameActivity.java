package com.blstream.urbangame;

import android.os.Bundle;
import android.widget.TabHost;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.blstream.urbangame.fragments.GameInfoFragment;
import com.blstream.urbangame.fragments.GameRankingFragment;
import com.blstream.urbangame.fragments.GameTasksFragment;
import com.blstream.urbangame.fragments.TabManager;

public class ActiveGameActivity extends SherlockFragmentActivity {
	private final String TAB_LAST_SELECTED = "tab";
	
	private TabHost tabHost;
	private TabManager tabManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
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
		
		String tagGameInfo = getString(R.string.tab_game_gameInfo);
		tabManager.addTab(tabHost.newTabSpec(tagGameInfo).setIndicator(tagGameInfo), GameInfoFragment.class);
		
		String tagTasks = getString(R.string.tab_game_tasks);
		tabManager.addTab(tabHost.newTabSpec(tagTasks).setIndicator(tagTasks), GameTasksFragment.class);
		
		String tagRanking = getString(R.string.tab_game_ranking);
		tabManager.addTab(tabHost.newTabSpec(tagRanking).setIndicator(tagRanking), GameRankingFragment.class);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(TAB_LAST_SELECTED, tabHost.getCurrentTabTag());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
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