package com.blstream.urbangame;

import java.util.ArrayList;

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

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;

public class MyGamesActivity extends SherlockActivity implements OnChildClickListener {
	
	private ExpandableListView mExpandableList;
	private ArrayList<MyGamesParent> mArrayHeaders;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_games);
		setSupportProgressBarVisibility(true);
		
		mExpandableList = (ExpandableListView) findViewById(R.id.expandableListViewMyGamesList);
		mExpandableList.setOnChildClickListener(this);
		
		mArrayHeaders = new ArrayList<MyGamesParent>();
		ArrayList<String> arrayChildren = new ArrayList<String>();
		
		Resources resources = getResources();
		
		MyGamesParent parent = new MyGamesParent();
		parent.setTitle(resources.getString(R.string.header_active));
		arrayChildren = getMockGameChildren();
		parent.setArrayChildren(arrayChildren);
		mArrayHeaders.add(parent);
		
		parent = new MyGamesParent();
		parent.setTitle(resources.getString(R.string.header_observed));
		arrayChildren = getMockGameChildren();
		parent.setArrayChildren(arrayChildren);
		mArrayHeaders.add(parent);
		
		parent = new MyGamesParent();
		parent.setTitle(resources.getString(R.string.header_ended));
		arrayChildren = getMockGameChildren();
		parent.setArrayChildren(arrayChildren);
		mArrayHeaders.add(parent);
		
		//sets the adapter that provides data to the list.
		mExpandableList.setAdapter(new MyGamesExpandableListAdapter(MyGamesActivity.this, mArrayHeaders));
		
	}
	
	private ArrayList<String> getMockGameChildren() {
		ArrayList<String> arrayChildren = new ArrayList<String>();
		for (int j = 0; j < 10; j++) {
			arrayChildren.add("NIE MA TAKIEGO WIDOKU");
		}
		return arrayChildren;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.top_bar_games_list, menu);
		menuInflater.inflate(R.menu.top_bar_menu_more, menu);
		configureSearchAction(menu);
		return true;
	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		Intent i;
		MyGamesParent header = mArrayHeaders.get(groupPosition);
		if (header.getTitle().equals(getResources().getString(R.string.header_observed))) {
			i = new Intent(MyGamesActivity.this, GameDetailsActivity.class);
		}
		else {
			i = new Intent(MyGamesActivity.this, ActiveGameActivity.class);
		}
		startActivity(i);
		return false;
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
	
	private class MyGamesExpandableListAdapter extends BaseExpandableListAdapter {
		
		private final LayoutInflater inflater;
		private final ArrayList<MyGamesParent> mMyGamesParent;
		
		public MyGamesExpandableListAdapter(Context context, ArrayList<MyGamesParent> parent) {
			mMyGamesParent = parent;
			inflater = LayoutInflater.from(context);
		}
		
		@Override
		//counts the number of group/parent items so the list knows how many times calls getGroupView() method
		public int getGroupCount() {
			return mMyGamesParent.size();
		}
		
		@Override
		//counts the number of children items so the list knows how many times calls getChildView() method
		public int getChildrenCount(int i) {
			return mMyGamesParent.get(i).getArrayChildren().size();
		}
		
		@Override
		//gets the title of each parent/group
		public Object getGroup(int i) {
			return mMyGamesParent.get(i).getTitle();
		}
		
		@Override
		//gets the name of each item
		public Object getChild(int i, int i1) {
			return mMyGamesParent.get(i).getArrayChildren().get(i1);
		}
		
		@Override
		public long getGroupId(int i) {
			return i;
		}
		
		@Override
		public long getChildId(int i, int i1) {
			return i1;
		}
		
		@Override
		public boolean hasStableIds() {
			return true;
		}
		
		@Override
		public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
			
			if (view == null) {
				view = inflater.inflate(R.layout.my_games_list_parent, viewGroup, false);
			}
			
			TextView textView = (TextView) view.findViewById(R.id.TextViewMyGamesHeader);
			//"i" is the position of the parent/group in the list
			textView.setText(getGroup(i).toString());
			
			return view;
		}
		
		@Override
		//in this method you must set the text to see the children on the list
		public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
			if (view == null) {
				view = inflater.inflate(R.layout.list_item_game_after_login, viewGroup, false);
			}
			
			TextView textViewGameName = (TextView) view.findViewById(R.id.textViewTempGameAfterLoginName);
			//"i" is the position of the parent/group in the list and 
			//"i1" is the position of the child
			textViewGameName.setText(mMyGamesParent.get(i).getArrayChildren().get(i1));
			
			//return the entire view
			return view;
		}
		
		@Override
		public boolean isChildSelectable(int i, int i1) {
			return true;
		}
		
		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			/* used to make the notifyDataSetChanged() method work */
			super.registerDataSetObserver(observer);
		}
		
	}
	
	private class MyGamesParent {
		private String mTitle;
		
		//this will be info about game after logging in, insted of String,
		//but there is no such class yet
		private ArrayList<String> mArrayChildren;
		
		public String getTitle() {
			return mTitle;
		}
		
		public void setTitle(String mTitle) {
			this.mTitle = mTitle;
		}
		
		public ArrayList<String> getArrayChildren() {
			return mArrayChildren;
		}
		
		public void setArrayChildren(ArrayList<String> mArrayChildren) {
			this.mArrayChildren = mArrayChildren;
		}
	}
}