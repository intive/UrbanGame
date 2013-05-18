package com.blstream.urbangame;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.blstream.urbangame.menuitem.MenuItemHelper;
import com.blstream.urbangame.adapters.GamesListAdapter;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.blstream.urbangame.database.helper.Base64ImageCoder;

public class MainActivity extends SherlockListActivity {
	private static final String TAG = "MainActivity";
	private GamesListAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSupportProgressBarVisibility(true);
		
		// FIXME remove mock data when it is no longer needed
		adapter = new GamesListAdapter(this, R.layout.list_item_game, mockData());
		setListAdapter(adapter);
	}
	
	/************************
	 ***** START MOCKING ****
	 ************************/
	
	private ArrayList<UrbanGameShortInfo> mockData() {
		ArrayList<UrbanGameShortInfo> mockList = new ArrayList<UrbanGameShortInfo>();
		mockList.add(new UrbanGameShortInfo(Long.valueOf(GameDetailsActivity.GAME_NOT_FOUND), "Krasnale Wroclawskie",
			"BLStream", 25, null, (new GregorianCalendar(2013, 7, 1)).getTime(), (new GregorianCalendar(2013, 7, 13))
				.getTime(), false, "Wroclaw", Base64ImageCoder.convertImage(getResources().getDrawable(
				R.drawable.ic_launcher_big)), Base64ImageCoder.convertImage(getResources().getDrawable(
				R.drawable.mock_logo_operator)), "details"));
		for (int i = 0; i < 10; i++) {
			mockList.add(new UrbanGameShortInfo(Long.valueOf(GameDetailsActivity.GAME_NOT_FOUND),
				"Krasnale Wroclawskie", "BLStream", 25, 75, (new GregorianCalendar(2012, 6, 1)).getTime(),
				(new GregorianCalendar(2013, 7, 3)).getTime(), true, "Wroclaw", Base64ImageCoder
					.convertImage(getResources().getDrawable(R.drawable.ic_launcher_big)), Base64ImageCoder
					.convertImage(getResources().getDrawable(R.drawable.mock_logo_operator)), "details"));
		}
		
		return mockList;
	}
	
	/************************
	 ****** END MOCKING *****
	 ************************/
	
	@Override
	protected void onListItemClick(ListView listView, View clickedView, int posViewInList, long idOfClickedItem) {
		super.onListItemClick(listView, clickedView, posViewInList, idOfClickedItem);
		Bundle bundle = new Bundle();
		
		UrbanGameShortInfo game = adapter.getItem(posViewInList);
		Long selectedGameId = (game == null ? -1 : game.getID());
		bundle.putLong(GameDetailsActivity.GAME_KEY, selectedGameId);
		
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