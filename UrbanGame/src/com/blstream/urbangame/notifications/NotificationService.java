package com.blstream.urbangame.notifications;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

public class NotificationService extends IntentService {
	private final static String SERVICE_NAME = "NotificationService";
	
	private NotificationManager notificationManager;
	private NotificationFactory notificationFactory;
	
	public NotificationService() {
		super(SERVICE_NAME);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		this.notificationFactory = new NotificationFactory(this);
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
			Bitmap operatorLogo = intent.getParcelableExtra(NotificationConstans.NOTIFICATION_OPERATOR_LOGO);
			String taskName = intent.getStringExtra(NotificationConstans.NOTIFICATION_TASK_NAME);
			
			switch (notificationIdToShow) {
				case NotificationConstans.GAME_NEW:
					notifyGameNew(gameName, operatorLogo);
					break;
				case NotificationConstans.GAME_CHANGED:
					notifyGameChanged(gameName, operatorLogo);
					break;
				case NotificationConstans.GAME_OVER:
					notifyGameOver(gameName, operatorLogo);
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
	
	public void notifyGameNew(String gameName, Bitmap operatorLogo) {
		Notification notificationGameNew = notificationFactory.getNotificationGameNew(gameName, operatorLogo);
		showNotification(NotificationConstans.GAME_NEW, notificationGameNew);
	}
	
	public void notifyGameChanged(String gameName, Bitmap operatorLogo) {
		Notification notificationGameChanged = notificationFactory.getNotificationGameChanged(gameName, operatorLogo);
		showNotification(NotificationConstans.GAME_CHANGED, notificationGameChanged);
	}
	
	public void notifyGameOver(String gameName, Bitmap operatorLogo) {
		Notification notificationGameOver = notificationFactory.getNotificationGameOver(gameName, operatorLogo);
		showNotification(NotificationConstans.GAME_OVER, notificationGameOver);
	}
	
	public void notifyTaskNew(String gameName, String taskName, Bitmap operatorLogo, Long gameID) {
		Notification notificationTaskNew = notificationFactory.getNotificationTaskNew(gameName, taskName, operatorLogo,
			gameID);
		showNotification(NotificationConstans.TASK_NEW, notificationTaskNew);
	}
	
	public void notifyTaskChanged(String gameName, String taskName, Bitmap operatorLogo, Long gameID) {
		Notification notificationTask = notificationFactory.getNotificationTaskChanged(gameName, taskName,
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