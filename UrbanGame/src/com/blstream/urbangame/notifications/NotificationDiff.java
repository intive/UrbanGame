package com.blstream.urbangame.notifications;

import java.util.Arrays;

import android.util.Log;

import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;

/**
 * Auxiliary class to make comparison between two games or tasks and obtain
 * user-friendly comparison result to present
 */
public class NotificationDiff {
	
	private final String TAG = NotificationDiff.class.getSimpleName();
	
	private final String arraySeparator = ", ";
	private final String fieldSeparator = ": ";
	private final String newLine = "\n";
	private final String emptyString = "";
	
	/*************************************
	 ********** TASK COMPARISON **********
	 *************************************/
	
	public String getTaskDiff(Task oldTask, Task newTask) {
		StringBuilder diff = new StringBuilder();
		
		if (canTasksBeCompared(oldTask, newTask)) {
			
			diff.append(calculateDiff(Task.FIELD_NAME_TITLE, oldTask.getTitle(), newTask.getTitle()));
			diff.append(calculateDiff(Task.FIELD_NAME_DESCRIPTION, oldTask.getDescription(), newTask.getDescription()));
			diff.append(calculateDiff(Task.FIELD_NAME_IS_REPEATABLE, oldTask.isRepetable(), newTask.isRepetable()));
			diff.append(calculateDiff(Task.FIELD_NAME_IS_HIDDEN, oldTask.isHidden(), newTask.isHidden()));
			diff.append(calculateDiff(Task.FIELD_NAME_NUMBER_HIDDEN, oldTask.getNumberOfHidden(),
				newTask.getNumberOfHidden()));
			diff.append(calculateDiff(Task.FIELD_NAME_ENDTIME, oldTask.getEndTime(), newTask.getEndTime()));
			diff.append(calculateDiff(Task.FIELD_NAME_MAX_POINTS, oldTask.getMaxPoints(), newTask.getMaxPoints()));
			
			if (oldTask.getType() == Task.TASK_TYPE_ABCD && newTask.getType() == Task.TASK_TYPE_ABCD) {
				
				ABCDTask oldABCDtask = (ABCDTask) oldTask;
				ABCDTask newABCDtask = (ABCDTask) newTask;
				
				diff.append(calculateDiff(ABCDTask.FIELD_NAME_QUESTION, oldABCDtask.getQuestion(),
					newABCDtask.getQuestion()));
				
				diff.append(calculateArrayDiff(ABCDTask.FIELD_NAME_ANSWERS, oldABCDtask.getAnswers(),
					newABCDtask.getAnswers()));
			}
		}
		else {
			Log.e(TAG, "getTaskDiff comparision fail");
		}
		
		// remove last newline
		if (diff.length() > 0) {
			diff.deleteCharAt(diff.length() - 1);
		}
		
		return diff.toString();
	}
	
	private boolean canTasksBeCompared(Task oldTask, Task newTask) {
		return (oldTask.getId() == newTask.getId() && oldTask.getType() == newTask.getType());
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
	
	private String calculateDiff(String fieldName, Object oldObject, Object newObject) {
		if (oldObject == null) return emptyString;
		if (newObject == null) return emptyString;
		
		if (oldObject.equals(newObject)) return emptyString;
		
		StringBuilder diffBuilder = new StringBuilder();
		diffBuilder.append(fieldName);
		diffBuilder.append(fieldSeparator);
		diffBuilder.append(newObject.toString());
		diffBuilder.append(newLine);
		
		return diffBuilder.toString();
	}
	
	private String calculateArrayDiff(String fieldName, Object[] oldObject, Object[] newObject) {
		if (oldObject == null) return emptyString;
		if (newObject == null) return emptyString;
		
		if (oldObject.length != newObject.length || Arrays.equals(oldObject, newObject)) return emptyString;
		
		StringBuilder arrayDiffBuilder = new StringBuilder();
		
		for (int i = 0; i < oldObject.length; ++i) {
			if (!oldObject[i].equals(newObject[i])) {
				if (arrayDiffBuilder.length() == 0) {
					arrayDiffBuilder.append(fieldName);
					arrayDiffBuilder.append(fieldSeparator);
				}
				else {
					arrayDiffBuilder.append(arraySeparator);
				}
				
				arrayDiffBuilder.append(newObject[i]);
			}
		}
		
		arrayDiffBuilder.append(newLine);
		return arrayDiffBuilder.toString();
	}
}