package com.blstream.urbangame.helpers;

import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

public class ExpandableListViewPropertiesSetter {
	public static void setPropertiesOfExpandableListView(BaseExpandableListAdapter adapter, ExpandableListView list) {
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
}
