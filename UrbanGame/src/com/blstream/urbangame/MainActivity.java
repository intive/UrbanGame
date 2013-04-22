package com.blstream.urbangame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;

public class MainActivity extends SherlockListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSupportProgressBarVisibility(true);
		
		mockData();
		DatabaseInterface di = new Database(getApplicationContext());
		System.out.println(di.getAllGamesShortInfo());
		//Exampler.fillDatabase(di);
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
			map.put("game_name", "Krasnale Wroc³awskie");
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
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Bundle bundle = new Bundle();
		//UrbanGameShortInfo game = (UrbanGameShortInfo) (l.getItemAtPosition(position));
		//Long selectedGameId = (game == null ? -1 : game.getID());
		//bundle.putLong(GameDetailsActivity.GAME_KEY, selectedGameId);
		bundle.putLong(GameDetailsActivity.GAME_KEY, -1); //mocked ID. In case of working games list, delete this line and uncomment previous three lines.
		
		Intent intent = new Intent(MainActivity.this, GameDetailsActivity.class);
		intent.putExtras(bundle);
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