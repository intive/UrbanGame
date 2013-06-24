package com.blstream.urbangame.database.test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;

public class DatabaseGamesQueryTest extends AndroidTestCase {
	
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
	
	private void prepareDataGame(Long ID, Double version, String title, String operatorName, String winningStrategy,
		Integer players, Integer maxPlayers, Date startDate, Date endDate, Integer difficulty, Boolean reward,
		String prizeInfo, String description, String gameLogo, String operatorLogo, String comments, String location,
		String detailsLink) {
		UrbanGame game = new UrbanGame(ID, version, title, operatorName, winningStrategy, players, maxPlayers,
			startDate, endDate, difficulty, reward, prizeInfo, description, gameLogo, operatorLogo, comments, location,
			detailsLink);
		database.insertGameInfo(game);
	}
	
	private void prepareDataGameShort(Long ID, String title, String operatorName, Integer players, Integer maxPlayers,
		Date startDate, Date endDate, Boolean reward, String gameLogo, String operatorLogo, String location,
		String detailsLink) {
		UrbanGameShortInfo game = new UrbanGameShortInfo(ID, title, operatorName, players, maxPlayers, startDate,
			endDate, reward, gameLogo, operatorLogo, location, detailsLink);
		database.insertGameShortInfo(game);
	}
	
	/* ------------------------ FULL LIST QUERIES ------------------------ */
	
	// nothing in database
	public void testNothingInDatabase() {
		List<UrbanGameShortInfo> gamesList = database.getAllGamesShortInfo();
		assertNull(gamesList);
	}
	
	// one item in database
	public void testOneItemInDatabase() {
		prepareDataGameShort(3L, "title", "operatorName", 2, 50, new Date(), new Date(), true, "location",
			"gamelogoBase64", "operatorlogoBase64", "detailsLink");
		List<UrbanGameShortInfo> gamesList = database.getAllGamesShortInfo();
		assertNotNull(gamesList);
		assertEquals(gamesList.size(), 1);
	}
	
	// many(30) items in database
	public void testManyItemsInDatabase() {
		for (int i = 0; i < 30; i++) {
			prepareDataGameShort((long) i, "title", "operatorName", 2, 50, new Date(), new Date(), true, "location",
				"gamelogoBase64", "operatorlogoBase64", "detailsLink");
		}
		List<UrbanGameShortInfo> gamesList = database.getAllGamesShortInfo();
		assertNotNull(gamesList);
		assertEquals(gamesList.size(), 30);
	}
	
	// many(30) items in database ordered by start time
	public void testManyItemsInDatabaseOrderedByStartTime() {
		for (int i = 0; i < 30; i++) {
			prepareDataGameShort((long) i, "title", "operatorName", 2, 50, new Date(), new Date(), true, "location",
				"gamelogoBase64", "operatorlogoBase64", "detailsLink");
		}
		List<UrbanGameShortInfo> gamesList = database.getAllGamesShortInfoOrderedByStartTime(0, 30);
		assertNotNull(gamesList);
		assertEquals(gamesList.size(), 30);
		
		Date d = gamesList.get(0).getStartDate();
		boolean areSorted = true;
		for (int i = 1; i < gamesList.size(); i++) {
			areSorted = areSorted && d.getTime() <= gamesList.get(i).getStartDate().getTime();
		}
		assertTrue(areSorted);
	}
	
	// many(30) items in database ordered by start time AND Pagination
	public void testManyItemsInDatabaseOrderedByStartTimeAndPagination() {
		for (int i = 0; i < 30; i++) {
			prepareDataGameShort((long) i, "title", "operatorName", 2, 50, new Date(), new Date(), true, "location",
				"gamelogoBase64", "operatorlogoBase64", "detailsLink");
		}
		List<UrbanGameShortInfo> gamesList = database.getAllGamesShortInfoOrderedByStartTime(10, 10);
		assertNotNull(gamesList);
		assertEquals(gamesList.size(), 10);
		
		Date d = gamesList.get(0).getStartDate();
		boolean areSorted = true;
		for (int i = 1; i < gamesList.size(); i++) {
			areSorted = areSorted && d.getTime() <= gamesList.get(i).getStartDate().getTime();
		}
		assertTrue(areSorted);
	}
	
	// many(30) items in database ordered by start time AND Pagination with end
	// data less than required
	// example 1 2 3 4 5, take 3 from 4 should return 5
	public void testManyItemsInDatabaseOrderedByStartTimeAndPaginationEndData() {
		Calendar c = Calendar.getInstance();
		for (int i = 0; i < 30; i++) {
			c.set((int) Math.random() * 2000, (int) Math.random() * 100 % 12 + 1, (int) Math.random() * 100 % 24 + 1,
				(int) Math.random() * 100 % 23 + 1, (int) Math.random() * 100 % 58 + 1);
			prepareDataGameShort((long) i, "title", "operatorName", 2, 50, new Date(), new Date(), true, "location",
				"gamelogoBase64", "operatorlogoBase64", "detailsLink");
		}
		List<UrbanGameShortInfo> gamesList = database.getAllGamesShortInfoOrderedByStartTime(24, 30);
		assertNotNull(gamesList);
		assertEquals(gamesList.size(), 6);
		
		Date d = gamesList.get(0).getStartDate();
		boolean areSorted = true;
		for (int i = 1; i < gamesList.size(); i++) {
			areSorted = areSorted && d.getTime() <= gamesList.get(i).getStartDate().getTime();
		}
		assertTrue(areSorted);
	}
	
	/* ------------------------ FULL LIST QUERIES END ------------------------ */
	
	/* ------------------------ SINGLE GAMES QUERIES ------------------------ */
	// nothing in database
	public void testNothingInDatabaseSingle() {
		UrbanGame game = database.getGameInfo(0L);
		assertNull(game);
		UrbanGameShortInfo gameShort = database.getGameShortInfo(0L);
		assertNull(gameShort);
	}
	
	// one item in database - no match
	public void testOneItemInDatabaseSingleNoMatch() {
		Calendar c = Calendar.getInstance();
		c.set(2013, 7, 30, 13, 31);
		Date startDate = c.getTime();
		c.set(2013, 8, 15, 14, 41);
		Date endDate = c.getTime();
		
		prepareDataGame(3L, 2.4, "title", "Operator Name", "winningStrategy", 34, 50, startDate, endDate, 2, true,
			"prizesInfo", "description", "gamelogoBase64", "operatorlogoBase64", "comments", "location", "detaisLink");
		UrbanGame game = database.getGameInfo(2L);
		assertNull(game);
	}
	
	// SHORT one item in database - no match
	public void testOneItemInDatabaseSingleNoMatchSHORT() {
		prepareDataGameShort(2L, "title", "operatorName", 2, 50, new Date(), new Date(), true, "location",
			"gamelogoBase64", "operatorlogoBase64", "detailsLink");
		UrbanGameShortInfo game = database.getGameShortInfo(1L);
		assertNull(game);
	}
	
	// many(30) items in database - no match
	public void testManyItemsInDatabaseSingleNoMatch() {
		Calendar c = Calendar.getInstance();
		c.set(2013, 7, 30, 13, 31);
		Date startDate = c.getTime();
		c.set(2013, 8, 15, 14, 41);
		Date endDate = c.getTime();
		
		prepareDataGame(2L, 2.4, "title", "Operator Name", "winningStrategy", 34, 50, startDate, endDate, 2, true,
			"prizesInfo", "description", "gamelogoBase64", "operatorlogoBase64", "comments", "location", "detaisLink");
		UrbanGame game = database.getGameInfo(223L);
		assertNull(game);
	}
	
	// SHORT many(30) items in database - no match
	public void testManyItemsInDatabaseSingleNoMatchSHORT() {
		for (int i = 0; i < 30; i++) {
			prepareDataGameShort((long) i, "title", "operatorName", 2, 50, new Date(), new Date(), true, "location",
				"gamelogoBase64", "operatorlogoBase64", "detailsLink");
		}
		UrbanGameShortInfo game = database.getGameShortInfo(123L);
		assertNull(game);
	}
	
	// one item in database - match
	public void testOneItemInDatabaseSingleMatch() {
		Calendar c = Calendar.getInstance();
		c.set(2013, 7, 30, 13, 31);
		c.set(Calendar.MILLISECOND, 0);
		Date startDate = c.getTime();
		c.set(2013, 8, 15, 14, 41);
		c.set(Calendar.MILLISECOND, 0);
		Date endDate = c.getTime();
		prepareDataGame(1L, 2.4, "title", "Operator Name", "winningStrategy", 34, 50, startDate, endDate, 2, true,
			"prizesInfo", "description", "gamelogoBase64", "operatorlogoBase64", "comments", "location", "detailsLink");
		UrbanGame game = database.getGameInfo(1L);
		assertNotNull(game);
		assertEquals(1L, game.getID().longValue());
		assertEquals(2.4, game.getGameVersion());
		assertEquals("title", game.getTitle());
		assertEquals("Operator Name", game.getOperatorName());
		assertEquals("winningStrategy", game.getWinningStrategy());
		assertEquals(34, game.getPlayers().intValue());
		assertEquals(50, game.getMaxPlayers().intValue());
		c.set(2013, 7, 30, 13, 31);
		c.set(Calendar.MILLISECOND, 0);
		Date d = c.getTime();
		assertEquals(d.getTime(), game.getStartDate().getTime());
		c.set(2013, 8, 15, 14, 41);
		c.set(Calendar.MILLISECOND, 0);
		d = c.getTime();
		assertEquals(d.getTime(), game.getEndDate().getTime());
		assertEquals(2, game.getDifficulty().intValue());
		assertEquals(true, game.getReward().booleanValue());
		assertEquals("prizesInfo", game.getPrizesInfo());
		assertEquals("description", game.getDescription());
		assertEquals("gamelogoBase64", game.getGameLogoBase64());
		assertEquals("operatorlogoBase64", game.getOperatorLogoBase64());
		assertEquals("comments", game.getComments());
		assertEquals("location", game.getLocation());
		assertEquals("detailsLink", game.getDetailsLink());
	}
	
	// many(30) items in database - match
	public void testManyItemsInDatabaseSingleMatch() {
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
		int testedGame = 13;
		UrbanGame game = database.getGameInfo((long) testedGame);
		assertNotNull(game);
		assertEquals(testedGame, game.getID().longValue());
		assertEquals((double) testedGame / 3, game.getGameVersion());
		assertEquals("title" + testedGame, game.getTitle());
		assertEquals("Operator Name" + testedGame, game.getOperatorName());
		assertEquals("winningStrategy" + testedGame, game.getWinningStrategy());
		assertEquals(testedGame * 3, game.getPlayers().intValue());
		assertEquals(testedGame * 10, game.getMaxPlayers().intValue());
		c.set(testedGame + 2013, testedGame % 12 + 1, testedGame % 10 + 1);
		assertEquals(c.getTime().getTime(), game.getStartDate().getTime());
		c.set(testedGame + 2014, (testedGame + 1) % 12 + 1, testedGame % 10 + 4);
		assertEquals(c.getTime().getTime(), game.getEndDate().getTime());
		assertEquals(testedGame % 5 + 1, game.getDifficulty().intValue());
		assertEquals(testedGame % 2 == 0, game.getReward().booleanValue());
		assertEquals("prizesInfo" + testedGame, game.getPrizesInfo());
		assertEquals("description" + testedGame, game.getDescription());
		assertEquals("gamelogoBase64" + testedGame, game.getGameLogoBase64());
		assertEquals("operatorlogoBase64" + testedGame, game.getOperatorLogoBase64());
		assertEquals("comments" + testedGame, game.getComments());
		assertEquals("location" + testedGame, game.getLocation());
		assertEquals("detailsLink" + testedGame, game.getDetailsLink());
	}
	
	// SHORT many(30) items in database - match
	public void testManyItemsInDatabaseSingleMatchSHORT() {
		Calendar c = Calendar.getInstance();
		for (int i = 0; i < 30; i++) {
			c.set(i + 2013, i % 12 + 1, i % 10 + 1);
			Date start = c.getTime();
			c.set(i + 2014, (i + 1) % 12 + 1, i % 10 + 4);
			Date end = c.getTime();
			prepareDataGameShort((long) i, "title" + i, "operatorName" + i, i * 3, i * 10, start, end, i % 2 == 0,
				"location" + i, "gamelogoBase64" + i, "operatorlogoBase64" + i, "detailsLink" + i);
			
		}
		int testedGame = 13;
		UrbanGameShortInfo game = database.getGameShortInfo((long) testedGame);
		assertNotNull(game);
		assertEquals(testedGame, game.getID().longValue());
		assertEquals("title" + testedGame, game.getTitle());
		assertEquals("operatorName" + testedGame, game.getOperatorName());
		assertEquals(testedGame * 3, game.getPlayers().intValue());
		assertEquals(testedGame * 10, game.getMaxPlayers().intValue());
		c.set(testedGame + 2013, testedGame % 12 + 1, testedGame % 10 + 1);
		assertEquals(c.getTime().getTime(), game.getStartDate().getTime());
		c.set(testedGame + 2014, (testedGame + 1) % 12 + 1, testedGame % 10 + 4);
		assertEquals(c.getTime().getTime(), game.getEndDate().getTime());
		assertEquals(testedGame % 2 == 0, game.getReward().booleanValue());
		assertEquals("gamelogoBase64" + testedGame, game.getGameLogoBase64());
		assertEquals("operatorlogoBase64" + testedGame, game.getOperatorLogoBase64());
		assertEquals("location" + testedGame, game.getLocation());
		assertEquals("detailsLink" + testedGame, game.getDetailsLink());
	}
	/*
	 * --------------------- SINGLE GAMES QUERIES END ---------------------
	 */
}