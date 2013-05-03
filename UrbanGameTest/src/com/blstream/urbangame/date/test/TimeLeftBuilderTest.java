package com.blstream.urbangame.date.test;

import java.util.Calendar;
import java.util.Date;

import android.content.res.Resources;
import android.test.AndroidTestCase;

import com.blstream.urbangame.date.TimeLeftBuilder;

public class TimeLeftBuilderTest extends AndroidTestCase {
	private static final int OFFSET = 10;
	private static final int HOURS_IN_DAY = 24;
	private static final int TIME_LAPSE = 1;
	
	private Calendar c;
	private int currentYear;
	private int currentMonth;
	private int currentDay;
	private int currentHour;
	private int currentMinute;
	
	private int futureDay;
	private int futureHour;
	private int pastDay;
	private int pastHour;
	
	private Resources resources;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		c = Calendar.getInstance();
		
		currentYear = c.get(Calendar.YEAR);
		currentMonth = c.get(Calendar.MONTH);
		currentDay = c.get(Calendar.DAY_OF_MONTH);
		currentHour = c.get(Calendar.HOUR_OF_DAY);
		
		/*
		 *  We need to delay time not to rely on System.currentTimeMillis()
		 *  Bigger delay ensures us in independent tests
		 *  It could be used also Thread.sleep(1000 * 60), but it'll cause
		 *  tests will run longer.
		 */
		currentMinute = c.get(Calendar.MINUTE) - TIME_LAPSE;
		
		pastDay = currentDay - OFFSET;
		pastHour = currentHour - OFFSET;
		futureDay = currentDay + OFFSET;
		futureHour = currentHour + OFFSET;
		
		resources = getContext().getResources();
	}
	
	// formatter:off
		/*
		 * 		-1 <- past
		 *  	 0 <- current
		 * 		+1 <- future
		 * 
		 * 	day		|   hour
		 * 	 -1		|  	  -1	
		 * 	 -1		|  	   0
		 * 	  0		|  	  -1
		 * 	  0		|  	   0	// No difference
		 * 
		 * 
		 * 	  1		|  	   0
		 * 	  0		|  	   1	// We should subtract 1 from OFFSET because current hour's started
		 * 	  1		|  	   1	// We should subtract 1 from OFFSET because current day's started
		 * 						// We should also subtract 1 from 24 hours because current hour started
		 * 	  1		|  	  -1
		 * 	 -1		|  	   1
		 */
	// formatter:on
	
	public void testPastDayPastHour() {
		assertEquals(-OFFSET, getLeftDays(pastDay, pastHour));
		assertEquals(-OFFSET, getLeftHours(pastDay, pastHour));
	}
	
	public void testPastDayCurrentHour() {
		assertEquals(-OFFSET, getLeftDays(pastDay, currentHour));
		assertEquals(0, getLeftHours(pastDay, currentHour));
	}
	
	public void testCurrentDayPastHour() {
		assertEquals(0, getLeftDays(currentDay, pastHour));
		assertEquals(-OFFSET, getLeftHours(currentDay, pastHour));
	}
	
	public void testCurrentDayCurrentHour() {
		assertEquals(0, getLeftDays(currentDay, currentHour));
		assertEquals(0, getLeftHours(currentDay, currentHour));
	}
	
	public void testCurrentDayFutureHour() {
		assertEquals(0, getLeftDays(currentDay, futureHour));
		assertEquals(OFFSET - TIME_LAPSE, getLeftHours(currentDay, futureHour));
	}
	
	public void testFutureDayCurrentHour() {
		assertEquals(OFFSET - TIME_LAPSE, getLeftDays(futureDay, currentHour));
		assertEquals(HOURS_IN_DAY - TIME_LAPSE, getLeftHours(futureDay, currentHour));
	}
	
	public void testFutureDayFutureHour() {
		assertEquals(OFFSET, getLeftDays(futureDay, futureHour));
		assertEquals(OFFSET - TIME_LAPSE, getLeftHours(futureDay, futureHour));
	}
	
	public void testFutureDayPastHour() {
		assertEquals(OFFSET - TIME_LAPSE, getLeftDays(futureDay, pastHour));
		assertEquals(HOURS_IN_DAY - TIME_LAPSE - OFFSET, getLeftHours(futureDay, pastHour));
	}
	
	public void testPastDayFutureHour() {
		assertEquals(TIME_LAPSE - OFFSET, getLeftDays(pastDay, futureHour));
		assertEquals(OFFSET - HOURS_IN_DAY, getLeftHours(pastDay, futureHour));
	}
	
	private int getLeftDays(int days, int hours) {
		return getLeftTimeFrom(days, hours).days;
	}
	
	private int getLeftHours(int days, int hours) {
		return getLeftTimeFrom(days, hours).hours;
	}
	
	private TimeLeftBuilder getLeftTimeFrom(int day, int hourOfDay) {
		return new TimeLeftBuilder(resources, getDate(day, hourOfDay));
	}
	
	private Date getDate(int day, int hourOfDay) {
		c.set(currentYear, currentMonth, day, hourOfDay, currentMinute);
		return c.getTime();
	}
}