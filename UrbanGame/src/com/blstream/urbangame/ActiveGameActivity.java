package com.blstream.urbangame;

import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.blstream.urbangame.fragments.GameInfoFragment;
import com.blstream.urbangame.fragments.GameRankingFragment;
import com.blstream.urbangame.fragments.GameTasksFragment;
import com.blstream.urbangame.fragments.TabManager;

public class ActiveGameActivity extends SherlockFragmentActivity {
	public static final String GAME_ID = "game_id";
	public static final String TAG_TAB_INFO = "fragment_info";
	public static final String TAG_TAB_TASKS = "fragment_tasks";
	public static final String TAG_TAB_RANKING = "fragment_tanking";
	private final String TAB_LAST_SELECTED = "tab";
	
	private TabHost tabHost;
	private TabManager tabManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		setUpTabHost(savedInstanceState);
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
		
		String tagGameInfo = getString(R.string.tab_game_gameInfo);
		TabSpec tabGameInfo = tabHost.newTabSpec(TAG_TAB_INFO).setIndicator(tagGameInfo);
		tabManager.addTab(tabGameInfo, GameInfoFragment.class, extras);
		
		String tagTasks = getString(R.string.tab_game_tasks);
		TabSpec tabTasks = tabHost.newTabSpec(TAG_TAB_TASKS).setIndicator(tagTasks);
		tabManager.addTab(tabTasks, GameTasksFragment.class, extras);
		
		String tagRanking = getString(R.string.tab_game_ranking);
		TabSpec tabRanking = tabHost.newTabSpec(TAG_TAB_RANKING).setIndicator(tagRanking);
		tabManager.addTab(tabRanking, GameRankingFragment.class, extras);
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