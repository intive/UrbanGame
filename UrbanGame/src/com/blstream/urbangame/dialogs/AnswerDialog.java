package com.blstream.urbangame.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.blstream.urbangame.R;
import com.blstream.urbangame.dialogs.UrbanGameDialog.UrbanGameDialogOnClickListener;

/**
 * Used to show dialogs displayed after submit button was clicked in
 * TaskAnswerFragment.
 */
public class AnswerDialog implements UrbanGameDialogOnClickListener {
	private static final long serialVersionUID = 1L;
	
	private final Context context;
	private UrbanGameDialog.DialogBuilder dialogBuilder;
	private DialogType dialogType;
	
	public enum DialogType {
		RIGHT_ANSWER, WRONG_ANSWER, PARTIALLY_RIGHT_ANSWER, NO_INTERNET_CONNECTION, NO_GPS_SIGNAL, GPS_OFF
	}
	
	public AnswerDialog(Context context) {
		this.context = context;
	}
	
	public void showDialog(DialogType dialogType) {
		showDialog(dialogType, null, null);
	}
	
	public void showDialog(DialogType dialogType, Integer points, Integer maxPoints) {
		this.dialogType = dialogType;
		
		dialogBuilder = new UrbanGameDialog.DialogBuilder(context);
		
		int titleId = 0;
		String message = "";
		String possitiveButton = context.getString(android.R.string.ok);
		
		switch (dialogType) {
			case RIGHT_ANSWER:
				titleId = R.string.title_right;
				message = String.format(context.getString(R.string.message_answer_score), points, maxPoints);
				break;
			case WRONG_ANSWER:
				titleId = R.string.title_wrong;
				message = String.format(context.getString(R.string.message_answer_score), points, maxPoints);
				break;
			case PARTIALLY_RIGHT_ANSWER:
				titleId = R.string.title_partially_right;
				message = String.format(context.getString(R.string.message_answer_score), points, maxPoints);
				break;
			case NO_INTERNET_CONNECTION:
				titleId = R.string.title_no_internet;
				message = context.getString(R.string.message_no_internet);
				break;
			case NO_GPS_SIGNAL:
				titleId = R.string.title_no_gps_signal;
				message = context.getString(R.string.message_no_gps_signal);
				break;
			case GPS_OFF:
				titleId = R.string.title_gps_off;
				message = context.getString(R.string.message_gps_off);
				possitiveButton = context.getString(R.string.answer_dialog_button_settings);
				dialogBuilder.setNegativeButton(context.getString(android.R.string.cancel), this);
				break;
		
		}
		
		dialogBuilder.setTitle(titleId);
		dialogBuilder.setMessage(message);
		dialogBuilder.setPositiveButton(possitiveButton, this);
		
		dialogBuilder.show();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		Intent intent;
		
		if (which == DialogInterface.BUTTON_POSITIVE) {
			switch (dialogType) {
				case GPS_OFF:
				case NO_GPS_SIGNAL:
					intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					context.startActivity(intent);
					break;
				default:
					break;
			}
		}
		dialog.dismiss();
	}
}