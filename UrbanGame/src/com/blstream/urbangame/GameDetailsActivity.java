package com.blstream.urbangame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class GameDetailsActivity extends SherlockActivity {
	
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Button buttonJoinLeaveGame = (Button) findViewById(R.id.buttonJoinLeaveGame);
		buttonJoinLeaveGame.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDecisionInJoining();
			}
		});
		getMockData();
	}
	
	public void getDecisionInJoining() {
	    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
	    dialogBuilder.setTitle(R.string.dialog_join_title);
	    dialogBuilder.setMessage(R.string.dialog_join_message);
	    dialogBuilder.setCancelable(false);
	    dialogBuilder.setPositiveButton(R.string.dialog_join_positive_button, new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int which) {
	    		// here you can add functions	    		
	    		}
	    		});
	    dialogBuilder.setNegativeButton(R.string.dialog_join_negative_button, new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int which) {
	    		// here you can add functions	    		
	    		}
	    		});
	    dialogBuilder.show();
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
	
	@Override public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
			case android.R.id.home:
				finish();
				break;
		}
		return true;
	}
}