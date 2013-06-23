package com.blstream.urbangame.database.test;

import java.util.Date;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;

public class DatabaseTaskInsertTest extends AndroidTestCase {
	
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
	
	/* ------------------------ NULL VALUES ------------------------ */
	//both methods use the same method to insert data of LocationTask. (It is a subset of values that repeats in both Task classes)
	
	//not allowed
	public void testNullTitle() {
		locationTask.setTitle(null);
		boolean isOK = database.insertTaskForGame(2L, locationTask);
		assertFalse(isOK);
	}
	
	//allowed
	public void testNullImage() {
		locationTask.setPictureBase64(null);
		boolean isOK = database.insertTaskForGame(2L, locationTask);
		assertTrue(isOK);
	}
	
	//not allowed
	public void testNullDescription() {
		locationTask.setDescription(null);
		boolean isOK = database.insertTaskForGame(2L, locationTask);
		assertFalse(isOK);
	}
	
	//not allowed
	public void testNullIsReadable() {
		locationTask.setIsRepetable(null);
		boolean isOK = database.insertTaskForGame(2L, locationTask);
		assertFalse(isOK);
	}
	
	//allowed
	public void testNullIsHidden() {
		locationTask.setIsHidden(null);
		boolean isOK = database.insertTaskForGame(2L, locationTask);
		assertTrue(isOK);
	}
	
	//allowed
	public void testNullNumberOfHidden() {
		locationTask.setNumberOfHidden(null);
		boolean isOK = database.insertTaskForGame(2L, locationTask);
		assertTrue(isOK);
	}
	
	//not allowed
	public void testNullEndTime() {
		locationTask.setEndTime(null);
		boolean isOK = database.insertTaskForGame(2L, locationTask);
		assertFalse(isOK);
	}
	
	//not allowed
	public void testNullMaxPoints() {
		locationTask.setMaxPoints(null);
		boolean isOK = database.insertTaskForGame(2L, locationTask);
		assertFalse(isOK);
	}
	
	//not allowed
	public void testNullQuestion() {
		abcdTask.setQuestion(null);
		boolean isOK = database.insertTaskForGame(2L, abcdTask);
		assertFalse(isOK);
	}
	
	//not allowed
	public void testNullAnswers() {
		abcdTask.setAnswers(null);
		boolean isOK = database.insertTaskForGame(2L, abcdTask);
		assertFalse(isOK);
	}
	
	/* ------------------------ NULL VALUES END ------------------------ */
	
	/* ------------------------ NORMAL VALUES ------------------------ */
	public void testABCDTaskNormalvalues() {
		boolean isOK = database.insertTaskForGame(2L, abcdTask);
		assertTrue(isOK);
	}
	
	public void testLocationTaskNormalvalues() {
		boolean isOK = database.insertTaskForGame(2L, locationTask);
		assertTrue(isOK);
	}
	/* ------------------------ NORMAL VALUES END ------------------------ */
}
