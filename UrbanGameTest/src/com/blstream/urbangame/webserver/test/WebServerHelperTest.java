package com.blstream.urbangame.webserver.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.blstream.urbangame.webserver.helper.WebResponse;
import com.blstream.urbangame.webserver.helper.WebServerHelper;
import com.blstream.urbangame.webserver.mock.MockWebServer;

public class WebServerHelperTest extends InstrumentationTestCase {
	private final String TAG = "WebServerHelperTest";
	private final int TIMEOUT = 30;
	private CountDownLatch signal = new CountDownLatch(1);
	private Long gid;
	private Long tid;
	
	private MockUserClass mockUserClass;
	private MockWebServer mockWebServer;
	
	private class MockUserClass implements WebServerHelper.WebServerResponseInterface {
		//
		// Implementation of the interfaces
		//
		public void onWebServerResponse(WebResponse webResponse) {
			// If there was correct response from web server
			if (webResponse != null) {
				
				switch (webResponse.getQueryType()) {
				
				// If query returned information about single UrbanGame
					case GetUrbanGameDetails: {
						UrbanGame urbanGame = webResponse.getUrbanGame();
						
						assertNotNull(urbanGame);
						
						checkUrbanGamesEqual(urbanGame, mockWebServer.getMockUrbanGameDetails(gid));
						Log.d(TAG, "checkUrbanGamesEqual singleGame equal");
						
						signal.countDown();
						break;
					}
					// if query returned list of all available games
					case GetUrbanGameBaseList: {
						
						List<UrbanGameShortInfo> urbanGames = webResponse.getUrbanGameShortInfoList();
						
						assertNotNull(urbanGames);
						
						ArrayList<UrbanGameShortInfo> mockAllUrbanGames = mockWebServer.getMockAllUrbanGames();
						
						for (int i = 0; i < mockAllUrbanGames.size(); ++i) {
							checkUrbanGameShortInfoEqual(mockAllUrbanGames.get(i), urbanGames.get(i));
							Log.d(TAG, "checkUrbanGameShortInfoEqual index i: " + i);
						}
						
						signal.countDown();
						break;
					}
					// if query returned single Task
					case GetTask: {
						Task task = webResponse.getTask();
						assertNotNull(task);
						checkTasksEqual(task, mockWebServer.getMockSingleTask(gid, tid));
						
						Log.d(TAG, "checkTaskEqual singleTask equal");
						signal.countDown();
						break;
					}
					
					// if query returned list of Tasks for a particular game
					case GetTaskList: {
						List<Task> taskList = webResponse.getTaskList();
						assertNotNull(taskList);
						ArrayList<Task> mockTaskList = mockWebServer.getMockTaskList(gid);
						
						for (int i = 0; i < mockTaskList.size(); ++i) {
							checkTasksEqual(mockTaskList.get(i), taskList.get(i));
							Log.d(TAG, "checkTasksEqual index i: " + i);
						}
						
						signal.countDown();
						break;
					}
					default:
					  Log.e(TAG, "Incorrect queryType " + webResponse.getQueryType().toString());
					  break;
					
				}
			}
		}
		
		//
		// Public methods
		//
		public void issueGetUrbanGameDetails(long _gid) {
			gid = _gid;
			WebServerHelper.getUrbanGameDetails(this, gid);
		}
		
		public void issueGetUrbanGameBaseList() {
			WebServerHelper.getUrbanGameBaseList(this);
		}
		
		public void issueGetTaskList(long _gid) {
			gid = _gid;
			WebServerHelper.getTaskList(this, gid);
		}
		
		public void issueGetTask(long _gid, long _tid) {
			gid = _gid;
			tid = _tid;
			WebServerHelper.getTask(this, gid, tid);
		}
		
		public void checkUrbanGamesEqual(UrbanGame expected, UrbanGame actual) {
			
			checkUrbanGameShortInfoEqual(expected.getPrimaryInfo(), actual.getPrimaryInfo());
			
			assertEquals(expected.getComments(), actual.getComments());
			assertEquals(expected.getDescription(), actual.getDescription());
			assertEquals(expected.getDifficulty(), actual.getDifficulty());
			assertEquals(expected.getGameVersion(), actual.getGameVersion());
			assertEquals(expected.getPrizesInfo(), actual.getPrizesInfo());
			assertEquals(expected.getWinningStrategy(), actual.getWinningStrategy());
		}
		
		public void checkUrbanGameShortInfoEqual(UrbanGameShortInfo expected, UrbanGameShortInfo actual) {
			assertEquals(expected.getDetailsLink(), actual.getDetailsLink());
			assertEquals(expected.getEndDate(), actual.getEndDate());
			assertEquals(expected.getGameLogoBase64(), actual.getGameLogoBase64());
			assertEquals(expected.getID(), actual.getID());
			assertEquals(expected.getLocation(), actual.getLocation());
			assertEquals(expected.getMaxPlayers(), actual.getMaxPlayers());
			assertEquals(expected.getOperatorLogoBase64(), actual.getOperatorLogoBase64());
			assertEquals(expected.getOperatorName(), actual.getOperatorName());
			assertEquals(expected.getPlayers(), actual.getPlayers());
			assertEquals(expected.getReward(), actual.getReward());
			assertEquals(expected.getStartDate(), actual.getStartDate());
			assertEquals(expected.getTitle(), actual.getTitle());
		}
		
		public void checkTasksEqual(Task expected, Task actual) {
			assertEquals(expected.getDescription(), actual.getDescription());
			assertEquals(expected.getEndTime(), actual.getEndTime());
			assertEquals(expected.getId(), actual.getId());
			assertEquals(expected.getMaxPoints(), actual.getMaxPoints());
			assertEquals(expected.getNumberOfHidden(), actual.getNumberOfHidden());
			assertEquals(expected.getPictureBase64(), actual.getPictureBase64());
			assertEquals(expected.getTitle(), actual.getTitle());
			assertEquals(expected.getType(), actual.getType());
			
			if (expected.getType() == Task.TASK_TYPE_ABCD) {
				ABCDTask expectedABCD = (ABCDTask) expected;
				ABCDTask actualABCD = (ABCDTask) actual;
				
				String[] expectedAnswers = expectedABCD.getAnswers();
				String[] actualAnswers = actualABCD.getAnswers();
				
				for (int i = 0; i < expectedAnswers.length; ++i)
					assertEquals(expectedAnswers[i], actualAnswers[i]);
				
				assertEquals(expectedABCD.getQuestion(), actualABCD.getQuestion());
			}
		}
		
	}
	
	//
	// Test methods
	//
	public void setUp() {
		mockUserClass = new MockUserClass();
		mockWebServer = new MockWebServer();
	}
	
	public void testGetUrbanGameDetails() throws Throwable {
		// Get short information about all available games
		ArrayList<UrbanGameShortInfo> mockAllUrbanGames = mockWebServer.getMockAllUrbanGames();
		
		// For each game issue query to get game details
		for (int i = 0; i < mockAllUrbanGames.size(); ++i) {
			final long gameID = mockAllUrbanGames.get(i).getID();
			runTestOnUiThread(new Runnable() {
				@Override
				public void run() {
					mockUserClass.issueGetUrbanGameDetails(gameID);
				}
			});
			
			signal.await(TIMEOUT, TimeUnit.SECONDS);
			signal = new CountDownLatch(1);
			Log.d(TAG, "testGetUrbanGameDetails() completed");
		}
		
	}
	
	public void testGetUrbanGameBaseList() throws Throwable {
		runTestOnUiThread(new Runnable() {
			@Override
			public void run() {
				mockUserClass.issueGetUrbanGameBaseList();
			}
		});
		
		signal.await(TIMEOUT, TimeUnit.SECONDS);
		signal = new CountDownLatch(1);
		Log.d(TAG, "testGetUrbanGameBaseList() completed");
	}
	
	public void testGetTask() throws Throwable {
		// Get short information about all available games
		ArrayList<UrbanGameShortInfo> mockAllUrbanGames = mockWebServer.getMockAllUrbanGames();
		
		// For each game
		for (int i = 0; i < mockAllUrbanGames.size(); ++i) {
			
			// Get list of tasks that are related to a game
			ArrayList<Task> taskList = mockWebServer.getMockTaskList(mockAllUrbanGames.get(i).getID());
			final long gameID = mockAllUrbanGames.get(i).getID();
			
			// Now get information about the particular Task
			for (int j = 0; j < taskList.size(); ++j) {
				final long taskID = taskList.get(j).getId();
				
				runTestOnUiThread(new Runnable() {
					@Override
					public void run() {
						mockUserClass.issueGetTask(gameID, taskID);
					}
				});
				
				signal.await(TIMEOUT, TimeUnit.SECONDS);
				signal = new CountDownLatch(1);
			}
			
			Log.d(TAG, "testGetTask() completed");
		}
		
	}
	
	public void testGetTaskList() throws Throwable {
		// Get short information about all available games
		ArrayList<UrbanGameShortInfo> mockAllUrbanGames = mockWebServer.getMockAllUrbanGames();
				
		// For each game issue query to get Task list for the game
		for (int i = 0; i < mockAllUrbanGames.size(); ++i) {
			final long gameID = mockAllUrbanGames.get(i).getID();
			
			runTestOnUiThread(new Runnable() {
				@Override
				public void run() {
					mockUserClass.issueGetTaskList(gameID);
				}
			});
			
			signal.await(TIMEOUT, TimeUnit.SECONDS);
			signal = new CountDownLatch(1);
			Log.d(TAG, "testGetTaskList() completed");
		}
		
	}
	
	public void testMockSimulateNewTaskAvailable() {
		DatabaseInterface database = new Database(getInstrumentation().getTargetContext());
		Task task = null;
		
		// simulate that new ABCDTask was created
		task = WebServerHelper.mockSimulateNewTaskAvailable(Task.TASK_TYPE_ABCD, getInstrumentation()
			.getTargetContext());
		
		// Check that ABCDTask was created correctly and is not stored in Database
		assertNotNull(task);
		assertEquals((Integer) Task.TASK_TYPE_ABCD, task.getType());
		assertNull(database.getTask(task.getId()));
		
		// simulate that new LocationTask was created
		task = WebServerHelper.mockSimulateNewTaskAvailable(Task.TASK_TYPE_LOCATION, getInstrumentation()
			.getTargetContext());
		
		// Check that LocationTask was created correctly and is not stored in Database
		assertNotNull(task);
		assertEquals((Integer) Task.TASK_TYPE_LOCATION, task.getType());
		assertNull(database.getTask(task.getId()));
		Log.d(TAG, "testMockSimulateNewTaskAvailable() completed");
	}
	
}