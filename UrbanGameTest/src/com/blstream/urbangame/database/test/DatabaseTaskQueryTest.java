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

public class DatabaseTaskQueryTest extends AndroidTestCase {
	
	private DatabaseInterface database;
	private ABCDTask abcdTask;
	private LocationTask locationTask;
	
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
		
		abcdTask = new ABCDTask(id, title, pictureBase64, description, isRepetable, isHidden, numberOfHidden, endTime,
			maxPoints, question, answers);
		return database.insertTaskForGame(gameId, abcdTask);
	}
	
	private boolean prepareDataLocation(Long gameId, Long id, String title, String pictureBase64, String description,
		Boolean isRepetable, Boolean isHidden, Integer numberOfHidden, Date endTime, Integer maxPoints) {
		
		locationTask = new LocationTask(id, title, pictureBase64, description, isRepetable, isHidden, numberOfHidden,
			endTime, maxPoints);
		return database.insertTaskForGame(gameId, locationTask);
	}
	
	/* ------------------------ TASK QUERIES ------------------------ */
	
	public void testNothingInDatabase() {
		Task t = database.getTask(2L);
		assertNull(t);
	}
	
	public void testOneItemInDatabaseNoMatch() {
		prepareDataABCD(1L, 3L, "tytuł", "image", "description", true, true, 6, new Date(), 100, "question",
			new String[] { "A", "B", "C", "D" });
		Task t = database.getTask(1L);
		assertNull(t);
	}
	
	public void testOneItemInDatabaseMatchABCD() {
		Calendar c = Calendar.getInstance();
		c.set(2012, 1, 21, 12, 21, 2);
		prepareDataABCD(1L, 3L, "tytuł", "image", "description", true, true, 6, c.getTime(), 100, "question",
			new String[] { "A", "B", "C", "D" });
		Task t = database.getTask(3L);
		assertNotNull(t);
		assertTrue(t instanceof ABCDTask);
		ABCDTask taskFromDB = (ABCDTask) t;
		assertEquals(3L, taskFromDB.getId().longValue());
		assertEquals("tytuł", taskFromDB.getTitle());
		assertEquals("image", taskFromDB.getPictureBase64());
		assertEquals("description", taskFromDB.getDescription());
		assertEquals(true, taskFromDB.isRepetable().booleanValue());
		assertEquals(true, taskFromDB.isHidden().booleanValue());
		assertEquals(c.getTime().getTime(), taskFromDB.getEndTime().getTime());
		assertEquals(100, taskFromDB.getMaxPoints().intValue());
		assertEquals("question", taskFromDB.getQuestion());
		assertEquals("A", taskFromDB.getAnswers()[0]);
		assertEquals("B", taskFromDB.getAnswers()[1]);
		assertEquals("C", taskFromDB.getAnswers()[2]);
		assertEquals("D", taskFromDB.getAnswers()[3]);
	}
	
	public void testOneItemInDatabaseMatchLocation() {
		Calendar c = Calendar.getInstance();
		c.set(2012, 1, 21, 12, 21, 2);
		prepareDataLocation(1L, 3L, "tytuł", "image", "description", true, true, 6, c.getTime(), 100);
		Task t = database.getTask(3L);
		assertNotNull(t);
		assertTrue(t instanceof LocationTask);
		LocationTask taskFromDB = (LocationTask) t;
		assertEquals(3L, taskFromDB.getId().longValue());
		assertEquals("tytuł", taskFromDB.getTitle());
		assertEquals("image", taskFromDB.getPictureBase64());
		assertEquals("description", taskFromDB.getDescription());
		assertEquals(true, taskFromDB.isRepetable().booleanValue());
		assertEquals(true, taskFromDB.isHidden().booleanValue());
		assertEquals(c.getTime().getTime(), taskFromDB.getEndTime().getTime());
		assertEquals(100, taskFromDB.getMaxPoints().intValue());
	}
	
	public void testManyItemsInDatabaseNoMatch() {
		Calendar c = Calendar.getInstance();
		for (int i = 0; i < 30; i++) {
			c.set(15 / (i + 1) + 2012, i, i, i, i, i);
			prepareDataABCD(1L, (long) i, "tytuł" + i, "image" + i, "description" + i, true, true, 6, c.getTime(),
				i * 13, "question" + i, new String[] { "A" + i, "B" + i, "C" + i, "D" + i });
		}
		Task t = database.getTask(2521L);
		assertNull(t);
	}
	
	public void testManyItemsInDatabaseMatchABCD() {
		Calendar c = Calendar.getInstance();
		for (int i = 0; i < 30; i++) {
			c.set(15 / (i + 1) + 2012, i, i, i, i, i);
			prepareDataABCD(1L, (long) i, "tytuł" + i, "image" + i, "description" + i, i % 2 == 0, i % 2 == 1, 6,
				c.getTime(), i * 13, "question" + i, new String[] { "A" + i, "B" + i, "C" + i, "D" + i });
		}
		int whichToTest = 13;
		Task t = database.getTask((long) whichToTest);
		assertNotNull(t);
		assertTrue(t instanceof ABCDTask);
		ABCDTask taskFromDB = (ABCDTask) t;
		assertEquals(whichToTest, taskFromDB.getId().longValue());
		assertEquals("tytuł" + whichToTest, taskFromDB.getTitle());
		assertEquals("image" + whichToTest, taskFromDB.getPictureBase64());
		assertEquals("description" + whichToTest, taskFromDB.getDescription());
		assertEquals(whichToTest % 2 == 0, taskFromDB.isRepetable().booleanValue());
		assertEquals(whichToTest % 2 == 1, taskFromDB.isHidden().booleanValue());
		c.set(15 / (whichToTest + 1) + 2012, whichToTest, whichToTest, whichToTest, whichToTest, whichToTest);
		assertEquals(c.getTime().getTime(), taskFromDB.getEndTime().getTime());
		assertEquals(whichToTest * 13, taskFromDB.getMaxPoints().intValue());
		assertEquals("question" + whichToTest, taskFromDB.getQuestion());
		assertEquals("A" + whichToTest, taskFromDB.getAnswers()[0]);
		assertEquals("B" + whichToTest, taskFromDB.getAnswers()[1]);
		assertEquals("C" + whichToTest, taskFromDB.getAnswers()[2]);
		assertEquals("D" + whichToTest, taskFromDB.getAnswers()[3]);
	}
	
	public void testManyItemsInDatabaseMatchLocation() {
		Calendar c = Calendar.getInstance();
		for (int i = 0; i < 30; i++) {
			c.set(15 / (i + 1) + 2012, i, i, i, i, i);
			prepareDataLocation(1L, (long) i, "tytuł" + i, "image" + i, "description" + i, i % 2 == 0, i % 2 == 1, 6,
				c.getTime(), i * 13);
		}
		int whichToTest = 13;
		Task t = database.getTask((long) whichToTest);
		assertNotNull(t);
		assertTrue(t instanceof LocationTask);
		LocationTask taskFromDB = (LocationTask) t;
		assertEquals(whichToTest, taskFromDB.getId().longValue());
		assertEquals("tytuł" + whichToTest, taskFromDB.getTitle());
		assertEquals("image" + whichToTest, taskFromDB.getPictureBase64());
		assertEquals("description" + whichToTest, taskFromDB.getDescription());
		assertEquals(whichToTest % 2 == 0, taskFromDB.isRepetable().booleanValue());
		assertEquals(whichToTest % 2 == 1, taskFromDB.isHidden().booleanValue());
		c.set(15 / (whichToTest + 1) + 2012, whichToTest, whichToTest, whichToTest, whichToTest, whichToTest);
		assertEquals(c.getTime().getTime(), taskFromDB.getEndTime().getTime());
		assertEquals(whichToTest * 13, taskFromDB.getMaxPoints().intValue());
	}
	
	public void testListsForGamesNothingInDatabase() {
		List<Task> tasks = database.getTasksForGame(1L);
		assertNull(tasks);
	}
	
	public void testListsForGamesOneTaskInDatabaseNoGameMatch() {
		prepareDataABCD(1L, 3L, "tytuł", "image", "description", true, true, 6, new Date(), 100, "question",
			new String[] { "A", "B", "C", "D" });
		List<Task> tasks = database.getTasksForGame(1234L);
		assertNull(tasks);
	}
	
	public void testListsForGamesOneTaskInDatabaseGameMatch() {
		prepareDataABCD(1L, 3L, "tytuł", "image", "description", true, true, 6, new Date(), 100, "question",
			new String[] { "A", "B", "C", "D" });
		List<Task> tasks = database.getTasksForGame(1L);
		assertNotNull(tasks);
		assertEquals(1, tasks.size());
	}
	
	public void testListsForGameManyTaskInDatabaseDifferentGames() {
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
		
		List<Task> tasksGameOne = database.getTasksForGame(gameOneID);
		List<Task> tasksGameTwo = database.getTasksForGame(gameTwoID);
		
		assertNotNull(tasksGameOne);
		assertNotNull(tasksGameTwo);
		
		assertEquals(abcdTasksForGameOne + locationTasksForGameOne, tasksGameOne.size());
		assertEquals(abcdTasksForGameTwo + locationTasksForGameTwo, tasksGameTwo.size());
		
		int abcdTasksCounterGameOne = 0;
		int locationTasksCounterGameOne = 0;
		int abcdTasksCounterGameTwo = 0;
		int locationTasksCounterGameTwo = 0;
		
		for (Task task : tasksGameOne) {
			if (task instanceof ABCDTask) {
				abcdTasksCounterGameOne++;
			}
			if (task instanceof LocationTask) {
				locationTasksCounterGameOne++;
			}
		}
		assertEquals(abcdTasksForGameOne, abcdTasksCounterGameOne);
		assertEquals(locationTasksForGameOne, locationTasksCounterGameOne);
		for (Task task : tasksGameTwo) {
			if (task instanceof ABCDTask) {
				abcdTasksCounterGameTwo++;
			}
			if (task instanceof LocationTask) {
				locationTasksCounterGameTwo++;
			}
		}
		assertEquals(abcdTasksForGameTwo, abcdTasksCounterGameTwo);
		assertEquals(locationTasksForGameTwo, locationTasksCounterGameTwo);
	}
	/* ------------------------ TASK QUERIES END ------------------------ */
}
