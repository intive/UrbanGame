package com.blstream.urbangame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class GameDetailsActivity extends SherlockActivity implements OnClickListener {

	public static boolean isDialogCompleted = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Button joinLeaveButton = (Button) (findViewById(R.id.buttonJoinLeaveGame));
		joinLeaveButton.setOnClickListener(this);
		getMockData();
		if(!isDialogCompleted) showDialog();
	}

	private void getMockData() {
		Intent intent = getIntent();
		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				// TODO fill view
			}
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.top_bar_menu_more, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		isDialogCompleted = false;
		Log.i("UrbanGame", "Clicked view: " + v);
		if (v == findViewById(R.id.buttonJoinLeaveGame)) {
			showDialog();
		}
	}
	
	public void showDialog()
	{
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle(R.string.dialog_join_title);
		dialogBuilder.setMessage(R.string.dialog_join_message);
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton(R.string.dialog_join_positive_button,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.i("UrbanGame", "Dialog: clicked positibe button");
						isDialogCompleted=true;
					}
				});
		dialogBuilder.setNegativeButton(R.string.dialog_join_negative_button,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Log.i("UrbanGame", "Dialog: clicked negative button");
						isDialogCompleted=true;
					}
				});
		dialogBuilder.show();
	}
}