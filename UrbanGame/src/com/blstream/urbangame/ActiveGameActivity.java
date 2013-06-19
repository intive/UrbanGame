package com.blstream.urbangame;

import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.blstream.urbangame.fragments.GameInfoFragment;
import com.blstream.urbangame.fragments.GameRankingFragment;
import com.blstream.urbangame.fragments.GameTasksFragment;
import com.blstream.urbangame.fragments.TabManager;

public class ActiveGameActivity extends MenuActivity {
	
	private final String TAG = ActiveGameActivity.class.getSimpleName();
	
	public static final String GAME_ID = "game_id";
	public static final String TAG_TAB_INFO = "fragment_info";
	public static final String TAG_TAB_TASKS = "fragment_tasks";
	public static final String TAG_TAB_RANKING = "fragment_ranking";
	private final String TAB_LAST_SELECTED = "tab";
	
	private TabManager tabManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		setUpTabHost(savedInstanceState);
	}
	
	private void setUpTabHost(Bundle savedInstanceState) {
		fillTabHost();
		
		if (savedInstanceState != null) {
			int lastSelectedTabIndex = savedInstanceState.getInt(TAB_LAST_SELECTED);
			getSupportActionBar().setSelectedNavigationItem(lastSelectedTabIndex);
		}
	}
	
	private void fillTabHost() {
		tabManager = new TabManager(this, R.id.realtabcontent);
		Bundle extras = getIntent().getExtras();
		
		String tagTasks = getString(R.string.tab_game_tasks);
		ActionBar.Tab tabTasks = tabManager.prepareTab(TAG_TAB_TASKS, tagTasks);
		tabManager.addTab(tabTasks, GameTasksFragment.class, extras);
		
		String tagRanking = getString(R.string.tab_game_ranking);
		ActionBar.Tab tabRanking = tabManager.prepareTab(TAG_TAB_RANKING, tagRanking);
		tabManager.addTab(tabRanking, GameRankingFragment.class, extras);
		
		String tagGameInfo = getString(R.string.tab_game_gameInfo);
		TabSpec tabGameInfo = tabHost.newTabSpec(TAG_TAB_INFO).setIndicator(tagGameInfo);
		tabManager.addTab(tabGameInfo, GameInfoFragment.class, extras);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(TAB_LAST_SELECTED, getSupportActionBar().getSelectedNavigationIndex());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		this.supportInvalidateOptionsMenu();
		Log.i(TAG, "onResume completed");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.top_bar_message, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
}
