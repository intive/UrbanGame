package com.blstream.urbangame.notifications;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.blstream.urbangame.GameDetailsActivity;
import com.blstream.urbangame.R;

/**
 * Pop-up notification dialog do display notifications when application is
 * running. It allows do dismiss this notification or go directly to changed
 * game/task.
 */
public class NotificationDialog implements DialogInterface.OnClickListener {
	private AlertDialog.Builder builder;
	private Context context;
	
	private int notificationTitle;
	private String notificationMessage;
	private Intent gameIntent;
	
	public NotificationDialog(Context context) {
		this.context = context;
		this.builder = new AlertDialog.Builder(context);
	}
	
	public void showDialog(int notificationID, String gameName, String taskName, Bitmap operatorLogo, long gameID) {
		setType(notificationID, gameName, taskName);
		createIntentToChangedContent(gameID);
		
		Dialog notificationDialog = createDialog(gameName, taskName, operatorLogo, gameID);
		notificationDialog.show();
	}
	
	/**
	 * Creates specific dialog based on provided data.
	 * 
	 * @param notificationType - type of notification like new or change
	 * @param gameName
	 * @param taskName - may be null if change involves only game
	 */
	private void setType(int notificationType, String gameName, String taskName) {
		Resources resources = context.getResources();
		
		switch (notificationType) {
			case NotificationConstans.GAME_NEW:
				notificationTitle = R.string.notification_title_game_new;
				notificationMessage = resources.getString(R.string.notification_message_game_new);
				break;
			case NotificationConstans.GAME_CHANGED:
				notificationTitle = R.string.notification_title_game_changed;
				notificationMessage = resources.getString(R.string.notification_message_game_changed);
				break;
			case NotificationConstans.GAME_OVER:
				notificationTitle = R.string.notification_title_game_over;
				notificationMessage = resources.getString(R.string.notification_message_game_over);
				break;
			case NotificationConstans.TASK_NEW:
				notificationTitle = R.string.notification_title_task_new;
				notificationMessage = resources.getString(R.string.notification_message_task_new);
				break;
			case NotificationConstans.TASK_CHANGED:
				notificationTitle = R.string.notification_title_task_changed;
				notificationMessage = resources.getString(R.string.notification_message_task_changed);
				break;
		}
		
		formatMessage(gameName, taskName);
	}
	
	private void formatMessage(String gameName, String taskName) {
		notificationMessage = String.format(notificationMessage, gameName, taskName);
	}
	
	private Dialog createDialog(String gameName, String taskName, Bitmap operatorLogo, long gameID) {
		builder.setPositiveButton(R.string.dialog_show, this);
		builder.setNegativeButton(R.string.dialog_dismiss, this);
		
		builder.setTitle(notificationTitle);
		builder.setMessage(notificationMessage);
		
		return builder.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				finishAndOpenGameDetailsActivity();
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				break;
		}
	}
	
	private void finishAndOpenGameDetailsActivity() {
		try {
			Activity activity = (Activity) context;
			activity.finish();
		}
		finally {
			context.startActivity(gameIntent);
		}
	}
	
	/**
	 * Builds specific intent directing user to changed game or task to show
	 * specific details about these changes.
	 * 
	 * @param gameID - ID of game to display
	 */
	private void createIntentToChangedContent(long gameID) {
		gameIntent = new Intent(context, GameDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putLong(GameDetailsActivity.GAME_KEY, gameID);
		gameIntent.putExtras(bundle);
	}
}