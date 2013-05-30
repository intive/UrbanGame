package com.blstream.urbangame.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.blstream.urbangame.ActiveTaskActivity;
import com.blstream.urbangame.GameDetailsActivity;
import com.blstream.urbangame.R;
import com.blstream.urbangame.adapters.TaskListExpandableListAdapter;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.datastructures.ExpandableListHeader;

public class GameTasksFragment extends SherlockFragment implements OnChildClickListener {
	
	public static final String HIDDEN_TASK_HEADER = "Hidden task header";
	public static final String EMPTY_TASK_HEADER = "Empty taks header";
	
	private int numberOfHiddenTasks;
	private int numberOfFoundHiddenTasks;
	
	private ArrayList<Task> activeTasks;
	private ArrayList<Task> finishedTasks;
	private ArrayList<Task> inactiveTasks;
	private ArrayList<Task> canceledTasks;
	
	private DatabaseInterface database;
	private Long gameID;
	private String playerEmail;
	private TaskListExpandableListAdapter adapter;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Intent intent = activity.getIntent();
		if (intent.getExtras() != null) {
			gameID = intent.getExtras().getLong(GameDetailsActivity.GAME_KEY, GameDetailsActivity.GAME_NOT_FOUND);
		}
		else {
			gameID = GameDetailsActivity.GAME_NOT_FOUND;
		}
		database = new Database(getActivity());
		// FIXME mock delete when not needed
		gameID = 1L;
		
		playerEmail = database.getLoggedPlayerID();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tasks, container, false);
		ExpandableListView expandableListViewTaskList = (ExpandableListView) view
			.findViewById(R.id.expandableListViewTasksList);
		
		ArrayList<ExpandableListHeader<Task>> arrayTaskListHeaders = new ArrayList<ExpandableListHeader<Task>>();
		
		Resources resources = getResources();
		
		ExpandableListHeader<Task> activeTaskHeader = new ExpandableListHeader<Task>();
		ExpandableListHeader<Task> inactiveTaskHeader = new ExpandableListHeader<Task>();
		ExpandableListHeader<Task> finishedTaskHeader = new ExpandableListHeader<Task>();
		ExpandableListHeader<Task> canceledTaskHeader = new ExpandableListHeader<Task>();
		ExpandableListHeader<Task> hiddenTaskHaeder = new ExpandableListHeader<Task>();
		ExpandableListHeader<Task> emptyTaskHaeder = new ExpandableListHeader<Task>();
		
		activeTaskHeader.setTitle(resources.getString(R.string.header_active_tasks));
		inactiveTaskHeader.setTitle(resources.getString(R.string.header_inactive_tasks));
		finishedTaskHeader.setTitle(resources.getString(R.string.header_finished_tasks));
		canceledTaskHeader.setTitle(resources.getString(R.string.header_canceled_tasks));
		
		hiddenTaskHaeder.setTitle(HIDDEN_TASK_HEADER);
		emptyTaskHaeder.setTitle(EMPTY_TASK_HEADER);
		
		initLists();
		activeTaskHeader.setArrayChildren(activeTasks);
		inactiveTaskHeader.setArrayChildren(inactiveTasks);
		hiddenTaskHaeder.setArrayChildren(new ArrayList<Task>());
		finishedTaskHeader.setArrayChildren(finishedTasks);
		canceledTaskHeader.setArrayChildren(canceledTasks);
		emptyTaskHaeder.setArrayChildren(new ArrayList<Task>());
		
		// Order of adding headers shouldn't be changed. Display of this fragment depends on it.
		arrayTaskListHeaders.add(activeTaskHeader);
		arrayTaskListHeaders.add(emptyTaskHaeder);
		arrayTaskListHeaders.add(inactiveTaskHeader);
		arrayTaskListHeaders.add(hiddenTaskHaeder);
		arrayTaskListHeaders.add(finishedTaskHeader);
		arrayTaskListHeaders.add(canceledTaskHeader);
		
		adapter = new TaskListExpandableListAdapter(getActivity(), arrayTaskListHeaders);
		adapter.setNumbersOfHiddenTasks(numberOfHiddenTasks, numberOfFoundHiddenTasks);
		
		expandableListViewTaskList.setAdapter(adapter);
		
		expandableListViewTaskList.setOnChildClickListener(this);
		
		// Hide 'expand' arrow.
		expandableListViewTaskList.setGroupIndicator(null);
		
		return view;
	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		Task task = (Task) adapter.getChild(groupPosition, childPosition);
		Intent intent = new Intent(getActivity(), ActiveTaskActivity.class);
		intent.putExtra(ActiveTaskActivity.TASK_ID, task.getId());
		
		startActivity(intent);
		return true;
	}
	
	private void initLists() {
		
		ArrayList<Task> list;
		list = (ArrayList<Task>) database.getTasksForGame(gameID);
		
		numberOfHiddenTasks = 0;
		numberOfFoundHiddenTasks = 0;
		
		activeTasks = new ArrayList<Task>();
		finishedTasks = new ArrayList<Task>();
		inactiveTasks = new ArrayList<Task>();
		canceledTasks = new ArrayList<Task>();
		
		PlayerTaskSpecific playerTaskSpecific;
		
		if (list != null) {
			for (Task task : list) {
				
				playerTaskSpecific = database.getPlayerTaskSpecific(task.getId(), playerEmail);
				
				if (playerTaskSpecific == null) {
					if (task.isHidden()) {
						++numberOfHiddenTasks;
					}
					else {
						inactiveTasks.add(task);
					}
				}
				else {
					switch (playerTaskSpecific.getStatus()) {
						case PlayerTaskSpecific.ACTIVE:
							activeTasks.add(task);
							break;
						case PlayerTaskSpecific.INACTIVE:
							inactiveTasks.add(task);
							break;
						case PlayerTaskSpecific.FINISHED:
							finishedTasks.add(task);
							break;
						case PlayerTaskSpecific.CANCELED:
							canceledTasks.add(task);
							break;
						default:
							break;
					}
					if (playerTaskSpecific.getWasHidden()
						&& (playerTaskSpecific.getStatus() != PlayerTaskSpecific.CANCELED)) {
						++numberOfFoundHiddenTasks;
						++numberOfHiddenTasks;
					}
				}
			} // end of for loop
		}
	}
}