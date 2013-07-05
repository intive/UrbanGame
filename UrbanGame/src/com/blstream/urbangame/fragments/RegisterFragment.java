package com.blstream.urbangame.fragments;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.dialogs.UrbanGameDialog;
import com.blstream.urbangame.dialogs.UrbanGameDialog.UrbanGameDialogOnClickListener;
import com.blstream.urbangame.session.RegistrationManager;
import com.blstream.urbangame.session.SessionManager;
import com.blstream.urbangame.webserver.WebResponse;
import com.blstream.urbangame.webserver.WebServerNotificationListener;

public class RegisterFragment extends SherlockFragment implements OnClickListener, WebServerNotificationListener {
	private static final String NAME = RegisterFragment.class.getSimpleName();
	
	private LoginRegisterView loginRegisterView;
	private UrbanGameDialog.DialogBuilder invalidDataAlertDialog;
	private UrbanGameDialog.DialogBuilder userAlreadyExistsAlertDialog;
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
		createUserAlreadyExistsAlertDialog();
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
			.setPositiveButton(R.string.button_correct,  new UrbanGameDialogOnClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.create();
	}
	
	private void createUserAlreadyExistsAlertDialog() {
		userAlreadyExistsAlertDialog = new UrbanGameDialog.DialogBuilder(activity)
			.setTitle(R.string.dialog_data_invalid_tittle)
			.setMessage(R.string.dialog_user_already_exists)
			.setPositiveButton(R.string.button_correct,  new UrbanGameDialogOnClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
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
		boolean isRegisterDataValid = loginRegisterView.isDataSyntaxCorrect();
		
		if (isRegisterDataValid) {
			register(email, displayName, password);
		}
		else {
			showInvalidDataAlertDialog();
		}
	}
	
	private void showInvalidDataAlertDialog() {
		invalidDataAlertDialog.show();
	}
	
	private void register(String email, String displayName, String password) {
		new RegistrationManager(activity, this).register(email, displayName, password);
	}
	
	private final UrbanGameDialogOnClickListener registerSuccessfulListener = new UrbanGameDialogOnClickListener() {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			activity.setLoggedUserInDB(email);
			dialog.dismiss();
		}
	};
	
	@Override
	public void onWebServerResponse(Message message) {
		Log.d(SessionManager.TAG, NAME + " onWebServerResponse()");
		WebResponse webResponse = (WebResponse) message.obj;
		if (webResponse.isValid()) {
			Player player = new Player(email, password, displayName, (String) null);
			(new RegistrationManager(activity, this)).storeUserInDB(player);
			showRegistrationCompleteAlertDialog();
		}
		else {
			userAlreadyExistsAlertDialog.show();
		}
	}
	
	private void showRegistrationCompleteAlertDialog() {
		registerCompleteAlertDialog.show();
	}
}