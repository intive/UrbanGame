package com.blstream.urbangame.notifications;

import android.util.Log;

import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;

/**
 * Auxiliary class to make comparison between two games or tasks and obtain
 * user-friendly comparison result to present
 */
public class NotificationDiff {
	
	private final String TAG = NotificationDiff.class.getSimpleName();
	
	private final String arraySeparator = ", ";
	private final String fieldSeparator = ": ";
	private final String endDiffSeprator = ". ";
	private final String newLine = "\n";
	private final String emptyString = "";
	private final String fieldAdded = "Added";
	private final String fieldRemoved = "Removed";
	
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
			diff.append(calculateDiff(UrbanGameShortInfo.FIELD_NAME_TITLE, oldGame.getTitle(), newGame.getTitle()));
			diff.append(calculateDiff(UrbanGameShortInfo.FIELD_NAME_OPERATOR_NAME, oldGame.getOperatorName(),
				newGame.getOperatorName()));
			diff.append(calculateDiff(UrbanGameShortInfo.FIELD_NAME_PLAYERS, oldGame.getPlayers(), newGame.getPlayers()));
			diff.append(calculateDiff(UrbanGameShortInfo.FIELD_NAME_MAX_PLAYERS, oldGame.getMaxPlayers(),
				newGame.getMaxPlayers()));
			diff.append(calculateDiff(UrbanGameShortInfo.FIELD_NAME_START_DATE, oldGame.getStartDate(),
				newGame.getStartDate()));
			diff.append(calculateDiff(UrbanGameShortInfo.FIELD_NAME_END_DATE, oldGame.getEndDate(),
				newGame.getEndDate()));
			diff.append(calculateDiff(UrbanGameShortInfo.FIELD_NAME_REWARD, oldGame.getReward(), newGame.getReward()));
			diff.append(calculateDiff(UrbanGameShortInfo.FIELD_NAME_LOCATION, oldGame.getLocation(),
				newGame.getLocation()));
			
			diff.append(calculateDiff(UrbanGame.FIELD_NAME_WINNING_STRATEGY, oldGame.getWinningStrategy(),
				newGame.getWinningStrategy()));
			diff.append(calculateDiff(UrbanGame.FIELD_NAME_DIFFICULTY, oldGame.getDifficulty(), newGame.getDifficulty()));
			diff.append(calculateDiff(UrbanGame.FIELD_PRIZES_INFO, oldGame.getPrizesInfo(), newGame.getPrizesInfo()));
			diff.append(calculateDiff(UrbanGame.FIELD_DESCRIPTION, oldGame.getDescription(), newGame.getDescription()));
			diff.append(calculateDiff(UrbanGame.FIELD_COMMENTS, oldGame.getComments(), newGame.getComments()));
		}
		else {
			Log.e(TAG, "getGameDiff comparision fail");
		}
		
		// remove last newline
		if (diff.length() > 0) {
			diff.deleteCharAt(diff.length() - 1);
		}
		
		return diff.toString();
	}
	
	private boolean canGamesBeCompared(UrbanGame oldGame, UrbanGame newGame) {
		return oldGame.getID() == newGame.getID();
	}
	
	/*************************************
	 ********* COMMON COMPARISON *********
	 *************************************/
	
	public String calculateDiff(String diffName, Object oldObject, Object newObject) {
		if (oldObject == null || newObject == null || oldObject.equals(newObject)) return String.valueOf(emptyString);
		
		StringBuilder diffBuilder = new StringBuilder();
		diffBuilder.append(seperateDiff(diffName, diffBuilder));
		diffBuilder.append(newObject.toString());
		diffBuilder.append(newLine);
		
		return diffBuilder.toString();
	}
	
	public String calculateArrayDiff(String diffName, Object[] oldArray, Object[] newArray) {
		
		if (oldArray == null || newArray == null) return emptyString;
		
		StringBuilder arrayDiffBuilder = new StringBuilder();
		int min = oldArray.length < newArray.length ? oldArray.length : newArray.length;
		
		for (int i = 0; i < min; ++i) {
			if (!oldArray[i].equals(newArray[i])) {
				arrayDiffBuilder.append(seperateDiff(diffName, arrayDiffBuilder));
				arrayDiffBuilder.append(newArray[i]);
			}
		}
		
		// information about added values
		if (oldArray.length < newArray.length) {
			arrayDiffBuilder.append(calculateAddedOrRemovedArrayDiff(diffName, fieldAdded, oldArray, newArray,
				arrayDiffBuilder));
		}
		
		// information about removed values
		if (oldArray.length > newArray.length) {
			arrayDiffBuilder.append(calculateAddedOrRemovedArrayDiff(diffName, fieldRemoved, newArray, oldArray,
				arrayDiffBuilder));
		}
		
		if (arrayDiffBuilder.length() == 0) return emptyString;
		
		arrayDiffBuilder.append(newLine);
		return arrayDiffBuilder.toString();
	}
	
	private String seperateDiff(String diffName, StringBuilder stringBuilder) {
		StringBuilder seperateDiffBuilder = new StringBuilder();
		if (stringBuilder.length() == 0) {
			seperateDiffBuilder.append(diffName);
			seperateDiffBuilder.append(fieldSeparator);
		}
		else {
			seperateDiffBuilder.append(arraySeparator);
		}
		return seperateDiffBuilder.toString();
	}
	
	private String calculateAddedOrRemovedArrayDiff(String diffName, String actionDiffName, Object[] shorterArray,
		Object[] longerArray, StringBuilder stringBuilder) {
		StringBuilder diffBuilder = new StringBuilder();
		
		if (longerArray.length > shorterArray.length) {
			
			if (stringBuilder.length() == 0) {
				diffBuilder.append(diffName);
			}
			diffBuilder.append(endDiffSeprator);
			diffBuilder.append(actionDiffName);
			diffBuilder.append(fieldSeparator);
			
			for (int i = shorterArray.length; i < longerArray.length; ++i) {
				diffBuilder.append(longerArray[i]);
				if (i + 1 < longerArray.length) {
					diffBuilder.append(seperateDiff(diffName, diffBuilder));
				}
			}
		}
		
		if (diffBuilder.length() == 0) return emptyString;
		
		return diffBuilder.toString();
	}
	
}
