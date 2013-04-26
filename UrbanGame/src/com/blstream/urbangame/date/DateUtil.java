package com.blstream.urbangame.date;

import java.util.Date;

/**
 * Simple class used to easily find differences between current and provided
 * date in years, months, days, hours, minutes and seconds.
 * 
 */
public class DateUtil {
	private Date dateNow;
	private Date endDate;
	
	private int days;
	private int hours;
	private int minutes;
	
	public DateUtil(Date endDate) {
		this.dateNow = new Date();
		this.endDate = endDate;
		calculateDifferences();
	}
	
	private void calculateDifferences() {
		long diff = dateNow.getTime() - endDate.getTime();
		
		minutes = (int) (diff / (60 * 1000) % 60);
		hours = (int) (diff / (60 * 60 * 1000) % 24);
		days = (int) (diff / (24 * 60 * 60 * 1000));
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
		int differenceInDays = getDifferenceInDays();
		int differenceInHours = getDifferenceInHours();
		int differenceInMinutes = getDifferenceInMinutes();
		
		return String.format(
			"Time left: " +
							(differenceInDays 	   != 0 ? "%d days " 	 : "") +
							(differenceInHours 	   != 0 ? "%d hours " 	 : "") +
							(differenceInMinutes   != 0 ? "%d minutes."  : ""),
							 differenceInDays, 
							 differenceInHours,
							 differenceInMinutes);
	}
	// formatter:on
}