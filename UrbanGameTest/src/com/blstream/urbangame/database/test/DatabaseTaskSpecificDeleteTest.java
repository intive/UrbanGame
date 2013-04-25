package com.blstream.urbangame.database.test;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;

public class DatabaseTaskSpecificDeleteTest extends AndroidTestCase {
	
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
		mContext.deleteDatabase(Database.DATABASE_NAME);
		database = null;
	}
	
	private void prepareData(String playerEmail, Long taskID, Integer points, Boolean isFinishedByUser,
		Boolean areChanges, Boolean wasHidden) {
		PlayerTaskSpecific pts = new PlayerTaskSpecific(playerEmail, taskID, points, isFinishedByUser, areChanges,
			wasHidden);
		database.insertPlayerTaskSpecific(pts);
	}
	
	/* ------------------------ USER TASKS SPECIFIC DELETION ------------------------ */
	public void testNothingInDatabase() {
		boolean isOK = database.deletePlayerTaskSpecific(23L, "e@e.e");
		assertFalse(isOK);
	}
	
	public void testOneItemInDatabaseNoMatch() {
		prepareData("e@e.e", 1L, 10, true, true, true);
		boolean isOK = database.deletePlayerTaskSpecific(23L, "e@e.e");
		assertFalse(isOK);
	}
	
	public void testOneItemInDatabaseMatch() {
		prepareData("e@e.e", 1L, 10, true, true, true);
		boolean isOK = database.deletePlayerTaskSpecific(1L, "e@e.e");
		assertTrue(isOK);
	}
	
	public void testManyItemsInDatabaseNoMatch() {
		for (int i = 0; i < 30; i++) {
			prepareData("e@e.e", (long) i, i % 10, i % 2 == 0, i % 3 == 0, i % 4 == 0);
		}
		boolean isOK = database.deletePlayerTaskSpecific(1234L, "e@e.e");
		assertFalse(isOK);
	}
	
	public void testManyItemsInDatabaseMatch() {
		for (int i = 0; i < 30; i++) {
			prepareData("e@e.e", (long) i, i % 10, i % 2 == 0, i % 3 == 0, i % 4 == 0);
		}
		boolean isOK = database.deletePlayerTaskSpecific(23L, "e@e.e");
		assertTrue(isOK);
	}
	/* ------------------------ USER TASKS SPECIFIC DELETION END ------------------------ */
}
