package com.blstream.urbangame.database.test;

import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.Player;

public class DatabaseUserUpdateTest extends AndroidTestCase {
	
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
		mContext.deleteDatabase(Database.DATABASE_NAME);
		database = null;
	}
	
	/* ------------------------ UPDATE ------------------------ */
	public void testUpdateAll() {
		database.insertUser(player);
		player = new Player("em@em.em", "1234562", "herbatnik2", "avatarBase642");
		database.updatePlayer(player);
		Player p = database.getPlayer("em@em.em");
		assertNotNull(p);
		assertEquals("em@em.em", p.getEmail());
		assertEquals("1234562", p.getPassword());
		assertEquals("herbatnik2", p.getDisplayName());
		assertEquals("avatarBase642", p.getAvatarBase64());
	}
	
	public void testUpdateNothing() {
		database.insertUser(player);
		player = new Player("em@em.em", null, null, (String) null); // cast
																	// needed to
																	// define
																	// which
																	// constructor
																	// it has to
																	// use
		database.updatePlayer(player);
		Player p = database.getPlayer("em@em.em");
		assertNotNull(p);
		assertEquals("em@em.em", p.getEmail());
		assertEquals("123456", p.getPassword());
		assertEquals("herbatnik", p.getDisplayName());
		assertEquals("avatarBase64", p.getAvatarBase64());
	}
	/* ------------------------ UPDATE END ------------------------ */
}
