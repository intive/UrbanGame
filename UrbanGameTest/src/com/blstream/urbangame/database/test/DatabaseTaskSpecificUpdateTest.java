package com.blstream.urbangame.database.test;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;

public class DatabaseTaskSpecificUpdateTest extends AndroidTestCase {
	
	private DatabaseInterface database;
	private PlayerTaskSpecific playerTaskSpecific;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		if (database == null) {
			database = new Database(mContext);
		}
		playerTaskSpecific = new PlayerTaskSpecific("em@em.em", 1L, 10, false, false, false, "something",
			PlayerTaskSpecific.ACTIVE);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		mContext.deleteDatabase(Database.DATABASE_NAME);
		database = null;
	}
	
	/* ------------------------ UPDATE ------------------------ */
	public void testUpdateAll() {
		boolean isOK = database.insertPlayerTaskSpecific(playerTaskSpecific);
		assertTrue(isOK);
		playerTaskSpecific = new PlayerTaskSpecific("em@em.em", 1L, 20, true, true, true, "other",
			PlayerTaskSpecific.CANCELED);
		isOK = database.updatePlayerTaskSpecific(playerTaskSpecific);
		assertTrue(isOK);
		PlayerTaskSpecific ptsNew = database.getPlayerTaskSpecific(1L, "em@em.em");
		assertNotNull(ptsNew);
		assertEquals("em@em.em", ptsNew.getPlayerEmail());
		assertEquals(1L, ptsNew.getTaskID().longValue());
		assertEquals(20, ptsNew.getPoints().intValue());
		assertEquals(true, ptsNew.isFinishedByUser().booleanValue());
		assertEquals(true, ptsNew.getAreChanges().booleanValue());
		assertEquals(true, ptsNew.getWasHidden().booleanValue());
		assertEquals("other", ptsNew.getChanges());
		assertEquals(PlayerTaskSpecific.CANCELED, ptsNew.getStatus().intValue());
	}
	
	public void testUpdateNothing() {
		boolean isOK = database.insertPlayerTaskSpecific(playerTaskSpecific);
		assertTrue(isOK);
		playerTaskSpecific = new PlayerTaskSpecific();
		playerTaskSpecific.setPlayerEmail("em@em.em");
		playerTaskSpecific.setTaskID(1L);
		isOK = database.updatePlayerTaskSpecific(playerTaskSpecific);
		assertTrue(isOK);
		PlayerTaskSpecific ptsNew = database.getPlayerTaskSpecific(1L, "em@em.em");
		assertNotNull(ptsNew);
		assertEquals("em@em.em", ptsNew.getPlayerEmail());
		assertEquals(1L, ptsNew.getTaskID().longValue());
		assertEquals(10, ptsNew.getPoints().intValue());
		assertEquals(false, ptsNew.isFinishedByUser().booleanValue());
		assertEquals(false, ptsNew.getAreChanges().booleanValue());
		assertEquals(false, ptsNew.getWasHidden().booleanValue());
		assertEquals("something", ptsNew.getChanges());
		assertEquals(PlayerTaskSpecific.ACTIVE, ptsNew.getStatus().intValue());
	}
	/* ------------------------ UPDATE END ------------------------ */
}
