package com.blstream.urbangame.database.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.test.AndroidTestCase;
import android.test.IsolatedContext;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;

public class DatabaseGamesInsertTest extends AndroidTestCase {

	private DatabaseInterface database;
	private IsolatedContext isolatedContext;
	private UrbanGame game;
	private UrbanGameShortInfo gameShort;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		if (isolatedContext == null)
			isolatedContext = new IsolatedContext(null, mContext);
		if (database == null)
			database = new Database(isolatedContext);
		Calendar c = Calendar.getInstance();
		c.set(2013, 7, 30, 13, 31);
		Date startDate = c.getTime();
		c.set(2013, 8, 15, 14, 41);
		Date endDate = c.getTime();
		game = new UrbanGame(3L, 2.4, "title", "Operator Name",
				"winningStrategy", 34, 50, startDate, endDate, 2, true,
				"prizesInfo", "description", "gameLogoBase64",
				"operatorLogoBase64", "comments", "location", "detaisLink");
		gameShort = new UrbanGameShortInfo(3L, "title", "operatorName", 2, 50,
				startDate, endDate, true, "location", "gamelogoBase64",
				"operatorlogoBase64", "detailsLink");

	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		isolatedContext.deleteDatabase(Database.DATABASE_NAME);
		database = null;
	}

	/* ------------------------ NULL VALUES ------------------------ */

	// inserting null version - not allowed
	public void testNullVersion() {
		game.setGameVersion(null);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// inserting null title - not allowed
	public void testNullTitle() {
		game.setTitle(null);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// SHORT inserting null title - not allowed
	public void testNullTitleSHORT() {
		gameShort.setTitle(null);
		boolean done = database.insertGameShortInfo(gameShort);
		assertFalse(done);
	}

	// inserting null operator name - not allowed
	public void testNullOperatorName() {
		game.setOperatorName(null);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// SHORT inserting null operator name - not allowed
	public void testNullOperatorNameSHORT() {
		gameShort.setOperatorName(null);
		boolean done = database.insertGameShortInfo(gameShort);
		assertFalse(done);
	}

	// inserting null winning strategy - not allowed
	public void testNullWinningStrategy() {
		game.setWinningStrategy(null);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// inserting null number of players - not allowed
	public void testNullNumberOfPlayers() {
		game.setPlayers(null);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// SHORT inserting null number of players - not allowed
	public void testNullNumberOfPlayersSHORT() {
		gameShort.setPlayers(null);
		boolean done = database.insertGameShortInfo(gameShort);
		assertFalse(done);
	}

	// inserting null number of max players - not allowed
	public void testNullNumberOfMaxPlayers() {
		game.setMaxPlayers(null);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// SHORT inserting null number of max players - not allowed
	public void testNullNumberOfMaxPlayersSHORT() {
		gameShort.setMaxPlayers(null);
		boolean done = database.insertGameShortInfo(gameShort);
		assertFalse(done);
	}

	// inserting null start date - not allowed
	public void testNullStartDate() {
		game.setStartDate(null);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// SHORT inserting null start date - not allowed
	public void testNullStartDateSHORT() {
		gameShort.setStartDate(null);
		boolean done = database.insertGameShortInfo(gameShort);
		assertFalse(done);
	}

	// inserting null end date - not allowed
	public void testNullEndDate() {
		game.setEndDate(null);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// SHORT inserting null end date - not allowed
	public void testNullEndDateSHORT() {
		gameShort.setEndDate(null);
		boolean done = database.insertGameShortInfo(gameShort);
		assertFalse(done);
	}

	// inserting null difficulty - allowed
	public void testNullDifficulty() {
		game.setDifficulty(null);
		boolean done = database.insertGameInfo(game);
		assertTrue(done);
	}

	// inserting null reward - allowed
	public void testNullReward() {
		game.setReward(null);
		boolean done = database.insertGameInfo(game);
		assertTrue(done);
	}

	// SHORT inserting null reward - allowed
	public void testNullRewardSHORT() {
		gameShort.setReward(null);
		boolean done = database.insertGameShortInfo(gameShort);
		assertTrue(done);
	}

	// inserting null prize info - allowed
	public void testNullPrizeInfo() {
		game.setPrizesInfo(null);
		boolean done = database.insertGameInfo(game);
		assertTrue(done);
	}

	// inserting null description - not allowed
	public void testNullDescription() {
		game.setDescription(null);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// inserting null game logo - allowed
	public void testNullGameLogo() {
		game.setGameLogoBase64(null);
		boolean done = database.insertGameInfo(game);
		assertTrue(done);
	}

	// SHORT inserting null game logo - allowed
	public void testNullGameLogoSHORT() {
		gameShort.setGameLogoBase64(null);
		boolean done = database.insertGameShortInfo(gameShort);
		assertTrue(done);
	}

	// inserting null operator logo - allowed
	public void testNullOperatorLogo() {
		game.setOperatorLogoBase64(null);
		boolean done = database.insertGameInfo(game);
		assertTrue(done);
	}

	// SHORT inserting null Operator logo - allowed
	public void testNullOperatorLogoSHORT() {
		gameShort.setOperatorLogoBase64(null);
		boolean done = database.insertGameShortInfo(gameShort);
		assertTrue(done);
	}

	// inserting null city for game - not allowed
	public void testNullLocationForGame() {
		game.setLocation(null);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// SHORT inserting null city for game - not allowed
	public void testNullLocationForGameSHORT() {
		gameShort.setLocation(null);
		boolean done = database.insertGameShortInfo(gameShort);
		assertFalse(done);
	}

	// inserting null comments - allowed
	public void testNullComments() {
		game.setComments(null);
		boolean done = database.insertGameInfo(game);
		assertTrue(done);
	}

	// inserting null details link - not allowed
	public void testNullDetailsLink() {
		game.setDetailsLink(null);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// SHORT inserting null details link - not allowed
	public void testNullDetailsLinkSHORT() {
		gameShort.setDetailsLink(null);
		boolean done = database.insertGameShortInfo(gameShort);
		assertFalse(done);
	}

	/* ------------------------ NULL VALUES END ------------------------ */

	/* ------------------------ MIN MAX VALUES ------------------------ */
	// min version - 0.0 | max not specified
	public void testMinVersion() {
		game.setGameVersion(0.0);
		boolean done = database.insertGameInfo(game);
		assertTrue(done);
	}

	// min difficulty - 0
	public void testMinDifficulty() {
		game.setDifficulty(0);
		boolean done = database.insertGameInfo(game);
		assertTrue(done);
	}

	// max difficulty - 5
	public void testMaxDifficulty() {
		game.setDifficulty(5);
		boolean done = database.insertGameInfo(game);
		assertTrue(done);
	}

	// min start date - 1 january 2013 | max not specified
	public void testMinStartDate() {
		Date d = new GregorianCalendar(2013, 01, 01).getTime();
		game.setStartDate(d);
		boolean done = database.insertGameInfo(game);
		assertTrue(done);
	}

	// SHORT min start date - 1 january 2013 | max not specified
	public void testMinStartDateSHORT() {
		Date d = new GregorianCalendar(2013, 01, 01).getTime();
		gameShort.setStartDate(d);
		boolean done = database.insertGameShortInfo(gameShort);
		assertTrue(done);
	}

	// min number of contestants - 0
	public void testMinNumberOfContestants() {
		game.setPlayers(0);
		boolean done = database.insertGameInfo(game);
		assertTrue(done);
	}

	// SHORT min number of contestants - 0
	public void testMinNumberOfContestantsSHORT() {
		gameShort.setPlayers(0);
		boolean done = database.insertGameShortInfo(gameShort);
		assertTrue(done);
	}

	// max number of contestants - 0
	public void testMaxNumberOfContestants() {
		game.setPlayers(game.getMaxPlayers());
		boolean done = database.insertGameInfo(game);
		assertTrue(done);
	}

	// SHORT max number of contestants - 0
	public void testMaxNumberOfContestantsSHORT() {
		gameShort.setPlayers(gameShort.getMaxPlayers());
		boolean done = database.insertGameShortInfo(gameShort);
		assertTrue(done);
	}

	/* ------------------------ MIN MAX VALUES END ------------------------ */

	/* ------------------------ WRONG VALUES ------------------------ */
	// min version - 0.0
	public void testBelowMinVersion() {
		game.setGameVersion(-1.0);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// min difficulty - 0
	public void testBelowMinDifficulty() {
		game.setDifficulty(-1);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// max difficulty - 5
	public void testAboveMaxDifficulty() {
		game.setDifficulty(6);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// min start date - 1 january 2013 | max not specified
	public void testBelowMinStartDate() {
		Date d = new GregorianCalendar(2012, 12, 31).getTime();
		game.setStartDate(d);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// SHORT min start date - 1 january 2013 | max not specified
	public void testBelowMinStartDateSHORT() {
		Date d = new GregorianCalendar(2012, 12, 31).getTime();
		gameShort.setStartDate(d);
		boolean done = database.insertGameShortInfo(gameShort);
		assertFalse(done);
	}

	// min number of contestants - 0
	public void testBelowMinNumberOfContestants() {
		game.setPlayers(-1);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// SHORT min number of contestants - 0
	public void testBelowMinNumberOfContestantsSHORT() {
		gameShort.setPlayers(-1);
		boolean done = database.insertGameShortInfo(gameShort);
		assertFalse(done);
	}

	// max number of contestants - maxPlayers param
	public void testBelowMaxNumberOfContestants() {
		game.setPlayers(game.getMaxPlayers() + 1);
		boolean done = database.insertGameInfo(game);
		assertFalse(done);
	}

	// SHORT min number of contestants - maxPlayers param
	public void testBelowMaxNumberOfContestantsSHORT() {
		gameShort.setPlayers(game.getMaxPlayers() + 1);
		boolean done = database.insertGameShortInfo(gameShort);
		assertFalse(done);
	}

	/* ------------------------ WRONG VALUES END ------------------------ */

	/* ------------------------ NORMAL VALUES ------------------------ */
	public void testNormalValues() {
		boolean done = database.insertGameInfo(game);
		assertTrue(done);
	}

	public void testNormalValuesSHORT() {
		boolean done = database.insertGameShortInfo(gameShort);
		assertTrue(done);
	}
	
	public void testInsertGameInfoAfterInsertingGameShortInfo() {
		gameShort.setID(2L);
		game.setID(2L);
		boolean done = database.insertGameShortInfo(gameShort);
		done = done && database.insertGameInfo(game);
		assertTrue(done);
	}
	/* ------------------------ NORMAL VALUES END ------------------------ */
}