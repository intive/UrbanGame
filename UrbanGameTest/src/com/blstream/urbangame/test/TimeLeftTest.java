package com.blstream.urbangame.test;

import java.util.GregorianCalendar;

import android.test.AndroidTestCase;

import com.blstream.urbangame.TimeLeft;

public class TimeLeftTest extends AndroidTestCase {
	
	private final GregorianCalendar startDate = new GregorianCalendar(2012, 12, 12);
	private final GregorianCalendar endDate = new GregorianCalendar(2013, 4, 21);
	private final GregorianCalendar currentDate1 = new GregorianCalendar(2013, 5, 2);
	private final GregorianCalendar currentDate2 = new GregorianCalendar(2013, 4, 8);
	
	private final static String ENDED = "ended";
	private final static String TIME_LEFT = "1 week, 6 days left";
	
	public void testTimeLeft() {
		
		assertEquals(ENDED,
			TimeLeft.timeLeft(startDate.getTime(), endDate.getTime(), currentDate1.getTime(), getContext()));
		assertEquals(TIME_LEFT,
			TimeLeft.timeLeft(startDate.getTime(), endDate.getTime(), currentDate2.getTime(), getContext()));
	}
}
