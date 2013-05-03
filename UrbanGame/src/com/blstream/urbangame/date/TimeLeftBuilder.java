package com.blstream.urbangame.date;

import java.util.Date;

import android.content.res.Resources;

import com.blstream.urbangame.R;

public class TimeLeftBuilder {
	private final String COMMA = ", ";
	private final String SPACE = " ";
	
	private Resources resources;
	private DateUtil dateUtil;
	private StringBuilder stringBuilder;
	
	private String leftTime;
	
	public final int days;
	public final int hours;
	public final int minutes;
	
	public TimeLeftBuilder(Resources resources, Date endDate) {
		this.resources = resources;
		this.dateUtil = new DateUtil(endDate);
		this.stringBuilder = new StringBuilder();
		
		days = dateUtil.getDifferenceInDays();
		hours = dateUtil.getDifferenceInHours();
		minutes = dateUtil.getDifferenceInMinutes();
		
		buildLeftTimeString();
	}
	
	private void buildLeftTimeString() {
		appendValue(R.plurals.numberOfDays, days);
		appendValue(R.plurals.numberOfHours, hours);
		appendValue(R.plurals.numberOfMinutes, minutes);
		
		appendEnding();
		
		leftTime = stringBuilder.toString();
	}
	
	private void appendValue(int plurarStringResourceId, int howMany) {
		if (howMany > 0) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append(COMMA);
			}
			String appender = resources.getQuantityString(plurarStringResourceId, howMany, howMany);
			stringBuilder.append(appender);
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
			stringBuilder.append(resources.getString(R.string.string_noMoreTime));
		}
		stringBuilder.append(SPACE);
		stringBuilder.append(resources.getString(R.string.string_left));
	}
	
	public String getLeftTime() {
		return leftTime;
	}
	
	public String getLeftTimeForDebug() {
		return dateUtil.getDateDifferenceString();
	}
}