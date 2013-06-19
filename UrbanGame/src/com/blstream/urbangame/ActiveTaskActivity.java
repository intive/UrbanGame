package com.blstream.urbangame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.fragments.ABCDTaskAnswerFragment;
import com.blstream.urbangame.fragments.GpsTaskAnswerFragment;
import com.blstream.urbangame.fragments.TabManager;
import com.blstream.urbangame.fragments.TaskDescriptionFragment;

public class ActiveTaskActivity extends AbstractMenuActivity {
	private final String TAG = ActiveTaskActivity.class.getSimpleName();
	
	public static final String TASK_ID = "task_id";
	public static final String TAG_TAB_DESCRIPTION = "fragment_description";
	public static final String TAG_TAB_ANSWER = "fragment_answer";
	
	private final String TAB_LAST_SELECTED = "tab";
	
	private TabManager tabManager;
	private Task task;	//I load it here, because otherwise I would have to load it twice
	
	private DatabaseInterface databaseInterface;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost_layout);
		
		long taskID = getSelectedTaskID();
		databaseInterface = new Database(this);
		this.task = databaseInterface.getTask(taskID);
		
		databaseInterface.closeDatabase();
		configureActionBar();
		setUpTabHost(savedInstanceState);
		databaseInterface.closeDatabase();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		this.supportInvalidateOptionsMenu();
		Log.i(TAG, "onResume completed");
	}
	
	private void configureActionBar() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}
	
	private long getSelectedTaskID() {
		long taskID = 0L;
		
		Bundle arguments = getIntent().getExtras();
		if (arguments != null) {
			taskID = arguments.getLong(ActiveTaskActivity.TASK_ID, taskID);
		}
		
		return taskID;
	}
	
	private void setUpTabHost(Bundle savedInstanceState) {
		fillTabHost();
		
		if (savedInstanceState != null) {
			int lastSelectedTabTag = savedInstanceState.getInt(TAB_LAST_SELECTED);
			getSupportActionBar().setSelectedNavigationItem(lastSelectedTabTag);
		}
	}
	
	private void fillTabHost() {
		tabManager = new TabManager(this, R.id.realtabcontent);
		Intent intent = getIntent();
		Bundle extras = null;
		if (intent != null) {
			extras = intent.getExtras();
		}
		
		if (extras == null) {
			extras = new Bundle();
		}
		extras.putParcelable(Task.TASK_KEY, task);
		
		String tagTaskDescription = getString(R.string.tab_task_description);
		ActionBar.Tab tabDescription = tabManager.prepareTab(TAG_TAB_DESCRIPTION, tagTaskDescription);
		tabManager.addTab(tabDescription, TaskDescriptionFragment.class, extras);
		
		String tagTasksAnswer = getString(R.string.tab_task_answer);
		ActionBar.Tab tabAnswer = tabManager.prepareTab(TAG_TAB_ANSWER, tagTasksAnswer);
		//check weather it should open gps or abcd task fragment
		if (task.getType() == Task.TASK_TYPE_ABCD) {
			tabManager.addTab(tabAnswer, ABCDTaskAnswerFragment.class, extras);
		}
		else {
			tabManager.addTab(tabAnswer, GpsTaskAnswerFragment.class, extras);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(TAB_LAST_SELECTED, getSupportActionBar().getSelectedNavigationIndex());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.top_bar_alert, menu);
		menuInflater.inflate(R.menu.top_bar_message, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
}