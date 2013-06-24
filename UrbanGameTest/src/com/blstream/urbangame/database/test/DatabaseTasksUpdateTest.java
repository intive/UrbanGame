package com.blstream.urbangame.database.test;

import java.util.Date;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.Task;

public class DatabaseTasksUpdateTest extends AndroidTestCase {
	
	private DatabaseInterface database;
	private ABCDTask abcdTask;
	private LocationTask locationTask;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		if (database == null) {
			database = new Database(mContext);
		}
		abcdTask = new ABCDTask(3L, "tytuł", "image", "description", true, true, 6, new Date(), 100, "question",
			new String[] { "A", "B", "C", "D" });
		locationTask = new LocationTask(4L, "tytuł", "image", "description", true, true, 6, new Date(), 100);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		mContext.deleteDatabase(database.getDatabaseName());
		database = null;
	}
	
	/* ------------------------ UPDATE ------------------------ */
	public void testUpdateNothingABCDTask() {
		abcdTask.setId(3L);
		boolean isOK = database.insertTaskForGame(1L, abcdTask);
		assertTrue(isOK);
		abcdTask = new ABCDTask();
		abcdTask.setId(3L);
		isOK = database.updateTask(abcdTask);
		assertTrue(isOK);
		
		Task t = database.getTask(3L);
		assertTrue(t instanceof ABCDTask);
		
		ABCDTask taskFromDB = (ABCDTask) t;
		assertEquals(3L, taskFromDB.getId().longValue());
		assertEquals("tytuł", taskFromDB.getTitle());
		assertEquals("image", taskFromDB.getPictureBase64());
		assertEquals("description", taskFromDB.getDescription());
		assertEquals(true, taskFromDB.isRepetable().booleanValue());
		assertEquals(true, taskFromDB.isHidden().booleanValue());
		assertEquals(100, taskFromDB.getMaxPoints().intValue());
		assertEquals("question", taskFromDB.getQuestion());
		assertEquals("A", taskFromDB.getAnswers()[0]);
		assertEquals("B", taskFromDB.getAnswers()[1]);
		assertEquals("C", taskFromDB.getAnswers()[2]);
		assertEquals("D", taskFromDB.getAnswers()[3]);
	}
	
	public void testUpdateNothingLocationTask() {
		locationTask.setId(3L);
		boolean isOK = database.insertTaskForGame(1L, locationTask);
		assertTrue(isOK);
		locationTask = new LocationTask();
		locationTask.setId(3L);
		isOK = database.updateTask(locationTask);
		assertTrue(isOK);
		
		Task t = database.getTask(3L);
		assertTrue(t instanceof LocationTask);
		
		LocationTask taskFromDB = (LocationTask) t;
		assertEquals(3L, taskFromDB.getId().longValue());
		assertEquals("tytuł", taskFromDB.getTitle());
		assertEquals("image", taskFromDB.getPictureBase64());
		assertEquals("description", taskFromDB.getDescription());
		assertEquals(true, taskFromDB.isRepetable().booleanValue());
		assertEquals(true, taskFromDB.isHidden().booleanValue());
		assertEquals(100, taskFromDB.getMaxPoints().intValue());
	}
	
	public void testUpdateAllABCDTask() {
		abcdTask.setId(3L);
		boolean isOK = database.insertTaskForGame(1L, abcdTask);
		assertTrue(isOK);
		abcdTask = new ABCDTask(3L, "tytuł2", "image2", "description2", false, false, 6 + 2, new Date(), 100 + 2,
			"question2", new String[] { "A2", "B2", "C2", "D2" });
		abcdTask.setId(3L);
		isOK = database.updateTask(abcdTask);
		assertTrue(isOK);
		
		Task t = database.getTask(3L);
		assertTrue(t instanceof ABCDTask);
		
		ABCDTask taskFromDB = (ABCDTask) t;
		assertEquals(3L, taskFromDB.getId().longValue());
		assertEquals("tytuł2", taskFromDB.getTitle());
		assertEquals("image2", taskFromDB.getPictureBase64());
		assertEquals("description2", taskFromDB.getDescription());
		assertEquals(false, taskFromDB.isRepetable().booleanValue());
		assertEquals(false, taskFromDB.isHidden().booleanValue());
		assertEquals(100 + 2, taskFromDB.getMaxPoints().intValue());
		assertEquals("question2", taskFromDB.getQuestion());
		assertEquals("A2", taskFromDB.getAnswers()[0]);
		assertEquals("B2", taskFromDB.getAnswers()[1]);
		assertEquals("C2", taskFromDB.getAnswers()[2]);
		assertEquals("D2", taskFromDB.getAnswers()[3]);
	}
	
	public void testUpdateAllLocationTask() {
		locationTask.setId(3L);
		boolean isOK = database.insertTaskForGame(1L, locationTask);
		assertTrue(isOK);
		locationTask = new LocationTask(3L, "tytuł2", "image2", "description2", false, false, 6 + 2, new Date(),
			100 + 2);
		locationTask.setId(3L);
		isOK = database.updateTask(locationTask);
		assertTrue(isOK);
		
		Task t = database.getTask(3L);
		assertTrue(t instanceof LocationTask);
		
		LocationTask taskFromDB = (LocationTask) t;
		assertEquals(3L, taskFromDB.getId().longValue());
		assertEquals("tytuł2", taskFromDB.getTitle());
		assertEquals("image2", taskFromDB.getPictureBase64());
		assertEquals("description2", taskFromDB.getDescription());
		assertEquals(false, taskFromDB.isRepetable().booleanValue());
		assertEquals(false, taskFromDB.isHidden().booleanValue());
		assertEquals(100 + 2, taskFromDB.getMaxPoints().intValue());
	}
	/* ------------------------ UPDATE END ------------------------ */
}
