package com.blstream.urbangame.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.blstream.urbangame.LoginRegisterActivity;
import com.blstream.urbangame.R;
import com.blstream.urbangame.dialogs.UrbanGameDialog;
import com.blstream.urbangame.dialogs.UrbanGameDialog.UrbanGameDialogOnClickListener;
import com.blstream.urbangame.session.RegistrationManager;

public class RegisterFragment extends SherlockFragment implements OnClickListener {
	private LoginRegisterView loginRegisterView;
	private UrbanGameDialog.DialogBuilder invalidDataAlertDialog;
	private UrbanGameDialog.DialogBuilder registerCompleteAlertDialog;
	private LoginRegisterActivity activity;
	
	private String email;
	private String displayName;
	private String password;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (LoginRegisterActivity) activity;
		createInvalidDataAlertDialog();
		createRegisterCompleteAlertDialog();
	}
	
	// formatter:off
	private void createRegisterCompleteAlertDialog() {
		registerCompleteAlertDialog = new UrbanGameDialog.DialogBuilder(activity)
			.setTitle(R.string.dialog_register_title)
			.setMessage(R.string.dialog_register_message)
			.setPositiveButton(android.R.string.ok, registerSuccessfulListener)
			.create();
	}
	
	private void createInvalidDataAlertDialog() {
		invalidDataAlertDialog = new UrbanGameDialog.DialogBuilder(activity)
			.setTitle(R.string.dialog_data_invalid_tittle)
			.setMessage(R.string.dialog_data_invalid_message)
			.setPositiveButton(R.string.button_correct, null)
			.create();
	}
	
	// formatter:on
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		loginRegisterView = new LoginRegisterView(getActivity(), null);
		loginRegisterView.setType(LoginRegisterView.TYPE_REGISTER);
		loginRegisterView.setOnButtonClickListener(this);
		return loginRegisterView;
	}
	
	@Override
	public void onClick(View v) {
		email = loginRegisterView.getEmail();
		displayName = loginRegisterView.getDisplayName();
		password = loginRegisterView.getPassword();
		boolean isRegisterDataValid = isRegisterDataValid(email, displayName, password);
		
		if (isRegisterDataValid) {
			showRegistrationCompleteAlertDialog();
		}
		else {
			showInvalidDataAlertDialog();
		}
	}
	
	private void showRegistrationCompleteAlertDialog() {
		registerCompleteAlertDialog.show();
	}
	
	private void showInvalidDataAlertDialog() {
		invalidDataAlertDialog.show();
	}
	
	private boolean isRegisterDataValid(String email, String displayName, String password) {
		return loginRegisterView.isDataCorrect() && !isPlayerAlreadyRegistered(email);
	}
	
	private boolean isPlayerAlreadyRegistered(String email) {
		return RegistrationManager.getInstance(activity).doesPlayerExist(email);
	}
	
	private final UrbanGameDialogOnClickListener registerSuccessfulListener = new UrbanGameDialogOnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			activity.registerAndLoginUser(email, displayName, password);
		}
	};
}