package com.blstream.urbangame.webserver.helper;

import android.util.Log;

import com.blstream.urbangame.webserver.helper.WebResponse.QueryType;

/*
 * This class keeps information about web resources and how to use them. 
 */
public class WebResource {
	
	public final static String RESOURCE_ROOT = "root";
	public final static String RESOURCE_GAMES = "games";
	public final static String RESOURCE_SELF = "self";
	public final static String RESOURCE_TASKS = "tasks";
	
	public final static String ROOT_URL = "/";
	
	private final String TAG = WebResource.class.getSimpleName();
	
	public String[] resources;
	public int currentResource;
	
	public WebResource(QueryType queryType) {
		currentResource = -1;
		
		switch (queryType) {
			case GetUrbanGameBaseList:
				resources = new String[] { RESOURCE_ROOT, RESOURCE_GAMES };
				break;
			case GetUrbanGameDetails:
				resources = new String[] { RESOURCE_ROOT, RESOURCE_GAMES, RESOURCE_SELF };
				break;
			case GetTaskList:
				resources = new String[] { RESOURCE_ROOT, RESOURCE_GAMES, RESOURCE_SELF, RESOURCE_TASKS };
				break;
			case GetTask:
				resources = new String[] { RESOURCE_ROOT, RESOURCE_GAMES, RESOURCE_SELF, RESOURCE_TASKS, RESOURCE_SELF };
				break;
			default:
				Log.e(TAG, "Incorrect queryType " + queryType.toString());
				break;
		}
		
	}
	
	public boolean isLastResource() {
		if (currentResource + 1 == resources.length) return true;
		
		return false;
	}
	
	public String getNextResource() {
		if (currentResource + 1 >= resources.length) return null;
		
		++currentResource;
		return resources[currentResource];
	}
}
