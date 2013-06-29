package com.blstream.urbangame.notifications;

import android.content.Context;
import android.content.Intent;

import com.blstream.urbangame.UrbanGameApplication;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;

/**
 * NotificationsManager helps to simply manage notifications in application. It
 * provides a couple of methods to notify about some event and then decides how
 * to inform user about them.
 */
public class NotificationsManager implements NotificationListener {
	private final NotificationPopupWindow notificationPopupWindow;
	private final NotificationDiff notificationDiff;
	private final UrbanGameApplication urbanGameApplication;
	private final Context context;
	
	public NotificationsManager(Context context) {
		this.context = context;
		this.notificationPopupWindow = new NotificationPopupWindow(context);
		this.notificationDiff = new NotificationDiff();
		this.urbanGameApplication = (UrbanGameApplication) context.getApplicationContext();
	}
	
	@Override
	public void onGameWon(UrbanGame game) {
		notifyGameWon(game.getTitle(), game.getOperatorLogoBase64(), game.getID());
	}
	
	@Override
	public void onGameLost(UrbanGame game) {
		notifyGameLost(game.getTitle(), game.getOperatorLogoBase64(), game.getID());
	}
	
	@Override
	public void onGameChanged(UrbanGame oldGame, UrbanGame newGame) {
		String diff = notificationDiff.getGameDiff(oldGame, newGame);
		notifyGameChanged(newGame.getTitle(), newGame.getOperatorLogoBase64(), diff, newGame.getID());
	}
	
	@Override
	public void onTaskNew(UrbanGame game, Task task) {
		notifyTaskNew(game.getTitle(), game.getOperatorLogoBase64(), game.getID(), task.getTitle());
	}
	
	@Override
	public void onTaskChanged(UrbanGame game, Task oldTask, Task newTask) {
		String diff = notificationDiff.getTaskDiff(oldTask, newTask);
		notifyTaskChanged(game.getTitle(), game.getOperatorLogoBase64(), diff, game.getID(), newTask.getTitle());
	}
	
	private void notifyGameWon(String gameName, String operatorLogo, long gameID) {
		if (isAppRunningInBackground()) {
			showNotification(NotificationConstans.GAME_WON, gameName, null, operatorLogo, null, gameID);
		}
		else {
			showAlertDialog(NotificationConstans.GAME_WON, gameName, null, null, gameID);
		}
	}
	
	private void notifyGameLost(String gameName, String operatorLogo, long gameID) {
		if (isAppRunningInBackground()) {
			showNotification(NotificationConstans.GAME_LOST, gameName, null, operatorLogo, null, gameID);
		}
		else {
			showAlertDialog(NotificationConstans.GAME_LOST, gameName, null, null, gameID);
		}
	}
	
	private void notifyGameChanged(String gameName, String operatorLogo, String diff, long gameID) {
		if (isAppRunningInBackground()) {
			showNotification(NotificationConstans.GAME_CHANGED, gameName, null, operatorLogo, diff, gameID);
		}
		else {
			showAlertDialog(NotificationConstans.GAME_CHANGED, gameName, null, diff, gameID);
		}
	}
	
	private void notifyTaskNew(String gameName, String operatorLogo, long gameID, String taskName) {
		if (isAppRunningInBackground()) {
			showNotification(NotificationConstans.TASK_NEW, gameName, taskName, operatorLogo, null, gameID);
		}
		else {
			showAlertDialog(NotificationConstans.TASK_NEW, gameName, taskName, null, gameID);
		}
	}
	
	private void notifyTaskChanged(String gameName, String operatorLogo, String diff, long gameID, String taskName) {
		if (isAppRunningInBackground()) {
			showNotification(NotificationConstans.TASK_CHANGED, gameName, taskName, operatorLogo, diff, gameID);
		}
		else {
			showAlertDialog(NotificationConstans.TASK_CHANGED, gameName, taskName, diff, gameID);
		}
	}
	
	/**
	 * Shows ApplicationBar specific notification
	 * 
	 * @param notificationType
	 * @param gameName
	 * @param taskName
	 * @param operatorLogo
	 * @param gameID
	 */
	private void showNotification(int notificationType, String gameName, String taskName, String operatorLogo,
		String diff, long gameID) {
		startService(NotificationConstans.NOTIFICATION_SHOW, notificationType, gameName, taskName, operatorLogo, diff,
			gameID);
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
	private void showAlertDialog(int notificationID, String gameName, String taskName, String diff, long gameID) {
		notificationPopupWindow.showDialog(notificationID, gameName, taskName, diff, gameID);
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
		String operatorLogo, String diff, long gameID) {
		Intent intent = new Intent(context, NotificationService.class);
		
		intent.putExtra(notificationBehaviour, notificationID);
		intent.putExtra(NotificationConstans.NOTIFICATION_GAME_NAME, gameName);
		intent.putExtra(NotificationConstans.NOTIFICATION_GAME_ID, gameID);
		intent.putExtra(NotificationConstans.NOTIFICATION_OPERATOR_LOGO, operatorLogo);
		intent.putExtra(NotificationConstans.NOTIFICATION_TASK_NAME, taskName);
		intent.putExtra(NotificationConstans.NOTIFICATION_DIFF, diff);
		
		context.startService(intent);
	}
	
	private boolean isAppRunningInBackground() {
		return urbanGameApplication.isAppInBackground();
	}
}