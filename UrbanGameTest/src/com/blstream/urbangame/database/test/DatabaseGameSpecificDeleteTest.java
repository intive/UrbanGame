package com.blstream.urbangame.database.test;

import java.util.Calendar;
import java.util.Date;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;
import com.blstream.urbangame.database.entity.UrbanGame;

public class DatabaseGameSpecificDeleteTest extends AndroidTestCase {
	
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
	
	private void prepareDataUserSpecific(Integer rank, String email, Long gameID, Integer status) {
		PlayerGameSpecific player = new PlayerGameSpecific(rank, email, gameID, status);
		database.insertUserGameSpecific(player);
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
	
	private void prepareDataUser(String email, String pass, String displayName, String avatarBase64) {
		Player player = new Player(email, pass, displayName, avatarBase64);
		database.insertUser(player);
	}
	
	/* ------------------- USER SPECIFIC DELETION ------------------- */
	public void testNothingInDatabase() {
		boolean isSuccessful = database.deleteUserGameSpecific("a", 2L);
		assertFalse(isSuccessful);
	}
	
	public void testOneItemInDatabaseNoMatch() {
		prepareDataUserSpecific(2, "em@em.em", 1L, PlayerGameSpecific.GAME_ACTIVE);
		boolean isSuccessful = database.deleteUserGameSpecific("a", 2L);
		assertFalse(isSuccessful);
	}
	
	public void testOneItemInDatabaseMatch() {
		prepareDataUserSpecific(2, "em@em.em", 1L, PlayerGameSpecific.GAME_ACTIVE);
		boolean isSuccessful = database.deleteUserGameSpecific("em@em.em", 1L);
		assertTrue(isSuccessful);
	}
	
	public void testManyItemsInDatabaseNoMatch() {
		for (int i = 0; i < 30; i++) {
			prepareDataUserSpecific(i, "em@em.em" + i, (long) i, i % 2 == 0 ? PlayerGameSpecific.GAME_ACTIVE
				: PlayerGameSpecific.GAME_OBSERVED);
		}
		boolean isSuccessful = database.deleteUserGameSpecific("a", 3L);
		assertFalse(isSuccessful);
	}
	
	public void testManyItemsInDatabaseMatch() {
		for (int i = 0; i < 30; i++) {
			prepareDataUserSpecific(i, "em@em.em" + i, (long) i, i % 2 == 0 ? PlayerGameSpecific.GAME_ACTIVE
				: PlayerGameSpecific.GAME_OBSERVED);
		}
		boolean isSuccessful = database.deleteUserGameSpecific("em@em.em" + 13, 13L);
		assertTrue(isSuccessful);
	}
	
	public void testWipeOutUserData() {
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
		prepareDataUser("em@em.em", "123", "herbatnik", "ciastko");
		for (int i = 0; i < 30; i++) {
			prepareDataUserSpecific(i * 7, "em@em.em", (long) i, i % 2 == 0 ? PlayerGameSpecific.GAME_ACTIVE
				: PlayerGameSpecific.GAME_OBSERVED);
		}
		boolean isSuccessful = database.wipeOutUserData("em@em.em");
		assertTrue(isSuccessful);
		
		Player p = database.getPlayer("em@em.em");
		assertNull(p);
		
		for (int i = 0; i < 30; i++) {
			PlayerGameSpecific pgs = database.getUserGameSpecific("em@em.em", (long) i);
			assertNull(pgs);
		}
	}
	/* ------------------- USER SPECIFIC DELETION END ------------------- */
}
