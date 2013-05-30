package com.blstream.urbangame.notifications;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

public class NotificationsManager implements NotificationInterface {
	private Context context;
	
	public NotificationsManager(Context context) {
		this.context = context;
	}
	
	@Override
	public void notifyGameChanged(String gameName, Bitmap operatorLogo) {
		showNotification(NotificationConstans.GAME_CHANGED, gameName, null, operatorLogo, -1);
	}
	
	@Override
	public void notifyGameNew(String gameName, Bitmap operatorLogo) {
		showNotification(NotificationConstans.GAME_NEW, gameName, null, operatorLogo, -1);
	}
	
	@Override
	public void notifyGameOver(String gameName, Bitmap operatorLogo) {
		showNotification(NotificationConstans.GAME_OVER, gameName, null, operatorLogo, -1);
	}
	
	@Override
	public void notifyTaskNew(String gameName, String taskName, Bitmap operatorLogo, long gameID) {
		showNotification(NotificationConstans.TASK_NEW, gameName, taskName, operatorLogo, gameID);
	}
	
	@Override
	public void notifyTaskChanged(String gameName, String taskName, Bitmap operatorLogo, long gameID) {
		showNotification(NotificationConstans.TASK_CHANGED, gameName, taskName, operatorLogo, gameID);
	}
	
	private void showNotification(int notificationID, String gameName, String taskName, Bitmap operatorLogo, long gameID) {
		startService(NotificationConstans.NOTIFICATION_SHOW, notificationID, gameName, taskName, operatorLogo, gameID);
	}
	
	/* FOR FUTURE USE:
	 * 
		private void dismissNotification(int notificationID) {
			startService(NotificationConstans.NOTIFICATION_HIDE, notificationID, null, null, null);
		}
	 */
	
	private void startService(String notificationBehaviour, int notificationID, String gameName, String taskName,
		Bitmap operatorLogo, long gameID) {
		Intent intent = new Intent(context, NotificationService.class);
		
		intent.putExtra(notificationBehaviour, notificationID);
		intent.putExtra(NotificationConstans.NOTIFICATION_GAME_NAME, gameName);
		intent.putExtra(NotificationConstans.NOTIFICATION_GAME_ID, gameID);
		intent.putExtra(NotificationConstans.NOTIFICATION_OPERATOR_LOGO, operatorLogo);
		intent.putExtra(NotificationConstans.NOTIFICATION_TASK_NAME, taskName);
		
		context.startService(intent);
	}
}