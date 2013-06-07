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
import com.blstream.urbangame.SplashScreenActivity;

public class NotificationFactory {
	private Context context;
	
	private String message_game_new;
	private String message_game_changed;
	private String message_game_over;
	private String message_task_new;
	private String message_task_changed;
	
	private Builder notificationBuilderGame;
	private Builder notificationBuilderTask;
	
	public NotificationFactory(Context context) {
		this.context = context;
		this.message_game_new = context.getString(R.string.notification_game_new);
		this.message_game_changed = context.getString(R.string.notification_game_changed);
		this.message_game_over = context.getString(R.string.notification_game_over);
		this.message_task_new = context.getString(R.string.notification_task_new);
		this.message_task_changed = context.getString(R.string.notification_task_changed);
	}
	
	public Notification getNotificationGameNew(String gameName, Bitmap operatorLogo) {
		createBuilderGame(gameName, operatorLogo, message_game_new);
		return notificationBuilderGame.build();
	}
	
	public Notification getNotificationGameChanged(String gameName, Bitmap operatorLogo) {
		createBuilderGame(gameName, operatorLogo, message_game_changed);
		return notificationBuilderGame.build();
	}
	
	public Notification getNotificationGameOver(String gameName, Bitmap operatorLogo) {
		createBuilderGame(gameName, operatorLogo, message_game_over);
		return notificationBuilderGame.build();
	}
	
	public Notification getNotificationTaskNew(String gameName, String taskName, Bitmap operatorLogo, Long gameID) {
		createBuilderTask(gameName, taskName, operatorLogo, gameID, message_task_new);
		return notificationBuilderTask.build();
	}
	
	public Notification getNotificationTaskChanged(String gameName, String taskName, Bitmap operatorLogo, Long gameID) {
		createBuilderTask(gameName, taskName, operatorLogo, gameID, message_task_changed);
		return notificationBuilderTask.build();
	}
	
	private void createBuilderGame(String gameName, Bitmap operatorLogo, String message) {
		notificationBuilderGame = createNotification(gameName, message, operatorLogo, 0, SplashScreenActivity.class);
	}
	
	private void createBuilderTask(String gameName, String taskName, Bitmap operatorLogo, Long gameID, String message) {
		notificationBuilderTask = createNotification(gameName + ": " + taskName, message, operatorLogo, 0, gameID);
	}
	
	private Builder createNotification(String title, String message, Bitmap operatorLogo, int counter, Long gameID) {
		PendingIntent pendingIntent = getPendingIntent(gameID);
		return createNotification(title, message, operatorLogo, counter, pendingIntent);
		
	}
	
	private Builder createNotification(String gameName, String message, Bitmap operatorLogo, int counter, Class<?> cls) {
		PendingIntent pendingIntent = getPendingIntent(cls);
		return createNotification(gameName, message, operatorLogo, counter, pendingIntent);
	}
	
	private Builder createNotification(String gameName, String message, Bitmap operatorLogo, int counter,
		PendingIntent pendingIntent) {
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
		notificationBuilder.setContentText(message);
		notificationBuilder.setContentTitle(gameName);
		notificationBuilder.setSmallIcon(R.drawable.ic_stat_notification);
		notificationBuilder.setLargeIcon(operatorLogo);
		notificationBuilder.setContentInfo(String.valueOf(counter));
		notificationBuilder.setContentIntent(pendingIntent);
		notificationBuilder.setAutoCancel(true);
		notificationBuilder.setLights(Color.GREEN, 100, 200);
		notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
		return notificationBuilder;
	}
	
	private PendingIntent getPendingIntent(Class<?> cls) {
		Intent resultIntent = new Intent(context, cls);
		return getPendingIntentFromIntent(resultIntent);
	}
	
	private PendingIntent getPendingIntent(Long gameID) {
		Intent resultIntent = new Intent(context, GameDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putLong(GameDetailsActivity.GAME_KEY, gameID);
		resultIntent.putExtras(bundle);
		return getPendingIntentFromIntent(resultIntent);
	}
	
	@SuppressLint("InlinedApi")
	private PendingIntent getPendingIntentFromIntent(Intent resultIntent) {
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		
		// formatter:off
		PendingIntent resultPendingIntent =
		    PendingIntent.getActivity(
		    context,
		    0,
		    resultIntent,
		    PendingIntent.FLAG_UPDATE_CURRENT
		);

		// formatter:on
		
		return resultPendingIntent;
	}
}