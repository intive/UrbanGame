package com.blstream.urbangame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.blstream.urbangame.fragments.GameInfoFragment;
import com.blstream.urbangame.fragments.GameRankingFragment;
import com.blstream.urbangame.fragments.GameTasksFragment;
import com.blstream.urbangame.fragments.TabManager;
import com.blstream.urbangame.web.WebHighLevel;
import com.blstream.urbangame.web.WebHighLevelInterface;
import com.blstream.urbangame.webserver.ServerResponseHandler;
import com.blstream.urbangame.webserver.WebServerNotificationListener;

public class ActiveGameActivity extends AbstractMenuActivity implements WebServerNotificationListener {
	
	private final String TAG = ActiveGameActivity.class.getSimpleName();
	
	public static final String GAME_ID = "game_id";
	public static final String TAG_TAB_INFO = "fragment_info";
	public static final String TAG_TAB_TASKS = "fragment_tasks";
	public static final String TAG_TAB_RANKING = "fragment_ranking";
	private final String TAB_LAST_SELECTED = "tab";
	
	private TabManager tabManager;
	private ServerResponseHandler handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		long gameID = getGameID();
		
		// it downloads tasks for game (current user related) if not in database
		this.handler = new ServerResponseHandler(this);
		WebHighLevelInterface web = new WebHighLevel(handler, this);
		web.downloadTasksForGame(gameID);
		
		setUpTabHost(savedInstanceState);
	}
	
	private long getGameID() {
		long gameID;
		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			gameID = intent.getExtras().getLong(GameDetailsActivity.GAME_KEY, GameDetailsActivity.GAME_NOT_FOUND);
		}
		else {
			gameID = GameDetailsActivity.GAME_NOT_FOUND;
		}
		return gameID;
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
		ActionBar.Tab tabGameInfo = tabManager.prepareTab(TAG_TAB_INFO, tagGameInfo);
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
	
	@Override
	public void onWebServerResponse(Message message) {
		// TODO implement on response behavior
		// FIXME setting views should be moved here
	}
}