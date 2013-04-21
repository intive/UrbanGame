package com.blstream.urbangame.database.test;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;

public class DatabaseGameSpecificQueryTest extends AndroidTestCase {
	
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
	
	private void prepareData(Integer rank, String email, Long gameID, Integer status) {
		PlayerGameSpecific player = new PlayerGameSpecific(rank, email, gameID, status);
		database.insertUserGameSpecific(player);
	}
	
	/* ------------------------ USER QUERIES ------------------------ */
	public void testNothingInDatabase() {
		PlayerGameSpecific p = database.getUserGameSpecific("a", 1L);
		assertNull(p);
	}
	
	public void testOneItemInDatabaseNoMatch() {
		prepareData(2, "em@em.em", 1L, PlayerGameSpecific.GAME_ACTIVE);
		PlayerGameSpecific p = database.getUserGameSpecific("a", 1L);
		assertNull(p);
	}
	
	public void testOneItemInDatabaseMatch() {
		prepareData(2, "em@em.em", 1L, PlayerGameSpecific.GAME_ACTIVE);
		PlayerGameSpecific p = database.getUserGameSpecific("em@em.em", 1L);
		assertNotNull(p);
		assertEquals(2, p.getRank().intValue());
		assertEquals("em@em.em", p.getPlayerEmail());
		assertEquals(1L, p.getGameID().longValue());
		assertEquals(PlayerGameSpecific.GAME_ACTIVE, p.getState().intValue());
	}
	
	public void testManyItemsInDatabaseNoMatch() {
		for (int i = 0; i < 30; i++) {
			prepareData(i + 1, "em@em.em" + i, (long) i * 2, i % 2 == 0 ? PlayerGameSpecific.GAME_ACTIVE
				: PlayerGameSpecific.GAME_OBSERVED);
		}
		PlayerGameSpecific p = database.getUserGameSpecific("a", 2L);
		assertNull(p);
	}
	
	public void testManyItemsInDatabaseMatch() {
		for (int i = 0; i < 30; i++) {
			prepareData(i + 1, "em@em.em" + i, (long) i * 2, i % 2 == 0 ? PlayerGameSpecific.GAME_ACTIVE
				: PlayerGameSpecific.GAME_OBSERVED);
		}
		int tested = 13;
		PlayerGameSpecific p = database.getUserGameSpecific("em@em.em" + tested, (long) tested * 2);
		assertNotNull(p);
		assertEquals(tested + 1, p.getRank().intValue());
		assertEquals("em@em.em" + tested, p.getPlayerEmail());
		assertEquals((long) tested * 2, p.getGameID().longValue());
		assertEquals(tested % 2 == 0 ? PlayerGameSpecific.GAME_ACTIVE : PlayerGameSpecific.GAME_OBSERVED, p.getState()
			.intValue());
	}
	/* ------------------------ USER QUERIES END ------------------------ */
}