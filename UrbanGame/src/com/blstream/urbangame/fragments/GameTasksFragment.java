package com.blstream.urbangame.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

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
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.datastructures.ExpandableListHeader;

public class GameTasksFragment extends SherlockFragment implements OnChildClickListener {
	
	private int numberOfHiddenTasks;
	private int numberOfFindedHiddenTasks;
	
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
		// we don't need title for this two
		hiddenTaskHaeder.setTitle("");
		emptyTaskHaeder.setTitle("");
		
		initLists();
		activeTaskHeader.setArrayChildren(activeTasks);
		inactiveTaskHeader.setArrayChildren(inactiveTasks);
		hiddenTaskHaeder.setArrayChildren(new ArrayList<Task>());
		finishedTaskHeader.setArrayChildren(finishedTasks);
		canceledTaskHeader.setArrayChildren(canceledTasks);
		emptyTaskHaeder.setArrayChildren(new ArrayList<Task>());
		
		// Order of adding headers shouldn't be changed. Display of this fragment depends on it.
		// If changes are made then adjustment should be made in method getGroupView in
		// TaskListExpandableListAdapter class.
		arrayTaskListHeaders.add(activeTaskHeader);
		arrayTaskListHeaders.add(emptyTaskHaeder);
		arrayTaskListHeaders.add(inactiveTaskHeader);
		arrayTaskListHeaders.add(hiddenTaskHaeder);
		arrayTaskListHeaders.add(finishedTaskHeader);
		arrayTaskListHeaders.add(canceledTaskHeader);
		
		adapter = new TaskListExpandableListAdapter(getActivity(), arrayTaskListHeaders);
		adapter.setNumbersOfHiddenTasks(numberOfHiddenTasks, numberOfFindedHiddenTasks);
		
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
		//FIXME At this point there is nothing that indicates which tasks have been canceled,
		// so for now list with canceled tasks is always empty. When something will change adding
		// element to that list should be implemented.
		
		ArrayList<Task> list;
		//FIXME remove mock when it is no longer needed
		list = getMockTaskList();
		// FIXME uncomment after deleting mock
		//list = (ArrayList<Task>) database.getTasksForGame(gameID);
		
		numberOfHiddenTasks = 0;
		numberOfFindedHiddenTasks = 0;
		
		activeTasks = new ArrayList<Task>();
		finishedTasks = new ArrayList<Task>();
		inactiveTasks = new ArrayList<Task>();
		canceledTasks = new ArrayList<Task>();
		
		PlayerTaskSpecific playerTaskSpecific;
		
		for (Task task : list) {
			
			//FIXME mock
			playerTaskSpecific = getPlayerTaskSpecificMock(task.getId());
			// FIXME uncomment
			// 	playerTaskSpecific = database.getPlayerTaskSpecific(task.getId(), playerEmail);
			
			if (playerTaskSpecific == null) {
				if (task.isHidden()) {
					++numberOfHiddenTasks;
				}
				else {
					inactiveTasks.add(task);
				}
			}
			else {
				if (playerTaskSpecific.getWasHidden()) {
					++numberOfFindedHiddenTasks;
					++numberOfHiddenTasks;
				}
				if (playerTaskSpecific.isFinishedByUser()) {
					finishedTasks.add(task);
				}
				else {
					activeTasks.add(task);
				}
			}
		}
	}
	
	// ---- START MOCK DATA
	
	private ArrayList<Task> getMockTaskList() {
		ArrayList<Task> list = new ArrayList<Task>();
		String[] answers = { "answer1", "answer2" };
		Date endTime = (new GregorianCalendar(2013, 7, 13)).getTime();
		list.add(new ABCDTask(Long.valueOf(-1), "Spoon for Gourmand", null, "description", true, false, 8, endTime, 10,
			"question", answers));
		list.add(new LocationTask(Long.valueOf(1), "Title", null, "desctription", false, true, 8, endTime, 6));
		list.add(new ABCDTask(Long.valueOf(2), "Title 2", null, "description", false, false, 8, endTime, 10,
			"question", answers));
		for (int i = 1; i < 6; i++) {
			list.add(new ABCDTask(Long.valueOf(-1), "Title", null, "description", true, false, 8, endTime, 10,
				"question", answers));
		}
		list.add(new ABCDTask(Long.valueOf(-1), "Hidden", null, "description", false, true, 8, endTime, 10, "question",
			answers));
		
		return list;
	}
	
	public static PlayerTaskSpecific getPlayerTaskSpecificMock(Long taskID) {
		PlayerTaskSpecific playerTaskSpecific = null;
		
		switch (taskID.intValue()) {
			case 1:
				playerTaskSpecific = new PlayerTaskSpecific("", taskID, 3, true, true, true);
				break;
			case 2:
				playerTaskSpecific = new PlayerTaskSpecific("", taskID, 4, false, false, false);
				break;
			default:
				break;
		}
		
		return playerTaskSpecific;
	}
	// ---- END MOCK DATA
}