package com.blstream.urbangame.database.test;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;

public class DatabaseGameSpecificUpdateTest extends AndroidTestCase {
	
	private DatabaseInterface database;
	private PlayerGameSpecific playerSpecific;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		if (database == null) {
			database = new Database(mContext);
		}
		playerSpecific = new PlayerGameSpecific(12, "em@em.em", 2L, PlayerGameSpecific.GAME_ACTIVE, "email changed",
			true);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		mContext.deleteDatabase(database.getDatabaseName());
		database = null;
	}
	
	/* ------------------------ UPDATE ------------------------ */
	public void testUpdateAll() {
		database.insertUserGameSpecific(playerSpecific);
		playerSpecific = new PlayerGameSpecific(1, "em@em.em", 2L, PlayerGameSpecific.GAME_OBSERVED, "city changed",
			false);
		database.updateUserGameSpecific(playerSpecific);
		PlayerGameSpecific p = database.getUserGameSpecific("em@em.em", 2L);
		assertNotNull(p);
		assertEquals("em@em.em", p.getPlayerEmail());
		assertEquals(2L, p.getGameID().longValue());
		assertEquals(PlayerGameSpecific.GAME_OBSERVED, p.getState().intValue());
		assertEquals(1, p.getRank().intValue());
		assertEquals("city changed", p.getChanges());
		assertEquals(false, p.hasChanges().booleanValue());
	}
	
	public void testUpdateNothing() {
		database.insertUserGameSpecific(playerSpecific);
		playerSpecific = new PlayerGameSpecific(null, "em@em.em", 2L, null, null, null);
		database.updateUserGameSpecific(playerSpecific);
		PlayerGameSpecific p = database.getUserGameSpecific("em@em.em", 2L);
		assertNotNull(p);
		assertEquals("em@em.em", p.getPlayerEmail());
		assertEquals(2L, p.getGameID().longValue());
		assertEquals(PlayerGameSpecific.GAME_ACTIVE, p.getState().intValue());
		assertEquals(12, p.getRank().intValue());
		assertEquals("email changed", p.getChanges());
		assertEquals(true, p.hasChanges().booleanValue());
	}
	/* ------------------------ UPDATE END ------------------------ */
}
