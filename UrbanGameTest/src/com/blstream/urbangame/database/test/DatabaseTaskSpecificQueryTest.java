package com.blstream.urbangame.database.test;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;

public class DatabaseTaskSpecificQueryTest extends AndroidTestCase {
	
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
	
	private void prepareData(String playerEmail, Long taskID, Integer points, Boolean isFinishedByUser,
		Boolean areChanges, Boolean wasHidden, String changes, Integer status) {
		PlayerTaskSpecific pts = new PlayerTaskSpecific(playerEmail, taskID, points, isFinishedByUser, areChanges,
			wasHidden, changes, status, null);
		database.insertPlayerTaskSpecific(pts);
	}
	
	/* ------------------------ PLAYER TASKS SPECIFIC QUERIES ------------------------ */
	public void testNothingInDatabase() {
		PlayerTaskSpecific pts = database.getPlayerTaskSpecific(1L, "em@em.em");
		assertNull(pts);
	}
	
	public void testOneItemInDatabaseNoMatch() {
		prepareData("em@em.em", 2L, 12, true, false, true, "no", PlayerTaskSpecific.ACTIVE);
		PlayerTaskSpecific pts = database.getPlayerTaskSpecific(1L, "em@em.em2");
		assertNull(pts);
	}
	
	public void testOneItemInDatabaseMatch() {
		prepareData("em@em.em", 2L, 12, true, false, true, "ok", PlayerTaskSpecific.ACTIVE);
		PlayerTaskSpecific pts = database.getPlayerTaskSpecific(2L, "em@em.em");
		assertNotNull(pts);
		assertEquals("em@em.em", pts.getPlayerEmail());
		assertEquals(2L, pts.getTaskID().longValue());
		assertEquals(12, pts.getPoints().intValue());
		assertEquals(true, pts.isFinishedByUser().booleanValue());
		assertEquals(false, pts.getAreChanges().booleanValue());
		assertEquals(true, pts.getWasHidden().booleanValue());
		assertEquals("ok", pts.getChanges());
		assertEquals(PlayerTaskSpecific.ACTIVE, pts.getStatus().intValue());
	}
	
	public void testManyItemsInDatabaseNoMatch() {
		for (int i = 0; i < 30; i++) {
			prepareData("em@em.em" + i, (long) i, i, i % 2 == 0, i % 3 == 0, i % 4 == 0, "ok" + i,
				PlayerTaskSpecific.ACTIVE);
		}
		
		PlayerTaskSpecific pts = database.getPlayerTaskSpecific(123L, "em@em.em");
		assertNull(pts);
	}
	
	public void testManyItemsInDatabaseMatch() {
		for (int i = 0; i < 30; i++) {
			prepareData("em@em.em" + i, (long) i, i, i % 2 == 0, i % 3 == 0, i % 4 == 0, "ok" + i,
				PlayerTaskSpecific.ACTIVE);
		}
		
		int testedItem = 13;
		
		PlayerTaskSpecific pts = database.getPlayerTaskSpecific((long) testedItem, "em@em.em" + testedItem);
		assertNotNull(pts);
		assertEquals("em@em.em" + testedItem, pts.getPlayerEmail());
		assertEquals(testedItem, pts.getTaskID().longValue());
		assertEquals(testedItem, pts.getPoints().intValue());
		assertEquals(testedItem % 2 == 0, pts.isFinishedByUser().booleanValue());
		assertEquals(testedItem % 3 == 0, pts.getAreChanges().booleanValue());
		assertEquals(testedItem % 4 == 0, pts.getWasHidden().booleanValue());
		assertEquals("ok" + testedItem, pts.getChanges());
		assertEquals(PlayerTaskSpecific.ACTIVE, pts.getStatus().intValue());
	}
	/* ------------------------ PLAYER TASKS SPECIFIC QUERIES END ------------------------ */
}
