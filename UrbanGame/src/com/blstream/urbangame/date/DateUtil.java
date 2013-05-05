package com.blstream.urbangame.date;

import java.util.Date;

/**
 * Simple class used to easily find differences between current and provided
 * date in years, months, days, hours, minutes and seconds.
 * 
 */
public class DateUtil {
	private static final int MINUTE = 60 * 1000;
	private static final int HOUR = 60 * MINUTE;
	private static final int DAY = 24 * HOUR;
	private static final int WEEK = 7;
	
	private Date dateNow;
	private Date endDate;
	
	private int weeks;
	private int days;
	private int hours;
	private int minutes;
	
	public DateUtil(Date endDate) {
		this.dateNow = new Date();
		this.endDate = endDate;
		calculateDifferences();
	}
	
	private void calculateDifferences() {
		long diff = endDate.getTime() - dateNow.getTime();
		
		minutes = (int) (diff / MINUTE % 60);	// minutes left, which are not included in full hours
		hours = (int) (diff / HOUR % 24);		// hours left, which are not included in full days
		days = (int) (diff / DAY);
		weeks = days / WEEK;
	}
	
	public int getDifferenceInWeeks() {
		return weeks;
	}
	
	public int getDifferenceInDays() {
		return days;
	}
	
	public int getDifferenceInHours() {
		return hours;
	}
	
	public int getDifferenceInMinutes() {
		return minutes;
	}
	
	public Date getDateNow() {
		return dateNow;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public String getDateNowString() {
		return dateNow.toString();
	}
	
	public String getEndDateString() {
		return endDate.toString();
	}
	
	// formatter:off
	public String getDateDifferenceString() {
		int differenceInWeeks = getDifferenceInWeeks();
		int differenceInDays = getDifferenceInDays();
		int differenceInHours = getDifferenceInHours();
		int differenceInMinutes = getDifferenceInMinutes();
		
		return String.format(
			"Time left: " +
							(differenceInWeeks	   != 0 ? "%d weeks " 	 : "") +
							(differenceInDays 	   != 0 ? "%d days " 	 : "") +
							(differenceInHours 	   != 0 ? "%d hours " 	 : "") +
							(differenceInMinutes   != 0 ? "%d minutes."  : ""),
							 differenceInWeeks,
							 differenceInDays,
							 differenceInHours,
							 differenceInMinutes);
	}
	// formatter:on
}