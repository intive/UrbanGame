package com.blstream.urbangame.webserver.helper;

import java.util.Date;

import android.content.Context;
import android.util.Log;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.webserver.asynctask.AsyncSendWebQuery;
import com.blstream.urbangame.webserver.helper.WebResponse.QueryType;

/* WebServerHelper is an intermediary class which issues HTTP requests to web
 * server. After query to web server has been completed listener which you
 * provided as first argument to one of WebServerHelper query methods (for
 * example to "getUrbanGameDetails()" method) will be called. If there was not
 * correct response from web server (for example because of timeout) then you
 * will get as an answer null WebResponse object in your listener. */

public class WebServerHelper {
	private final static String TAG = "WebServerHelper";
	
	public final static String scheme = "http";
	public final static String authority = "blstream.com";
	public final static String basePath = "urbangame";
	public final static String gameSubPath = "games";
	public final static String taskSubpath = "tasks";
	
	//
	// Declarations of the interfaces
	//
	public interface WebServerResponseInterface {
		// This method will be called after a query to web server has completed.
		// If the query was successful webResponse is not null.
		// If the query failed webResponse is null.
		public void onWebServerResponse(WebResponse webResponse);
	}
	
	//
	// Public methods
	//
	public static void getUrbanGameDetails(WebServerResponseInterface webServerResponseInterface, long gid) {
		// This method issues HTTP request to server to collect game
		// data for particular GID number.
		
		if (webServerResponseInterface != null) {
			new AsyncSendWebQuery(webServerResponseInterface, QueryType.GetUrbanGameDetails, gid).execute();
			
			Log.i(TAG, "getUrbanGameDetails success");
		}
		else Log.e(TAG, "getUrbanGameDetails listener not set");
	}
	
	public static void getUrbanGameBaseList(WebServerResponseInterface webServerResponseInterface) {
		// This method issues HTTP request to server to collect list
		// of all available games;
		
		if (webServerResponseInterface != null) {
			new AsyncSendWebQuery(webServerResponseInterface, QueryType.GetUrbanGameBaseList).execute();
			
			Log.i(TAG, "getUrbanGameBaseList success");
		}
		else Log.e(TAG, "getUrbanGameBaseList listener not set");
	}
	
	public static void getTaskList(WebServerResponseInterface webServerResponseInterface, long gid) {
		// This method issues HTTP request to server to collect Task list
		// for particular game.
		
		if (webServerResponseInterface != null) {
			new AsyncSendWebQuery(webServerResponseInterface, QueryType.GetTaskList, gid).execute();
			
			Log.i(TAG, "getTaskList success");
		}
		else Log.e(TAG, "getTaskList listener not set");
	}
	
	public static void getTask(WebServerResponseInterface webServerResponseInterface, long gid, long tid) {
		// This method issues HTTP request to server to collect information
		// about particular Task that belongs to a specified game.
		
		if (webServerResponseInterface != null) {
			new AsyncSendWebQuery(webServerResponseInterface, QueryType.GetTask, gid, tid).execute();
			
			Log.i(TAG, "getTask success");
		}
		else Log.e(TAG, "getTask listener not set");
	}
	
	//
	// Mock methods
	// These methods can be helpful to simulate functionalities of web server.
	//	
	public static Task mockSimulateNewTaskAvailable(int taskType, Context context) {
		// Method returns new object of class "Task" that is not stored in Database. 
		// The first parameter is the type of Task that you want to get as return. 
		// If it wasn't possible to create a new Task null is returned.
		
		DatabaseInterface database = new Database(context);
		long taskId = 0;
		long i;
		
		// Look for task ID that is not stored in Database
		for (i = 1; i < Long.MAX_VALUE; ++i) {
			if (database.getTask(i) == null) {
				taskId = i;
				break;
			}
		}
		
		// If all TIDs are used null is returned
		if (i == Long.MAX_VALUE) {
			Log.e(TAG, "mockSimulateNewTaskAvailable couldn't create new task");
			return null;
		}
		
		Task task = null;
		if (taskType == Task.TASK_TYPE_ABCD) task = new ABCDTask(Long.valueOf(taskId), "ABCDTaskTitle" + taskId,
			"ABCDTaskImage" + taskId, "ABCDTaskDescription" + taskId, true, true, 1, new Date(), 1, "ABCDTaskQuestion"
				+ taskId, new String[] { "A" + taskId, "B" + taskId, "C" + taskId, "D" + taskId });
		else task = new LocationTask(Long.valueOf(taskId), "LocationTaskTitle" + taskId, "LocationTaskImage" + taskId,
			"LocationTaskDescription" + taskId, true, true, 1, new Date(), 1);
		
		return task;
	}
}