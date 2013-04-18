package com.blstream.urbangame.database.test;

import java.util.Calendar;
import java.util.Date;

import android.test.AndroidTestCase;
import android.test.IsolatedContext;
import android.util.Log;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;

public class DatabaseGamesDeleteTest extends AndroidTestCase {

	private DatabaseInterface database;
	private IsolatedContext isolatedContext;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		if (isolatedContext == null)
			isolatedContext = new IsolatedContext(null, mContext);
		if (database == null)
			database = new Database(isolatedContext);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		isolatedContext.deleteDatabase(Database.DATABASE_NAME);
		database = null;
	}

	private void prepareDataGame(Long ID, Double version, String title,
			String operatorName, String winningStrategy, Integer players,
			Integer maxPlayers, Date startDate, Date endDate,
			Integer difficulty, Boolean reward, String prizeInfo,
			String description, String gameLogo, String operatorLogo,
			String comments, String location, String detailsLink) {
		UrbanGame game = new UrbanGame(ID, version, title, operatorName,
				winningStrategy, players, maxPlayers, startDate, endDate,
				difficulty, reward, prizeInfo, description, gameLogo,
				operatorLogo, comments, location, detailsLink);
		boolean ok = database.insertGameInfo(game);
		if (ok) {
			Log.i("O", "OK");
		} else {
			Log.w("O", "XXX");
		}
	}

	private void prepareDataGameShort(Long ID, String title,
			String operatorName, Integer players, Integer maxPlayers,
			Date startDate, Date endDate, Boolean reward, String gameLogo,
			String operatorLogo, String location, String detailsLink) {
		UrbanGameShortInfo game = new UrbanGameShortInfo(ID, title,
				operatorName, players, maxPlayers, startDate, endDate, reward,
				gameLogo, operatorLogo, location, detailsLink);
		database.insertGameShortInfo(game);
	}

	/* ------------------------ DELETE ------------------------ */
	// nothing in database
	public void testNothingInDatabase() {
		boolean isSuccesful = database.deleteGameInfoAndShortInfo(2L);
		assertFalse(isSuccesful);
	}

	// one item in database - no match
	public void testOneItemInDatabaseNoMatch() {
		Calendar c = Calendar.getInstance();
		c.set(2013, 7, 30, 13, 31);
		Date startDate = c.getTime();
		c.set(2013, 8, 15, 14, 41);
		Date endDate = c.getTime();

		prepareDataGame(3L, 2.4, "title", "Operator Name", "winningStrategy",
				34, 50, startDate, endDate, 2, true, "prizesInfo",
				"description", "gamelogoBase64", "operatorlogoBase64",
				"comments", "location", "detaisLink");
		boolean isSuccesful = database.deleteGameInfoAndShortInfo(34L);
		assertFalse(isSuccesful);
	}

	// SHORT one item in database - no match
	public void testOneItemInDatabaseNoMatchSHORT() {
		Calendar c = Calendar.getInstance();
		c.set(2013, 7, 30, 13, 31);
		Date startDate = c.getTime();
		c.set(2013, 8, 15, 14, 41);
		Date endDate = c.getTime();

		prepareDataGameShort(2L, "title", "operatorName", 2, 50, startDate,
				endDate, true, "location", "gamelogoBase64",
				"operatorlogoBase64", "detailsLink");
		boolean isSuccesful = database.deleteGameInfoAndShortInfo(34L);
		assertFalse(isSuccesful);
	}

	// many(30) items in database - no match
	public void testManyItemsInDatabaseNoMatch() {
		Calendar c = Calendar.getInstance();
		for (int i = 0; i < 30; i++) {
			c.set(i + 2013, i % 12 + 1, i % 10 + 1);
			Date start = c.getTime();
			c.set(i + 2014, (i + 1) % 12 + 1, i % 10 + 4);
			Date end = c.getTime();
			prepareDataGame((long) i, (double) i / 3, "title" + i,
					"Operator Name" + i, "winningStrategy" + i, i * 3, i * 10,
					start, end, i % 5 + 1, i % 2 == 0, "prizesInfo" + i,
					"description" + i, "gamelogoBase64" + i,
					"operatorlogoBase64" + i, "comments" + i, "location" + i,
					"detailsLink" + i);

		}
		boolean isSuccesful = database.deleteGameInfoAndShortInfo(2134L);
		assertFalse(isSuccesful);
	}

	// SHORT many(30) items in database - no match
	public void testManyItemsInDatabaseNoMatchSHORT() {
		for (int i = 0; i < 30; i++) {
			prepareDataGameShort((long) i, "title", "operatorName", 2, 50,
					new Date(), new Date(), true, "location", "gamelogoBase64",
					"operatorlogoBase64", "detailsLink");
		}
		boolean isSuccesful = database.deleteGameInfoAndShortInfo(2134L);
		assertFalse(isSuccesful);
	}

	// one item in database - match
	public void testOneItemInDatabaseMatch() {
		Calendar c = Calendar.getInstance();
		c.set(2013, 7, 30, 13, 31);
		Date startDate = c.getTime();
		c.set(2013, 8, 15, 14, 41);
		Date endDate = c.getTime();

		prepareDataGame(3L, 2.4, "title", "Operator Name", "winningStrategy",
				34, 50, startDate, endDate, 2, true, "prizesInfo",
				"description", "gamelogoBase64", "operatorlogoBase64",
				"comments", "location", "detaisLink");
		boolean isSuccesful = database.deleteGameInfoAndShortInfo(3L);
		assertTrue(isSuccesful);
	}

	// SHORT one item in database - match
	public void testOneItemInDatabaseMatchSHORT() {
		Calendar c = Calendar.getInstance();
		c.set(2013, 7, 30, 13, 31);
		Date startDate = c.getTime();
		c.set(2013, 8, 15, 14, 41);
		Date endDate = c.getTime();

		prepareDataGameShort(2L, "title", "operatorName", 2, 50, startDate,
				endDate, true, "location", "gamelogoBase64",
				"operatorlogoBase64", "detailsLink");
		boolean isSuccesful = database.deleteGameInfoAndShortInfo(2L);
		assertTrue(isSuccesful);
	}

	// many(30) items in database - match
	public void testManyItemsInDatabaseMatch() {
		Calendar c = Calendar.getInstance();
		for (int i = 0; i < 30; i++) {
			c.set(i + 2013, i % 12 + 1, i % 10 + 1);
			Date start = c.getTime();
			c.set(i + 2014, (i + 1) % 12 + 1, i % 10 + 4);
			Date end = c.getTime();
			prepareDataGame((long) i, (double) i / 3, "title" + i,
					"Operator Name" + i, "winningStrategy" + i, i * 3, i * 10,
					start, end, i % 5 + 1, i % 2 == 0, "prizesInfo" + i,
					"description" + i, "gamelogoBase64" + i,
					"operatorlogoBase64" + i, "comments" + i, "location" + i,
					"detailsLink" + i);

		}
		boolean isSuccesful = database.deleteGameInfoAndShortInfo(13L);
		assertTrue(isSuccesful);
	}

	// SHORT many(30) items in database - match
	public void testManyItemsInDatabaseMatchSHORT() {
		for (int i = 0; i < 30; i++) {
			prepareDataGameShort((long) i, "title", "operatorName", 2, 50,
					new Date(), new Date(), true, "location", "gamelogoBase64",
					"operatorlogoBase64", "detailsLink");
		}
		boolean isSuccesful = database.deleteGameInfoAndShortInfo(13L);
		assertTrue(isSuccesful);
	}
	/* --------------------- DELETE END --------------------- */
}