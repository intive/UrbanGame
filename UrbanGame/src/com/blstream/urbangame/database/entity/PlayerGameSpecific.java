package com.blstream.urbangame.database.entity;

public class PlayerGameSpecific {
	
	public static final int GAME_ACTIVE = 0;
	public static final int GAME_OBSERVED = 1;
	
	private Integer rank;
	private String playerEmail;
	private Long gameID;
	private Integer state;
	private final String changes;
	
	/**
	 * use if user is participant in game
	 * 
	 * @param rank
	 * @param playerEmail
	 * @param gameID
	 * @param changes
	 */
	public PlayerGameSpecific(Integer rank, String playerEmail, Long gameID, String changes) {
		this.rank = rank;
		this.playerEmail = playerEmail;
		this.gameID = gameID;
		this.changes = changes;
		this.setState(GAME_ACTIVE);
	}
	
	/**
	 * use if user observes game
	 * 
	 * @param playerEmail
	 * @param gameID
	 * @param changes
	 */
	public PlayerGameSpecific(String playerEmail, Long gameID, String changes) {
		this.rank = null;
		this.playerEmail = playerEmail;
		this.gameID = gameID;
		this.changes = changes;
		this.setState(GAME_OBSERVED);
	}
	
	/**
	 * should be used only by database
	 * 
	 * @param rank
	 * @param playerEmail
	 * @param gameID
	 * @param state
	 * @param changes
	 */
	public PlayerGameSpecific(Integer rank, String playerEmail, Long gameID, Integer state, String changes) {
		this.rank = rank;
		this.playerEmail = playerEmail;
		this.gameID = gameID;
		this.state = state;
		this.changes = changes;
	}
	
	/**
	 * @return the rank
	 */
	public Integer getRank() {
		return rank;
	}
	
	/**
	 * @param rank the rank to set
	 */
	public void setRank(Integer rank) {
		this.rank = rank;
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
	 * @return the gameID
	 */
	public Long getGameID() {
		return gameID;
	}
	
	/**
	 * @param gameID the gameID to set
	 */
	public void setGameID(Long gameID) {
		this.gameID = gameID;
	}
	
	/**
	 * @return the state
	 */
	public Integer getState() {
		return state;
	}
	
	/**
	 * @param state the state to set
	 */
	public void setState(Integer state) {
		this.state = state;
	}

	/**
	 * @return the changes
	 */
	public String getChanges() {
		return changes;
	}
}
