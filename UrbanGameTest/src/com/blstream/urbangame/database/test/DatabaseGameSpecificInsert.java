package com.blstream.urbangame.database.test;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;

public class DatabaseGameSpecificInsert extends AndroidTestCase {
	
	private DatabaseInterface database;
	private PlayerGameSpecific playerSpecific;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		if (database == null) {
			database = new Database(mContext);
		}
		playerSpecific = new PlayerGameSpecific(12, "em@em.em", 2L, PlayerGameSpecific.GAME_ACTIVE);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		mContext.deleteDatabase(Database.DATABASE_NAME);
		database = null;
	}
	
	/* ------------------------ NULL VALUES ------------------------ */
	// allowed
	public void testInsertNullRank() {
		playerSpecific.setRank(null);
		boolean ok = database.insertUserGameSpecific(playerSpecific);
		assertTrue(ok);
	}
	
	// allowed
	public void testInsertNullState() {
		playerSpecific.setState(null);
		boolean ok = database.insertUserGameSpecific(playerSpecific);
		assertTrue(ok);
	}
	
	/* ------------------------ NULL VALUES END ------------------------ */
	
	/* ------------------------ WRONG VALUES END ------------------------ */
	public void testKeyDuplication() {
		playerSpecific.setGameID(2L);
		playerSpecific.setPlayerEmail("em@em.em");
		boolean ok = database.insertUserGameSpecific(playerSpecific);
		assertTrue(ok);
		playerSpecific.setGameID(2L);
		playerSpecific.setPlayerEmail("em@em.em");
		ok = database.insertUserGameSpecific(playerSpecific);
		assertFalse(ok);
	}
	
	/* ------------------------ WRONG VALUES END ------------------------ */
	
	/* ------------------------ NORMAL VALUES ------------------------ */
	public void testInsertNormalValues() {
		boolean ok = database.insertUserGameSpecific(playerSpecific);
		assertTrue(ok);
	}
	/* ------------------------ NORMAL VALUES END ------------------------ */
}
