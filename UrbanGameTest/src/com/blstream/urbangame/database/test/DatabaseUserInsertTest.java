package com.blstream.urbangame.database.test;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.Player;

public class DatabaseUserInsertTest extends AndroidTestCase {
	
	private DatabaseInterface database;
	private Player player;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		if (database == null) {
			database = new Database(mContext);
		}
		player = new Player("em@em.em", "123456", "herbatnik", "avatarBase64");
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		mContext.deleteDatabase(database.getDatabaseName());
		database = null;
	}
	
	/* ------------------------ NULL VALUES ------------------------ */
	// allowed
	public void testInsertNullPassword() {
		player.setPassword(null);
		boolean ok = database.insertUser(player);
		assertTrue(ok);
	}
	
	// allowed
	public void testInsertNullDisplayName() {
		player.setDisplayName(null);
		boolean ok = database.insertUser(player);
		assertTrue(ok);
	}
	
	// allowed
	public void testInsertNullAvatarBase64() {
		player.setAvatarBase64(null);
		boolean ok = database.insertUser(player);
		assertTrue(ok);
	}
	
	// not allowed
	public void testUserLoggingNull() {
		boolean ok = database.setLoggedPlayer(null);
		assertFalse(ok);
	}
	
	/* ------------------------ NULL VALUES END ------------------------ */
	
	/* ------------------------ WRONG VALUES END ------------------------ */
	public void testKeyDuplication() {
		player.setEmail("em@em.em");
		boolean ok = database.insertUser(player);
		assertTrue(ok);
		player.setEmail("em@em.em");
		ok = database.insertUser(player);
		assertFalse(ok);
	}
	
	//not allowed
	public void testMultiLogging() {
		boolean ok = database.setLoggedPlayer("em@em.em");
		assertTrue(ok);
		ok = database.setLoggedPlayer("em2@em2.em2");
		assertFalse(ok);
	}
	
	/* ------------------------ WRONG VALUES END ------------------------ */
	
	/* ------------------------ NORMAL VALUES ------------------------ */
	public void testInsertNormalValues() {
		boolean ok = database.insertUser(player);
		assertTrue(ok);
	}
	
	public void testUserLogging() {
		boolean ok = database.setLoggedPlayer("em@em.em");
		assertTrue(ok);
	}
	/* ------------------------ NORMAL VALUES END ------------------------ */
}
