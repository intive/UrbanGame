package com.blstream.urbangame.notifications.test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.InstrumentationTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.blstream.urbangame.notification.NotificationHelper;
import com.blstream.urbangame.webserver.mock.MockWebServer;

public class NotificationHelperTest extends InstrumentationTestCase {
	private DatabaseInterface database;
	private MockWebServer mockWebServer;
	private NotificationHelper notificationHelper;
	
	private final int TIMEOUT = 30;
	private CountDownLatch signal = new CountDownLatch(1);
	
	protected void setUp() throws Exception {
		super.setUp();
		
		mockWebServer = new MockWebServer();
		signal = new CountDownLatch(1);
		
		notificationHelper = NotificationHelper.getInstance();
		
		database = new Database(getInstrumentation().getTargetContext());
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testOnWebServerResponse() throws Throwable {
		List<UrbanGameShortInfo> urbanGames = mockWebServer.getMockAllUrbanGames();
		
		UrbanGame serverGame;
		List<Task> serverTasks;
		UrbanGame databaseGame;
		Task databaseTask;
		
		// Add 1/2 games with tasks from server to database
		for (int i = 0; i <= urbanGames.size() / 2; ++i) {
			serverGame = mockWebServer.getMockUrbanGameDetails(urbanGames.get(i).getID());
			
			if (database.getGameInfo(serverGame.getID()) == null) {
				assertTrue(database.insertGameInfo(serverGame));
			}
			else {
				assertTrue(database.updateGame(serverGame));
			}
			
			serverTasks = mockWebServer.getMockTaskList(serverGame.getID());
			for (Task serverTask : serverTasks) {
				if (database.getTask(serverTask.getId()) == null) {
					assertTrue(database.insertTaskForGame(serverGame.getID(), serverTask));
				}
				else {
					assertTrue(database.updateTask(serverTask));
				}
			}
			
			// Check if a game from server was added to database
			databaseGame = database.getGameInfo(serverGame.getID());
			assertNotNull(databaseGame);
			assertTrue(serverGame.equals(databaseGame));
			
			// Check if tasks for a game were added to database
			for (Task serverTask : serverTasks) {
				databaseTask = database.getTask(serverTask.getId());
				assertNotNull(databaseTask);
				assertTrue(serverTask.equals(databaseTask));
			}
		}
		
		// Check if the rest of games from server are not in database		
		for (int i = urbanGames.size() / 2; i < urbanGames.size() / 2; ++i) {
			serverGame = mockWebServer.getMockUrbanGameDetails(urbanGames.get(i).getID());
			databaseGame = database.getGameInfo(serverGame.getID());
			assertNull(databaseGame);
		}
		
		// Modify 1/4 of games inserted into database
		for (int i = 0; i <= urbanGames.size() / 4; ++i) {
			databaseGame = database.getGameInfo((urbanGames.get(i).getID()));
			databaseGame.setDescription("Modified game");
			assertTrue(database.updateGame(databaseGame));
			
			serverTasks = mockWebServer.getMockTaskList(databaseGame.getID());
			// Check if tasks for a game were added to database
			for (Task serverTask : serverTasks) {
				databaseTask = database.getTask(serverTask.getId());
				databaseTask.setDescription("Modified task");
				assertTrue(database.updateTask(databaseTask));
			}
			
			// Check if a game stored in a database and a game from server
			// are different
			serverGame = mockWebServer.getMockUrbanGameDetails(urbanGames.get(i).getID());
			databaseGame = database.getGameInfo(serverGame.getID());
			assertNotNull(databaseGame);
			assertFalse(serverGame.equals(databaseGame));
			
			// Check if tasks stored in a database and tasks from server
			// are different
			for (Task serverTask : serverTasks) {
				databaseTask = database.getTask(serverTask.getId());
				assertNotNull(databaseTask);
				assertFalse(serverTask.equals(databaseTask));
			}
		}
		
		// NotificationHelper should now issue queries to web server
		// and check for changes with database, and update it.
		// Test will now wait for TimeUnit.SECONDS to complete these
		// queries.
		notificationHelper.executeTestWebServerQuery(getInstrumentation().getTargetContext());
		signal.await(TIMEOUT, TimeUnit.SECONDS);
		
		// Check if all games and tasks from server are now stored
		// in database
		for (int i = 0; i < urbanGames.size(); ++i) {
			
			serverGame = mockWebServer.getMockUrbanGameDetails(urbanGames.get(i).getID());
			databaseGame = database.getGameInfo((urbanGames.get(i).getID()));
			assertTrue(databaseGame.equals(serverGame));
			
			serverTasks = mockWebServer.getMockTaskList(databaseGame.getID());
			// Check if tasks for a game were added to database
			for (Task serverTask : serverTasks) {
				databaseTask = database.getTask(serverTask.getId());
				assertTrue(databaseTask.equals(serverTask));
			}
			
		}
		
		// Clear information about games and tasks which are stored
		// in database and which can be obtained from server
		for (int i = 0; i < urbanGames.size(); ++i) {
			serverGame = mockWebServer.getMockUrbanGameDetails(urbanGames.get(i).getID());
			
			/*
			serverTasks =  mockWebServer.getMockTaskList(serverGame.getID());
			for (Task serverTask : serverTasks)
			{
				assertTrue( database.deleteTask(serverGame.getID(), serverTask.getId()));
			}	
			*/
			
			assertTrue(database.deleteGameInfoAndShortInfo(serverGame.getID()));
		}
		
	}
	
}
