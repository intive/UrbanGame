package com.blstream.urbangame;

import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.example.ExampleData;

public class GameDetailsActivity extends SherlockActivity implements OnClickListener {
	
	public static final String GAME_KEY = "gameID";
	public static final Long GAME_NOT_FOUND = -1L;
	public static boolean isDialogCompleted = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Button joinLeaveButton = (Button) (findViewById(R.id.buttonJoinLeaveGame));
		joinLeaveButton.setOnClickListener(this);
		if (!isDialogCompleted) {
			showDialog();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		Long idOfSelectedGame = GAME_NOT_FOUND;
		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				idOfSelectedGame = extras.getLong(GAME_KEY, GAME_NOT_FOUND);
			}
		}
		
		/* FIXME 
		 * If clause below should be rewritten, when listview will be finished.
		*/
		UrbanGame selectedGame = null;
		if (idOfSelectedGame == GAME_NOT_FOUND) {
			selectedGame = getMockGame();
		}
		else {
			DatabaseInterface di = new Database(getApplicationContext());
			selectedGame = di.getGameInfo(idOfSelectedGame);
		}
		
		ImageView ivGameLogo = (ImageView) findViewById(R.id.imageViewGameLogo);
		TextView tvGameName = (TextView) findViewById(R.id.textViewGameName);
		ImageView ivOperatorLogo = (ImageView) findViewById(R.id.imageViewOperatorLogo);
		TextView tvOperatorName = (TextView) findViewById(R.id.textViewOperatorName);
		TextView tvNumberOfTotalPlayers = (TextView) findViewById(R.id.textViewNumberOfTotalPlayers);
		TextView tvNumberOfCurrentPlayers = (TextView) findViewById(R.id.textViewNumberOfCurrentPlayers);
		TextView tvStartTime = (TextView) findViewById(R.id.textViewStartTime);
		TextView tvEndTime = (TextView) findViewById(R.id.textViewEndTime);
		RatingBar rbDifficulty = (RatingBar) findViewById(R.id.ratingBarDifficulty);
		TextView tvPrizes = (TextView) findViewById(R.id.TextViewPrizes);
		TextView tvGameDesc = (TextView) findViewById(R.id.textViewGameDescription);
		TextView tvWinningStrategy = (TextView) findViewById(R.id.textViewWinningStrategy);
		
		ivGameLogo.setImageDrawable(selectedGame.getGameLogoDrawable(getResources()));
		ivOperatorLogo.setImageDrawable(selectedGame.getOperatorLogoDrawable(getResources()));
		
		tvGameName.setText(selectedGame.getTitle());
		tvOperatorName.setText(selectedGame.getOperatorName());
		tvNumberOfTotalPlayers.setText(selectedGame.getMaxPlayers() + "");
		tvNumberOfCurrentPlayers.setText(selectedGame.getPlayers() + "");
		CharSequence parsedStartDate = DateFormat.format("E, MMM d 'at' h:mm a", selectedGame.getStartDate());
		tvStartTime.setText(parsedStartDate);
		CharSequence parsedEndDate = DateFormat.format("E, MMM d 'at' h:mm a", selectedGame.getEndDate());
		tvEndTime.setText(parsedEndDate);
		rbDifficulty.setRating(selectedGame.getDifficulty());
		tvPrizes.setText(selectedGame.getPrizesInfo());
		tvGameDesc.setText(selectedGame.getDescription());
		tvWinningStrategy.setText(selectedGame.getWinningStrategy());
	}
	
	private UrbanGame getMockGame() {
		Long id;
		Double gameVersion;
		String title;
		String operatorName;
		String winningStrategy;
		Integer players;
		Integer maxPlayers;
		Date startDate;
		Date endDate;
		Integer difficulty;
		Boolean reward;
		String prizesInfo;
		String description;
		String gameLogoBase64;
		String operatorLogoBase64;
		String comments;
		String location;
		String detailsLink;
		
		id = 1L;
		gameVersion = 1.0;
		title = "Game title";
		operatorName = "Operator";
		winningStrategy = "Winning strategy";
		players = 211;
		maxPlayers = 311;
		Calendar c = Calendar.getInstance();
		c.set(2013, 7, 30, 13, 31);
		startDate = c.getTime();
		c.set(2013, 8, 15, 14, 41);
		endDate = c.getTime();
		difficulty = 5;
		reward = true;
		prizesInfo = "Prize description";
		description = "Game description";
		gameLogoBase64 = ExampleData.LOGO_BASE_64;
		operatorLogoBase64 = ExampleData.OPERATOR_LOGO_BASE_64;
		comments = "Comments";
		location = "Location";
		detailsLink = "www.domain.highdomain";
		
		return new UrbanGame(id, gameVersion, title, operatorName, winningStrategy, players, maxPlayers, startDate,
			endDate, difficulty, reward, prizesInfo, description, gameLogoBase64, operatorLogoBase64, comments,
			location, detailsLink);
		
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
	
	public void showDialog() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle(R.string.dialog_join_title);
		dialogBuilder.setMessage(R.string.dialog_join_message);
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton(R.string.dialog_join_positive_button, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i("UrbanGame", "Dialog: clicked positibe button");
				isDialogCompleted = true;
			}
		});
		dialogBuilder.setNegativeButton(R.string.dialog_join_negative_button, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i("UrbanGame", "Dialog: clicked negative button");
				isDialogCompleted = true;
			}
		});
		dialogBuilder.show();
	}
}