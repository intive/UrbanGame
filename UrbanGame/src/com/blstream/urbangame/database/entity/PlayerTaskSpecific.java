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
	private String changes;
	private Integer status;
	private Boolean[] selectedAnswers;
	
	/**
	 * @param playerEmail
	 * @param taskID
	 * @param points
	 * @param isFinishedByUser
	 * @param areChanges
	 * @param wasHidden
	 * @param changes
	 * @param status
	 * @param selectedAnswers
	 */
	public PlayerTaskSpecific(String playerEmail, Long taskID, Integer points, Boolean isFinishedByUser,
		Boolean areChanges, Boolean wasHidden, String changes, Integer status, Boolean[] selectedAnswers) {
		this.playerEmail = playerEmail;
		this.taskID = taskID;
		this.points = points;
		this.isFinishedByUser = isFinishedByUser;
		this.areChanges = areChanges;
		this.wasHidden = wasHidden;
		this.changes = changes;
		this.status = status;
		this.selectedAnswers = selectedAnswers;
	}
	
	public PlayerTaskSpecific() {
		this(null, null, null, null, null, null, null, null, null);
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
	public Boolean isFinishedByUser() {
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
	
	/**
	 * @return the changes
	 */
	public String getChanges() {
		return changes;
	}
	
	/**
	 * @param changes the changes to set
	 */
	public void setChanges(String changes) {
		this.changes = changes;
	}
	
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	/**
	 * @return the selectedAnswers
	 */
	public Boolean[] getSelectedAnswers() {
		return selectedAnswers;
	}
	
	/**
	 * @param selections the selections to set
	 */
	public void setSelectedAnswers(Boolean[] selections) {
		this.selectedAnswers = selections;
	}
}
