package com.blstream.urbangame.webserver.mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

import android.util.Log;

import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.blstream.urbangame.webserver.helper.WebResponse;
import com.google.gson.Gson;

/* MockWebServer class is simulating web server behavior. Try to not use this
 * class directly in code anywhere else then in tests, so there will be less
 * code to correct once a real web server is used. To access data kept in
 * MockWebServer use WebServerHelper class instead. */

public class MockWebServer {
	private final String TAG = "MockWebServer";
	
	private ArrayList<UrbanGameShortInfo> mockAllUrbanGames;
	private ArrayList<UrbanGame> mockUrbanGameDetails;
	private Hashtable<Long, ArrayList<Task>> mockTaskLists;
	
	//
	// Constructor
	//
	public MockWebServer() {
		
		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = curFormater.parse("04/05/2010");
			endDate = curFormater.parse("04/06/2010");
		}
		catch (ParseException e) {
			Log.e(TAG, "ParseException " + e.toString());
		}
		
		mockUrbanGameDetails = new ArrayList<UrbanGame>();
		
		for (int i = 0; i < 6; ++i) {
			mockUrbanGameDetails.add(new UrbanGame(Long.valueOf(i), Double.valueOf(i), "MyGameTitle" + i,
				"MyOperatorName" + i, "MyWinningStrategy" + i, i, i, startDate, endDate, i, true, "MyPrizesInfo" + i,
				"MyDescription" + i, "MyGameLogoBase64" + i, "MyOperatorLogoBase64" + i, "MyComments" + i, "MyLocation"
					+ i, "MyDetaisLink" + i));
		}
		
		mockAllUrbanGames = new ArrayList<UrbanGameShortInfo>();
		
		for (int i = 0; i < mockUrbanGameDetails.size(); ++i) {
			mockAllUrbanGames.add(new UrbanGameShortInfo(Long.valueOf(i), "MyGameTitle" + i, "MyOperatorName" + i, i,
				i, startDate, endDate, true, "MyLocation" + i, "MyGamelogoBase64" + i, "MyOperatorlogoBase64" + i,
				"MyDetailsLink" + i));
		}
		
		mockTaskLists = new Hashtable<Long, ArrayList<Task>>();
		
		int tid = 0;
		for (int i = 0; i < mockUrbanGameDetails.size(); ++i) {
			ArrayList<Task> taskList = new ArrayList<Task>();
			
			taskList.add(new ABCDTask(Long.valueOf(tid), "ABCDTaskTitle" + tid, "ABCDTaskImage" + tid,
				"ABCDTaskDescription" + tid, true, true, tid, endDate, tid, "ABCDTaskQuestion" + tid, new String[] {
					"A" + tid, "B" + tid, "C" + tid, "D" + tid }));
			++tid;
			
			taskList.add(new LocationTask(Long.valueOf(tid), "LocationTaskTitle" + tid, "LocationTaskImage1" + tid,
				"LocationTaskDescription" + tid, true, true, tid, endDate, tid));
			++tid;
			
			mockTaskLists.put(Long.valueOf(i), taskList);
		}
		
	}
	
	//
	// Public methods
	//
	public ArrayList<UrbanGameShortInfo> getMockAllUrbanGames() {
		return mockAllUrbanGames;
	}
	
	public UrbanGame getMockUrbanGameDetails(long gid) {
		UrbanGame urbanGame = null;
		
		for (int i = 0; i < mockUrbanGameDetails.size(); ++i)
			if (mockUrbanGameDetails.get(i).getID() == gid) {
				urbanGame = mockUrbanGameDetails.get(i);
				break;
			}
		return urbanGame;
	}
	
	public ArrayList<Task> getMockTaskList(long gid) {
		return mockTaskLists.get(gid);
	}
	
	public Task getMockSingleTask(long gid, long tid) {
		ArrayList<Task> taskList = mockTaskLists.get(gid);
		Task task = null;
		
		if (taskList != null) {
			for (int i = 0; i < taskList.size(); ++i) {
				if (taskList.get(i).getId() == tid) {
					task = taskList.get(i);
					break;
				}
			}
		}
		
		return task;
		
	}
	
	public String getResponse(String queryString, int queryType, long gid, long tid) {
		// Method returns JSON string which is a server response for
		// a queryString
		
		Log.i(TAG, "queryString " + queryString);	
		Gson gson = new Gson();
		String returnString = null;
		int i;
		
		switch (queryType) {
			case (WebResponse.queryTypeGetUrbanGameDetails):
				UrbanGame urbanGame = getMockUrbanGameDetails(gid);
				if (urbanGame != null) returnString = gson.toJson(urbanGame);
				
				break;
			
			case (WebResponse.queryTypeGetUrbanGameBaseList):
				returnString = "[";
				
				for (i = 0; i < mockAllUrbanGames.size() - 1; ++i)
					returnString = returnString + gson.toJson(mockAllUrbanGames.get(i)) + ",";
				
				returnString = returnString + gson.toJson(mockAllUrbanGames.get(i)) + "]";
				break;
			
			case (WebResponse.queryTypeGetTaskList):
				
				ArrayList<Task> taskList = mockTaskLists.get(gid);
				if (taskList != null) {
					returnString = "[";
					
					for (i = 0; i < taskList.size() - 1; ++i)
						returnString = returnString + gson.toJson(taskList.get(i)) + ",";
					
					returnString = returnString + gson.toJson(taskList.get(i)) + "]";
				}
				break;
			
			case (WebResponse.queryTypeGetTask):
				Task task = getMockSingleTask(gid, tid);
				if (task != null) returnString = gson.toJson(task);
				
				break;
		}
		
		return returnString;
	}
	
}