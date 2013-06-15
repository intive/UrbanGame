package com.blstream.urbangame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.blstream.urbangame.adapters.GamesListAdapter;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.blstream.urbangame.session.LoginManager;

public class GamesListActivity extends MenuActivity implements OnChildClickListener {
	private static final String TAG = "GamesListActivity";
	
	private GamesListAdapter adapter;
	private ExpandableListView list;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_games_list);
		setSupportProgressBarVisibility(true);
		
		list = (ExpandableListView) findViewById(R.id.listViewAllGamesList);
		
		adapter = new GamesListAdapter(this, R.layout.list_item_game);
		list.setAdapter(adapter);
		list.setOnChildClickListener(this);
		
		setPropertiesOfExpandableListView(adapter);
	}
	
	private void setPropertiesOfExpandableListView(BaseExpandableListAdapter adapter) {
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			list.expandGroup(i);
		}
		
		//disabling ability of collapsing group
		list.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				return true;
			}
		});
	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		Bundle bundle = new Bundle();
		
		UrbanGameShortInfo game = (UrbanGameShortInfo) adapter.getChild(groupPosition, childPosition);
		Long selectedGameId = (game == null ? -1 : game.getID());
		bundle.putLong(GameDetailsActivity.GAME_KEY, selectedGameId);
		
		Intent intent = new Intent(GamesListActivity.this, GameDetailsActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.top_bar_games_list, menu);
		
		configureLoginAction(menu);
		configureSearchAction(menu);
		
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		this.supportInvalidateOptionsMenu();
		Log.i(TAG, "onResume completed");
	}
	
	private void configureLoginAction(Menu menu) {
		final MenuItem loginItem = menu.findItem(R.id.menu_login);
		loginItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Log.i(TAG, "onMenuItemClick(): " + item.getTitleCondensed());
				Intent intent = getLoginIntent();
				startActivity(intent);
				return true;
			}
			
			private Intent getLoginIntent() {
				LoginManager loginManager = LoginManager.getInstance(GamesListActivity.this);
				boolean isUserLoggedIn = loginManager.isUserLoggedIn();
				Intent intent = new Intent(GamesListActivity.this, isUserLoggedIn ? MyGamesActivity.class
					: LoginRegisterActivity.class);
				return intent;
			}
		});
	}
	
	private void configureSearchAction(Menu menu) {
		final MenuItem moreItem = menu.findItem(R.id.menu_more);;
		final MenuItem loginItem = menu.findItem(R.id.menu_login);
		
		MenuItem searchItem = menu.findItem(R.id.menu_list_search);
		searchItem.setOnActionExpandListener(new OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				moreItem.setVisible(false);
				loginItem.setVisible(false);
				return true;
			}
			
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				moreItem.setVisible(true);
				loginItem.setVisible(true);
				return true;
			}
		});
		
		SearchView searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				return true;
			}
		});
	}
}