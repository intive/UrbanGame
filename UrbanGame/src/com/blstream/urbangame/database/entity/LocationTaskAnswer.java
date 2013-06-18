package com.blstream.urbangame.database.entity;

import java.util.Date;

import android.location.Location;

public class LocationTaskAnswer {
	
	private Location answeredLocation;
	private Date answerDate;
	
	/**
	 * @param answeredLocation
	 * @param answerDate
	 */
	public LocationTaskAnswer(Location answeredLocation, Date answerDate) {
		this.answeredLocation = answeredLocation;
		this.answerDate = answerDate;
	}
	
	public LocationTaskAnswer() {
		answeredLocation = null;
	}
	
	/**
	 * @return answered location
	 */
	public Location getAnsweredLocation() {
		return answeredLocation;
	}
	
	/**
	 * @param answered location
	 */
	public void setAnsweredLocation(Location answeredLocation) {
		this.answeredLocation = answeredLocation;
	}
	
	/**
	 * @return the answerDate
	 */
	public Date getAnswerDate() {
		return answerDate;
	}
	
	/**
	 * @param answerDate the answerDate to set
	 */
	public void setAnswerDate(Date answerDate) {
		this.answerDate = answerDate;
	}
}
