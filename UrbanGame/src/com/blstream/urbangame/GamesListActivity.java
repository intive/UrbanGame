package com.blstream.urbangame;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import android.R.bool;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.blstream.urbangame.adapters.GamesListAdapter;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.blstream.urbangame.database.helper.Base64ImageCoder;
import com.blstream.urbangame.session.LoginManager;

public class GamesListActivity extends MenuListActivity {
	private static final String TAG = "GamesListActivity";
	
	private GamesListAdapter adapter;
	private static boolean initFinished = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSupportProgressBarVisibility(true);
		
		// FIXME remove mock data when it is no longer needed
    		adapter = new GamesListAdapter(this, R.layout.list_item_game, mockData());
    		setListAdapter(adapter);
    		// FIXME remove mock when it is no longer needed
    		if (!initFinished) {
        		putMockDataToDatabase();
        		initFinished = true;
    		}
	}
	
	/************************
	 ***** START MOCKING ****
	 ************************/
	
	public static final Long MOCK_GAME_ID = 100000001L;
	public static final String MOCK_PLAYER_EMAIL = "mockPlayer@mockSite.com";
	
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
	
	@SuppressWarnings("deprecation")
	private void putMockDataToDatabase() {
		DatabaseInterface database = new Database(this);
		Date startDate = new Date(2013, 7, 5);
		Date endDate = new Date(2013, 8, 5);
		database.insertGameShortInfo(new UrbanGameShortInfo(MOCK_GAME_ID, "Looking for dwarwes", "City hall", 5, 100,
			startDate, endDate, true, "Wroclaw", null, null, "details"));
		//database.insertUser(new Player(MOCK_PLAYER_EMAIL, "", "Name", ""));
		//database.setLoggedPlayer(MOCK_PLAYER_EMAIL);
		for (Task element : getMockTaskList()) {
			database.insertTaskForGame(MOCK_GAME_ID, element);
		}
		for (PlayerTaskSpecific element : getPlayerTaskSpecificListMock()) {
			database.insertPlayerTaskSpecific(element);
		}
	}
	
	private ArrayList<Task> getMockTaskList() {
		ArrayList<Task> list = new ArrayList<Task>();
		String[] answers = { "answer1", "answer2" };
		Date endTime = (new GregorianCalendar(2013, 7, 13)).getTime();
		list.add(new ABCDTask(Long.valueOf(100057), "Spoon for Gourmand", Base64ImageCoder.convertImage(getResources()
			.getDrawable(R.drawable.mock_task_image)), "description", true, false, 8, endTime, 10, "question", answers));
		list.add(new LocationTask(Long.valueOf(100058), "Find dwarf with newspaper", Base64ImageCoder
			.convertImage(getResources().getDrawable(R.drawable.mock_task_image)), "desctription", false, true, 8,
			endTime, 6));
		list.add(new ABCDTask(Long.valueOf(100059), "Dwarves", Base64ImageCoder.convertImage(getResources()
			.getDrawable(R.drawable.mock_task_image)), "description", false, false, 8, endTime, 10, "question", answers));
		for (int i = 100050; i <= 100056; i++) {
			list.add(new ABCDTask(Long.valueOf(i), "Another generic task", Base64ImageCoder.convertImage(getResources()
				.getDrawable(R.drawable.mock_task_image)), "description", true, false, 8, endTime, 10, "question",
				answers));
		}
		list.add(new ABCDTask(Long.valueOf(100060), "Hidden", Base64ImageCoder.convertImage(getResources().getDrawable(
			R.drawable.mock_task_image)), "description", false, true, 8, endTime, 10, "question", answers));
		
		return list;
	}
	
	private ArrayList<PlayerTaskSpecific> getPlayerTaskSpecificListMock() {
		
		ArrayList<PlayerTaskSpecific> list = new ArrayList<PlayerTaskSpecific>();
		
		list.add(new PlayerTaskSpecific(MOCK_PLAYER_EMAIL, Long.valueOf(100057), 3, false, true, true, "g",
			PlayerTaskSpecific.ACTIVE));
		list.add(new PlayerTaskSpecific(MOCK_PLAYER_EMAIL, Long.valueOf(100058), 3, false, false, false, "",
			PlayerTaskSpecific.INACTIVE));
		for (int i = 100050; i <= 100056; i++) {
			list.add(new PlayerTaskSpecific(MOCK_PLAYER_EMAIL, Long.valueOf(i), 3, true, false, true, "",
				PlayerTaskSpecific.FINISHED));
		}
		list.add(new PlayerTaskSpecific(MOCK_PLAYER_EMAIL, Long.valueOf(100059), 3, true, false, false, "",
			PlayerTaskSpecific.FINISHED));
		list.add(new PlayerTaskSpecific(MOCK_PLAYER_EMAIL, Long.valueOf(100060), 3, true, false, false, "",
			PlayerTaskSpecific.CANCELED));
		
		return list;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		DatabaseInterface database = new Database(this);
		for (int i = 100050; i <= 100058; i++) {
			database.deletePlayerTaskSpecific(Long.valueOf(i), MOCK_PLAYER_EMAIL);
			database.deleteTask(MOCK_GAME_ID, Long.valueOf(i));
		}
		database.deleteTask(MOCK_GAME_ID, Long.valueOf(100059));
		database.deleteTask(MOCK_GAME_ID, Long.valueOf(100060));
		database.deleteGameInfoAndShortInfo(MOCK_GAME_ID);
		database.deletePlayer(MOCK_PLAYER_EMAIL);
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
		
		Intent intent = new Intent(GamesListActivity.this, GameDetailsActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
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