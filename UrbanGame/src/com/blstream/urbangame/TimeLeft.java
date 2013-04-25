package com.blstream.urbangame;

import java.util.Date;

import android.content.Context;

/**
 * Class used to compute how much time is left to the given date.
 */
public class TimeLeft {
	
	private static final long SECONDS_IN_MINUTE = 60;
	private static final long MINUTES_IN_HOUR = 60;
	private static final long HOURS_IN_DAY = 24;
	private static final long DAYS_IN_WEEK = 7;
	private static final long MILLISECONDS_IN_SECOND = 1000;
	private static final long MILLISECONDS_IN_MINUTE = MILLISECONDS_IN_SECOND * SECONDS_IN_MINUTE;
	private static final long MILLISECONDS_IN_HOUR = MILLISECONDS_IN_MINUTE * MINUTES_IN_HOUR;
	private static final long MILLISECONDS_IN_DAY = MILLISECONDS_IN_HOUR * HOURS_IN_DAY;
	private static final long MILLISECONDS_IN_WEEK = MILLISECONDS_IN_DAY * DAYS_IN_WEEK;
	private static final int SECONDS = 0;
	private static final int MINUTES = 1;
	private static final int HOURS = 2;
	private static final int DAYS = 3;
	private static final int WEEKS = 4;
	
	/**
	 * Calculates if the currentDate is before startDate, after endDate or if it
	 * is between those two it evaluates how much time is left until endDate and
	 * returns that time as String object.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param currentDate
	 * @param context
	 * @return
	 */
	static public String timeLeft(Date startDate, Date endDate, Date currentDate, Context context) {
		
		if (currentDate.before(startDate)) return startDate.toString();
		if (currentDate.after(endDate)) return context.getString(R.string.ended);
		return timeLeftToEnd(endDate, context, currentDate);
	}
	
	private static String timeLeftToEnd(Date endDate, Context context, Date currentDate) {
		
		StringBuffer time = new StringBuffer("");
		long millisecondsLeft = endDate.getTime() - currentDate.getTime();
		
		if (millisecondsLeft / MILLISECONDS_IN_WEEK > 0) {
			long weeks = millisecondsLeft / MILLISECONDS_IN_WEEK;
			long days = millisecondsLeft / MILLISECONDS_IN_DAY % DAYS_IN_WEEK;
			
			time.append(timeToString(weeks, WEEKS, context));
			time.append(", ");
			time.append(timeToString(days, DAYS, context));
			time.append(" ");
			time.append(context.getString(R.string.left));
			
			return time.toString();
		}
		if (millisecondsLeft / MILLISECONDS_IN_DAY > 0) {
			long days = millisecondsLeft / MILLISECONDS_IN_DAY % DAYS_IN_WEEK;
			long hours = millisecondsLeft / MILLISECONDS_IN_HOUR % HOURS_IN_DAY;
			
			time.append(timeToString(days, DAYS, context));
			time.append(", ");
			time.append(timeToString(hours, HOURS, context));
			time.append(" ");
			time.append(context.getString(R.string.left));
			
			return time.toString();
		}
		if (millisecondsLeft / MILLISECONDS_IN_HOUR > 0) {
			long hours = millisecondsLeft / MILLISECONDS_IN_HOUR % HOURS_IN_DAY;
			long minutes = millisecondsLeft / MILLISECONDS_IN_MINUTE % MINUTES_IN_HOUR;
			
			time.append(timeToString(hours, HOURS, context));
			time.append(", ");
			time.append(timeToString(minutes, MINUTES, context));
			time.append(" ");
			time.append(context.getString(R.string.left));
			
			return time.toString();
		}
		if (millisecondsLeft / MILLISECONDS_IN_MINUTE > 0) {
			long minutes = millisecondsLeft / MILLISECONDS_IN_MINUTE % MINUTES_IN_HOUR;
			long seconds = millisecondsLeft / MILLISECONDS_IN_SECOND % SECONDS_IN_MINUTE;
			
			time.append(timeToString(minutes, MINUTES, context));
			time.append(", ");
			time.append(timeToString(seconds, SECONDS, context));
			time.append(" ");
			time.append(context.getString(R.string.left));
			
			return time.toString();
		}
		
		time.append(context.getString(R.string.ended));
		return time.toString();
	}
	
	private static String timeToString(long number, int unit, Context context) {
		
		StringBuffer time = new StringBuffer(Long.toString(number) + " ");
		
		if (number == 1) {
			switch (unit) {
				case SECONDS:
					time.append(context.getString(R.string.second));
					break;
				case MINUTES:
					time.append(context.getString(R.string.minute));
					break;
				case HOURS:
					time.append(context.getString(R.string.hour));
					break;
				case DAYS:
					time.append(context.getString(R.string.day));
					break;
				case WEEKS:
					time.append(context.getString(R.string.week));
					break;
			}
		}
		if (number > 1) {
			switch (unit) {
				case SECONDS:
					time.append(context.getString(R.string.seconds));
					break;
				case MINUTES:
					time.append(context.getString(R.string.minutes));
					break;
				case HOURS:
					time.append(context.getString(R.string.hours));
					break;
				case DAYS:
					time.append(context.getString(R.string.days));
					break;
				case WEEKS:
					time.append(context.getString(R.string.weeks));
					break;
			}
		}
		
		return time.toString();
	}
}
