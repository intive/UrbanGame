package com.blstream.urbangame.database.test;

import java.util.Calendar;
import java.util.Date;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;

public class DatabaseGamesUpdateTest extends AndroidTestCase {
	
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
	
	/* ------------------------ NORMAL VALUES ------------------------ */
	public void testUpdateValuesInShortGameChangeAll() {
		UrbanGameShortInfo game = new UrbanGameShortInfo(2L, "title", "operatorName", 2, 50, new Date(), new Date(),
			true, "location", "gamelogoBase64", "operatorlogoBase64", "detailsLink");
		database.insertGameShortInfo(game);
		
		Calendar c = Calendar.getInstance();
		c.set(2013, 21, 2, 23, 2);
		Date start = c.getTime();
		c.set(2014, 1, 1, 1, 1);
		Date end = c.getTime();
		UrbanGameShortInfo gameUpdate = new UrbanGameShortInfo(2L, "titleChanged", "operatorName222", 23, 40, start,
			end, false, "location2222", "gamelogoBase6422", "operatorlogoBase64223", "detailsLink2");
		database.updateGameShortInfo(gameUpdate);
		
		UrbanGameShortInfo gameFromDB = database.getGameShortInfo(2L);
		assertNotNull(gameFromDB);
		assertEquals(2L, gameFromDB.getID().longValue());
		assertEquals("titleChanged", gameFromDB.getTitle());
		assertEquals("operatorName222", gameFromDB.getOperatorName());
		assertEquals(23, gameFromDB.getPlayers().intValue());
		assertEquals(40, gameFromDB.getMaxPlayers().intValue());
		c.set(2013, 21, 2, 23, 2);
		assertEquals(c.getTime().getTime(), gameFromDB.getStartDate().getTime());
		c.set(2014, 1, 1, 1, 1);
		assertEquals(c.getTime().getTime(), gameFromDB.getEndDate().getTime());
		assertEquals(false, gameFromDB.getReward().booleanValue());
		assertEquals("gamelogoBase6422", gameFromDB.getGameLogoBase64());
		assertEquals("operatorlogoBase64223", gameFromDB.getOperatorLogoBase64());
		assertEquals("location2222", gameFromDB.getLocation());
		assertEquals("detailsLink2", gameFromDB.getDetailsLink());
	}
	
	public void testUpdateValuesInShortGameChangeNothing() {
		Calendar c = Calendar.getInstance();
		c.set(2013, 21, 2, 23, 2);
		Date start = c.getTime();
		c.set(2014, 1, 1, 1, 1);
		Date end = c.getTime();
		UrbanGameShortInfo game = new UrbanGameShortInfo(2L, "title", "operatorName", 2, 50, start, end, true,
			"location", "gamelogoBase64", "operatorlogoBase64", "detailsLink");
		database.insertGameShortInfo(game);
		
		UrbanGameShortInfo gameUpdate = new UrbanGameShortInfo(2L, null, null, null, null, null, null, null, null,
			null, null, null);
		database.updateGameShortInfo(gameUpdate);
		
		UrbanGameShortInfo gameFromDB = database.getGameShortInfo(2L);
		assertNotNull(gameFromDB);
		assertEquals(2L, gameFromDB.getID().longValue());
		assertEquals("title", gameFromDB.getTitle());
		assertEquals("operatorName", gameFromDB.getOperatorName());
		assertEquals(2, gameFromDB.getPlayers().intValue());
		assertEquals(50, gameFromDB.getMaxPlayers().intValue());
		assertEquals(start.getTime(), gameFromDB.getStartDate().getTime());
		assertEquals(end.getTime(), gameFromDB.getEndDate().getTime());
		assertEquals(true, gameFromDB.getReward().booleanValue());
		assertEquals("gamelogoBase64", gameFromDB.getGameLogoBase64());
		assertEquals("operatorlogoBase64", gameFromDB.getOperatorLogoBase64());
		assertEquals("location", gameFromDB.getLocation());
		assertEquals("detailsLink", gameFromDB.getDetailsLink());
	}
	
	public void testUpdateValuesInGameChangeAll() {
		UrbanGame game = new UrbanGame(1L, 2.4, "title", "Operator Name", "winningStrategy", 34, 50, new Date(),
			new Date(), 2, true, "prizesInfo", "description", "gamelogoBase64", "operatorlogoBase64", "comments",
			"location", "detailsLink");
		database.insertGameInfo(game);
		
		Calendar c = Calendar.getInstance();
		c.set(2013, 21, 2, 23, 2);
		Date start = c.getTime();
		c.set(2014, 1, 1, 1, 1);
		Date end = c.getTime();
		UrbanGame gameUpdate = new UrbanGame(1L, 3.0, "titleChanged", "operatorName222", "winningStrategy22", 23, 40,
			start, end, 5, false, "prizesInfo222", "description22", "gamelogoBase6422", "operatorlogoBase64223",
			"comments22", "location2222", "detailsLink2");
		database.updateGame(gameUpdate);
		
		UrbanGame gameFromDB = database.getGameInfo(1L);
		assertNotNull(gameFromDB);
		assertEquals(1L, gameFromDB.getID().longValue());
		assertEquals(3.0, gameFromDB.getGameVersion());
		assertEquals("titleChanged", gameFromDB.getTitle());
		assertEquals("operatorName222", gameFromDB.getOperatorName());
		assertEquals("winningStrategy22", gameFromDB.getWinningStrategy());
		assertEquals(23, gameFromDB.getPlayers().intValue());
		assertEquals(40, gameFromDB.getMaxPlayers().intValue());
		c.set(2013, 21, 2, 23, 2);
		assertEquals(c.getTime().getTime(), gameFromDB.getStartDate().getTime());
		c.set(2014, 1, 1, 1, 1);
		assertEquals(c.getTime().getTime(), gameFromDB.getEndDate().getTime());
		assertEquals(5, gameFromDB.getDifficulty().intValue());
		assertEquals(false, gameFromDB.getReward().booleanValue());
		assertEquals("prizesInfo222", gameFromDB.getPrizesInfo());
		assertEquals("description22", gameFromDB.getDescription());
		assertEquals("gamelogoBase6422", gameFromDB.getGameLogoBase64());
		assertEquals("operatorlogoBase64223", gameFromDB.getOperatorLogoBase64());
		assertEquals("comments22", gameFromDB.getComments());
		assertEquals("location2222", gameFromDB.getLocation());
		assertEquals("detailsLink2", gameFromDB.getDetailsLink());
	}
	
	public void testUpdateValuesInGameChangeNothing() {
		Calendar c = Calendar.getInstance();
		c.set(2013, 21, 2, 23, 2);
		Date start = c.getTime();
		c.set(2014, 1, 1, 1, 1);
		Date end = c.getTime();
		UrbanGame game = new UrbanGame(1L, 2.4, "title", "Operator Name", "winningStrategy", 34, 50, start, end, 2,
			true, "prizesInfo", "description", "gamelogoBase64", "operatorlogoBase64", "comments", "location",
			"detailsLink");
		database.insertGameInfo(game);
		
		UrbanGame gameUpdate = new UrbanGame(null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, null);
		database.updateGame(gameUpdate);
		UrbanGame gameFromDB = database.getGameInfo(1L);
		
		assertNotNull(gameFromDB);
		assertEquals(1L, gameFromDB.getID().longValue());
		assertEquals(2.4, gameFromDB.getGameVersion());
		assertEquals("title", gameFromDB.getTitle());
		assertEquals("Operator Name", gameFromDB.getOperatorName());
		assertEquals("winningStrategy", gameFromDB.getWinningStrategy());
		assertEquals(34, gameFromDB.getPlayers().intValue());
		assertEquals(50, gameFromDB.getMaxPlayers().intValue());
		c.set(2013, 21, 2, 23, 2);
		assertEquals(c.getTime().getTime(), gameFromDB.getStartDate().getTime());
		c.set(2014, 1, 1, 1, 1);
		assertEquals(c.getTime().getTime(), gameFromDB.getEndDate().getTime());
		assertEquals(2, gameFromDB.getDifficulty().intValue());
		assertEquals(true, gameFromDB.getReward().booleanValue());
		assertEquals("prizesInfo", gameFromDB.getPrizesInfo());
		assertEquals("description", gameFromDB.getDescription());
		assertEquals("gamelogoBase64", gameFromDB.getGameLogoBase64());
		assertEquals("operatorlogoBase64", gameFromDB.getOperatorLogoBase64());
		assertEquals("comments", gameFromDB.getComments());
		assertEquals("location", gameFromDB.getLocation());
		assertEquals("detailsLink", gameFromDB.getDetailsLink());
	}
	/* ------------------------ NORMAL VALUES ------------------------ */
}
