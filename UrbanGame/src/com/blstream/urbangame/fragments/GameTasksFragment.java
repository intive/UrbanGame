package com.blstream.urbangame.fragments;

import java.util.ArrayList;

import android.content.Context;
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

import com.actionbarsherlock.app.SherlockFragment;
import com.blstream.urbangame.R;
import com.blstream.urbangame.database.entity.Task;

public class GameTasksFragment extends SherlockFragment implements OnChildClickListener {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tasks, container, false);
		ExpandableListView expandableListViewTaskList = (ExpandableListView) view
			.findViewById(R.id.expandableListViewTasksList);
		
		ArrayList<ExpandableListHeader> arrayTaskListHeaders = new ArrayList<ExpandableListHeader>();
		
		Resources resources = getResources();
		
		ExpandableListHeader activeTaskHeader = new ExpandableListHeader();
		ExpandableListHeader inactiveTaskHeader = new ExpandableListHeader();
		ExpandableListHeader finishedTaskHeader = new ExpandableListHeader();
		ExpandableListHeader canceledTaskHeader = new ExpandableListHeader();
		
		activeTaskHeader.setTitle(resources.getString(R.string.header_active_tasks));
		inactiveTaskHeader.setTitle(resources.getString(R.string.header_inactive_tasks));
		finishedTaskHeader.setTitle(resources.getString(R.string.header_finished_tasks));
		canceledTaskHeader.setTitle(resources.getString(R.string.header_canceled_tasks));
		
		//TODO put list with content here
		activeTaskHeader.setArrayChildren(new ArrayList<Task>());
		inactiveTaskHeader.setArrayChildren(new ArrayList<Task>());
		finishedTaskHeader.setArrayChildren(new ArrayList<Task>());
		canceledTaskHeader.setArrayChildren(new ArrayList<Task>());
		
		arrayTaskListHeaders.add(activeTaskHeader);
		arrayTaskListHeaders.add(inactiveTaskHeader);
		arrayTaskListHeaders.add(finishedTaskHeader);
		arrayTaskListHeaders.add(canceledTaskHeader);
		
		expandableListViewTaskList.setAdapter(new TaskListExpandableListAdapter(getActivity(), arrayTaskListHeaders));
		
		expandableListViewTaskList.setOnChildClickListener(this);
		
		return view;
	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		// TODO navigate to task details 
		return false;
	}
	
	//Adapter for expandable list
	private class TaskListExpandableListAdapter extends BaseExpandableListAdapter {
		
		private final LayoutInflater inflater;
		private final ArrayList<ExpandableListHeader> mExpandableListHeader;
		
		public TaskListExpandableListAdapter(Context context, ArrayList<ExpandableListHeader> parent) {
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
			Task task = mExpandableListHeader.get(groupPosition).getArrayChildren().get(childPosition);
			return task;
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
				convertView = inflater.inflate(R.layout.list_item_task, parent, false);
			}
			//TODO put task info on task list item
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
	
	//Header for expandable list parent ("Active Tasks","Inactive Tasks","Ended Tasks", "Canceld Tasks")
	private class ExpandableListHeader {
		private String mTitle;
		
		private ArrayList<Task> mArrayTaskInfo;
		
		public String getTitle() {
			return mTitle;
		}
		
		public void setTitle(String mTitle) {
			this.mTitle = mTitle;
		}
		
		public ArrayList<Task> getArrayChildren() {
			return mArrayTaskInfo;
		}
		
		public void setArrayChildren(ArrayList<Task> mArrayTaskInfo) {
			this.mArrayTaskInfo = mArrayTaskInfo;
		}
	}
}