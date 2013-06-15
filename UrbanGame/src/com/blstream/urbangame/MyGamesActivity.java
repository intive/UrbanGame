package com.blstream.urbangame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.blstream.urbangame.helpers.ExpandableListViewPropertiesSetter;

public class MyGamesActivity extends MenuActivity implements OnChildClickListener, OnNavigationListener {
	
	private final String TAG = MyGamesActivity.class.getSimpleName();
	
	private static final Integer ACTIVE_GAMES_KEY = 0x1;
	private static final Integer OBSERVED_GAMES_KEY = 0x2;
	private static final Integer ENDED_GAMES_KEY = 0x3;
	
	private ExpandableListView mExpandableList;
	private ArrayList<ExpandableListHeader> mArrayHeaders;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_games);
		setSupportProgressBarVisibility(true);
		
		mExpandableList = (ExpandableListView) findViewById(R.id.expandableListViewMyGamesList);
		mExpandableList.setOnChildClickListener(this);
		
		mArrayHeaders = new ArrayList<ExpandableListHeader>();
		ArrayList<UrbanGameShortInfo> arrayChildren = new ArrayList<UrbanGameShortInfo>();
		HashMap<Integer, List<UrbanGameShortInfo>> dividedGamesInGroups = readGamesInfo();
		
		Resources resources = getResources();
		
		ExpandableListHeader parent = new ExpandableListHeader();
		parent.setTitle(resources.getString(R.string.header_active));
		arrayChildren = (ArrayList<UrbanGameShortInfo>) dividedGamesInGroups.get(ACTIVE_GAMES_KEY);
		parent.setArrayChildren(arrayChildren);
		mArrayHeaders.add(parent);
		
		parent = new ExpandableListHeader();
		parent.setTitle(resources.getString(R.string.header_observed));
		arrayChildren = (ArrayList<UrbanGameShortInfo>) dividedGamesInGroups.get(OBSERVED_GAMES_KEY);
		parent.setArrayChildren(arrayChildren);
		mArrayHeaders.add(parent);
		
		parent = new ExpandableListHeader();
		parent.setTitle(resources.getString(R.string.header_ended));
		arrayChildren = (ArrayList<UrbanGameShortInfo>) dividedGamesInGroups.get(ENDED_GAMES_KEY);
		parent.setArrayChildren(arrayChildren);
		mArrayHeaders.add(parent);
		
		//sets the adapter that provides data to the list.
		MyGamesExpandableListAdapter adapter = new MyGamesExpandableListAdapter(MyGamesActivity.this, mArrayHeaders);
		mExpandableList.setAdapter(adapter);
		
		ExpandableListViewPropertiesSetter.setPropertiesOfExpandableListView(adapter, mExpandableList);
	}
	
	private HashMap<Integer, List<UrbanGameShortInfo>> readGamesInfo() {
		DatabaseInterface database = new Database(getApplicationContext());
		String email = database.getLoggedPlayerID();
		HashMap<Integer, List<UrbanGameShortInfo>> gamesList = new HashMap<Integer, List<UrbanGameShortInfo>>();
		
		List<UrbanGameShortInfo> gameStateList = database.getAllUserGamesShortInfoByItsState(email,
			PlayerGameSpecific.GAME_ACTIVE);
		gamesList.put(ACTIVE_GAMES_KEY, nullToEmptyArrayList(gameStateList));
		
		gameStateList = database.getAllUserGamesShortInfoByItsState(email, PlayerGameSpecific.GAME_OBSERVED);
		gamesList.put(OBSERVED_GAMES_KEY, nullToEmptyArrayList(gameStateList));
		
		gameStateList = database.getAllUserGamesShortInfoByItsState(email, PlayerGameSpecific.GAME_ENDED);
		gamesList.put(ENDED_GAMES_KEY, nullToEmptyArrayList(gameStateList));
		
		return gamesList;
	}
	
	private <E> List<E> nullToEmptyArrayList(List<E> list) {
		return list == null ? new ArrayList<E>() : list;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		this.supportInvalidateOptionsMenu();
		Log.i(TAG, "onResume completed");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Context context = getSupportActionBar().getThemedContext();
		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.menu_navigation_list,
			R.layout.sherlock_spinner_item);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSupportActionBar().setListNavigationCallbacks(list, this);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.top_bar_my_games_list, menu);
		configureSearchAction(menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
		Intent intent;
		ExpandableListHeader header = mArrayHeaders.get(groupPosition);
		Bundle bundle = new Bundle();
		UrbanGameShortInfo game = header.getArrayChildren().get(childPosition);
		Long selectedGameId = (game == null ? -1 : game.getID());
		bundle.putLong(GameDetailsActivity.GAME_KEY, selectedGameId);
		
		if (header.getTitle().equals(getResources().getString(R.string.header_observed))) {
			intent = new Intent(MyGamesActivity.this, GameDetailsActivity.class);
		}
		else {
			intent = new Intent(MyGamesActivity.this, ActiveGameActivity.class);
		}
		
		intent.putExtras(bundle);
		
		startActivity(intent);
		return false;
	}
	
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Switch to all games
		switch (itemPosition) {
			case 1:
				startActivity(new Intent(this, GamesListActivity.class));
				break;
			default:
				break;
		}
		return false;
	}
	
	private void configureSearchAction(Menu menu) {
		final MenuItem moreItem = menu.findItem(R.id.menu_more);
		
		MenuItem searchItem = menu.findItem(R.id.menu_search);
		searchItem.setOnActionExpandListener(new OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				moreItem.setVisible(false);
				return true;
			}
			
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				moreItem.setVisible(true);
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
	
	//Adapter for expandable list
	private class MyGamesExpandableListAdapter extends BaseExpandableListAdapter {
		
		private final LayoutInflater inflater;
		private final ArrayList<ExpandableListHeader> mExpandableListHeader;
		
		public MyGamesExpandableListAdapter(Context context, ArrayList<ExpandableListHeader> parent) {
			mExpandableListHeader = parent;
			inflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getGroupCount() {
			return mExpandableListHeader.size();
		}
		
		@Override
		public int getChildrenCount(int groupPosition) {
			return mExpandableListHeader.get(groupPosition).getArrayChildren().size();
		}
		
		@Override
		public Object getGroup(int groupPosition) {
			return mExpandableListHeader.get(groupPosition).getTitle();
		}
		
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			UrbanGameShortInfo urbanGameShortInfo = mExpandableListHeader.get(groupPosition).getArrayChildren() //FIXME change to class containing game after login
				.get(childPosition);
			return urbanGameShortInfo;
		}
		
		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}
		
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}
		
		@Override
		public boolean hasStableIds() {
			return true;
		}
		
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.expandable_lists_header, parent, false);
			}
			
			TextView textView = (TextView) convertView.findViewById(R.id.TextViewMyGamesHeader);
			textView.setText(getGroup(groupPosition).toString());
			
			return convertView;
		}
		
		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_item_game, parent, false);
			}
			//FIXME change to class containing game after login
			UrbanGameShortInfo gameItemInfo = (UrbanGameShortInfo) getChild(groupPosition, childPosition);
			
			//FIXME here put data from gameItemInfo into views
			((TextView) convertView.findViewById(R.id.textViewGameName)).setText(gameItemInfo.getTitle());
			//The final view will be different so now I only set Title for example, another items will be set similary 
			
			return convertView;
		}
		
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			super.registerDataSetObserver(observer);
		}
		
	}
	
	//Header for expandable list parent ("Active","Observed","End")
	private class ExpandableListHeader {
		private String mTitle;
		
		private ArrayList<UrbanGameShortInfo> mArrayGameInfo; //FIXME change to class containing game after login
		
		public String getTitle() {
			return mTitle;
		}
		
		public void setTitle(String mTitle) {
			this.mTitle = mTitle;
		}
		
		//FIXME change to class containing game after login
		public ArrayList<UrbanGameShortInfo> getArrayChildren() {
			return mArrayGameInfo;
		}
		
		//FIXME change to class containing game after login
		public void setArrayChildren(ArrayList<UrbanGameShortInfo> mArrayGameInfo) {
			this.mArrayGameInfo = mArrayGameInfo;
		}
	}
}