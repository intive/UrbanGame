package com.blstream.urbangame.notifications.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;
import android.util.Log;

import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.notifications.NotificationDiff;

public class NotificationDiffTest extends TestCase {
	
	private final String TAG = NotificationDiffTest.class.getSimpleName();
	
	private NotificationDiff notificationDiff;
	
	private ABCDTask oldABCDTask;
	private ABCDTask newABCDTask;
	private LocationTask oldLocationTask;
	private LocationTask newLocationTask;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		notificationDiff = new NotificationDiff();
		
		SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
		Date oldEndDate = null;
		Date newEndDate = null;
		try {
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
