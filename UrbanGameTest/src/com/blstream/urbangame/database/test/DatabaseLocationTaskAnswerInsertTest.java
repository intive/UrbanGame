package com.blstream.urbangame.database.test;

import java.util.Date;

import android.location.Location;
import android.test.AndroidTestCase;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.LocationTaskAnswer;

public class DatabaseLocationTaskAnswerInsertTest extends AndroidTestCase {
	
	private LocationTaskAnswer answer;
	private DatabaseInterface database;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		if (database == null) {
			database = new Database(mContext);
		}
		Location location = new Location("test");
		location.setLatitude(34.2);
		location.setLongitude(56.2);
		answer = new LocationTaskAnswer(location, new Date(), "demo@demo.com");
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		mContext.deleteDatabase(Database.DATABASE_NAME);
		database = null;
	}
	
	public void testTaskAnswerNormalvalues() {
		boolean isOK = database.insertLocationTaskAnswerForTask(2L, answer);
		assertTrue(isOK);
	}
}