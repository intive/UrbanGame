package com.blstream.urbangame.notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

import com.blstream.urbangame.GameDetailsActivity;
import com.blstream.urbangame.R;
import com.blstream.urbangame.database.helper.Base64ImageCoder;

/**
 * NotificationApplicationBar builds different kind of ApplicationBar
 * notifications.
 */
public class NotificationApplicationBar {
	private final Context context;
	
	private final String message_game_changed;
	private final String message_game_over;
	private final String message_game_won;
	private final String message_game_lost;
	private final String message_task_new;
	private final String message_task_changed;
	
	private Builder notificationBuilderGame;
	private Builder notificationBuilderTask;
	
	public NotificationApplicationBar(Context context) {
		this.context = context;
		this.message_game_changed = context.getString(R.string.notification_title_game_changed);
		this.message_game_over = context.getString(R.string.notification_title_game_over);
		this.message_game_won = context.getString(R.string.notification_title_game_won);
		this.message_game_lost = context.getString(R.string.notification_title_game_lost);
		this.message_task_new = context.getString(R.string.notification_title_task_new);
		this.message_task_changed = context.getString(R.string.notification_title_task_changed);
	}
	
	public Notification getNotificationGameChanged(String gameName, String operatorLogo, long gameID) {
		createBuilderGame(gameName, operatorLogo, gameID, message_game_changed);
		return notificationBuilderGame.build();
	}
	
	public Notification getNotificationGameWon(String gameName, String operatorLogo, long gameID) {
		return getNotificationGameOver(gameName, operatorLogo, gameID, message_game_won);
	}
	
	public Notification getNotificationGameLost(String gameName, String operatorLogo, long gameID) {
		return getNotificationGameOver(gameName, operatorLogo, gameID, message_game_lost);
	}
	
	private Notification getNotificationGameOver(String gameName, String operatorLogo, long gameID, String message) {
		createBuilderGame(gameName, operatorLogo, gameID, message_game_over + message);
		return notificationBuilderGame.build();
	}
	
	public Notification getNotificationTaskNew(String gameName, String taskName, String operatorLogo, long gameID) {
		createBuilderTask(gameName, taskName, operatorLogo, gameID, message_task_new);
		return notificationBuilderTask.build();
	}
	
	public Notification getNotificationTaskChanged(String gameName, String taskName, String operatorLogo, long gameID) {
		createBuilderTask(gameName, taskName, operatorLogo, gameID, message_task_changed);
		return notificationBuilderTask.build();
	}
	
	private void createBuilderGame(String gameName, String operatorLogo, long gameID, String message) {
		notificationBuilderGame = createNotification(gameName, message, operatorLogo, 1, gameID);
	}
	
	private void createBuilderTask(String gameName, String taskName, String operatorLogo, Long gameID, String message) {
		notificationBuilderTask = createNotification(gameName + ": " + taskName, message, operatorLogo, 1, gameID);
	}
	
	private Builder createNotification(String title, String message, String operatorLogo, int counter, Long gameID) {
		PendingIntent pendingIntent = getPendingIntent(gameID);
		return createNotification(title, message, operatorLogo, counter, pendingIntent);
	}
	
	private PendingIntent getPendingIntent(Long gameID) {
		Intent resultIntent = new Intent(context, GameDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putLong(GameDetailsActivity.GAME_KEY, gameID);
		resultIntent.putExtras(bundle);
		return getPendingIntentFromIntent(resultIntent);
	}
	
	private Builder createNotification(String gameName, String message, String operatorLogo, int counter,
		PendingIntent pendingIntent) {
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
		notificationBuilder.setContentText(message);
		notificationBuilder.setContentTitle(gameName);
		notificationBuilder.setSmallIcon(R.drawable.ic_stat_notification);
		notificationBuilder.setLargeIcon(getLogoBitmap(operatorLogo));
		notificationBuilder.setContentInfo(String.valueOf(counter));
		notificationBuilder.setContentIntent(pendingIntent);
		notificationBuilder.setAutoCancel(true);
		notificationBuilder.setLights(Color.GREEN, 100, 200);
		notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
		return notificationBuilder;
	}

	private Bitmap getLogoBitmap(String operatorLogo) {
		return Base64ImageCoder.getBitmapFromStringBase64(operatorLogo);
	}
	
	@SuppressLint("InlinedApi")
	private PendingIntent getPendingIntentFromIntent(Intent resultIntent) {
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		
		// formatter:off
		PendingIntent resultPendingIntent =
		    PendingIntent.getActivity(
		    context,
		    0,	// requestCode (not used)
		    resultIntent,
		    PendingIntent.FLAG_UPDATE_CURRENT
		);
		// formatter:on
		
		return resultPendingIntent;
	}
}