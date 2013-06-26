package com.blstream.urbangame.notifications;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.blstream.urbangame.UrbanGameApplication;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
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
	private UrbanGameApplication urbanGameApplication;
	private static NotificationServer instance;
	private final List<NotificationListener> observators;
	private final Handler mHandler;
	private final DatabaseInterface database;
	private final String playerEmail;
	private Context context;
	
	private Runnable serverQuery;
	
	public static NotificationServer getInstance(Context context) {
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
	}
	
	public void updateContext(Context context) {
		this.context = context;
	}
	
	public void setApplication(UrbanGameApplication urbanGameApplication) {
		this.urbanGameApplication = urbanGameApplication;
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
		mHandler.post(serverQuery);
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
		PlayerGameSpecific pgs = database.getUserGameSpecific(playerEmail, newGame.getID());
		pgs.setHasChanges(true);
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
	
	private void setTaskHasChanges(Task newTask) {
		PlayerTaskSpecific pts = database.getPlayerTaskSpecific(newTask.getId(), playerEmail);
		pts.setAreChanges(true);
	}
}