package com.blstream.urbangame.dialogs;

import java.io.Serializable;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.blstream.urbangame.R;

public class UrbanGameDialog extends SherlockDialogFragment {
	
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
	private boolean isCancelable;
	
	private static final String TITLE_KEY = "title";
	private static final String MESSAGE_KEY = "message";
	private static final String BUTTON_POSITIVE_KEY = "btn_possitive";
	private static final String BUTTON_POSITIVE_LISTENER_KEY = "btn_possitive_listener";
	private static final String BUTTON_NEGATIVE_KEY = "btn_negative";
	private static final String BUTTON_NEGATIVE_LISTENER_KEY = "btn_negative_listener";
	private static final String IS_POSITIVE_ENABLED_KEY = "is_positive_enabled";
	private static final String IS_NEGATIVE_ENABLED_KEY = "is_negative_enabled";
	private static final String IS_CANCELABLE = "is_cancelable";
	
	private Activity activity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		
		loadDataFromArguments();
		setCancelable(isCancelable);
		
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		View v = null;
		TextView dialogTitle = null;
		TextView dialogMessage = null;
		Button positive = null;
		Button negative = null;
		
		if (isPositiveEnabled && isNegativeEnabled) {
			v = activity.getLayoutInflater().inflate(R.layout.dialog_two_buttons, null);
			
			positive = (Button) v.findViewById(R.id.buttonDialogPositive);
			negative = (Button) v.findViewById(R.id.buttonDialogNegative);
		}
		else {
			v = activity.getLayoutInflater().inflate(R.layout.dialog_one_button, null);
			
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
	
	private void loadDataFromArguments() {
		Bundle b = getArguments();
		title = b.getString(TITLE_KEY);
		message = b.getString(MESSAGE_KEY);
		buttonPositive = b.getString(BUTTON_POSITIVE_KEY);
		buttonPositiveListener = (UrbanGameDialogOnClickListener) b.getSerializable(BUTTON_POSITIVE_LISTENER_KEY);
		buttonNegative = b.getString(BUTTON_NEGATIVE_KEY);
		buttonNegativeListener = (UrbanGameDialogOnClickListener) b.getSerializable(BUTTON_NEGATIVE_LISTENER_KEY);
		isPositiveEnabled = b.getBoolean(IS_POSITIVE_ENABLED_KEY);
		isNegativeEnabled = b.getBoolean(IS_NEGATIVE_ENABLED_KEY);
		isCancelable = b.getBoolean(IS_CANCELABLE);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		int width = (int) getResources().getDimension(R.dimen.dialogWidth);
		getDialog().getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);
	}
	
	public static class DialogBuilder {
		
		public final String TAG = DialogBuilder.class.getSimpleName();
		
		private final SherlockFragmentActivity context;
		private final FragmentManager manager;
		private String title;
		private String message;
		
		private String buttonPositive;
		private UrbanGameDialogOnClickListener buttonPositiveListener;
		
		private String buttonNegative;
		private UrbanGameDialogOnClickListener buttonNegativeListener;
		
		private boolean isPositiveEnabled;
		private boolean isNegativeEnabled;
		private boolean isCancelable;
		
		public DialogBuilder(Context context) {
			this.context = (SherlockFragmentActivity) context;
			this.manager = this.context.getSupportFragmentManager();
			isCancelable = true;
		}
		
		public DialogBuilder setTitle(int resourceID) {
			title = context.getResources().getString(resourceID);
			return this;
		}
		
		public DialogBuilder setTitle(String title) {
			this.title = title;
			return this;
		}
		
		public DialogBuilder setMessage(int resourceID) {
			message = context.getResources().getString(resourceID);
			return this;
		}
		
		public DialogBuilder setMessage(String message) {
			this.message = message;
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
			this.isCancelable = isCancelable;
			return this;
		}
		
		public void show() {
			hide();
			
			if ((isPositiveEnabled || isNegativeEnabled)) {
				Bundle arugments = new Bundle();
				arugments.putString(TITLE_KEY, title);
				arugments.putString(MESSAGE_KEY, message);
				arugments.putString(BUTTON_POSITIVE_KEY, buttonPositive);
				arugments.putSerializable(BUTTON_POSITIVE_LISTENER_KEY, buttonPositiveListener);
				arugments.putString(BUTTON_NEGATIVE_KEY, buttonNegative);
				arugments.putSerializable(BUTTON_NEGATIVE_LISTENER_KEY, buttonNegativeListener);
				arugments.putBoolean(IS_POSITIVE_ENABLED_KEY, isPositiveEnabled);
				arugments.putBoolean(IS_NEGATIVE_ENABLED_KEY, isNegativeEnabled);
				arugments.putBoolean(IS_CANCELABLE, isCancelable);
				
				UrbanGameDialog dial = new UrbanGameDialog();
				dial.setArguments(arugments);
				
				FragmentTransaction transaction = manager.beginTransaction();
				transaction.add(dial, TAG);
				transaction.commitAllowingStateLoss();
			}
		}
		
		public DialogBuilder create() {
			return this;
		}
		
		public void hide() {
			Fragment fragment = manager.findFragmentByTag(TAG);
			if (fragment != null) {
				manager.beginTransaction().remove(fragment).commitAllowingStateLoss();
				manager.executePendingTransactions();
			}
		}
	}
	
	private class ButtonOnClickToDialogOnClickAdapter implements OnClickListener, Serializable {
		private static final long serialVersionUID = 1L;
		
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
		}
	}
	
	public interface UrbanGameDialogOnClickListener extends Serializable, DialogInterface.OnClickListener {}
}
