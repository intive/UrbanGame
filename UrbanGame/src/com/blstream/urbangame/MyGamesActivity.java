package com.blstream.urbangame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;

public class MyGamesActivity extends SherlockActivity implements OnItemClickListener {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_games);
		setSupportProgressBarVisibility(true);
		
		mockData();
	}
	
	/************************
	 ***** START MOCKING ****
	 ************************/
	
	private void mockData() {
		ArrayList<HashMap<String, String>> data = getMockData();
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(
			MyGamesActivity.this,
			data,
			R.layout.list_item_game,
			new String[] { "game_name", "operator_name", "location", "start_time", "current_players", "total_players" },
			new int[] { R.id.textViewGameName, R.id.textViewOperatorName, R.id.textViewLocation,
				R.id.textViewStartTime, R.id.textViewNumberOfCurrentPlayers, R.id.textViewNumberOfTotalPlayers });
		
		ListView activeGamesListView = (ListView) findViewById(R.id.ListViewActiveGameList);
		ListView observedGamesListview = (ListView) findViewById(R.id.ListViewObservedGameList);
		ListView endedGamesListView = (ListView) findViewById(R.id.ListViewEndedGameList);
		
		activeGamesListView.setOnItemClickListener(this);
		observedGamesListview.setOnItemClickListener(this);
		endedGamesListView.setOnItemClickListener(this);
		
		activeGamesListView.setAdapter(simpleAdapter);
		observedGamesListview.setAdapter(simpleAdapter);
		endedGamesListView.setAdapter(simpleAdapter);
	}
	
	public ArrayList<HashMap<String, String>> getMockData() {
		ArrayList<HashMap<String, String>> listOfMap = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = null;
		Random random = new Random();
		
		for (int i = 0; i < 10; i++) {
			map = new HashMap<String, String>();
			map.put("game_name", "Krasnale Wroc�awskie");
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
	public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
		
		Intent intent = new Intent(MyGamesActivity.this, ActiveGameActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.top_bar_games_list, menu);
		menuInflater.inflate(R.menu.top_bar_menu_more, menu);
		configureSearchAction(menu);
		return true;
	}
	
	private void configureSearchAction(Menu menu) {
		final MenuItem moreItem = menu.findItem(R.id.menu_more);
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
		return super.onMenuItemSelected(featureId, item);
	}
}