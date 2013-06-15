package com.blstream.urbangame.notification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.blstream.urbangame.R;
import com.blstream.urbangame.UrbanGameApplication;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.blstream.urbangame.database.helper.Base64ImageCoder;
import com.blstream.urbangame.notifications.NotificationsManager;
import com.blstream.urbangame.webserver.helper.WebResponse;
import com.blstream.urbangame.webserver.helper.WebServerHelper;

/**
 * This is a singleton notifier class. You can implement an observator interface
 * (NotificationInterface) when there will appear any change in game or task the
 * observator's interface method will be called.
 */

public class NotificationHelper implements WebServerHelper.WebServerResponseInterface {
	
	private final static String TAG = NotificationHelper.class.getSimpleName();
	private final static NotificationHelper instance = new NotificationHelper();
	
	private final List<NotificationInterface> observators;
	private Thread mThread;
	private Handler mHandler;
	private Runnable serverQuery;
	private DatabaseInterface database;
	private Context context;
	private NotificationsManager notificationsManager;
	private Date startDate;
	private Date endDate;
	private long timeToNextQuery;
	private boolean test;
	
	public void onWebServerResponse(WebResponse webResponse) {
		
		// If there was correct response from web server
		if (webResponse != null) {
			switch (webResponse.getQueryType()) {
			
			// If query returned detailed information about single UrbanGame
				case GetUrbanGameDetails: {
					
					UrbanGame serverGame = webResponse.getUrbanGame();
					if (serverGame != null) {
						UrbanGame databaseGame = database.getGameInfo(serverGame.getID());
						boolean success;
						
						// If serverGame is not in a database
						if (databaseGame == null) {
							success = database.insertGameInfo(serverGame);
							Log.i(TAG, "game " + serverGame.getID() + " inserted to database " + success);
							//notificationsManager.notifyGameNew(serverGame.getTitle(), null, serverGame.getID());
						}
						else {
							if (!databaseGame.equals(serverGame)) {
								success = database.updateGame(serverGame);
								Log.i(TAG, "game " + serverGame.getID() + " in database updated " + success);
								//notificationsManager.notifyGameChanged(serverGame.getTitle(), null, serverGame.getID());
							}
							else {
								Log.i(TAG, "server and database game equal " + serverGame.getID());
							}
						}
						
						// Database has got actual info for a game.
						// Now issue query to get a task list for the game.
						WebServerHelper.getTaskList(getInstance(), serverGame.getID().longValue());
					}
					
					break;
				}
				
				// if query returned list of short info for all games
				case GetUrbanGameBaseList: {
					
					if (UrbanGameApplication.isApplicationRunning() == false && test == false) {
						stop();
					}
					else {
						
						List<UrbanGameShortInfo> serverGames = webResponse.getUrbanGameShortInfoList();
						
						if (serverGames != null) {
							// Get detailed information for every game from a server
							for (UrbanGameShortInfo serverGame : serverGames) {
								WebServerHelper.getUrbanGameDetails(getInstance(), serverGame.getID().longValue());
							}
						}
						break;
					}
				}
				// if query returned list of Tasks for a particular game
				case GetTaskList: {
					
					List<Task> serverTasks = webResponse.getTaskList();
					if (serverTasks != null) {
						Task databaseTask;
						boolean success;
						for (Task serverTask : serverTasks) {
							databaseTask = database.getTask(serverTask.getId());
							
							// If serverTask is not in database
							if (databaseTask == null) {
								success = database.insertTaskForGame(webResponse.getGameId(), serverTask);
								Log.i(TAG, "task " + serverTask.getId() + " for game " + webResponse.getGameId()
									+ " inserted to database " + success);
								//notificationsManager.notifyTaskNew(serverTask.getTitle(), null,
								//	webResponse.getGameId(), "gameTittle");
							}
							else {
								if (!databaseTask.equals(serverTask)) {
									success = database.updateTask(serverTask);
									Log.i(TAG, "task " + serverTask.getId() + " for game " + webResponse.getGameId()
										+ " updated in database " + success);
									//notificationsManager.notifyTaskChanged(serverTask.getTitle(), null,
									//	webResponse.getGameId(), "gameTitle");
								}
								else {
									Log.i(TAG, "server and database task equal " + serverTask.getId());
								}
							}
							
						}
					}
					
					break;
				}
				// TODO: send a query to a web server and check
				// if a game is over. If so display notification
				// and get list of winners for the game.
				
				default:
					Log.e(TAG, "Incorrect queryType " + webResponse.getQueryType().toString());
					break;
			}
		}
		
	}
	
	//
	// Constructor
	//
	private NotificationHelper() {
		observators = new ArrayList<NotificationHelper.NotificationInterface>();
		test = false;
		
		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		try {
			startDate = curFormater.parse("04/05/2013");
			endDate = curFormater.parse("04/06/2013");
		}
		catch (ParseException e) {
			Log.e(TAG, "ParseException " + e.toString());
		}
		
		// If you want to run a query to web server use
		// "setWebServerQuery()" instead of "setNoWebServerQuery()";
		
		//setWebServerQuery();
		setNoWebServerQuery();
	}
	
	//
	// Private Methods
	// 
	private void setWebServerQuery() {
		timeToNextQuery = 30 * 1000; // 30 seconds
		serverQuery = new Runnable() {
			@Override
			public void run() {
				WebServerHelper.getUrbanGameBaseList(getInstance());
				Log.i(TAG, "web server query started");
				mHandler.postDelayed(serverQuery, timeToNextQuery);
			}
		};
	}
	
	//
	// Public methods
	//
	public static NotificationHelper getInstance() {
		return instance;
	}
	
	public void start(Context context) {
		if (this.context == null) {
			this.context = context.getApplicationContext();
			database = new Database(context);
			notificationsManager = new NotificationsManager(context);
			
			mThread = new Thread(new Runnable() {
				public void run() {
					Looper.prepare();
					mHandler = new Handler();
					Looper.loop();
				}
			});
			mThread.start();
			
			mHandler.postDelayed(serverQuery, timeToNextQuery);
			Log.i(TAG, "context set");
		}
		else {
			notificationsManager = new NotificationsManager(context);
		}
	}
	
	public void stop() {
		if (context != null && mHandler != null) {
			mHandler.getLooper().quit();
			context = null;
			Log.i(TAG, "looper finished");
		}
	}
	
	public synchronized void addOnNotificationListener(NotificationInterface onNotification) {
		observators.add(onNotification);
	}
	
	public synchronized void removeOnNotificationListener(NotificationInterface onNotification) {
		observators.remove(onNotification);
	}
	
	public synchronized void notifyListeners(NotificationEvent event) {
		for (NotificationInterface listener : observators) {
			listener.onNotification(event);
		}
	}
	
	/**
	 * Interface for notification listener
	 */
	public interface NotificationInterface {
		/**
		 * This method must return activity context in order to notifications
		 * work properly
		 */
		public Context getApplicationContext();
		
		/**
		 * @see NotificationEvent for details
		 */
		public void onNotification(NotificationEvent type);
	}
	
	/**
	 * It is an event that contains information about change obtained from
	 * server. <br>
	 * <br>
	 * <b>getId()</b> - id of changed item (it may be game id or task id, to
	 * determine which id it is check field type) <br>
	 * <b>getType()</b> - type of notification event, may be only TYPE_GAME or
	 * TYPE_TASK <br>
	 * <b>getStatus()</b> - status of notification event, information is the
	 * notification an update, change or new task/game is available.
	 */
	public class NotificationEvent {
		
		public static final int TYPE_GAME = 0x1;
		public static final int TYPE_TASK = 0x2;
		
		public static final int STATUS_UPDATED_STATUS = 0x11;
		public static final int STATUS_CHANGED_DATA = 0x12;
		public static final int STATUS_NEW = 0x13;
		
		private final Long id;
		private final int type;
		private final int status;
		
		public NotificationEvent(Long id, int type, int status) {
			super();
			this.id = id;
			this.type = type;
			this.status = status;
		}
		
		public Long getId() {
			return id;
		}
		
		public int getType() {
			return type;
		}
		
		public int getStatus() {
			return status;
		}
		
	}
	
	//**********************//
	//						//
	//		M O C K	  		//
	//						//
	//**********************//	
	
	private void setNoWebServerQuery() {
		timeToNextQuery = 10 * 1000; // 10 seconds
		serverQuery = new Runnable() {
			@Override
			public void run() {
				
				if (UrbanGameApplication.isApplicationRunning() == false) {
					stop();
				}
				else {
					//it is a web query mock 			
					String loggedPlayer = database.getLoggedPlayerID();
					
					UrbanGameShortInfo game = null;
					Task task = null;
					Random r = new Random();
					
					List<UrbanGameShortInfo> gameList = database.getAllGamesShortInfo();
					if (gameList != null) {
						game = gameList.get(r.nextInt(gameList.size()));
					}
					
					List<Task> taskList = database.getTasksForGame(game.getID());
					if (taskList != null) {
						task = taskList.get(r.nextInt(taskList.size()));
					}
					
					NotificationEvent event = null;
					
					if (r.nextBoolean() || game == null) { //game
					
						if (r.nextBoolean()) { //simulate that new game is available
							UrbanGame newGame = mockSimulateNewGameAvailable();
							database.insertGameInfo(newGame);
							Log.i(TAG, "Mock simulate new game available");
							
							event = new NotificationEvent(newGame.getID(), NotificationEvent.TYPE_GAME,
								NotificationEvent.STATUS_NEW);
							notificationsManager.notifyGameNew(newGame.getTitle(), null, newGame.getID());
						}
						else { //update game
							if (r.nextBoolean()) { // simulate that game is over
							
								if (loggedPlayer != null) changeGameStatus(game, loggedPlayer);
								Log.i(TAG, "Mock simulate game is over");
								
								event = new NotificationEvent(game.getID(), NotificationEvent.TYPE_GAME,
									NotificationEvent.STATUS_UPDATED_STATUS);
								notificationsManager.notifyGameOver(game.getTitle(), null, game.getID());
							}
							else // simulate that game content changed
							{
								Log.i(TAG, "Mock simulate game changed");
								UrbanGame urbanGame = database.getGameInfo(game.getID());
								if (urbanGame != null) updateGameDataContent(r, urbanGame);
								
								event = new NotificationEvent(game.getID(), NotificationEvent.TYPE_GAME,
									NotificationEvent.STATUS_CHANGED_DATA);
								notificationsManager.notifyGameChanged(game.getTitle(), null, game.getID());
							}
						}
					}
					else //task
					{
						if (r.nextBoolean() || task == null) { //simulate that new task is available
						
							Task newTask = null;
							
							if (r.nextBoolean()) {
								newTask = mockSimulateNewTaskAvailable(Task.TASK_TYPE_ABCD);
							}
							else {
								newTask = mockSimulateNewTaskAvailable(Task.TASK_TYPE_LOCATION);
							}
							
							database.insertTaskForGame(game.getID(), newTask);
							Log.i(TAG, "Mock simulate new task available");
							
							event = new NotificationEvent(game.getID(), NotificationEvent.TYPE_TASK,
								NotificationEvent.STATUS_NEW);
							
							notificationsManager.notifyTaskNew(newTask.getTitle(), null, game.getID(), game.getTitle());
						}
						
						else //update task
						{
							if (r.nextBoolean()) { // simulate task status changed
							
								if (loggedPlayer != null) changeTaskStatus(r, task, loggedPlayer);
								
								Log.i(TAG, "Mock simulate task status changed");
								
								event = new NotificationEvent(game.getID(), NotificationEvent.TYPE_TASK,
									NotificationEvent.STATUS_UPDATED_STATUS);
								notificationsManager.notifyTaskChanged(task.getTitle(), null, game.getID(),
									game.getTitle());
							}
							else { // simulate that task content changed							
								updateTaskDataContent(r, task);
								Log.i(TAG, "Mock simulate task changed");
								
								event = new NotificationEvent(task.getId(), NotificationEvent.TYPE_TASK,
									NotificationEvent.STATUS_CHANGED_DATA);
								notificationsManager.notifyTaskChanged(task.getTitle(), null, game.getID(),
									game.getTitle());
							}
						}
					}
					
					// Notify all listeners
					if (event != null) {
						notifyListeners(event);
					}
					
					mHandler.postDelayed(this, timeToNextQuery);
				}
			}
		};
	}
	
	protected void updateTaskDataContent(Random r, Task task) {
		Task newTask;
		
		if (task.getType() == Task.TASK_TYPE_ABCD) {
			newTask = new ABCDTask();
		}
		else {
			newTask = new LocationTask();
		}
		newTask.setId(task.getId());
		switch (task.getType()) {
			case Task.TASK_TYPE_ABCD:
				ABCDTask abcdTask = (ABCDTask) newTask;
				abcdTask.setQuestion(((ABCDTask) task).getQuestion() + " NEW MOCK");
			default:
				if (r.nextBoolean()) {
					newTask.setTitle(task.getTitle() + " NEW MOCK");
				}
				if (r.nextBoolean()) {
					newTask.setDescription(task.getDescription() + " NEW MOCK");
				}
				if (r.nextBoolean()) {
					newTask.setIsRepetable(task.isRepetable() ? false : true);
				}
				if (r.nextBoolean()) {
					newTask.setIsHidden(task.isHidden() ? false : true);
				}
				if (r.nextBoolean()) {
					newTask.setNumberOfHidden(r.nextInt(15));
				}
				break;
		}
		
		boolean success;
		success = database.updateTask(newTask);
		Log.i(TAG, "tasl updated in database " + success);
	}
	
	protected void changeTaskStatus(Random r, Task task, String loggedPlayer) {
		//changing data in database 
		PlayerTaskSpecific pts = new PlayerTaskSpecific();
		pts.setTaskID(task.getId());
		pts.setPlayerEmail(loggedPlayer);
		pts.setStatus(r.nextInt(5)); //randoms status, which are static final ints from 0 to 4
		database.updatePlayerTaskSpecific(pts);
	}
	
	protected void updateGameDataContent(Random r, UrbanGame game) {
		//changing data in database
		
		if (r.nextBoolean()) {
			game.setGameVersion(r.nextDouble());
		}
		if (r.nextBoolean()) {
			game.setTitle(game.getTitle() + " NEW MOCK");
		}
		if (r.nextBoolean()) {
			game.setOperatorName(game.getOperatorName() + " NEW MOCK");
		}
		if (r.nextBoolean()) {
			game.setWinningStrategy(game.getWinningStrategy() + " NEW MOCK");
		}
		if (r.nextBoolean()) {
			game.setDifficulty(r.nextInt(5));
		}
		if (r.nextBoolean()) {
			game.setReward(game.getReward() ? false : true);
		}
		if (r.nextBoolean()) {
			game.setPrizesInfo(game.getPrizesInfo() + " NEW MOCK");
		}
		if (r.nextBoolean()) {
			game.setDescription(game.getDescription() + " NEW MOCK");
		}
		if (r.nextBoolean()) {
			game.setComments(game.getComments() + " NEW MOCK");
		}
		if (r.nextBoolean()) {
			game.setLocation(game.getLocation() + " NEW MOCK");
		}
		
		boolean success;
		success = database.updateGame(game);
		Log.i(TAG, "game updated in database " + success);
	}
	
	protected void changeGameStatus(UrbanGameShortInfo game, String loggedPlayer) {
		//changing database content
		PlayerGameSpecific pgs = new PlayerGameSpecific(loggedPlayer, game.getID(), null);
		pgs.setState(PlayerGameSpecific.GAME_ENDED);
		database.updateUserGameSpecific(pgs);
	}
	
	public Task mockSimulateNewTaskAvailable(int taskType) {
		// Method returns new object of class "Task" that is not stored in Database. 
		// The first parameter is the type of Task that you want to get as return. 
		// If it wasn't possible to create a new Task null is returned.
		
		long taskId = 0;
		long i;
		
		// Look for task ID that is not stored in Database
		for (i = 0; i < Long.MAX_VALUE; ++i) {
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
	
	public UrbanGame mockSimulateNewGameAvailable() {
		// Method returns new object of class "UrbanGame" that is not stored in Database. 
		// If it wasn't possible to create a new UrbanGame null is returned.
		
		long gameId = 0;
		long i;
		
		// Look for game ID that is not stored in Database
		for (i = 0; i < Long.MAX_VALUE; ++i) {
			if (database.getGameInfo(i) == null) {
				gameId = i;
				break;
			}
		}
		
		// If all GIDs are used null is returned
		if (i == Long.MAX_VALUE) {
			Log.e(TAG, "mockSimulateNewTaskAvailable couldn't create new task");
			return null;
		}
		
		return new UrbanGame(Long.valueOf(gameId), Double.valueOf(i), "MyGameTitle" + i, "MyOperatorName" + i,
			"MyWinningStrategy" + i, 10, 15, startDate, endDate, 5, true, "MyPrizesInfo" + i, "MyDescription" + i,
			Base64ImageCoder.convertImage(context.getResources().getDrawable(R.drawable.ic_launcher_big)),
			Base64ImageCoder.convertImage(context.getResources().getDrawable(R.drawable.mock_logo_operator)),
			"MyComments" + i, "MyLocation" + i, "MyDetaisLink" + i);
	}
	
	public void executeTestWebServerQuery(Context context) {
		test = true;
		this.context = context.getApplicationContext();
		database = new Database(context);
		notificationsManager = new NotificationsManager(context);
		
		mThread = new Thread(new Runnable() {
			public void run() {
				Looper.prepare();
				mHandler = new Handler();
				Looper.loop();
			}
		});
		mThread.start();
		setWebServerQuery();
		mHandler.post(serverQuery);
	}
	
	//**********************//
	//						//
	//		M O C K	  END	//
	//						//
	//**********************//
	
}
