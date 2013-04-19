package com.blstream.urbangame.database.test;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.Player;

public class DatabaseUserQueryTest extends AndroidTestCase {
	
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
	
	private void prepareData(String email, String pass, String displayName, String avatarBase64) {
		Player player = new Player(email, pass, displayName, avatarBase64);
		database.insertUser(player);
	}
	
	/* ------------------------ USER QUERIES ------------------------ */
	public void testNothingInDatabase() {
		Player p = database.getPlayer("a");
		assertNull(p);
	}
	
	public void testOneItemInDatabaseNoMatch() {
		prepareData("email", "pass", "displayName", "avatarBase64");
		Player p = database.getPlayer("a");
		assertNull(p);
	}
	
	public void testOneItemInDatabaseMatch() {
		prepareData("email", "pass", "displayName", "avatarBase64");
		Player p = database.getPlayer("email");
		assertNotNull(p);
		assertEquals("email", p.getEmail());
		assertEquals("pass", p.getPassword());
		assertEquals("displayName", p.getDisplayName());
		assertEquals("avatarBase64", p.getAvatarBase64());
	}
	
	public void testManyItemsInDatabaseNoMatch() {
		for (int i = 0; i < 30; i++) {
			prepareData("email" + i, "pass" + i, "displayName" + i, "avatarBase64" + i);
		}
		Player p = database.getPlayer("a");
		assertNull(p);
	}
	
	public void testManyItemsInDatabaseMatch() {
		for (int i = 0; i < 30; i++) {
			prepareData("email" + i, "pass" + i, "displayName" + i, "avatarBase64" + i);
		}
		Player p = database.getPlayer("email" + 13);
		assertNotNull(p);
		assertEquals("email" + 13, p.getEmail());
		assertEquals("pass" + 13, p.getPassword());
		assertEquals("displayName" + 13, p.getDisplayName());
		assertEquals("avatarBase64" + 13, p.getAvatarBase64());
	}
	/* ------------------------ USER QUERIES END ------------------------ */
}
