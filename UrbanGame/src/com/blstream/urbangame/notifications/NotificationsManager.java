package com.blstream.urbangame.notifications;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

/**
 * NotificationsManager helps to simply manage notifications in application. It
 * provides a couple of methods to notify about some event and then decides how
 * to inform user about them.
 */
public class NotificationsManager implements NotificationInterface {
	private ActivityManager activityManager;
	private String urbanGamePackageName;
	
	private NotificationDialog notificationDialog;
	private Context context;
	
	public NotificationsManager(Context context) {
		this.context = context;
		this.notificationDialog = new NotificationDialog(context);
		this.activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		this.urbanGamePackageName = context.getPackageName();
	}
	
	@Override
	public void notifyGameNew(String gameName, Bitmap operatorLogo, long gameID) {
		if (isAppRunningInBackground()) {
			showNotification(NotificationConstans.GAME_NEW, gameName, null, operatorLogo, gameID);
		}
		else {
			showAlertDialog(NotificationConstans.GAME_NEW, gameName, null, operatorLogo, gameID);
		}
	}
	
	@Override
	public void notifyGameChanged(String gameName, Bitmap operatorLogo, long gameID) {
		if (isAppRunningInBackground()) {
			showNotification(NotificationConstans.GAME_CHANGED, gameName, null, operatorLogo, gameID);
		}
		else {
			showAlertDialog(NotificationConstans.GAME_CHANGED, gameName, null, operatorLogo, gameID);
		}
	}
	
	@Override
	public void notifyGameOver(String gameName, Bitmap operatorLogo, long gameID) {
		if (isAppRunningInBackground()) {
			showNotification(NotificationConstans.GAME_OVER, gameName, null, operatorLogo, gameID);
		}
		else {
			showAlertDialog(NotificationConstans.GAME_OVER, gameName, null, operatorLogo, gameID);
		}
	}
	
	@Override
	public void notifyTaskNew(String gameName, Bitmap operatorLogo, long gameID, String taskName) {
		if (isAppRunningInBackground()) {
			showNotification(NotificationConstans.TASK_NEW, gameName, taskName, operatorLogo, gameID);
		}
		else {
			showAlertDialog(NotificationConstans.TASK_CHANGED, gameName, taskName, operatorLogo, gameID);
		}
	}
	
	@Override
	public void notifyTaskChanged(String gameName, Bitmap operatorLogo, long gameID, String taskName) {
		if (isAppRunningInBackground()) {
			showNotification(NotificationConstans.TASK_CHANGED, gameName, taskName, operatorLogo, gameID);
		}
		else {
			showAlertDialog(NotificationConstans.TASK_CHANGED, gameName, taskName, operatorLogo, gameID);
		}
	}
	
	/**
	 * Shows ApplicationBar specific notification
	 * 
	 * @param notificationID
	 * @param gameName
	 * @param taskName
	 * @param operatorLogo
	 * @param gameID
	 */
	private void showNotification(int notificationID, String gameName, String taskName, Bitmap operatorLogo, long gameID) {
		if (isAppRunningInBackground()) {
			startService(NotificationConstans.NOTIFICATION_SHOW, notificationID, gameName, taskName, operatorLogo,
				gameID);
		}
	}
	
	/**
	 * Displays pop-up notification within application
	 * 
	 * @param notificationID
	 * @param gameName
	 * @param taskName
	 * @param operatorLogo
	 * @param gameID
	 */
	private void showAlertDialog(int notificationID, String gameName, String taskName, Bitmap operatorLogo, long gameID) {
		notificationDialog.showDialog(notificationID, gameName, taskName, operatorLogo, gameID);
	}
	
	/* FOR FUTURE USE:
		private void dismissNotification(int notificationID) {
			startService(NotificationConstans.NOTIFICATION_HIDE, notificationID, null, null, null);
		}
	 */
	
	/**
	 * Calls {@link NotificationService} to perform some notification with
	 * provided data.
	 * 
	 * @param notificationBehaviour - defines whether to show or hide
	 *        notification
	 * @param notificationID - specifies notification type
	 * 
	 *        All other data required to be displayed in notification:
	 * @param gameName
	 * @param taskName
	 * @param operatorLogo
	 * @param gameID
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
	
	/**
	 * Checks if application is displayed on screen.
	 * 
	 * @return true if application is not visible on screen
	 */
	private boolean isAppRunningInBackground() {
		List<RunningTaskInfo> topTask = activityManager.getRunningTasks(1);
		ComponentName topActivity = topTask.get(0).topActivity;
		String currentActivityPackageName = topActivity.getPackageName();
		
		return !currentActivityPackageName.equalsIgnoreCase(urbanGamePackageName);
	}
}