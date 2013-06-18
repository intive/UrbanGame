package com.blstream.urbangame;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.blstream.urbangame.helpers.ExpandableListViewPropertiesSetter;

public class MyGamesActivity extends AbstractGamesListActivity implements OnChildClickListener {
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
		ArrayList<UrbanGameShortInfo> arrayChildren = new ArrayList<UrbanGameShortInfo>();	//FIXME change to class containing game after login
		
		Resources resources = getResources();
		
		DatabaseInterface database = new Database(this);
		
		ExpandableListHeader parent = new ExpandableListHeader();
		parent.setTitle(resources.getString(R.string.header_active));
		arrayChildren = new ArrayList<UrbanGameShortInfo>();
		List<UrbanGame> urbanGameList = database.getAllUserGames(database.getLoggedPlayerID());
		if (urbanGameList != null) {
			for (UrbanGame game : urbanGameList) {
				arrayChildren.add(game.getPrimaryInfo());
			}
		}
		parent.setArrayChildren(arrayChildren);
		
		mArrayHeaders.add(parent);
		
		parent = new ExpandableListHeader();
		parent.setTitle(resources.getString(R.string.header_observed));
		arrayChildren = mockData();			//FIXME - put observed games here
		parent.setArrayChildren(arrayChildren);
		mArrayHeaders.add(parent);
		
		parent = new ExpandableListHeader();
		parent.setTitle(resources.getString(R.string.header_ended));
		arrayChildren = mockData();			//FIXME - put ended games here
		parent.setArrayChildren(arrayChildren);
		mArrayHeaders.add(parent);
		
		//sets the adapter that provides data to the list.
		MyGamesExpandableListAdapter adapter = new MyGamesExpandableListAdapter(MyGamesActivity.this, mArrayHeaders);
		mExpandableList.setAdapter(adapter);
		
		ExpandableListViewPropertiesSetter.setPropertiesOfExpandableListView(adapter, mExpandableList);
	}
	
	/************************
	 ***** START MOCKING ****
	 ************************/
	
	private ArrayList<UrbanGameShortInfo> mockData() {
		ArrayList<HashMap<String, String>> data = getMockData();
		
		ArrayList<UrbanGameShortInfo> gameItems = new ArrayList<UrbanGameShortInfo>();
		for (HashMap<String, String> map : data) {
			gameItems.add(new UrbanGameShortInfo(map.get("game_name"), map.get("operator_name"), Integer.parseInt(map
				.get("current_players")), Integer.parseInt(map.get("total_players")), new Date(), new Date(), true, map
				.get("location"), null, null, null));
		}
		
		return gameItems;
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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.top_bar_search, menu);
		configureSearchAction(menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
		Intent intent;
		ExpandableListHeader header = mArrayHeaders.get(groupPosition);
		Bundle bundle = new Bundle();
		//UrbanGameShortInfo game = header.getArrayChildren().get(childPosition);	//FIXME change to class containing game after login
		//Long selectedGameId = (game == null ? -1 : game.getID());	//FIXME uncomment when getID won't be null (class comes from database)
		bundle.putLong(GameDetailsActivity.GAME_KEY, -1);
		
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
	
	private void configureSearchAction(Menu menu) {
		final MenuItem moreItem = menu.findItem(R.id.menu_more);
		
		MenuItem searchItem = menu.findItem(R.id.menu_list_search);
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