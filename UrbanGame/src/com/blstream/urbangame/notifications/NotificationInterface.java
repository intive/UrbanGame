package com.blstream.urbangame.notifications;

import android.graphics.Bitmap;

/**
 * User available endpoint to manage notifications
 */
// formatter:off
public interface NotificationInterface {
	public void notifyGameNew		(String gameName, Bitmap operatorLogo, long gameID);
	public void notifyGameChanged	(String gameName, Bitmap operatorLogo, long gameID);
	public void notifyGameOver		(String gameName, Bitmap operatorLogo, long gameID);
	public void notifyTaskNew		(String gameName, Bitmap operatorLogo, long gameID, String taskName);
	public void notifyTaskChanged	(String gameName, Bitmap operatorLogo, long gameID, String taskName);
}