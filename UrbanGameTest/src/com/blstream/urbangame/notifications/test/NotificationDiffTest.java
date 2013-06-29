package com.blstream.urbangame.notifications.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.test.AndroidTestCase;
import android.util.Log;

import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.notifications.NotificationDiff;

public class NotificationDiffTest extends AndroidTestCase {
	
	private final String TAG = NotificationDiffTest.class.getSimpleName();
	
	private NotificationDiff notificationDiff;
	
	private ABCDTask oldABCDTask;
	private ABCDTask newABCDTask;
	private LocationTask oldLocationTask;
	private LocationTask newLocationTask;
	private UrbanGame oldGame;
	private UrbanGame newGame;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		notificationDiff = new NotificationDiff();
		
		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
		Date oldStartDate = null;
		Date newStartDate = null;
		Date oldEndDate = null;
		Date newEndDate = null;
		try {
			oldStartDate = curFormater.parse("04/04/2013 08:40");
			newStartDate = curFormater.parse("04/04/2013 13:40");
			oldEndDate = curFormater.parse("04/05/2013 08:40");
			newEndDate = curFormater.parse("04/05/2013 13:40");
		}
		catch (ParseException e) {
			Log.e(TAG, "ParseException " + e.toString());
		}
		
		oldABCDTask = new ABCDTask(Long.valueOf(0), "OldTaskTitle", "OldTaskImage", "OldTaskDescription", true, true,
			1, oldEndDate, 1, "OldTaskQuestion", new String[] { "OldA", "OldB", "OldC", "OldD" });
		
		newABCDTask = new ABCDTask(Long.valueOf(0), "NewTaskTitle", "NewTaskTitle", "NewTaskDescription", false, false,
			2, newEndDate, 2, "NewTaskQuestion", new String[] { "NewA", "NewB", "NewC", "NewD" });
		
		oldLocationTask = new LocationTask(Long.valueOf(0), "OldTaskTitle", "OldTaskImage", "OldTaskDescription", true,
			true, 1, oldEndDate, 1);
		
		newLocationTask = new LocationTask(Long.valueOf(0), "NewTaskTitle", "NewTaskTitle", "NewTaskDescription",
			false, false, 2, newEndDate, 2);
		
		oldGame = new UrbanGame(Long.valueOf(0), Double.valueOf(0.0), "OldGameTitle", "OldOperatorName",
			"OldWinningStrategy", 10, 20, oldStartDate, oldEndDate, 1, true, "OldPrizesInfo", "OldDescription", null,
			null, "OldComments", "OldLocation", "OldDetaisLink");
		
		newGame = new UrbanGame(Long.valueOf(0), Double.valueOf(1.0), "NewGameTitle", "NewOperatorName",
			"NewWinningStrategy", 20, 30, newStartDate, newEndDate, 2, false, "NewPrizesInfo", "NewDescription", null,
			null, "NewComments", "NewLocation", "NewDetaisLink");
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testGetTaskDiff() {
		String ABCDTaskDiff = notificationDiff.getTaskDiff(oldABCDTask, newABCDTask);
		Log.d(TAG, "ABCDTaskDiff " + ABCDTaskDiff);
		
		assertTrue(ABCDTaskDiff.contains(newABCDTask.getDescription()));
		assertTrue(ABCDTaskDiff.contains(newABCDTask.getTitle()));
		assertTrue(ABCDTaskDiff.contains(newABCDTask.getEndTime().toString()));
		assertTrue(ABCDTaskDiff.contains(newABCDTask.getMaxPoints().toString()));
		assertTrue(ABCDTaskDiff.contains(newABCDTask.getNumberOfHidden().toString()));
		assertTrue(ABCDTaskDiff.contains(newABCDTask.isHidden().toString()));
		assertTrue(ABCDTaskDiff.contains(newABCDTask.isRepetable().toString()));
		
		assertTrue(ABCDTaskDiff.contains(newABCDTask.getQuestion()));
		for (String answer : newABCDTask.getAnswers()) {
			assertTrue(ABCDTaskDiff.contains(answer));
		}
		
		String LocationTaskDiff = notificationDiff.getTaskDiff(oldLocationTask, newLocationTask);
		Log.d(TAG, "LocationTaskDiff " + LocationTaskDiff);
		
		assertTrue(LocationTaskDiff.contains(newLocationTask.getDescription()));
		assertTrue(LocationTaskDiff.contains(newLocationTask.getTitle()));
		assertTrue(LocationTaskDiff.contains(newLocationTask.getEndTime().toString()));
		assertTrue(LocationTaskDiff.contains(newLocationTask.getMaxPoints().toString()));
		assertTrue(LocationTaskDiff.contains(newLocationTask.getNumberOfHidden().toString()));
		assertTrue(LocationTaskDiff.contains(newLocationTask.isHidden().toString()));
		assertTrue(LocationTaskDiff.contains(newLocationTask.isRepetable().toString()));
	}
	
	public void testGetGameDiff() {
		String GameDiff = notificationDiff.getGameDiff(oldGame, newGame);
		Log.d(TAG, "GameDiff " + GameDiff);
		
		assertTrue(GameDiff.contains(newGame.getTitle()));
		assertTrue(GameDiff.contains(newGame.getOperatorName()));
		assertTrue(GameDiff.contains(newGame.getPlayers().toString()));
		assertTrue(GameDiff.contains(newGame.getMaxPlayers().toString()));
		assertTrue(GameDiff.contains(newGame.getStartDate().toString()));
		assertTrue(GameDiff.contains(newGame.getEndDate().toString()));
		assertTrue(GameDiff.contains(newGame.getReward().toString()));
		assertTrue(GameDiff.contains(newGame.getLocation()));
		
		assertTrue(GameDiff.contains(newGame.getWinningStrategy()));
		assertTrue(GameDiff.contains(newGame.getDifficulty().toString()));
		assertTrue(GameDiff.contains(newGame.getPrizesInfo()));
		assertTrue(GameDiff.contains(newGame.getDescription()));
		assertTrue(GameDiff.contains(newGame.getComments()));
	}
	
	public void testCalculateDiff() {
		String oldString = "oldString";
		String newString = "newString";
		
		String diffString = notificationDiff.calculateDiff("diff", oldString, newString);
		Log.d(TAG, "calculateDiff " + diffString);
		assertTrue(diffString.contains(newString));
	}
	
	public void testCalculateArrayDiff() {
		
		String[] oldArray = new String[] { "OldA", "OldB", "OldC" };
		String[] newArray = new String[] { "OldA", "OldB", "OldC" };
		
		String diffString = notificationDiff.calculateArrayDiff("diff", oldArray, newArray);
		Log.d(TAG, "testCalculateArrayDiff " + diffString);
		assertTrue(diffString.isEmpty());
		
		oldArray = new String[] { "OldA", "OldB", "OldC" };
		newArray = new String[] { "OldB", "OldA", "NewC" };
		diffString = notificationDiff.calculateArrayDiff("diff", oldArray, newArray);
		Log.d(TAG, "testCalculateArrayDiff " + diffString);
		assertFalse(diffString.isEmpty());
		assertTrue(diffString.contains("OldA"));
		assertTrue(diffString.contains("OldB"));
		assertTrue(diffString.contains("NewC"));
		
		oldArray = new String[] { "OldA", "OldB", "OldC" };
		newArray = new String[] { "NewD", "OldB", "OldA", "NewC" };
		diffString = notificationDiff.calculateArrayDiff("diff", oldArray, newArray);
		Log.d(TAG, "testCalculateArrayDiff " + diffString);
		assertFalse(diffString.isEmpty());
		assertTrue(diffString.contains("NewD"));
		assertFalse(diffString.contains("OldB"));
		assertTrue(diffString.contains("OldA"));
		assertTrue(diffString.contains("NewC"));
		
		oldArray = new String[] { "OldA", "OldB", "OldC" };
		newArray = new String[] { "NewD", "OldA" };
		diffString = notificationDiff.calculateArrayDiff("diff", oldArray, newArray);
		Log.d(TAG, "testCalculateArrayDiff " + diffString);
		assertFalse(diffString.isEmpty());
		assertTrue(diffString.contains("NewD"));
		assertTrue(diffString.contains("OldA"));
		assertTrue(diffString.contains("OldC"));
	}
	
}
