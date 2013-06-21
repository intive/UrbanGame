package com.blstream.urbangame.database.test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;
import com.blstream.urbangame.database.entity.UrbanGame;

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
		mContext.deleteDatabase(database.getDatabaseName());
		database = null;
	}
	
	private boolean prepareData(Integer rank, String email, Long gameID, Integer status, String changes,
		Boolean hasChanges) {
		PlayerGameSpecific player = new PlayerGameSpecific(rank, email, gameID, status, changes, hasChanges);
		return database.insertUserGameSpecific(player);
	}
	
	private void prepareDataGame(Long ID, Double version, String title, String operatorName, String winningStrategy,
		Integer players, Integer maxPlayers, Date startDate, Date endDate, Integer difficulty, Boolean reward,
		String prizeInfo, String description, String gameLogo, String operatorLogo, String comments, String location,
		String detailsLink) {
		UrbanGame game = new UrbanGame(ID, version, title, operatorName, winningStrategy, players, maxPlayers,
			startDate, endDate, difficulty, reward, prizeInfo, description, gameLogo, operatorLogo, comments, location,
			detailsLink);
		database.insertGameInfo(game);
	}
	
	/* ------------------------ USER QUERIES ------------------------ */
	public void testNothingInDatabase() {
		PlayerGameSpecific p = database.getUserGameSpecific("a", 1L);
		assertNull(p);
	}
	
	public void testOneItemInDatabaseNoMatch() {
		prepareData(2, "em@em.em", 1L, PlayerGameSpecific.GAME_ACTIVE, "email changed", true);
		PlayerGameSpecific p = database.getUserGameSpecific("a", 1L);
		assertNull(p);
	}
	
	public void testOneItemInDatabaseMatch() {
		prepareData(2, "em@em.em", 1L, PlayerGameSpecific.GAME_ACTIVE, "email changed", true);
		PlayerGameSpecific p = database.getUserGameSpecific("em@em.em", 1L);
		assertNotNull(p);
		assertEquals(2, p.getRank().intValue());
		assertEquals("em@em.em", p.getPlayerEmail());
		assertEquals(1L, p.getGameID().longValue());
		assertEquals(PlayerGameSpecific.GAME_ACTIVE, p.getState().intValue());
		assertEquals("email changed", p.getChanges());
		assertEquals(true, p.hasChanges().booleanValue());
	}
	
	public void testManyItemsInDatabaseNoMatch() {
		for (int i = 0; i < 30; i++) {
			prepareData(i + 1, "em@em.em" + i, (long) i * 2, i % 2 == 0 ? PlayerGameSpecific.GAME_ACTIVE
				: PlayerGameSpecific.GAME_OBSERVED, "email changed" + i, i % 2 == 0);
		}
		PlayerGameSpecific p = database.getUserGameSpecific("a", 2L);
		assertNull(p);
	}
	
	public void testManyItemsInDatabaseMatch() {
		for (int i = 0; i < 30; i++) {
			prepareData(i + 1, "em@em.em" + i, (long) i * 2, i % 2 == 0 ? PlayerGameSpecific.GAME_ACTIVE
				: PlayerGameSpecific.GAME_OBSERVED, "email changed" + i, i % 2 == 0);
		}
		int tested = 13;
		PlayerGameSpecific p = database.getUserGameSpecific("em@em.em" + tested, (long) tested * 2);
		assertNotNull(p);
		assertEquals(tested + 1, p.getRank().intValue());
		assertEquals("em@em.em" + tested, p.getPlayerEmail());
		assertEquals((long) tested * 2, p.getGameID().longValue());
		assertEquals(tested % 2 == 0 ? PlayerGameSpecific.GAME_ACTIVE : PlayerGameSpecific.GAME_OBSERVED, p.getState()
			.intValue());
		assertEquals("email changed" + tested, p.getChanges());
		assertEquals(tested % 2 == 0, p.hasChanges().booleanValue());
	}
	
	public void testAllUserGamesNoMatch() {
		Calendar c = Calendar.getInstance();
		for (int i = 0; i < 30; i++) {
			c.set(i + 2013, i % 12 + 1, i % 10 + 1);
			Date start = c.getTime();
			c.set(i + 2014, (i + 1) % 12 + 1, i % 10 + 4);
			Date end = c.getTime();
			prepareDataGame((long) i, (double) i / 3, "title" + i, "Operator Name" + i, "winningStrategy" + i, i * 3,
				i * 10, start, end, i % 5 + 1, i % 2 == 0, "prizesInfo" + i, "description" + i, "gamelogoBase64" + i,
				"operatorlogoBase64" + i, "comments" + i, "location" + i, "detailsLink" + i);
			
		}
		for (int i = 0; i < 30; i++) {
			prepareData(i + 1, "em@em.em" + i, (long) i * 2, i % 2 == 0 ? PlayerGameSpecific.GAME_ACTIVE
				: PlayerGameSpecific.GAME_OBSERVED, "email changed" + i, i % 2 == 0);
		}
		List<UrbanGame> userGames = database.getAllUserGames("asdsad@asd.sd");
		assertNull(userGames);
	}
	
	public void testAllUserGamesMatch() {
		Calendar c = Calendar.getInstance();
		for (int i = 0; i < 30; i++) {
			c.set(i + 2013, i % 12 + 1, i % 10 + 1);
			Date start = c.getTime();
			c.set(i + 2014, (i + 1) % 12 + 1, i % 10 + 4);
			Date end = c.getTime();
			prepareDataGame((long) i, (double) i / 3, "title" + i, "Operator Name" + i, "winningStrategy" + i, i * 3,
				i * 10, start, end, i % 5 + 1, i % 2 == 0, "prizesInfo" + i, "description" + i, "gamelogoBase64" + i,
				"operatorlogoBase64" + i, "comments" + i, "location" + i, "detailsLink" + i);
			
		}
		for (int i = 0; i < 30; i++) {
			assertTrue(prepareData(i + 1, "em@em.em", (long) i, i % 2 == 0 ? PlayerGameSpecific.GAME_ACTIVE
				: PlayerGameSpecific.GAME_OBSERVED, "email changed" + i, i % 2 == 0));
		}
		List<UrbanGame> userGames = database.getAllUserGames("em@em.em");
		assertNotNull(userGames);
		assertEquals(30, userGames.size());
	}
	/* ------------------------ USER QUERIES END ------------------------ */
}