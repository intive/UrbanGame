package com.blstream.urbangame.database.entity;

import java.util.Date;

import android.location.Location;

public class LocationTaskAnswer {
	
	private Location answeredLocation;
	private Date answerDate;
	private String userEmail;
	
	/**
	 * @param answeredLocation
	 * @param answerDate
	 */
	public LocationTaskAnswer(Location answeredLocation, Date answerDate, String userEmail) {
		this.answeredLocation = answeredLocation;
		this.answerDate = answerDate;
		this.userEmail = userEmail;
	}
	
	public LocationTaskAnswer() {
		answeredLocation = null;
		answerDate = null;
		userEmail = null;
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
	
	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}
	
	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
