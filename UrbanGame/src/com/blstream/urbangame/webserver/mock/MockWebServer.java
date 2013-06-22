package com.blstream.urbangame.webserver.mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.blstream.urbangame.webserver.helper.WebResource;
import com.blstream.urbangame.webserver.helper.WebResponse.QueryType;
import com.google.gson.Gson;

/* MockWebServer class is simulating web server behaviour. Try to not use this
 * class directly in code anywhere else then in tests, so there will be less
 * code to correct once a real web server is used. To access data kept in
 * MockWebServer use WebServerHelper class instead. */

public class MockWebServer {
	private final String TAG = "MockWebServer";
	
	private final ArrayList<UrbanGameShortInfo> mockAllUrbanGames;
	private final ArrayList<UrbanGame> mockUrbanGameDetails;
	private final Hashtable<Long, ArrayList<Task>> mockTaskLists;
	
	//
	// Constructor
	//
	public MockWebServer() {
		
		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = curFormater.parse("04/05/2013 08:40");
			endDate = curFormater.parse("04/06/2013 13:40");
		}
		catch (ParseException e) {
			Log.e(TAG, "ParseException " + e.toString());
		}
		
		mockUrbanGameDetails = new ArrayList<UrbanGame>();
		
		for (int i = 0; i < 6; ++i) {
			mockUrbanGameDetails.add(new UrbanGame(Long.valueOf(i), Double.valueOf(i), "MyGameTitle" + i,
				"MyOperatorName" + i, "MyWinningStrategy" + i, i, i, startDate, endDate, i, true, "MyPrizesInfo" + i,
				"MyDescription" + i, null, null, "MyComments" + i, "MyLocation" + i, "MyDetaisLink" + i));
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
					"A", "B", "C", "D" }));
			++tid;
			
			taskList.add(new LocationTask(Long.valueOf(tid), "LocationTaskTitle" + tid, "LocationTaskImage1" + tid,
				"LocationTaskDescription" + tid, true, true, tid, endDate, tid));
			++tid;
			
			mockTaskLists.put(Long.valueOf(i), taskList);
		}
		
	}
	
	private String removeBracesFromJson(String jsonString) {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(jsonString);
		stringBuilder.delete(0, 1);
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		return stringBuilder.toString();
	}
	
	private String createJsonLink(String resource, String link) {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("\"");
		stringBuilder.append(resource);
		stringBuilder.append("\": { \"href\": \"");
		stringBuilder.append(link);
		stringBuilder.append("\"},");
		return stringBuilder.toString();
	}
	
	private String createSingleLink(String resourceName, String link) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{_links:{");
		stringBuilder.append(createJsonLink(resourceName, link));
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		stringBuilder.append("},");
		return stringBuilder.toString();
	}
	
	//
	// Public methods
	//
	public ArrayList<UrbanGameShortInfo> getMockAllUrbanGames() {
		return mockAllUrbanGames;
	}
	
	public UrbanGame getMockUrbanGameDetails(long gid) {
		UrbanGame urbanGame = null;
		
		for (UrbanGame mockUrbanGame : mockUrbanGameDetails)
			if (mockUrbanGame.getID() == gid) {
				urbanGame = mockUrbanGame;
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
			for (Task mockTask : taskList) {
				if (mockTask.getId() == tid) {
					task = mockTask;
					break;
				}
			}
		}
		
		return task;
		
	}
	
	public String getResponse(String url, String resourceName, QueryType queryType, long gid, long tid) {
		// Method returns JSON string which is a server response for
		// a queryString
		
		if (url == null) {
			Log.e(TAG, "uri NULL");
			return null;
		}
		
		Log.i(TAG, "url: " + url);
		Log.i(TAG, "resourceName " + resourceName);
		
		Gson gson = new Gson();
		StringBuilder stringBuilder = new StringBuilder();
		int i;
		
		if (resourceName.equals(WebResource.RESOURCE_ROOT)) {
			stringBuilder.append("{_links:{");
			stringBuilder.append(createJsonLink("self", "/"));
			stringBuilder.append(createJsonLink("games", "/games"));
			stringBuilder.append(createJsonLink("login", "/login"));
			stringBuilder.append(createJsonLink("register", "/register"));
			stringBuilder.append(createJsonLink("userGames", "/my/games"));
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			stringBuilder.append("}}");
		}
		else if (resourceName.equals(WebResource.RESOURCE_GAMES)) {
			stringBuilder.append("{_links:{");
			stringBuilder.append(createJsonLink("self", "/games"));
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			stringBuilder.append("},\"_embedded\":[");
			
			for (i = 0; i < mockAllUrbanGames.size(); ++i) {
				stringBuilder.append(createSingleLink("self", "/games/" + mockAllUrbanGames.get(i).getID()));
				stringBuilder.append(removeBracesFromJson(gson.toJson(mockAllUrbanGames.get(i))));
				stringBuilder.append("},");
			}
			
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			stringBuilder.append("]}");
		}
		else if (resourceName.equals(WebResource.RESOURCE_SELF)) {
			if (url.contains("game") && !url.contains("task")) {
				
				stringBuilder.append("{_links:{");
				stringBuilder.append(createJsonLink("self", "/game/" + gid));
				stringBuilder.append(createJsonLink("gameStatic", "/game/" + gid + "/static"));
				stringBuilder.append(createJsonLink("gameDynamic", "/game/" + gid + "/dynamic"));
				stringBuilder.append(createJsonLink("userGameStatus", "/my/games/" + gid + "/dynamic"));
				stringBuilder.append(createJsonLink("tasks", "/games/" + gid + "/tasks"));
				stringBuilder.append(createJsonLink("games", "/games"));
				stringBuilder.deleteCharAt(stringBuilder.length() - 1);
				stringBuilder.append("},");
				
				UrbanGame urbanGame = getMockUrbanGameDetails(gid);
				if (urbanGame != null) {
					stringBuilder.append(removeBracesFromJson(gson.toJson(urbanGame)));
				}
				stringBuilder.append("}");
			}
			else if (url.contains("game") && url.contains("task")) {
				stringBuilder.append("{_links:{");
				stringBuilder.append(createJsonLink("self", "/games/" + gid + "/tasks/" + tid));
				stringBuilder.append(createJsonLink("game", "/games/" + gid));
				stringBuilder.append(createJsonLink("taskStatic", "/game/" + gid + "/tasks/" + tid + "/static"));
				stringBuilder.append(createJsonLink("taskDynamic", "/game/" + gid + "/tasks/" + tid + "/dynamic"));
				stringBuilder
					.append(createJsonLink("userTaskStatus", "/my/games/" + gid + "/tasks/" + tid + "/dynamic"));
				stringBuilder.deleteCharAt(stringBuilder.length() - 1);
				stringBuilder.append("},");
				
				Task task = getMockSingleTask(gid, tid);
				if (task != null) {
					stringBuilder.append(removeBracesFromJson(gson.toJson(task)));
				}
				stringBuilder.append("}");
			}
		}
		
		else if (resourceName.equals(WebResource.RESOURCE_TASKS)) {
			stringBuilder.append("{_links:{");
			stringBuilder.append(createJsonLink("self", "/games/" + gid + "/tasks"));
			stringBuilder.append(createJsonLink("game", "/game/" + gid));
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			stringBuilder.append("},\"_embedded\":[");
			
			ArrayList<Task> taskList = mockTaskLists.get(gid);
			for (i = 0; i < taskList.size(); ++i) {
				stringBuilder.append(createSingleLink("self", "/games/" + mockAllUrbanGames.get(i).getID() + "/task/"
					+ taskList.get(i).getId()));
				stringBuilder.append(removeBracesFromJson(gson.toJson(taskList.get(i))));
				stringBuilder.append("},");
			}
			
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			stringBuilder.append("]}");
		}
		
		if (stringBuilder.length() == 0) return null;
		
		return stringBuilder.toString();
	}
	
	//returns points
	public int sendGPSLocation(Context context, Task task, Location location) {
		
		double random = Math.random();
		int points = task.getMaxPoints();
		
		if (random > 0.8) return points;
		if (random < 0.2) return 0;
		return (int) (points * random);
		
	}
}