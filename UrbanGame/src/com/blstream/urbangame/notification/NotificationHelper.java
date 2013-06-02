package com.blstream.urbangame.notification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;

/**
 * This is a singleton notifier class. You can implement an observator interface
 * (NotificationInterface) when there will appear any change in game or task the
 * observator's interface method will be called.
 */

public class NotificationHelper {
	
	private final static String TAG = NotificationHelper.class.getSimpleName();
	
	//	FIXME this class should be a singleton - but for purpose of mocking it needs context so it cannot be singleton for now
	//	private final NotificationHelper instance = new NotificationHelper();
	private final List<NotificationInterface> observators;
	private final Handler mHandler;
	private Runnable serverQuery;
	
	private static long timeToNextQuery = 5 * 60 * 1000; // 5 minutes
	
	//**********************//
	//						//
	//		M O C K			//
	//						//
	//**********************//
	//context needed for database changes purpose 
	private final Context contextMOCK; //FIXME for mocking only, delete
	
	public NotificationHelper(Context context) {
		contextMOCK = context;
		observators = new ArrayList<NotificationHelper.NotificationInterface>();
		mHandler = new Handler();
		setServerQuery();
	}
	
	//**********************//
	//						//
	//	M O C K		E N D	//
	//						//
	//**********************//
	
	//	FIXME uncomment and delete mock contructor
	//	private NotificationHelper() {
	//		observators = new ArrayList<NotificationHelper.NotificationInterface>();
	//		mHandler = new Handler();
	//		setServerQuery();
	//	}
	
	private void setServerQuery() {
		serverQuery = new Runnable() {
			@Override
			public void run() {
				//it is a web query mock TODO
				//**********************//
				//						//
				//		M O C K			//
				//						//
				//**********************//
				DatabaseInterface db = new Database(contextMOCK);
				String loggedPlayer = db.getLoggedPlayerID();
				List<UrbanGame> gameList = db.getAllUserGames(loggedPlayer);
				Random r = new Random();
				
				UrbanGame game = gameList.get(r.nextInt(gameList.size()));
				List<Task> taskList = db.getTasksForGame(game.getID());
				Task task = taskList.get(r.nextInt(taskList.size()));
				
				NotificationEvent event;
				
				if (r.nextBoolean()) { //game
					if (r.nextBoolean()) { //change status to end
						Log.i(TAG, "GAME... changing status mock server query");
						
						changeGameStatus(game, loggedPlayer, db);
						
						//it notifies all listeners
						event = new NotificationEvent(game.getID(), NotificationEvent.TYPE_GAME,
							NotificationEvent.STATUS_UPDATED_STATUS);
						notifyListeners(event);
					}
					else { //update something
						Log.i(TAG, "GAME... updating some data server query");
						
						updateGameDataContent(r, game, db);
						
						//it notifies all listeners
						event = new NotificationEvent(game.getID(), NotificationEvent.TYPE_GAME,
							NotificationEvent.STATUS_CHANGED_DATA);
						notifyListeners(event);
					}
				}
				else { //task
					if (r.nextBoolean()) { //change status
						Log.i(TAG, "TASK... changing status mock server query");
						
						changeTaskStatus(r, task, loggedPlayer, db);
						
						//it notifies all listeners
						event = new NotificationEvent(game.getID(), NotificationEvent.TYPE_TASK,
							NotificationEvent.STATUS_UPDATED_STATUS);
						notifyListeners(event);
					}
					else { //update something
						Log.i(TAG, "TASK... updating some data mock server query");
						
						updateTaskDataContent(r, task, db);
						
						//it notifies all listeners
						event = new NotificationEvent(task.getId(), NotificationEvent.TYPE_TASK,
							NotificationEvent.STATUS_CHANGED_DATA);
					}
				}
				//**********************//
				//						//
				//		M O C K	  END	//
				//						//
				//**********************//
				mHandler.postDelayed(this, timeToNextQuery);
			}
		};
	}
	
	//FIXME uncomment
	//	public NotificationHelper getInstance() {
	//		return instance;
	//	}
	
	public synchronized void addOnNotificationListener(NotificationInterface onNotification) {
		observators.add(onNotification);
		if (observators.size() == 1) { //when there were nothing before add
			mHandler.postDelayed(serverQuery, 100);
		}
	}
	
	public synchronized void removeOnNotificationListener(NotificationInterface onNotification) {
		observators.remove(onNotification);
		if (observators.size() == 0) { // when there is nothing
			mHandler.removeCallbacks(serverQuery);
		}
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
	 * notification an update or change.
	 */
	public class NotificationEvent {
		
		public static final int TYPE_GAME = 0x1;
		public static final int TYPE_TASK = 0x2;
		
		public static final int STATUS_UPDATED_STATUS = 0x11;
		public static final int STATUS_CHANGED_DATA = 0x12;
		
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
	protected void updateTaskDataContent(Random r, Task task, DatabaseInterface db) {
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
				Calendar c = Calendar.getInstance();
				if (r.nextBoolean()) {
					c.setTime(task.getEndTime());
					c.add(Calendar.DAY_OF_MONTH, r.nextInt(20));
					newTask.setEndTime(c.getTime());
				}
				if (r.nextBoolean()) {
					newTask.setMaxPoints(r.nextInt(50));
				}
				break;
		}
		db.updateTask(newTask);
	}
	
	protected void changeTaskStatus(Random r, Task task, String loggedPlayer, DatabaseInterface db) {
		//changing data in database 
		PlayerTaskSpecific pts = new PlayerTaskSpecific();
		pts.setTaskID(task.getId());
		pts.setPlayerEmail(loggedPlayer);
		pts.setStatus(r.nextInt(5)); //randoms status, which are static final ints from 0 to 4
		db.updatePlayerTaskSpecific(pts);
	}
	
	protected void updateGameDataContent(Random r, UrbanGame game, DatabaseInterface db) {
		//changing data in database
		UrbanGame newGame = new UrbanGame(game.getID(), null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null, null);
		if (r.nextBoolean()) {
			newGame.setGameVersion(r.nextDouble());
		}
		if (r.nextBoolean()) {
			newGame.setTitle(game.getTitle() + "NEW MOCK");
		}
		if (r.nextBoolean()) {
			newGame.setOperatorName(game.getOperatorName() + "NEW MOCK");
		}
		if (r.nextBoolean()) {
			newGame.setWinningStrategy(game.getWinningStrategy() + "NEW MOCK");
		}
		if (r.nextBoolean()) {
			newGame.setPlayers(r.nextInt(game.getMaxPlayers()));
		}
		if (r.nextBoolean()) {
			newGame.setMaxPlayers(newGame.getPlayers() + r.nextInt(10));
		}
		Calendar c = Calendar.getInstance();
		if (r.nextBoolean()) {
			c.setTime(game.getStartDate());
			c.add(Calendar.DAY_OF_MONTH, r.nextInt(20));
			newGame.setStartDate(c.getTime());
		}
		if (r.nextBoolean()) {
			c.setTime(game.getEndDate());
			c.add(Calendar.DAY_OF_MONTH, r.nextInt(20));
			newGame.setEndDate(c.getTime());
		}
		
		if (r.nextBoolean()) {
			newGame.setDifficulty(r.nextInt(5));
		}
		if (r.nextBoolean()) {
			newGame.setReward(game.getReward() ? false : true);
		}
		if (r.nextBoolean()) {
			newGame.setPrizesInfo(game.getPrizesInfo() + "NEW MOCK");
		}
		if (r.nextBoolean()) {
			newGame.setDescription(game.getDescription() + "NEW MOCK");
		}
		if (r.nextBoolean()) {
			newGame.setComments(game.getComments() + "NEW MOCK");
		}
		if (r.nextBoolean()) {
			newGame.setLocation(game.getLocation() + "NEW MOCK");
		}
		db.updateGame(newGame);
		
	}
	
	protected void changeGameStatus(UrbanGame game, String loggedPlayer, DatabaseInterface db) {
		//changing database content
		PlayerGameSpecific pgs = new PlayerGameSpecific(loggedPlayer, game.getID(), null);
		pgs.setState(PlayerGameSpecific.GAME_ENDED);
		db.updateUserGameSpecific(pgs);
	}
	//**********************//
	//						//
	//		M O C K	  END	//
	//						//
	//**********************//
}
