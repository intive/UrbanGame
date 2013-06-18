package com.blstream.urbangame.notifications.test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.blstream.urbangame.notifications.NotificationServer;
import com.blstream.urbangame.webserver.mock.MockWebServer;

public class NotificationServerTest extends InstrumentationTestCase {
	
	private final String TAG = NotificationServerTest.class.getSimpleName();
	private DatabaseInterface database;
	private MockWebServer mockWebServer;
	private NotificationServer notificationServer;
	
	private final int TIMEOUT = 60;
	private CountDownLatch signal = new CountDownLatch(1);
	
	protected void setUp() throws Exception {
		super.setUp();
		
		mockWebServer = new MockWebServer();
		signal = new CountDownLatch(1);
		notificationServer = NotificationServer.getInstance(getInstrumentation().getTargetContext());
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
		
		// NotificationServer should now issue queries to web server
		// and check for changes with database, and update it.
		// Test will now wait for TimeUnit.SECONDS to complete these
		// queries.
		Log.d(TAG, "starting query");
		notificationServer.executeTestWebServerQuery();
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
		
	}
}
