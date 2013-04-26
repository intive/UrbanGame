package com.blstream.urbangame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.Player;

public class LoggingInActivity extends SherlockActivity {
	public static boolean isRegisterDialogCompleted = true;
	public static boolean isInvalidDataDialogCompleted = true;
	public static final String TAG = "LoggingInActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSupportProgressBarVisibility(true);
		setContentView(R.layout.logging_in);
		if (!isRegisterDialogCompleted) {
			showRegisterDialog();
		}
		if (!isInvalidDataDialogCompleted) {
			showInvalidDataDialog();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.top_bar_menu_more, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		CheckBox checkBoxShowPassword = ((CheckBox) findViewById(R.id.checkBoxShowPassword));
		checkBoxShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
				switch (editTextPassword.getInputType()) {
					case InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD:
						hidePassword();
						break;
					case InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD:
						showPassword();
						break;
				}
				
			}
		});
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		return super.onMenuItemSelected(featureId, item);
		
	}
	
	public void showPassword() {
		EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		
	}
	
	public void hidePassword() {
		EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
	}
	
	public void saveLoggedStatusInDB(String email) {
		DatabaseInterface di = new Database(getApplicationContext());
		di.setLoggedPlayer(email);
	}
	
	public void onRegisterButtonClicked(View v) {
		isRegisterDialogCompleted = false;
		showRegisterDialog();
	}
	
	public void onLogInButtonClicked(View v) {
		Log.i(TAG, "onLogInButtonClicked()");
		String email = ((EditText) findViewById(R.id.editTextEmail)).toString();
		String password = ((EditText) findViewById(R.id.editTextPassword)).toString();
		boolean valid = checkLoginAndPassword(email, password);
		if (valid) {
			Log.i(TAG, "validData");
			saveLoggedStatusInDB(email);
			//FIXME change below to open ProfileActivity 
			//Intent intent = new Intent(_____________, GameDetailsActivity.class);
			//startActivity(intent);
		}
		else {
			Log.i(TAG, "invalidData");
			showInvalidDataDialog();
		}
		
	}
	
	public boolean checkLoginAndPassword(String email, String password) {
		Log.i(TAG, "checkLoginAndPassword()");
		DatabaseInterface di;
		di = new Database(getApplicationContext());
		Player player = di.getPlayer(email);
		if (player == null) {
			isInvalidDataDialogCompleted = false;
			return false;
		}
		return player.getPassword().equals(password);
	}
	
	public void showRegisterDialog() {
		Log.i(TAG, "showRegisterDialog()");
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle(R.string.dialog_register_title);
		dialogBuilder.setMessage(R.string.dialog_register_message);
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton(R.string.dialog_register_button_positive,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					isRegisterDialogCompleted = true;
					
					//FIXME change below to open ProfileActivity 
					//Intent intent = new Intent(____________, GameDetailsActivity.class);
					//startActivity(intent);
					
				}
			});
		dialogBuilder.show();
	}
	
	public void showInvalidDataDialog() {
		Log.i(TAG, "showInvalidDataDialog()");
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle(R.string.invalid_data_tittle);
		dialogBuilder.setMessage(R.string.invalid_data_message);
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton(R.string.invalid_data_button, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				isInvalidDataDialogCompleted = true;
			}
		});
		dialogBuilder.show();
	}
	
}
