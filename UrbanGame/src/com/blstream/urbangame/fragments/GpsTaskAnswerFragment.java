package com.blstream.urbangame.fragments;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.blstream.urbangame.ActiveTaskActivity;
import com.blstream.urbangame.R;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.listeners.GpsLocationListener;

public class GpsTaskAnswerFragment extends SherlockFragment implements OnClickListener {
	
	private DatabaseInterface databaseInterface;
	private Task task;
	private LocationManager locationManager;
	private Activity context;
	private Button submitButton;
	private GpsLocationListener gpsLocationListener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		context = activity;
		
		task = getArguments().getParcelable(Task.TASK_KEY);
		Long taskID;
		if (task != null) {
			taskID = task.getId();
		}
		else {
			taskID = getSelectedTaskID();
		}
		
		Bundle b = activity.getIntent().getExtras();
		if (b != null) {
			gpsLocationListener = b.getParcelable(GpsLocationListener.GPS_LISTENER_KEY);
		}
		if (gpsLocationListener == null) {
			gpsLocationListener = new GpsLocationListener();
			locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, gpsLocationListener);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		
		view = inflater.inflate(R.layout.gps_task_answer, container, false);
		
		submitButton = (Button) view.findViewById(R.id.buttonGpsTaskAnswerSubmit);
		submitButton.setOnClickListener(this);
		return view;
	}
	
	private long getSelectedTaskID() {
		long taskID = 1L;
		
		Bundle arguments = getArguments();
		if (arguments != null) {
			taskID = arguments.getLong(ActiveTaskActivity.TASK_ID, taskID);
		}
		
		return taskID;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
	}
	
	//Get actual position and send it
	@Override
	public void onClick(View v) {
		
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
			|| gpsLocationListener.getLastLocation() == null || gpsLocationListener.getTimeFromLastUpdate() > 5000) {
			
			/*
			 * MISSING CODE
			 */
			//TODO display something when no GPS, waiting for UX answer
			/*
			 * END MISSING CODE
			 */
			
		}
		else {
			Location location = gpsLocationListener.getLastLocation();
			Toast.makeText(context, location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();
			
			boolean result = sendLocationForVeryfication(location);
			/*
			 * MISSING CODE
			 */
			
			//TODO Display result, don't know how UX see it
			
			//TODO Get PlayerTaskSpecific from database and set new Points number
			//how do we know how much points there is for a right place? We get it from the server or count it somehow?
			
			//TODO Store new PlayerTaskSpecyfic in db
			
			/*
			 * END MISSING CODE
			 */
		}
	}
	
	private boolean sendLocationForVeryfication(Location location) {
		/*
		 * MISSING CODE
		 */
		//TODO send Data to server
		
		//TODO wait for answer + display a loading dialog, UX
		
		//TODO get answer
		
		/*
		 * END MISSING CODE
		 */
		
		//FIXME Replace Mock
		return (Math.random() > 0.5) ? true : false;
		
	}
}