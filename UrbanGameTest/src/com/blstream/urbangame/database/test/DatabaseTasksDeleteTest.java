package com.blstream.urbangame.database.test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.Task;

public class DatabaseTasksDeleteTest extends AndroidTestCase {
	
	private DatabaseInterface database;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		if (database == null) {
			database = new Database(mContext);
		}
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		mContext.deleteDatabase(database.getDatabaseName());
		database = null;
	}
	
	private boolean prepareDataABCD(Long gameId, Long id, String title, String pictureBase64, String description,
		Boolean isRepetable, Boolean isHidden, Integer numberOfHidden, Date endTime, Integer maxPoints,
		String question, String[] answers) {
		
		ABCDTask abcdTask = new ABCDTask(id, title, pictureBase64, description, isRepetable, isHidden, numberOfHidden,
			endTime, maxPoints, question, answers);
		return database.insertTaskForGame(gameId, abcdTask);
	}
	
	private boolean prepareDataLocation(Long gameId, Long id, String title, String pictureBase64, String description,
		Boolean isRepetable, Boolean isHidden, Integer numberOfHidden, Date endTime, Integer maxPoints) {
		
		LocationTask locationTask = new LocationTask(id, title, pictureBase64, description, isRepetable, isHidden,
			numberOfHidden, endTime, maxPoints);
		return database.insertTaskForGame(gameId, locationTask);
	}
	
	/* ------------------------ TASK DELETION ------------------------ */
	public void testNothingInDatabase() {
		boolean isOK = database.deleteTask(1L, 2L);
		assertFalse(isOK);
	}
	
	public void testOneItemInDatabaseNoMatchABCD() {
		boolean isOK = prepareDataABCD(1L, 3L, "tytuł", "image", "description", true, true, 6, new Date(), 100,
			"question", new String[] { "A", "B", "C", "D" });
		assertTrue(isOK);
		isOK = database.deleteTask(1L, 2L);
		assertFalse(isOK);
	}
	
	public void testOneItemInDatabaseNoMatchLocation() {
		boolean isOK = prepareDataLocation(1L, 3L, "tytuł", "image", "description", true, true, 6, new Date(), 100);
		assertTrue(isOK);
		isOK = database.deleteTask(1L, 2L);
		assertFalse(isOK);
	}
	
	public void testOneItemInDatabaseMatchABCD() {
		boolean isOK = prepareDataABCD(1L, 3L, "tytuł", "image", "description", true, true, 6, new Date(), 100,
			"question", new String[] { "A", "B", "C", "D" });
		assertTrue(isOK);
		isOK = database.deleteTask(1L, 3L);
		assertTrue(isOK);
	}
	
	public void testOneItemInDatabaseMatchLocation() {
		boolean isOK = prepareDataLocation(1L, 3L, "tytuł", "image", "description", true, true, 6, new Date(), 100);
		assertTrue(isOK);
		isOK = database.deleteTask(1L, 3L);
		assertTrue(isOK);
	}
	
	public void testManyItemsInDatabaseNoMatch() {
		Calendar c = Calendar.getInstance();
		
		int abcdTasksForGameOne = 12;
		int locationTasksForGameOne = 12;
		int abcdTasksForGameTwo = 20;
		int locationTasksForGameTwo = 20;
		
		long gameOneID = 1L;
		long gameTwoID = 2L;
		
		boolean insertOK;
		
		for (int i = 1, j = 1; i <= abcdTasksForGameOne || j <= locationTasksForGameOne; i++, j++) {
			if (i <= abcdTasksForGameOne) {
				c.set(15 / (i + 1) + 2012, i, i, i, i, i);
				insertOK = prepareDataABCD(gameOneID, (long) i, "tytuł" + i, "image" + i, "description" + i, true,
					true, 6, c.getTime(), i * 13, "question" + i, new String[] { "A" + i, "B" + i, "C" + i, "D" + i });
				assertTrue(insertOK);
			}
			if (j <= locationTasksForGameOne) {
				c.set(15 / (i + 1) + 2012, i, i, i, i, i);
				insertOK = prepareDataLocation(gameOneID, (long) abcdTasksForGameOne + j, "tytuł" + i, "image" + i,
					"description" + i, i % 2 == 0, i % 2 == 1, 6, c.getTime(), i * 13);
				assertTrue(insertOK);
			}
		}
		
		for (int i = 1, j = 1; i <= abcdTasksForGameTwo || j <= locationTasksForGameTwo; i++, j++) {
			if (i <= abcdTasksForGameTwo) {
				c.set(15 / (i + 1) + 2012, i, i, i, i, i);
				insertOK = prepareDataABCD(gameTwoID, (long) abcdTasksForGameOne + locationTasksForGameOne + i + 1,
					"tytuł" + i, "image" + i, "description" + i, true, true, 6, c.getTime(), i * 13, "question" + i,
					new String[] { "A" + i, "B" + i, "C" + i, "D" + i });
				assertTrue(insertOK);
			}
			if (j <= locationTasksForGameTwo) {
				c.set(15 / (i + 1) + 2012, i, i, i, i, i);
				insertOK = prepareDataLocation(gameTwoID, (long) abcdTasksForGameOne + locationTasksForGameOne
					+ abcdTasksForGameTwo + 1 + j, "tytuł" + i, "image" + i, "description" + i, i % 2 == 0, i % 2 == 1,
					6, c.getTime(), i * 13);
				assertTrue(insertOK);
			}
		}
		
		boolean isSuccessfull = database.deleteTask(gameOneID, 212321L);
		assertFalse(isSuccessfull);
		
		List<Task> tasksGameOne = database.getTasksForGame(gameOneID);
		List<Task> tasksGameTwo = database.getTasksForGame(gameTwoID);
		
		assertNotNull(tasksGameOne);
		assertNotNull(tasksGameTwo);
		
		assertEquals(abcdTasksForGameOne + locationTasksForGameOne, tasksGameOne.size());
		assertEquals(abcdTasksForGameTwo + locationTasksForGameTwo, tasksGameTwo.size());
	}
	
	public void testManyItemsInDatabaseMatch() {
		Calendar c = Calendar.getInstance();
		
		int abcdTasksForGameOne = 12;
		int locationTasksForGameOne = 12;
		int abcdTasksForGameTwo = 20;
		int locationTasksForGameTwo = 20;
		
		long gameOneID = 1L;
		long gameTwoID = 2L;
		
		boolean insertOK;
		
		for (int i = 1, j = 1; i <= abcdTasksForGameOne || j <= locationTasksForGameOne; i++, j++) {
			if (i <= abcdTasksForGameOne) {
				c.set(15 / (i + 1) + 2012, i, i, i, i, i);
				insertOK = prepareDataABCD(gameOneID, (long) i, "tytuł" + i, "image" + i, "description" + i, true,
					true, 6, c.getTime(), i * 13, "question" + i, new String[] { "A" + i, "B" + i, "C" + i, "D" + i });
				assertTrue(insertOK);
			}
			if (j <= locationTasksForGameOne) {
				c.set(15 / (i + 1) + 2012, i, i, i, i, i);
				insertOK = prepareDataLocation(gameOneID, (long) abcdTasksForGameOne + j, "tytuł" + i, "image" + i,
					"description" + i, i % 2 == 0, i % 2 == 1, 6, c.getTime(), i * 13);
				assertTrue(insertOK);
			}
		}
		
		for (int i = 1, j = 1; i <= abcdTasksForGameTwo || j <= locationTasksForGameTwo; i++, j++) {
			if (i <= abcdTasksForGameTwo) {
				c.set(15 / (i + 1) + 2012, i, i, i, i, i);
				insertOK = prepareDataABCD(gameTwoID, (long) abcdTasksForGameOne + locationTasksForGameOne + i + 1,
					"tytuł" + i, "image" + i, "description" + i, true, true, 6, c.getTime(), i * 13, "question" + i,
					new String[] { "A" + i, "B" + i, "C" + i, "D" + i });
				assertTrue(insertOK);
			}
			if (j <= locationTasksForGameTwo) {
				c.set(15 / (i + 1) + 2012, i, i, i, i, i);
				insertOK = prepareDataLocation(gameTwoID, (long) abcdTasksForGameOne + locationTasksForGameOne
					+ abcdTasksForGameTwo + 1 + j, "tytuł" + i, "image" + i, "description" + i, i % 2 == 0, i % 2 == 1,
					6, c.getTime(), i * 13);
				assertTrue(insertOK);
			}
		}
		
		boolean isSuccessfull = database.deleteTask(gameOneID, 2L);
		assertTrue(isSuccessfull);
		isSuccessfull = database.deleteTask(gameTwoID, (long) (abcdTasksForGameOne + locationTasksForGameOne + 2));
		assertTrue(isSuccessfull);
		isSuccessfull = database.deleteTask(gameTwoID, (long) (abcdTasksForGameOne + locationTasksForGameOne + 4));
		assertTrue(isSuccessfull);
		
		List<Task> tasksGameOne = database.getTasksForGame(gameOneID);
		List<Task> tasksGameTwo = database.getTasksForGame(gameTwoID);
		
		assertNotNull(tasksGameOne);
		assertNotNull(tasksGameTwo);
		
		assertEquals(abcdTasksForGameOne + locationTasksForGameOne - 1, tasksGameOne.size());
		assertEquals(abcdTasksForGameTwo + locationTasksForGameTwo - 2, tasksGameTwo.size());
	}
	/* ------------------------ TASK DELETION END ------------------------ */
}
