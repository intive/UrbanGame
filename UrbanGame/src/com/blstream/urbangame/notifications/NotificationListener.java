package com.blstream.urbangame.notifications;

import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;

/**
 * Interface for notification listeners
 */
public interface NotificationListener {
	/**
	 * @see NotificationEvent for details
	 */
	public void onGameWon(UrbanGame game);
	
	public void onGameLost(UrbanGame game);
	
	public void onGameChanged(UrbanGame oldGame, UrbanGame newGame);
	
	public void onTaskNew(UrbanGame game, Task task);
	
	public void onTaskChanged(UrbanGame game, Task oldTask, Task newTask);
}