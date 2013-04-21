package com.blstream.urbangame.database.entity;

import com.blstream.urbangame.database.helper.Base64ImageCoder;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class Player {

	private String email;
	private String password;
	private String displayName;
	private String avatarBase64;
	private Drawable avatar;

	/**
	 * @param email
	 * @param password
	 *            - may be null
	 * @param displayName
	 *            - may be null
	 * @param avatarBase64
	 *            - may be null
	 */
	public Player(String email, String password, String displayName,
			String avatarBase64) {
		this.email = email;
		this.setPassword(password);
		this.displayName = displayName;
		this.avatarBase64 = avatarBase64;
	}

	/**
	 * @param email
	 * @param password
	 *            - may be null
	 * @param displayName
	 *            - may be null
	 * @param avatar
	 *            - may be null
	 */
	public Player(String email, String password, String displayName,
			Drawable avatar) {
		this.email = email;
		this.setPassword(password);
		this.displayName = displayName;
		this.avatar = avatar;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param res
	 *            - can be obtained by getResources() from Activity
	 * @return the avatar
	 */
	public Drawable getAvatar(Resources res) {
		avatar = Base64ImageCoder.decodeImage(avatarBase64, res);
		return avatar;
	}

	/**
	 * @param avatar
	 *            the avatar to set
	 */
	public void setAvatar(Drawable avatar) {
		this.avatar = avatar;
		avatarBase64 = Base64ImageCoder.convertImage(avatar);
	}

	/**
	 * @return the avatarBase64
	 */
	public String getAvatarBase64() {
		if (avatarBase64 == null && avatar != null) 
			avatarBase64 = Base64ImageCoder.convertImage(avatar);
		return avatarBase64;
	}

	/**
	 * @param avatarBase64
	 *            the avatarBase64 to set
	 */
	public void setAvatarBase64(String avatarBase64) {
		this.avatarBase64 = avatarBase64;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = cipher(password);
	}

	private String cipher(String password) {
		// TODO ciphering method
		return password;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
