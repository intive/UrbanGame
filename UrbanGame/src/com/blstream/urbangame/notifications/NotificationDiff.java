package com.blstream.urbangame.notifications;

import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;

/**
 * Auxiliary class to make comparison between two games or tasks and obtain
 * user-friendly comparison result to present
 */
public class NotificationDiff {
	/*************************************
	 ********** TASK COMPARISON **********
	 *************************************/
	public String getTaskDiff(Task oldTask, Task newTask) {
		StringBuilder diff = new StringBuilder();
		
		if (canTasksBeCompared(oldTask, newTask)) {
			// FIXME to be implemented in diff sub-task
		}
		return "\n\n" + diff.toString();
	}
	
	private boolean canTasksBeCompared(Task oldTask, Task newTask) {
		return oldTask.getId() == newTask.getId();
	}
	
	/*************************************
	 ********** GAME COMPARISON **********
	 *************************************/
	public String getGameDiff(UrbanGame oldGame, UrbanGame newGame) {
		StringBuilder diff = new StringBuilder();
		
		if (canGamesBeCompared(oldGame, newGame)) {
			// FIXME to be implemented in diff sub-task
		}
		return "\n\n" + diff.toString();
	}
	
	private boolean canGamesBeCompared(UrbanGame oldGame, UrbanGame newGame) {
		return oldGame.getID() == newGame.getID();
	}
	
	/*************************************
	 ********* COMMON COMPARISON *********
	 *************************************/
}