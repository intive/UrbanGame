package com.blstream.urbangame;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
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

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.example.ExampleData;

public class GameDetailsActivity extends AbstractMenuActivity implements OnClickListener {
	public static final String TAG = "GameDetailsActivity";
	public static final String GAME_KEY = "gameID";
	public static final Long GAME_NOT_FOUND = -1L;
	public static boolean isDialogCompleted = true;
	private static boolean isSomeoneLogged = false;
	private static boolean isPlayerAParticipantOfCurrentGame = false;
	
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
	
	//setting button text
	private void setJoinLeaveButtonText(Button joinLeaveButton) {
		if (isPlayerAParticipantOfCurrentGame) {
			joinLeaveButton.setText(R.string.button_leave);
		}
		else {
			joinLeaveButton.setText(R.string.button_join);
		}
	}
	
	//checks is player a participant of current game
	private void checkIsPlayerIsLogedAndIsParticipantOfCurrentGame(Long idOfSelectedGame) {
		DatabaseInterface database = new Database(getApplicationContext());
		String loggedPlayerEmail = database.getLoggedPlayerID();
		if (loggedPlayerEmail != null) {
			isSomeoneLogged = true;
			PlayerGameSpecific playerGameSpecific = database.getUserGameSpecific(loggedPlayerEmail, idOfSelectedGame);
			if (playerGameSpecific != null && playerGameSpecific.getState() == PlayerGameSpecific.GAME_ACTIVE) {// if player has that it means he has joined in game previously
				isPlayerAParticipantOfCurrentGame = true;
				return;
			}
		}
		else {
			isSomeoneLogged = false;
		}
		isPlayerAParticipantOfCurrentGame = false;
		database.closeDatabase();
	}
	
	//gets game id from intent
	private Long getSelectedGameID() {
		Intent intent = getIntent();
		Long idOfSelectedGame = GAME_NOT_FOUND;
		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				idOfSelectedGame = extras.getLong(GAME_KEY, GAME_NOT_FOUND);
			}
		}
		return idOfSelectedGame;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Long idOfSelectedGame = getSelectedGameID();
		checkIsPlayerIsLogedAndIsParticipantOfCurrentGame(idOfSelectedGame); //after this step we know is player logged and is he participates a game in class's fields isSomeoneLogged and isPlayerAParticipantOfCurrentGame 
		setJoinLeaveButtonText((Button) findViewById(R.id.buttonJoinLeaveGame));
		
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
		
		//setting headers
		setHeaderText(R.id.detailsHeader, R.string.header_details);
		setHeaderText(R.id.PrizesHeader, R.string.header_prizes);
		setHeaderText(R.id.descriptionHeader, R.string.header_description);
		
		this.supportInvalidateOptionsMenu();
		Log.i(TAG, "onResume completed");
	}
	
	private void setHeaderText(int headerID, int textID) {
		View header = findViewById(headerID);
		TextView headerText = (TextView) header.findViewById(R.id.TextViewMyGamesHeader);
		headerText.setText(textID);
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
	public void onClick(View v) {
		isDialogCompleted = false;
		Log.i("UrbanGame", "Clicked view: " + v);
		if (v == findViewById(R.id.buttonJoinLeaveGame)) {
			if (!isPlayerAParticipantOfCurrentGame) {//join game
				Log.i(TAG, "Joining the game button clicked");
				showDialog();
			}
			else { //leave game
				Log.i(TAG, "Leaving the game");
				showDialog();
			}
		}
	}
	
	public void showDialog() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle(!isPlayerAParticipantOfCurrentGame ? R.string.dialog_join_title
			: R.string.dialog_leave_title);
		dialogBuilder.setMessage(R.string.dialog_join_leave_message);
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton(R.string.dialog_join_leave_positive_button,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.i("UrbanGame", "Dialog: clicked positive button");
					isDialogCompleted = true;
					
					if (isSomeoneLogged) {
						if (isPlayerAParticipantOfCurrentGame) {
							leavePlayerFromGame();
						}
						else {
							Log.i(TAG, "There's already logged player");
							joinPlayerToTheGame();
						}
					}
					else {
						Log.i(TAG, "There's no one logged");
						//starts login activity
						Intent intent = new Intent(GameDetailsActivity.this, LoginRegisterActivity.class);
						intent.putExtra(LoginRegisterActivity.ACTION_AFTER_LOGIN,
							LoginRegisterActivity.ACTION_RETURN_LOGIN_RESULT);
						startActivityForResult(intent, LoginRegisterActivity.LOGIN_REQUEST_CODE);
					}
				}
			});
		dialogBuilder.setNegativeButton(R.string.dialog_join_leave_negative_button,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.i("UrbanGame", "Dialog: clicked negative button");
					isDialogCompleted = true;
				}
			});
		dialogBuilder.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == LoginRegisterActivity.LOGIN_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Log.i(TAG, "Logged properly");
				joinPlayerToTheGame();
			}
			else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i(TAG, "NOT Logged properly");
			}
		}
	}
	
	private void joinPlayerToTheGame() {
		Log.i(TAG, "Joning the game");
		// FIXME invocation to server should occur here
		//******************//
		//					//
		//	   M O C K		//
		//					//
		//******************//
		DatabaseInterface db = new Database(getApplicationContext());
		String playerEmail = db.getLoggedPlayerID();
		Long gameID = getSelectedGameID();
		Log.i(TAG, "Sending information to server that player " + playerEmail + " want to join the game " + gameID);
		
		// FIXME code below should be invoked after positive response from server [move that code when appropriate class is ready]
		int rankFromServer = 10;
		PlayerGameSpecific playerGameInfo = new PlayerGameSpecific(rankFromServer, playerEmail, gameID, null, false);
		playerGameInfo.setState(PlayerGameSpecific.GAME_ACTIVE);
		PlayerGameSpecific actual = db.getUserGameSpecific(playerEmail, gameID);
		if (actual == null) {
			db.insertUserGameSpecific(playerGameInfo);
		}
		else {
			db.updateUserGameSpecific(playerGameInfo);
		}
		db.closeDatabase();
		//******************//
		//					//
		//	M O C K  E N D	//
		//					//
		//******************//
		isPlayerAParticipantOfCurrentGame = true;
		startActivityAfterCompletedJoinAction();
	}
	
	private void startActivityAfterCompletedJoinAction() {
		Intent intent = new Intent(GameDetailsActivity.this, MyGamesActivity.class);
		startActivity(intent);
	}
	
	private void leavePlayerFromGame() {
		// FIXME invocation to server should occur here
		//******************//
		//					//
		//	   M O C K		//
		//					//
		//******************//
		DatabaseInterface db = new Database(getApplicationContext());
		String playerEmail = db.getLoggedPlayerID();
		Long gameID = getSelectedGameID();
		Log.i(TAG, "Sending information to server that player " + playerEmail + " leaved the game " + gameID);
		
		// FIXME code below should be invoked after positive response from server [move that code when appropriate class is ready]
		db.deleteUserGameSpecific(playerEmail, gameID);
		db.closeDatabase();
		((Button) findViewById(R.id.buttonJoinLeaveGame)).setText(R.string.button_join);
		//******************//
		//					//
		//	M O C K  E N D	//
		//					//
		//******************//
		isPlayerAParticipantOfCurrentGame = false;
	}
}