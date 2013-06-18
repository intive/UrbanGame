package com.blstream.urbangame.database.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class UrbanGame {
	
	// holds very basic info about game(list view info) - that gives less code,
	// but little less clarity
	private UrbanGameShortInfo primaryInfo;
	
	private Double gameVersion;
	private String winningStrategy;
	private Integer difficulty;
	private String prizesInfo;
	private String description;
	private String comments;
	
	/** sets everything to null */
	public UrbanGame() {
		gameVersion = null;
		winningStrategy = null;
		difficulty = null;
		primaryInfo = new UrbanGameShortInfo();
		prizesInfo = null;
		description = null;
		comments = null;
	}
	
	/** @param iD
	 * @param gameVersion
	 * @param title
	 * @param operatorName
	 * @param winningStrategy
	 * @param players
	 * @param maxPlayers
	 * @param startDate
	 * @param endDate
	 * @param difficulty
	 * @param reward
	 * @param prizesInfo
	 * @param description
	 * @param gamelogoBase64
	 * @param operatorlogoBase64
	 * @param comments
	 * @param location
	 * @param detailsLink */
	public UrbanGame(Long iD, Double gameVersion, String title, String operatorName, String winningStrategy,
		Integer players, Integer maxPlayers, Date startDate, Date endDate, Integer difficulty, Boolean reward,
		String prizesInfo, String description, String gameLogoBase64, String operatorLogoBase64, String comments,
		String location, String detailsLink) {
		this(gameVersion, title, operatorName, winningStrategy, players, maxPlayers, startDate, endDate, difficulty,
			reward, prizesInfo, description, gameLogoBase64, operatorLogoBase64, comments, location, detailsLink);
		primaryInfo.setID(iD);
	}
	
	/** @param gameVersion
	 * @param title
	 * @param operatorName
	 * @param winningStrategy
	 * @param players
	 * @param maxPlayers
	 * @param startDate
	 * @param endDate
	 * @param difficulty
	 * @param reward
	 * @param prizesInfo
	 * @param description
	 * @param gamelogoBase64
	 * @param operatorlogoBase64
	 * @param comments */
	public UrbanGame(Double gameVersion, String title, String operatorName, String winningStrategy, Integer players,
		Integer maxPlayers, Date startDate, Date endDate, Integer difficulty, Boolean reward, String prizesInfo,
		String description, String gameLogoBase64, String operatorLogoBase64, String comments, String location,
		String detailsLink) {
		this.gameVersion = gameVersion;
		primaryInfo = new UrbanGameShortInfo();
		primaryInfo.setTitle(title);
		primaryInfo.setOperatorName(operatorName);
		this.winningStrategy = winningStrategy;
		primaryInfo.setPlayers(players);
		primaryInfo.setMaxPlayers(maxPlayers);
		primaryInfo.setStartDate(startDate);
		primaryInfo.setEndDate(endDate);
		this.difficulty = difficulty;
		primaryInfo.setReward(reward);
		this.prizesInfo = prizesInfo;
		this.description = description;
		primaryInfo.setGameLogoBase64(gameLogoBase64);
		primaryInfo.setOperatorLogoBase64(operatorLogoBase64);
		this.comments = comments;
		primaryInfo.setLocation(location);
		primaryInfo.setDetailsLink(detailsLink);
	}
	
	/** @return the iD */
	public Long getID() {
		return primaryInfo.getID();
	}
	
	/** @param iD the iD to set */
	public void setID(Long iD) {
		primaryInfo.setID(iD);
	}
	
	/** @return the gameVersion */
	public Double getGameVersion() {
		return gameVersion;
	}
	
	/** @param gameVersion the gameVersion to set */
	public void setGameVersion(Double gameVersion) {
		this.gameVersion = gameVersion;
	}
	
	/** @return the title */
	public String getTitle() {
		return primaryInfo.getTitle();
	}
	
	/** @param title the title to set */
	public void setTitle(String title) {
		primaryInfo.setTitle(title);
	}
	
	/** @return the operatorName */
	public String getOperatorName() {
		return primaryInfo.getOperatorName();
	}
	
	/** @param operatorName the operatorName to set */
	public void setOperatorName(String operatorName) {
		primaryInfo.setOperatorName(operatorName);
	}
	
	/** @return the winningStrategy */
	public String getWinningStrategy() {
		return winningStrategy;
	}
	
	/** @param winningStrategy the winningStrategy to set */
	public void setWinningStrategy(String winningStrategy) {
		this.winningStrategy = winningStrategy;
	}
	
	/** @return the players */
	public Integer getPlayers() {
		return primaryInfo.getPlayers();
	}
	
	/** @param players the players to set */
	public void setPlayers(Integer players) {
		primaryInfo.setPlayers(players);
	}
	
	/** @return the maxPlayers */
	public Integer getMaxPlayers() {
		return primaryInfo.getMaxPlayers();
	}
	
	/** @param maxPlayers the maxPlayers to set */
	public void setMaxPlayers(Integer maxPlayers) {
		primaryInfo.setMaxPlayers(maxPlayers);
	}
	
	/** @return the startDate */
	public Date getStartDate() {
		return primaryInfo.getStartDate();
	}
	
	/** @param startDate the startDate to set */
	public void setStartDate(Date startDate) {
		primaryInfo.setStartDate(startDate);
	}
	
	/** @return the endDate */
	public Date getEndDate() {
		return primaryInfo.getEndDate();
	}
	
	/** @param endDate the endDate to set */
	public void setEndDate(Date endDate) {
		primaryInfo.setEndDate(endDate);
	}
	
	/** @return the difficulty */
	public Integer getDifficulty() {
		return difficulty;
	}
	
	/** @param difficulty the difficulty to set */
	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}
	
	/** @return the reward */
	public Boolean getReward() {
		return primaryInfo.getReward();
	}
	
	/** @param reward the reward to set */
	public void setReward(Boolean reward) {
		primaryInfo.setReward(reward);
	}
	
	/** @return the prizesInfo */
	public String getPrizesInfo() {
		return prizesInfo;
	}
	
	/** @param prizesInfo the prizesInfo to set */
	public void setPrizesInfo(String prizesInfo) {
		this.prizesInfo = prizesInfo;
	}
	
	/** @return the description */
	public String getDescription() {
		return description;
	}
	
	/** @param description the description to set */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/** @return the logoBase64 */
	public String getGameLogoBase64() {
		return primaryInfo.getGameLogoBase64();
	}
	
	/** @param logoBase64 the logoBase64 to set */
	public void setGameLogoBase64(String logoBase64) {
		primaryInfo.setGameLogoBase64(logoBase64);
	}
	
	/** @return the logoBase64 */
	public String getOperatorLogoBase64() {
		return primaryInfo.getOperatorLogoBase64();
	}
	
	/** @param logoBase64 the logoBase64 to set */
	public void setOperatorLogoBase64(String logoBase64) {
		primaryInfo.setOperatorLogoBase64(logoBase64);
	}
	
	/** @param res - can be obtained by getResource() invoked in Activity
	 * @return the logoDrawable */
	public Drawable getGameLogoDrawable(Resources res) {
		return primaryInfo.getGameLogoDrawable(res);
	}
	
	/** @param logoDrawable the logoDrawable to set */
	public void setGameLogoDrawable(Drawable logoDrawable) {
		primaryInfo.setGameLogoDrawable(logoDrawable);
	}
	
	/** @param res - can be obtained by getResource() invoked in Activity
	 * @return the logoDrawable */
	public Drawable getOperatorLogoDrawable(Resources res) {
		return primaryInfo.getOperatorLogoDrawable(res);
	}
	
	/** @param logoDrawable the logoDrawable to set */
	public void setOperatorLogoDrawable(Drawable logoDrawable) {
		primaryInfo.setOperatorLogoDrawable(logoDrawable);
	}
	
	/** @return the primaryInfo */
	public UrbanGameShortInfo getPrimaryInfo() {
		return primaryInfo;
	}
	
	/** @param primaryInfo the primaryInfo to set */
	public void setPrimaryInfo(UrbanGameShortInfo primaryInfo) {
		this.primaryInfo = primaryInfo;
	}
	
	/** @return the comments */
	public String getComments() {
		return comments;
	}
	
	/** @param comments the comments to set */
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	/** @return the location */
	public String getLocation() {
		return primaryInfo.getLocation();
	}
	
	/** @param location the location to set */
	public void setLocation(String location) {
		primaryInfo.setLocation(location);
	}
	
	/** @return the detailsLink */
	public String getDetailsLink() {
		return primaryInfo.getDetailsLink();
	}
	
	/** @param detailsLink the detailsLink to set */
	public void setDetailsLink(String detailsLink) {
		primaryInfo.setDetailsLink(detailsLink);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == this) return true;
		if (object == null || object.getClass() != this.getClass()) return false;
		
		UrbanGame urbanGame = (UrbanGame) object;
		
		if (getPrimaryInfo().equals(urbanGame.getPrimaryInfo()) && getComments().equals(urbanGame.getComments())
			&& getDescription().equals(urbanGame.getDescription()) && getDifficulty().equals(urbanGame.getDifficulty())
			&& getGameVersion().equals(urbanGame.getGameVersion()) && getPrizesInfo().equals(urbanGame.getPrizesInfo())
			&& getWinningStrategy().equals(urbanGame.getWinningStrategy())) return true;
		
		return false;
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(getPrimaryInfo());
		builder.append(getComments());
		builder.append(getDescription());
		builder.append(getDifficulty());
		builder.append(getGameVersion());
		builder.append(getPrizesInfo());
		builder.append(getWinningStrategy());
		return builder.toHashCode();
	}
	
}
