package com.blstream.urbangame.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.blstream.urbangame.R;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.dialogs.AnswerDialog;
import com.blstream.urbangame.dialogs.AnswerDialog.DialogType;
import com.blstream.urbangame.listeners.GpsLocationListener;
import com.blstream.urbangame.webserver.mock.MockWebServer;

public class GpsTaskAnswerFragment extends SherlockFragment implements OnClickListener {
	
	private Task task;
	private PlayerTaskSpecific playerTaskSpecific;
	private LocationManager locationManager;
	private Activity context;
	private Button submitButton;
	private GpsLocationListener gpsLocationListener;
	private static final int GPS_UPDATE_TIME = 1000;
	private static final int GPS_UPDATE_LOCATION = 0;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		context = activity;
		
		task = getArguments().getParcelable(Task.TASK_KEY);
		
		Intent activityIntent = activity.getIntent();
		Bundle extras = null;
		if (activityIntent != null) {
			extras = activity.getIntent().getExtras();
		}
		
		gpsLocationListener = new GpsLocationListener();
		locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_UPDATE_TIME, GPS_UPDATE_LOCATION,
			gpsLocationListener);
		
		DatabaseInterface database = new Database(activity);
		playerTaskSpecific = database.getPlayerTaskSpecific(task.getId(), database.getLoggedPlayerID());
		database.closeDatabase();
		Toast.makeText(activity, "pobrano taks sp: " + playerTaskSpecific, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		
		view = inflater.inflate(R.layout.gps_task_answer, container, false);
		
		submitButton = (Button) view.findViewById(R.id.buttonGpsTaskAnswerSubmit);
		submitButton.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
	}
	
	//Get actual position and send it
	@Override
	public void onClick(View v) {
		
		AnswerDialog dialog = new AnswerDialog(context);
		
		//gps is off
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			dialog.showDialog(DialogType.GPS_OFF);
		}
		//no gps signal
		else if (gpsLocationListener.getLastLocation() == null || !gpsLocationListener.isGPSFix()) {
			dialog.showDialog(DialogType.NO_GPS_SIGNAL);
		}
		//no network connection
		else if (!isOnline()) {
			dialog.showDialog(DialogType.NO_INTERNET_CONNECTION);
		}
		//everything went ok, we are sending data to server
		else {
			
			Location location = gpsLocationListener.getLastLocation();
			
			int result = sendLocationForVeryfication(location);
			
			int maxPoints = task.getMaxPoints();
			
			if (result == maxPoints) {
				dialog.showDialog(DialogType.RIGHT_ANSWER, result, maxPoints);
			}
			else if (result == 0) {
				dialog.showDialog(DialogType.WRONG_ANSWER, result, maxPoints);
			}
			else {
				dialog.showDialog(DialogType.PARTIALLY_RIGHT_ANSWER, result, maxPoints);
			}
			
			//this shouldn't happen in future, this if is to test it before we have playerTaskSpecyfic in db
			if (playerTaskSpecific == null) {
				playerTaskSpecific = new PlayerTaskSpecific();
				playerTaskSpecific.setTaskID(task.getId());
			}
			
			playerTaskSpecific.setPoints(result);
			playerTaskSpecific.setIsFinishedByUser(true);
			
			DatabaseInterface database = new Database(context);
			database.updatePlayerTaskSpecific(playerTaskSpecific);
			database.closeDatabase();
		}
	}
	
	private int sendLocationForVeryfication(Location location) {
		
		//FIXME get real server data
		MockWebServer mockWebServer = new MockWebServer();
		return mockWebServer.sendGPSLocation(context, task, location);
	}
	
	//check internet connection
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) return true;
		return false;
	}
	
}