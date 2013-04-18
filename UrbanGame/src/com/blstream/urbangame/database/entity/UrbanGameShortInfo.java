package com.blstream.urbangame.database.entity;

import java.util.Date;

import com.blstream.urbangame.database.helper.Base64ImageCoder;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class UrbanGameShortInfo {

	private Long ID;
	private String title;
	private String operatorName;
	private Integer players;
	private Integer maxPlayers;
	private Date startDate;
	private Date endDate;
	private Boolean reward;
	private String gameLogoBase64;
	private Drawable gameLogoDrawable;
	private String operatorLogoBase64;
	private Drawable operatorLogoDrawable;
	private String location;
	private String detailsLink;

	/**
	 * @param title
	 * @param operatorName
	 * @param players
	 * @param maxPlayers
	 * @param startDate
	 * @param endDate
	 * @param reward
	 * @param location
	 * @param logoBase64
	 *            - encoded in base 64 format (it comes in web server response
	 * @param detailsLink
	 */
	public UrbanGameShortInfo(String title, String operatorName,
			Integer players, Integer maxPlayers, Date startDate, Date endDate,
			Boolean reward, String location, String gameLogoBase64,
			String operatorLogoBase64, String detailsLink) {
		this.title = title;
		this.operatorName = operatorName;
		this.players = players;
		this.maxPlayers = maxPlayers;
		this.startDate = startDate;
		this.endDate = endDate;
		this.setReward(reward);
		this.location = location;
		this.gameLogoBase64 = gameLogoBase64;
		this.operatorLogoBase64 = operatorLogoBase64;
		this.detailsLink = detailsLink;
	}

	/**
	 * should be used for creation only by database
	 * 
	 * @param id
	 * @param title
	 * @param operatorName
	 * @param players
	 * @param maxPlayers
	 * @param startDate
	 * @param endDate
	 * @param reward
	 * @param location
	 * @param gamelogoBase64
	 *            - encoded in base 64 format (it comes in web server response
	 * @param operatorlogoBase64
	 *            - encoded in base 64 format (it comes in web server response
	 * @param detailsLink
	 */
	public UrbanGameShortInfo(Long ID, String title, String operatorName,
			Integer players, Integer maxPlayers, Date startDate, Date endDate,
			Boolean reward, String location, String gameLogoBase64,
			String operatorLogoBase64, String detailsLink) {
		this(title, operatorName, players, maxPlayers, startDate, endDate,
				reward, location, gameLogoBase64, operatorLogoBase64,
				detailsLink);
		this.ID = ID;
	}

	/**
	 * @deprecated - I think it should not be used by outside class.. delete if
	 *             not needed.
	 * 
	 * @param title
	 * @param operatorName
	 * @param players
	 * @param maxPlayers
	 * @param startDate
	 * @param endDate
	 * @param logoDrawable
	 *            - drawable format
	 */
	@Deprecated
	public UrbanGameShortInfo(String title, String operatorName,
			Integer players, Integer maxPlayers, Date startDate, Date endDate,
			Boolean reward, String location, Drawable logoDrawable,
			Drawable operatorLogoDrawable) {
		this.title = title;
		this.operatorName = operatorName;
		this.players = players;
		this.maxPlayers = maxPlayers;
		this.startDate = startDate;
		this.endDate = endDate;
		this.setReward(reward);
		this.location = location;
		this.gameLogoDrawable = logoDrawable;
		this.operatorLogoDrawable = operatorLogoDrawable;
	}

	/**
	 * sets everything to null
	 */
	public UrbanGameShortInfo() {
		this(null, null, null, null, null, null, null, null, null, null, null,
				null);
	}

	/**
	 * @return the iD
	 */
	public Long getID() {
		return ID;
	}

	/**
	 * @param iD
	 *            the iD to set
	 */
	public void setID(Long iD) {
		ID = iD;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the operatorName
	 */
	public String getOperatorName() {
		return operatorName;
	}

	/**
	 * @param operatorName
	 *            the operatorName to set
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/**
	 * @return the players
	 */
	public Integer getPlayers() {
		return players;
	}

	/**
	 * @param players
	 *            the players to set
	 */
	public void setPlayers(Integer players) {
		this.players = players;
	}

	/**
	 * @return the maxPlayers
	 */
	public Integer getMaxPlayers() {
		return maxPlayers;
	}

	/**
	 * @param maxPlayers
	 *            the maxPlayers to set
	 */
	public void setMaxPlayers(Integer maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the reward
	 */
	public Boolean getReward() {
		return reward;
	}

	/**
	 * @param reward
	 *            the reward to set
	 */
	public void setReward(Boolean reward) {
		this.reward = reward;
	}

	/**
	 * @return the logoBase64
	 */
	public String getGameLogoBase64() {
		return gameLogoBase64;
	}

	/**
	 * @param logoBase64
	 *            the logoBase64 to set
	 */
	public void setGameLogoBase64(String logoBase64) {
		this.gameLogoBase64 = logoBase64;
	}

	/**
	 * @param res
	 *            - can be obtained by getResources() from Activity
	 * @return the logoDrawable
	 */
	public Drawable getGameLogoDrawable(Resources res) {
		if (gameLogoDrawable == null && gameLogoBase64 != null) {
			gameLogoDrawable = Base64ImageCoder
					.decodeImage(gameLogoBase64, res);
		}
		return gameLogoDrawable;
	}

	/**
	 * @param logoDrawable
	 *            the logoDrawable to set
	 */
	public void setGameLogoDrawable(Drawable logoDrawable) {
		this.gameLogoDrawable = logoDrawable;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the detailsLink
	 */
	public String getDetailsLink() {
		return detailsLink;
	}

	/**
	 * @param detailsLink
	 *            the detailsLink to set
	 */
	public void setDetailsLink(String detailsLink) {
		this.detailsLink = detailsLink;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the logoBase64
	 */
	public String getOperatorLogoBase64() {
		return operatorLogoBase64;
	}

	/**
	 * @param logoBase64
	 *            the logoBase64 to set
	 */
	public void setOperatorLogoBase64(String logoBase64) {
		this.operatorLogoBase64 = logoBase64;
	}

	/**
	 * @param res
	 *            - can be obtained by getResources() from Activity
	 * @return the logoDrawable
	 */
	public Drawable getOperatorLogoDrawable(Resources res) {
		if (operatorLogoDrawable == null && operatorLogoBase64 != null) {
			operatorLogoDrawable = Base64ImageCoder.decodeImage(
					operatorLogoBase64, res);
		}
		return operatorLogoDrawable;
	}

	/**
	 * @param logoDrawable
	 *            the logoDrawable to set
	 */
	public void setOperatorLogoDrawable(Drawable logoDrawable) {
		this.operatorLogoDrawable = logoDrawable;
	}
}
