package com.blstream.urbangame.date;

import java.util.Date;

import android.content.res.Resources;
import android.text.format.DateFormat;

import com.blstream.urbangame.R;

public class TimeLeftBuilder {
	private final String COMMA = ", ";
	private final String SPACE = " ";
	
	private final Resources resources;
	private final DateUtil dateUtil;
	private final StringBuilder stringBuilder;
	
	private String leftTime;
	
	public final int weeks;
	public final int days;
	public final int hours;
	public final int minutes;
	public final int seconds;
	
	private boolean timeToStart = false;
	private int howManyUnits;
	
	public TimeLeftBuilder(Resources resources, Date endDate) {
		this.resources = resources;
		this.dateUtil = new DateUtil(endDate);
		this.stringBuilder = new StringBuilder();
		
		weeks = dateUtil.getDifferenceInWeeks();
		days = dateUtil.getDifferenceInDays();
		hours = dateUtil.getDifferenceInHours();
		minutes = dateUtil.getDifferenceInMinutes();
		seconds = dateUtil.getDifferenceInSeconds();
		
		this.howManyUnits = 2;
		
		buildLeftTimeString();
	}
	
	public TimeLeftBuilder(Resources resources, Date startDate, Date endDate) {
		this.resources = resources;
		Date currentDate = new Date();
		if (currentDate.before(startDate)) {
			this.dateUtil = new DateUtil(startDate);
			timeToStart = true;
		}
		else {
			this.dateUtil = new DateUtil(endDate);
		}
		this.stringBuilder = new StringBuilder();
		
		weeks = dateUtil.getDifferenceInWeeks();
		days = dateUtil.getDifferenceInDays();
		hours = dateUtil.getDifferenceInHours();
		minutes = dateUtil.getDifferenceInMinutes();
		seconds = dateUtil.getDifferenceInSeconds();
		
		this.howManyUnits = 2;
		
		buildLeftTimeString();
	}
	
	private void buildLeftTimeString() {
		appendValue(R.plurals.numberOfWeeks, weeks);
		appendValue(R.plurals.numberOfDays, days);
		appendValue(R.plurals.numberOfHours, hours);
		appendValue(R.plurals.numberOfMinutes, minutes);
		appendValue(R.plurals.numberOfSeconds, seconds);
		
		appendEnding();
		
		leftTime = stringBuilder.toString();
	}
	
	private void appendValue(int plurarStringResourceId, int howMany) {
		if (howManyUnits > 0) {
			if (howMany > 0) {
				if (stringBuilder.length() > 0) {
					stringBuilder.append(COMMA);
				}
				String appender = resources.getQuantityString(plurarStringResourceId, howMany, howMany);
				stringBuilder.append(appender);
				howManyUnits--;
			}
			else {
				removeLastCommaIfExists();
			}
		}
		else {
			removeLastCommaIfExists();
		}
	}
	
	private void removeLastCommaIfExists() {
		int length = stringBuilder.length();
		int lastIndexOfComma = stringBuilder.lastIndexOf(COMMA);
		
		if (lastIndexOfComma != -1 && lastIndexOfComma == (length - 2)) {
			stringBuilder.delete(lastIndexOfComma, length);
		}
	}
	
	private void appendEnding() {
		if (stringBuilder.length() == 0) {
			stringBuilder.append(resources.getString(R.string.label_ended));
			CharSequence parsedEndDate = DateFormat.format(" MMM d, h:mm a", dateUtil.getEndDate());
			stringBuilder.append(parsedEndDate);
		}
		else {
			stringBuilder.append(SPACE);
			if (timeToStart) {
				stringBuilder.append(resources.getString(R.string.label_to_start));
			}
			else {
				stringBuilder.append(resources.getString(R.string.label_to_end));
			}
		}
	}
	
	public String getLeftTime() {
		return leftTime;
	}
	
	public String getLeftTimeForDebug() {
		return dateUtil.getDateDifferenceString();
	}
}