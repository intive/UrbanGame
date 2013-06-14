package com.blstream.urbangame.notifications;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

/**
 * NotificationService allows to display notifications in ApplicationBar. It is
 * invoked from {@link NotificationsManager} and obtains notifications from
 * {@link NotificationApplicationBar}.
 */
public class NotificationService extends IntentService {
	private final static String SERVICE_NAME = "NotificationService";
	
	private NotificationManager notificationManager;
	private NotificationApplicationBar notificationApplicationBar;
	
	public NotificationService() {
		super(SERVICE_NAME);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		this.notificationApplicationBar = new NotificationApplicationBar(this);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		int notificationIdToHide = intent.getIntExtra(NotificationConstans.NOTIFICATION_HIDE, -1);
		manageHideNotification(notificationIdToHide);
		
		int notificationIdToShow = intent.getIntExtra(NotificationConstans.NOTIFICATION_SHOW, -1);
		manageShowNotification(notificationIdToShow, intent);
	}
	
	private void manageHideNotification(int notificationIdToHide) {
		if (notificationIdToHide != -1) {
			dismissNotification(notificationIdToHide);
		}
	}
	
	private void manageShowNotification(int notificationIdToShow, Intent intent) {
		if (notificationIdToShow != -1) {
			String gameName = intent.getStringExtra(NotificationConstans.NOTIFICATION_GAME_NAME);
			Long gameID = intent.getLongExtra(NotificationConstans.NOTIFICATION_GAME_ID, -1);
			String operatorLogo = intent.getStringExtra(NotificationConstans.NOTIFICATION_OPERATOR_LOGO);
			String taskName = intent.getStringExtra(NotificationConstans.NOTIFICATION_TASK_NAME);
			
			switch (notificationIdToShow) {
				case NotificationConstans.GAME_WON:
					notifyGameWon(gameName, operatorLogo, gameID);
					break;
				case NotificationConstans.GAME_LOST:
					notifyGameLost(gameName, operatorLogo, gameID);
					break;
				case NotificationConstans.GAME_CHANGED:
					notifyGameChanged(gameName, operatorLogo, gameID);
					break;
				case NotificationConstans.TASK_NEW:
					notifyTaskNew(gameName, taskName, operatorLogo, gameID);
					break;
				case NotificationConstans.TASK_CHANGED:
					notifyTaskChanged(gameName, taskName, operatorLogo, gameID);
					break;
			}
		}
	}
	
	public void notifyGameChanged(String gameName, String operatorLogo, Long gameID) {
		Notification notificationGameChanged = notificationApplicationBar.getNotificationGameChanged(gameName,
			operatorLogo, gameID);
		showNotification(NotificationConstans.GAME_CHANGED, notificationGameChanged);
	}
	
	public void notifyGameWon(String gameName, String operatorLogo, Long gameID) {
		Notification notificationGameOver = notificationApplicationBar.getNotificationGameWon(gameName, operatorLogo,
			gameID);
		showNotification(NotificationConstans.GAME_WON, notificationGameOver);
	}
	
	public void notifyGameLost(String gameName, String operatorLogo, Long gameID) {
		Notification notificationGameOver = notificationApplicationBar.getNotificationGameLost(gameName, operatorLogo,
			gameID);
		showNotification(NotificationConstans.GAME_LOST, notificationGameOver);
	}
	
	public void notifyTaskNew(String gameName, String taskName, String operatorLogo, Long gameID) {
		Notification notificationTaskNew = notificationApplicationBar.getNotificationTaskNew(gameName, taskName,
			operatorLogo, gameID);
		showNotification(NotificationConstans.TASK_NEW, notificationTaskNew);
	}
	
	public void notifyTaskChanged(String gameName, String taskName, String operatorLogo, Long gameID) {
		Notification notificationTask = notificationApplicationBar.getNotificationTaskChanged(gameName, taskName,
			operatorLogo, gameID);
		showNotification(NotificationConstans.TASK_CHANGED, notificationTask);
	}
	
	private void showNotification(int notificationID, Notification notification) {
		notificationManager.notify(notificationID, notification);
	}
	
	private void dismissNotification(int notificationID) {
		notificationManager.cancel(notificationID);
	}
}