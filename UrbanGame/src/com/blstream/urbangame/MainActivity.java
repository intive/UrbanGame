package com.blstream.urbangame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.blstream.urbangame.menuitem.MenuItemHelper;

public class MainActivity extends SherlockListActivity {
	private static final String TAG = "MainActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSupportProgressBarVisibility(true);
		mockData();
	}
	
	/************************
	 ***** START MOCKING ****
	 ************************/
	
	private void mockData() {
		ArrayList<HashMap<String, String>> data = getMockData();
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this, data, R.layout.list_item_game, new String[] {
			"game_name", "operator_name", "location", "start_time", "current_players", "total_players" }, new int[] {
			R.id.textViewGameName, R.id.textViewOperatorName, R.id.textViewLocation, R.id.textViewStartTime,
			R.id.textViewNumberOfCurrentPlayers, R.id.textViewNumberOfTotalPlayers });
		
		setListAdapter(simpleAdapter);
	}
	
	public ArrayList<HashMap<String, String>> getMockData() {
		ArrayList<HashMap<String, String>> listOfMap = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = null;
		Random random = new Random();
		
		for (int i = 0; i < 10; i++) {
			map = new HashMap<String, String>();
			map.put("game_name", "Krasnale Wrocławskie");
			map.put("operator_name", "BLStream");
			map.put("location", "Wroclaw");
			map.put("start_time", "Mon, Apr 1, 2013 9:00 AM");
			map.put("current_players", String.valueOf(random.nextInt(50)));
			map.put("total_players", String.valueOf(random.nextInt(50)));
			listOfMap.add(map);
		}
		
		return listOfMap;
	}
	
	/************************
	 ****** END MOCKING *****
	 ************************/
	
	@Override
	protected void onListItemClick(ListView listView, View clickedView, int posViewInList, long idOfClickedItem) {
		super.onListItemClick(listView, clickedView, posViewInList, idOfClickedItem);
		Bundle bundle = new Bundle();
		/* FIXME 
		 * Should be uncomment, when listview will be finished.
		 * UrbanGameShortInfo game = (UrbanGameShortInfo) (l.getItemAtPosition(position));
		 * Long selectedGameId = (game == null ? -1 : game.getID());
		 * bundle.putLong(GameDetailsActivity.GAME_KEY, selectedGameId);
		*/
		bundle.putLong(GameDetailsActivity.GAME_KEY, -1); // FIXME mocked ID. In case of working games list, delete this line
		
		Intent intent = new Intent(MainActivity.this, GameDetailsActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.top_bar_games_list, menu);
		menuInflater.inflate(R.menu.top_bar_menu_more, menu);
		Log.i(TAG, "onCreateOptionsMenu");
		MenuItemHelper.initLogoutMenuItem(this, menu);
		
		configureSearchAction(menu);
		configureLoginAction(menu);
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
				Intent intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
				startActivity(intent);
				return true;
			}
		});
	}

	private void configureSearchAction(Menu menu) {
		final MenuItem moreItem = menu.findItem(R.id.menu_more);;
		final MenuItem loginItem = menu.findItem(R.id.menu_login);
		
		MenuItem searchItem = menu.findItem(R.id.menu_search);
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
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
			case R.id.menu_logout:
				MenuItemHelper.invokeActionLogoutMenuItem(this);
				break;
		}
		return true;
	}
}