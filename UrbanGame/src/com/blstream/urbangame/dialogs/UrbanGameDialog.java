package com.blstream.urbangame.dialogs;

import java.io.Serializable;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.blstream.urbangame.R;

public class UrbanGameDialog extends DialogFragment {
	
	public static final int POSITIVE_BUTTON = DialogInterface.BUTTON_POSITIVE;
	public static final int NEGATIVE_BUTTON = DialogInterface.BUTTON_NEGATIVE;
	
	private String title;
	private String message;
	
	private String buttonPositive;
	private UrbanGameDialogOnClickListener buttonPositiveListener;
	
	private String buttonNegative;
	private UrbanGameDialogOnClickListener buttonNegativeListener;
	
	private boolean isPositiveEnabled;
	private boolean isNegativeEnabled;
	
	private static final String TITLE_KEY = "title";
	private static final String MESSAGE_KEY = "message";
	private static final String BUTTON_POSITIVE_KEY = "btn_possitive";
	private static final String BUTTON_POSITIVE_LISTENER_KEY = "btn_possitive_listener";
	private static final String BUTTON_NEGATIVE_KEY = "btn_negative";
	private static final String BUTTON_NEGATIVE_LISTENER_KEY = "btn_negative_listener";
	private static final String IS_POSITIVE_ENABLED_KEY = "is_positive_enabled";
	private static final String IS_NEGATIVE_ENABLED_KEY = "is_negative_enabled";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		Dialog dialog = super.onCreateDialog(savedInstanceState);//new Dialog(getActivity());
		
		if (savedInstanceState != null) {
			loadData(savedInstanceState);
		}
		
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		View v = null;
		TextView dialogTitle = null;
		TextView dialogMessage = null;
		Button positive = null;
		Button negative = null;
		
		if (isPositiveEnabled && isNegativeEnabled) {
			v = View.inflate(getActivity(), R.layout.dialog_two_buttons, null);
			
			positive = (Button) v.findViewById(R.id.buttonDialogPositive);
			negative = (Button) v.findViewById(R.id.buttonDialogNegative);
		}
		else {
			v = View.inflate(getActivity(), R.layout.dialog_one_button, null);
			
			if (isPositiveEnabled) {
				positive = (Button) v.findViewById(R.id.buttonDialogPositive);
			}
			else {
				negative = (Button) v.findViewById(R.id.buttonDialogPositive);
			}
		}
		
		dialogTitle = (TextView) v.findViewById(R.id.textViewDialogTitleTwoButtons);
		dialogMessage = (TextView) v.findViewById(R.id.textViewDialogContentTwoButtons);
		
		dialogTitle.setText(title);
		dialogMessage.setText(message);
		
		if (isPositiveEnabled) {
			positive.setText(buttonPositive);
			positive
				.setOnClickListener(new ButtonOnClickToDialogOnClickAdapter(buttonPositiveListener, POSITIVE_BUTTON));
			
		}
		if (isNegativeEnabled) {
			negative.setText(buttonNegative);
			negative
				.setOnClickListener(new ButtonOnClickToDialogOnClickAdapter(buttonNegativeListener, NEGATIVE_BUTTON));
		}
		
		dialog.setContentView(v);
		
		return dialog;
	}
	
	private void loadData(Bundle savedInstanceState) {
		title = savedInstanceState.getString(TITLE_KEY);
		message = savedInstanceState.getString(MESSAGE_KEY);
		buttonPositive = savedInstanceState.getString(BUTTON_POSITIVE_KEY);
		buttonPositiveListener = (UrbanGameDialogOnClickListener) savedInstanceState
			.getSerializable(BUTTON_POSITIVE_LISTENER_KEY);
		buttonNegative = savedInstanceState.getString(BUTTON_NEGATIVE_KEY);
		buttonNegativeListener = (UrbanGameDialogOnClickListener) savedInstanceState
			.getSerializable(BUTTON_NEGATIVE_LISTENER_KEY);
		isPositiveEnabled = savedInstanceState.getBoolean(IS_POSITIVE_ENABLED_KEY);
		isNegativeEnabled = savedInstanceState.getBoolean(IS_NEGATIVE_ENABLED_KEY);
	}
	
	@Override
	public void onSaveInstanceState(Bundle out) {
		super.onSaveInstanceState(out);
		out.putString(TITLE_KEY, title);
		out.putString(MESSAGE_KEY, message);
		out.putString(BUTTON_POSITIVE_KEY, buttonPositive);
		out.putSerializable(BUTTON_POSITIVE_LISTENER_KEY, buttonPositiveListener);
		out.putString(BUTTON_NEGATIVE_KEY, buttonNegative);
		out.putSerializable(BUTTON_NEGATIVE_LISTENER_KEY, buttonNegativeListener);
		out.putBoolean(IS_POSITIVE_ENABLED_KEY, isPositiveEnabled);
		out.putBoolean(IS_NEGATIVE_ENABLED_KEY, isNegativeEnabled);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		int width = (int) getResources().getDimension(R.dimen.dialogWidth);
		getDialog().getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);
	}
	
	public final String TAG = DialogBuilder.class.getSimpleName();
	
	public class DialogBuilder {
		
		private final SherlockFragmentActivity context;
		
		public DialogBuilder(Context context) {
			this.context = (SherlockFragmentActivity) context;
		}
		
		public DialogBuilder setTitle(int resourceID) {
			title = context.getResources().getString(resourceID);
			return this;
		}
		
		public DialogBuilder setTitle(String title) {
			UrbanGameDialog.this.title = title;
			return this;
		}
		
		public DialogBuilder setMessage(int resourceID) {
			message = context.getResources().getString(resourceID);
			return this;
		}
		
		public DialogBuilder setMessage(String message) {
			UrbanGameDialog.this.message = message;
			return this;
		}
		
		public DialogBuilder setPositiveButton(String buttonLabel, UrbanGameDialogOnClickListener listener) {
			buttonPositive = buttonLabel;
			buttonPositiveListener = listener;
			isPositiveEnabled = true;
			return this;
		}
		
		public DialogBuilder setPositiveButton(int buttonLabelResourceID, UrbanGameDialogOnClickListener listener) {
			buttonPositive = context.getResources().getString(buttonLabelResourceID);
			setPositiveButton(buttonPositive, listener);
			return this;
		}
		
		public DialogBuilder setNegativeButton(String buttonLabel, UrbanGameDialogOnClickListener listener) {
			buttonNegative = buttonLabel;
			buttonNegativeListener = listener;
			isNegativeEnabled = true;
			return this;
		}
		
		public DialogBuilder setNegativeButton(int buttonLabelResourceID, UrbanGameDialogOnClickListener listener) {
			buttonNegative = context.getResources().getString(buttonLabelResourceID);
			setNegativeButton(buttonNegative, listener);
			return this;
		}
		
		public DialogBuilder setCancelable(boolean isCancelable) {
			UrbanGameDialog.this.setCancelable(isCancelable);
			return this;
		}
		
		public void show() {
			FragmentManager manager = context.getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			Fragment previous = manager.findFragmentByTag(TAG);
			if (previous != null) {
				transaction.remove(previous);
			}
			if ((isPositiveEnabled || isNegativeEnabled)) {
				UrbanGameDialog.this.show(transaction, TAG);
			}
		}
		
		public DialogBuilder create() {
			return this;
		}
	}
	
	@SuppressWarnings("serial")
	private class ButtonOnClickToDialogOnClickAdapter implements OnClickListener, Serializable {
		
		private final UrbanGameDialogOnClickListener dialogInterfaceListener;
		private final int which;
		
		public ButtonOnClickToDialogOnClickAdapter(UrbanGameDialogOnClickListener listener, int which) {
			dialogInterfaceListener = listener;
			this.which = which;
		}
		
		@Override
		public void onClick(View v) {
			if (dialogInterfaceListener != null) {
				dialogInterfaceListener.onClick(getDialog(), which);
			}
			UrbanGameDialog.this.dismiss();
		}
	}
	
	public interface UrbanGameDialogOnClickListener extends Serializable, DialogInterface.OnClickListener {}
}
