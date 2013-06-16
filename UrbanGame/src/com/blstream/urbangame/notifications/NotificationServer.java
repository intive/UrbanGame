package com.blstream.urbangame.notifications;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.os.Handler;

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
 * (NotificationListener) when there will appear any change in game or task the
 * observator's interface method will be called.
 */

public class NotificationServer {
	private final static String TAG = NotificationServer.class.getSimpleName();
	
	private NotificationServer instance;
	private final List<NotificationListener> observators;
	private final Handler mHandler;
	private final DatabaseInterface database;
	private final Context context;
	private final String playerEmail;
	
	private Runnable serverQuery;
	private final long timeToNextQuery = 5 * 60 * 1000; // 5 minutes
	
	public NotificationServer getInstance(Context context) {
		if (instance == null) {
			instance = new NotificationServer(context);
		}
		return instance;
	}
	
	private NotificationServer(Context context) {
		this.observators = new ArrayList<NotificationListener>();
		this.mHandler = new Handler();
		this.context = context;
		this.database = new Database(context);
		this.playerEmail = database.getLoggedPlayerID();
		
		turnOnNotifications();
		setServerQuery();
	}
	
	public void turnOnNotifications() {
		registerNotificationListener(new NotificationsManager(context));
	}
	
	public void turnOffNotifications() {
		for (NotificationListener notificationListener : observators) {
			observators.remove(notificationListener);
		}
	}
	
	public synchronized void registerNotificationListener(NotificationListener notificationListener) {
		if (observators.isEmpty()) {
			startCallback();
		}
		observators.add(notificationListener);
	}
	
	public synchronized void unregisterNotificationListener(NotificationListener notificationListener) {
		observators.remove(notificationListener);
		if (observators.isEmpty()) {
			cancellCallback();
		}
	}
	
	private void startCallback() {
		mHandler.postDelayed(serverQuery, 100);
	}
	
	private void cancellCallback() {
		mHandler.removeCallbacks(serverQuery);
	}
	
	private synchronized void notifyGameWon(UrbanGame game) {
		setGameHasChanges(game);
		
		for (NotificationListener notificationListener : observators) {
			notificationListener.onGameWon(game);
		}
	}
	
	private synchronized void notifyGameLost(UrbanGame game) {
		setGameHasChanges(game);
		
		for (NotificationListener notificationListener : observators) {
			notificationListener.onGameLost(game);
		}
	}
	
	private synchronized void notifyGameChanged(UrbanGame oldGame, UrbanGame newGame) {
		setGameHasChanges(newGame);
		
		for (NotificationListener notificationListener : observators) {
			notificationListener.onGameChanged(oldGame, newGame);
		}
	}
	
	private void setGameHasChanges(UrbanGame newGame) {
		// TODO SET IN DB GAME CHANGES
	}
	
	private synchronized void notifyTaskNew(UrbanGame game, Task task) {
		setTaskHasChanges(task);
		
		for (NotificationListener notificationListener : observators) {
			notificationListener.onTaskNew(game, task);
		}
	}
	
	private synchronized void notifyTaskChanged(UrbanGame game, Task oldTask, Task newTask) {
		setTaskHasChanges(newTask);
		
		for (NotificationListener notificationListener : observators) {
			notificationListener.onTaskChanged(game, oldTask, newTask);
		}
	}
	
	private void setTaskHasChanges(Task task) {
		PlayerTaskSpecific pts = database.getPlayerTaskSpecific(task.getId(), playerEmail);
		pts.setAreChanges(true);
	}
	
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
				DatabaseInterface db = new Database(context);
				String loggedPlayer = db.getLoggedPlayerID();
				List<UrbanGame> gameList = db.getAllUserGames(loggedPlayer);
				Random r = new Random();
				
				UrbanGame game = gameList.get(r.nextInt(gameList.size()));
				List<Task> taskList = db.getTasksForGame(game.getID());
				Task task = taskList.get(r.nextInt(taskList.size()));
				
				// FIXME to be implemented
				// TODO add mock server behaviour to generate notifications 
				
				//**********************//
				//						//
				//		M O C K	  END	//
				//						//
				//**********************//
				mHandler.postDelayed(this, timeToNextQuery);
			}
		};
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
		PlayerGameSpecific pgs = new PlayerGameSpecific(loggedPlayer, game.getID(), null, false);
		pgs.setState(PlayerGameSpecific.GAME_ENDED);
		db.updateUserGameSpecific(pgs);
	}
	//**********************//
	//						//
	//		M O C K	  END	//
	//						//
	//**********************//
}
