package com.blstream.urbangame.notifications;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.blstream.urbangame.GameDetailsActivity;
import com.blstream.urbangame.R;
import com.blstream.urbangame.dialogs.UrbanGameDialog;
import com.blstream.urbangame.dialogs.UrbanGameDialog.UrbanGameDialogOnClickListener;

/**
 * Pop-up notification dialog do display notifications when application is
 * running. It allows do dismiss this notification or go directly to changed
 * game/task.
 */
public class NotificationPopupWindow implements UrbanGameDialogOnClickListener {
	private final UrbanGameDialog.DialogBuilder builder;
	private final Context context;
	
	private int notificationTitle;
	private String notificationMessage;
	private Intent gameIntent;
	
	public NotificationPopupWindow(Context context) {
		this.context = context;
		this.builder = new UrbanGameDialog().new DialogBuilder(context);
	}
	
	public void showDialog(int notificationType, String gameName, String taskName, String diff, long gameID) {
		setType(notificationType);
		formatMessage(gameName, taskName);
		createIntentToChangedContent(gameID);
		createDialog(gameName, taskName, diff).show();
	}
	
	/**
	 * Creates specific dialog based on provided data.
	 * 
	 * @param notificationType - type of notification like new or change
	 * @param gameName
	 * @param taskName - may be null if change involves only game
	 */
	private void setType(int notificationType) {
		Resources resources = context.getResources();
		
		switch (notificationType) {
			case NotificationConstans.GAME_WON:
				notificationTitle = R.string.notification_title_game_over;
				notificationMessage = resources.getString(R.string.notification_message_game_over) + " "
					+ resources.getString(R.string.notification_title_game_won);
				break;
			case NotificationConstans.GAME_LOST:
				notificationTitle = R.string.notification_title_game_over;
				notificationMessage = resources.getString(R.string.notification_message_game_over) + " "
					+ resources.getString(R.string.notification_title_game_lost);
				break;
			case NotificationConstans.GAME_CHANGED:
				notificationTitle = R.string.notification_title_game_changed;
				notificationMessage = resources.getString(R.string.notification_message_game_changed);
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
	}
	
	private void formatMessage(String gameName, String taskName) {
		notificationMessage = String.format(notificationMessage, gameName, taskName);
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
	
	private UrbanGameDialog.DialogBuilder createDialog(String gameName, String taskName, String diff) {
		builder.setPositiveButton(R.string.dialog_show, this);
		builder.setNegativeButton(R.string.dialog_dismiss, this);
		
		builder.setTitle(notificationTitle);
		builder.setMessage(notificationMessage + diff == null ? "" : diff);
		
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
}