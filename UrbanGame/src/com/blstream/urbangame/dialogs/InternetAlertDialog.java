package com.blstream.urbangame.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.blstream.urbangame.R;
import com.blstream.urbangame.dialogs.UrbanGameDialog.UrbanGameDialogOnClickListener;

public class InternetAlertDialog implements UrbanGameDialog.UrbanGameDialogOnClickListener {
	private static final long serialVersionUID = 1L;
	private final UrbanGameDialog.DialogBuilder dialogBuilder;
	private final Context context;
	
	public InternetAlertDialog(Context context) {
		this(context, null);
	}
	
	public InternetAlertDialog(Context context, UrbanGameDialogOnClickListener onClickListener) {
		this.context = context;
		this.dialogBuilder = new UrbanGameDialog.DialogBuilder(context);
		buildAlertDialog(onClickListener);
	}
	
	protected void buildAlertDialog(UrbanGameDialogOnClickListener onClickListener) {
		dialogBuilder.setTitle(R.string.title_no_internet);
		dialogBuilder.setMessage(R.string.message_device_offline);
		dialogBuilder.setPositiveButton(R.string.menu_settings, this);
		dialogBuilder.setNegativeButton(android.R.string.cancel, onClickListener == null ? this : onClickListener);
	}
	
	public void showDialog() {
		dialogBuilder.show();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
			context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
		}
		dialogBuilder.hide();
	}
}