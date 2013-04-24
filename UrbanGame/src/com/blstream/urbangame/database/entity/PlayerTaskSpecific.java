package com.blstream.urbangame.database.entity;

public class PlayerTaskSpecific {
	
	public static final int ACTIVE = 0;
	public static final int INACTIVE = 1;
	public static final int FINISHED = 2;
	public static final int CANCELED = 3;
	
	private String playerEmail;
	private Long taskID;
	private Integer points;
	private Boolean isFinishedByUser;
	private Boolean areChanges;
	private Boolean wasHidden; //true if it was a hidden task
	
	/**
	 * @param playerEmail
	 * @param taskID
	 * @param points
	 * @param isFinishedByUser
	 * @param areChanges
	 * @param wasHidden
	 */
	public PlayerTaskSpecific(String playerEmail, Long taskID, Integer points, Boolean isFinishedByUser,
		Boolean areChanges, Boolean wasHidden) {
		this.playerEmail = playerEmail;
		this.taskID = taskID;
		this.points = points;
		this.isFinishedByUser = isFinishedByUser;
		this.areChanges = areChanges;
		this.wasHidden = wasHidden;
	}
	
	/**
	 * @return the playerEmail
	 */
	public String getPlayerEmail() {
		return playerEmail;
	}
	
	/**
	 * @param playerEmail the playerEmail to set
	 */
	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}
	
	/**
	 * @return the taskID
	 */
	public Long getTaskID() {
		return taskID;
	}
	
	/**
	 * @param taskID the taskID to set
	 */
	public void setTaskID(Long taskID) {
		this.taskID = taskID;
	}
	
	/**
	 * @return the points
	 */
	public Integer getPoints() {
		return points;
	}
	
	/**
	 * @param points the points to set
	 */
	public void setPoints(Integer points) {
		this.points = points;
	}
	
	/**
	 * @return the isFinishedByUser
	 */
	public Boolean getIsFinishedByUser() {
		return isFinishedByUser;
	}
	
	/**
	 * @param isFinishedByUser the isFinishedByUser to set
	 */
	public void setIsFinishedByUser(Boolean isFinishedByUser) {
		this.isFinishedByUser = isFinishedByUser;
	}
	
	/**
	 * @return the areChanges
	 */
	public Boolean getAreChanges() {
		return areChanges;
	}
	
	/**
	 * @param areChanges the areChanges to set
	 */
	public void setAreChanges(Boolean areChanges) {
		this.areChanges = areChanges;
	}
	
	/**
	 * @return the wasHidden
	 */
	public Boolean getWasHidden() {
		return wasHidden;
	}
	
	/**
	 * @param wasHidden the wasHidden to set
	 */
	public void setWasHidden(Boolean wasHidden) {
		this.wasHidden = wasHidden;
	}
}
