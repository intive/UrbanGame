package com.blstream.urbangame.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.blstream.urbangame.LoginRegisterActivity;
import com.blstream.urbangame.R;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.Player;

public class RegisterFragment extends SherlockFragment implements OnClickListener {
	private LoginRegisterView loginRegisterView;
	private DatabaseInterface databaseInterface;
	private AlertDialog invalidDataAlertDialog;
	private AlertDialog registerCompleteAlertDialog;
	private LoginRegisterActivity activity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (LoginRegisterActivity) activity;
		this.databaseInterface = new Database(activity);
		createInvalidDataAlertDialog();
		createRegisterCompleteAlertDialog();
	}
	
	private void createRegisterCompleteAlertDialog() {
		registerCompleteAlertDialog = new AlertDialog.Builder(activity).setTitle(R.string.dialog_register_title)
			.setMessage(R.string.dialog_register_message)
			.setPositiveButton(android.R.string.ok, registerSuccessfulListener).create();
	}
	
	private void createInvalidDataAlertDialog() {
		invalidDataAlertDialog = new AlertDialog.Builder(activity).setTitle(R.string.dialog_data_invalid_tittle)
			.setMessage(R.string.dialog_data_invalid_message).setPositiveButton(R.string.button_correct, null).create();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		loginRegisterView = new LoginRegisterView(getActivity(), null);
		loginRegisterView.setType(LoginRegisterView.TYPE_REGISTER);
		loginRegisterView.setOnButtonClickListener(this);
		return loginRegisterView;
	}
	
	@Override
	public void onClick(View v) {
		String email = loginRegisterView.getEmail();
		String displayName = loginRegisterView.getDisplayName();
		String password = loginRegisterView.getPassword();
		boolean valid = isRegisterDataValid(email, displayName, password);
		
		if (valid) {
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
		if (email.length() == 0 || displayName.length() == 0 || password.length() == 0) return false;
		return isPlayerNotAlreadyRegistered(email);
	}
	
	private boolean isPlayerNotAlreadyRegistered(String email) {
		Player player = databaseInterface.getPlayer(email);
		return player == null;
	}
	
	private final DialogInterface.OnClickListener registerSuccessfulListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			databaseInterface.setLoggedPlayer(loginRegisterView.getEmail());
			activity.finishAndOpenProfileAcitivty();
		}
	};
}