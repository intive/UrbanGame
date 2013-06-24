package com.blstream.urbangame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.date.TimeLeftBuilder;
import com.blstream.urbangame.fragments.ABCDTaskAnswerFragment;
import com.blstream.urbangame.fragments.GpsTaskAnswerFragment;
import com.blstream.urbangame.fragments.TabManager;
import com.blstream.urbangame.fragments.TaskDescriptionFragment;

public class ActiveTaskActivity extends AbstractMenuActivity {
	
	public static final String TASK_ID = "task_id";
	public static final String TAG_TAB_DESCRIPTION = "fragment_description";
	public static final String TAG_TAB_ANSWER = "fragment_answer";
	
	private final String TAB_LAST_SELECTED = "tab";
	
	private TabManager tabManager;
	private Task task;	//I load it here, because otherwise I would have to load it twice
	private PlayerTaskSpecific playerTaskSpecific;
	
	private DatabaseInterface databaseInterface;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		long taskID = getSelectedTaskID();
		databaseInterface = new Database(this);
		this.task = databaseInterface.getTask(taskID);
		this.playerTaskSpecific = databaseInterface
			.getPlayerTaskSpecific(taskID, databaseInterface.getLoggedPlayerID());
		
		if (playerTaskSpecific != null) {
			switch (playerTaskSpecific.getStatus()) {
				case PlayerTaskSpecific.ACTIVE:
					setContentView(R.layout.tabhost_layout);
					setUpTabHost(savedInstanceState);
					break;
				case PlayerTaskSpecific.CANCELED:
				case PlayerTaskSpecific.FINISHED:
				case PlayerTaskSpecific.INACTIVE:
					setContentView(R.layout.activity_active_task);
					setUpDisplay(playerTaskSpecific.getStatus());
					break;
			}
		}
		else {
			// FIXME for test when playerTaskSpecific could be null
			setContentView(R.layout.tabhost_layout);
			setUpTabHost(savedInstanceState);
		}
		
		configureActionBar();
		databaseInterface.closeDatabase();
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
	
	private void setUpDisplay(int status) {
		View listItem = findViewById(R.id.included_list_item_task);
		
		ImageView imageViewTaskLogo = (ImageView) listItem.findViewById(R.id.imageViewTaskLogo);
		ImageView imageViewNewTaskIndicator = (ImageView) listItem.findViewById(R.id.imageViewNewTaskIndicator);
		TextView textViewMaximalTaskPoints = (TextView) listItem.findViewById(R.id.textViewMaximalTaskPoints);
		TextView textViewTaskPoints = (TextView) listItem.findViewById(R.id.textViewTaskPoints);
		TextView textViewTaskRepeatable = (TextView) listItem.findViewById(R.id.textViewTaskRepeatable);
		TextView textViewTaskTimeLeft = (TextView) listItem.findViewById(R.id.textViewTasksTimeLeft);
		TextView textViewTaskTitle = (TextView) listItem.findViewById(R.id.textViewTaskTitle);
		
		textViewTaskTitle.setText(task.getTitle());
		if (task.getType() == Task.TASK_TYPE_ABCD) {
			imageViewTaskLogo.setImageResource(R.drawable.task_abcd_icon);
		}
		else {
			imageViewTaskLogo.setImageResource(R.drawable.task_gps_icon);
		}
		textViewTaskTimeLeft.setText((new TimeLeftBuilder(getResources(), task.getEndTime())).getLeftTime());
		textViewMaximalTaskPoints.setText(task.getMaxPoints().toString());
		if (task.isRepetable()) {
			textViewTaskRepeatable.setText(getText(R.string.label_taksRepeatable));
		}
		else {
			textViewTaskRepeatable.setText(getText(R.string.label_taskNon_repeatable));
		}
		
		if (playerTaskSpecific.getAreChanges()) {
			imageViewNewTaskIndicator.setImageResource(R.drawable.new_task_indicator);
		}
		else {
			imageViewNewTaskIndicator.setImageDrawable(null);
		}
		textViewTaskPoints.setText(playerTaskSpecific.getPoints().toString());
		
		String statusString = "";
		String headerDescription = "";
		String descriptionString = "";
		
		TextView textViewPointsInfo = (TextView) findViewById(R.id.textViewActiveTaskActivityPointsInfo);
		
		switch (status) {
			case PlayerTaskSpecific.CANCELED:
				descriptionString = task.getDescription();
				statusString = getResources().getString(R.string.info_task_canceled);
				headerDescription = getResources().getString(R.string.header_description);
				textViewPointsInfo.setText(getResources().getString(R.string.info_task_canceled_points));
				break;
			case PlayerTaskSpecific.FINISHED:
				descriptionString = task.getDescription();
				statusString = getResources().getString(R.string.info_task_finished);
				headerDescription = getResources().getString(R.string.header_description);
				break;
			case PlayerTaskSpecific.INACTIVE:
				statusString = getResources().getString(R.string.info_task_inactive);
				headerDescription = getResources().getString(R.string.header_prerequisites);
				
				descriptionString = prerequesites();
				break;
		}
		
		TextView textViewStatus = (TextView) findViewById(R.id.textViewActiveTaskActivityStatus);
		textViewStatus.setText(statusString);
		
		TextView header = (TextView) findViewById(R.id.textViewActiveTaskDescriptionHeader);
		header.setText(headerDescription);
		
		TextView textViewDescription = (TextView) findViewById(R.id.textViewActiveTaskDescription);
		textViewDescription.setText(descriptionString);
	}
	
	private String prerequesites() {
		
		String checkMark = "\u2714 ";
		String crossedMark = "\u2718 ";
		
		StringBuilder stringBuilder = new StringBuilder();
		
		// FIXME mock data 2 lines
		String[] mockData = { "First prerequisite", "Second prerequisite", "Third prerequisite" };
		boolean isPrerequisiteCompleted = true;
		
		for (String element : mockData) { // FIXME replace mockData with list of prerequisites
			if (isPrerequisiteCompleted) { // FIXME replace with method that checks if given prerequisite is satisfied
				stringBuilder.append(checkMark);
			}
			else {
				stringBuilder.append(crossedMark);
			}
			stringBuilder.append(element);
			stringBuilder.append("\n\n");
			
			isPrerequisiteCompleted = !isPrerequisiteCompleted; // FIXME  mock  delete
		}
		
		return stringBuilder.toString();
	}
}