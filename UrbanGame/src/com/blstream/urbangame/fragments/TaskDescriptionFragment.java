package com.blstream.urbangame.fragments;

import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.blstream.urbangame.ActiveTaskActivity;
import com.blstream.urbangame.R;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.date.TimeLeftBuilder;

public class TaskDescriptionFragment extends SherlockFragment implements OnClickListener {
	private DatabaseInterface databaseInterface;
	private Task task;
	
	private ImageView imageViewTaskImage;
	private ImageView imageViewTaskTypeIcon;
	private TextView taskTitle;
	private TextView taskDescription;
	private TextView taskTimeLeft;
	private TextView taskRepeatable;
	private TextView taskPoints;
	
	private Dialog imageDialog;
	private ImageView imageViewDialogTaskImage;
	private Bitmap taskImageDrawable;
	
	private int awardedPoints;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		this.imageDialog = new Dialog(activity);
		this.imageViewDialogTaskImage = new ImageView(activity);
		this.databaseInterface = new Database(activity);
		
		task = getArguments().getParcelable(Task.TASK_KEY);
		Long taskID;
		if (task != null) {
			taskID = task.getId();
		}
		else {
			taskID = getSelectedTaskID();
		}
		this.awardedPoints = getPlayerAwardedPoints(taskID);
		this.task = databaseInterface.getTask(taskID);
		
	}
	
	/*
	 * Example of use:
	 * 
	  	Intent showTaskIntent = new Intent(MainActivity.this, ActiveTaskActivity.class);
		showTaskIntent.putExtra(ActiveTaskActivity.TASK_ID, ID);
		startActivity(showTaskIntent);
	 *	
	 *	Where ID is an ID of clicked Task on the tasks list
	 */
	private long getSelectedTaskID() {
		long taskID = 1L;
		
		Bundle arguments = getArguments();
		if (arguments != null) {
			taskID = arguments.getLong(ActiveTaskActivity.TASK_ID, taskID);
		}
		
		return taskID;
	}
	
	private int getPlayerAwardedPoints(long taskID) {
		String playerID = databaseInterface.getLoggedPlayerID();
		int awardedPoints = 0;
		
		if (playerID != null) {
			PlayerTaskSpecific playerTaskSpecific = databaseInterface.getPlayerTaskSpecific(taskID, playerID);
			if (playerTaskSpecific != null) {
				awardedPoints = playerTaskSpecific.getPoints();
			}
		}
		
		return awardedPoints;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*
		 * Configuring dialog with no title, which shows just image view
		 */
		imageDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		imageDialog.setContentView(imageViewDialogTaskImage);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_description, container, false);
		
		taskTitle = (TextView) view.findViewById(R.id.textViewTaskTitle);
		taskDescription = (TextView) view.findViewById(R.id.textViewTaskDescription);
		taskTimeLeft = (TextView) view.findViewById(R.id.textViewTaskTimeLeft);
		taskRepeatable = (TextView) view.findViewById(R.id.textViewIsTaskRepeatable);
		taskPoints = (TextView) view.findViewById(R.id.textViewTaskPoints);
		imageViewTaskTypeIcon = (ImageView) view.findViewById(R.id.imageViewTaskTypeIcon);
		imageViewTaskImage = (ImageView) view.findViewById(R.id.imageViewTaskImage);
		imageViewTaskImage.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		if (task == null) return;
		
		taskTitle.setText(task.getTitle());
		taskDescription.setText(task.getDescription());
		taskTimeLeft.setText(getTimeLeft());
		taskRepeatable.setText(task.isRepetable() ? R.string.label_taksRepeatable : R.string.string_empty);
		taskPoints.setText(awardedPoints + "/" + task.getMaxPoints());
		
		setTaskImage();
		setTaskTypeIcon();
	}
	
	private void setTaskImage() {
		taskImageDrawable = getTaskBitmap();
		imageViewTaskImage.setImageBitmap(taskImageDrawable);
		imageViewDialogTaskImage.setImageBitmap(taskImageDrawable);
	}
	
	private Bitmap getTaskBitmap() {
		String base64logo = task.getPictureBase64();
		byte[] byteImage = Base64.decode(base64logo, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
	}
	
	private void setTaskTypeIcon() {
		int imageResourceId;
		if (task.getType().equals(Task.TASK_TYPE_ABCD)) {
			imageResourceId = R.drawable.task_abcd_icon;
		}
		else {
			imageResourceId = R.drawable.task_gps_icon;
		}
		imageViewTaskTypeIcon.setImageResource(imageResourceId);
	}
	
	private String getTimeLeft() {
		Date endDate = task.getEndTime();
		TimeLeftBuilder timeLeft = new TimeLeftBuilder(getResources(), endDate);
		Log.d("TASK", timeLeft.getLeftTimeForDebug());
		return timeLeft.getLeftTime();
	}
	
	@Override
	public void onClick(View v) {
		if (imageViewTaskImage == v) {
			imageDialog.show();
		}
	}
}