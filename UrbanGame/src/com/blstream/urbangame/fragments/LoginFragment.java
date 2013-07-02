package com.blstream.urbangame.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.blstream.urbangame.LoginRegisterActivity;
import com.blstream.urbangame.R;
import com.blstream.urbangame.dialogs.UrbanGameDialog;
import com.blstream.urbangame.session.LoginManager;
import com.blstream.urbangame.session.SessionManager;
import com.blstream.urbangame.web.WebHighLevel;
import com.blstream.urbangame.web.WebHighLevelInterface;
import com.blstream.urbangame.webserver.ServerResponseHandler;
import com.blstream.urbangame.webserver.WebResponse;
import com.blstream.urbangame.webserver.WebServerNotificationListener;

public class LoginFragment extends SherlockFragment implements OnClickListener, WebServerNotificationListener {
	private static final String NAME = LoginFragment.class.getSimpleName();
	
	private LoginRegisterView loginRegisterView;
	private UrbanGameDialog.DialogBuilder invalidDataAlertDialog;
	private UrbanGameDialog.DialogBuilder invalidLoginAlertDialog;
	private LoginRegisterActivity activity;
	
	protected ServerResponseHandler handler;
	protected WebHighLevelInterface web;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (LoginRegisterActivity) activity;
		
		this.handler = new ServerResponseHandler(this);
		this.web = new WebHighLevel(handler, activity);
		
		createAlertDialog();
		createInvalidLoginAlertDialog();
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
	
	private void createInvalidLoginAlertDialog() {
		invalidLoginAlertDialog = 
			new UrbanGameDialog.DialogBuilder(activity)
				.setTitle(R.string.dialog_data_invalid_tittle)
				.setMessage(R.string.dialog_invalid_login)
				.setPositiveButton(R.string.button_correct, null)
				.create();
	}
	// formatter:on
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		loginRegisterView = new LoginRegisterView(activity, null);
		loginRegisterView.setOnButtonClickListener(this);
		return loginRegisterView;
	}
	
	@Override
	public void onClick(View v) {
		String email = loginRegisterView.getEmail();
		String password = loginRegisterView.getPassword();
		boolean isLoginDataSyntaxValid = loginRegisterView.isDataSyntaxCorrect();
		
		if (isLoginDataSyntaxValid) {
			new LoginManager(activity, this).loginUser(email, password);
		}
		else {
			showInvalidDataAlertDialog();
		}
	}
	
	private void showInvalidDataAlertDialog() {
		invalidDataAlertDialog.show();
	}
	
	@Override
	public void onWebServerResponse(Message message) {
		Log.d(SessionManager.TAG, NAME + " onWebServerResponse()");
		boolean invalidData = processServerResponse(message);
		if (invalidData) {
			invalidLoginAlertDialog.show();
		}
		else {
			setLoggedUserAndStartBrowsing(loginRegisterView.getEmail());
		}
	}
	
	private boolean processServerResponse(Message message) {
		JSONObject jsonObject = null;
		boolean invalidData = false;
		try {
			jsonObject = new JSONObject(((WebResponse) message.obj).getResponse());
		}
		catch (JSONException e) {
			invalidData = true;
		}
		try {
			if (jsonObject != null) {
				if (jsonObject.getString("code").equals("401")) {
					invalidData = true;
				}
			}
		}
		catch (JSONException e) {}
		return invalidData;
	}
	
	private void setLoggedUserAndStartBrowsing(String email) {
		activity.setLoggedUserInDB(email);
	}
}