package com.blstream.urbangame.notifications;

import android.graphics.Bitmap;

/**
 * User available endpoint to manage notifications
 */
public interface NotificationInterface {
	public void notifyGameNew(String gameName, Bitmap operatorLogo);
	
	public void notifyGameChanged(String gameName, Bitmap operatorLogo);
	
	public void notifyGameOver(String gameName, Bitmap operatorLogo);
	
	public void notifyTaskNew(String gameName, String taskName, Bitmap operatorLogo, long gameID);
	
	public void notifyTaskChanged(String gameName, String taskName, Bitmap operatorLogo, long gameID);
}