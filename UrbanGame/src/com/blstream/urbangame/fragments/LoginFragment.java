package com.blstream.urbangame.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.blstream.urbangame.LoginRegisterActivity;
import com.blstream.urbangame.R;
import com.blstream.urbangame.dialogs.UrbanGameDialog;
import com.blstream.urbangame.session.LoginManager;

public class LoginFragment extends SherlockFragment implements OnClickListener {
	private LoginRegisterView loginRegisterView;
	private UrbanGameDialog.DialogBuilder invalidDataAlertDialog;
	private LoginRegisterActivity activity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (LoginRegisterActivity) activity;
		createAlertDialog();
	}
	
	// formatter:off
	private void createAlertDialog() {
		invalidDataAlertDialog = 
			new UrbanGameDialog.DialogBuilder(activity)
				.setTitle(R.string.dialog_data_invalid_tittle)
				.setMessage(R.string.dialog_data_invalid_message)
				.setPositiveButton(R.string.button_correct, null)
				.create();
	}
	// formatter:on
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		loginRegisterView = new LoginRegisterView(getActivity(), null);
		loginRegisterView.setOnButtonClickListener(this);
		return loginRegisterView;
	}
	
	@Override
	public void onClick(View v) {
		String email = loginRegisterView.getEmail();
		String password = loginRegisterView.getPassword();
		boolean isLoginDataValid = loginRegisterView.isDataCorrect()
			&& LoginManager.getInstance(activity).isLoginDataValid(email, password);
		
		if (isLoginDataValid) {
			setLoggedUserAndStartBrowsing(email);
		}
		else {
			showInvalidDataAlertDialog();
		}
	}
	
	private void setLoggedUserAndStartBrowsing(String email) {
		activity.loginUser(email);
	}
	
	private void showInvalidDataAlertDialog() {
		invalidDataAlertDialog.show();
	}
}