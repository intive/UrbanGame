package com.blstream.urbangame;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.date.TimeLeftBuilder;
import com.blstream.urbangame.dialogs.UrbanGameDialog;
import com.blstream.urbangame.dialogs.UrbanGameDialog.UrbanGameDialogOnClickListener;
import com.blstream.urbangame.web.WebHighLevel;
import com.blstream.urbangame.web.WebHighLevelInterface;
import com.blstream.urbangame.webserver.ServerResponseHandler;
import com.blstream.urbangame.webserver.WebServerNotificationListener;

public class GameDetailsActivity extends AbstractMenuActivity implements OnClickListener, WebServerNotificationListener {
	public static final String TAG = "GameDetailsActivity";
	public static final String GAME_KEY = "gameID";
	public static final Long GAME_NOT_FOUND = -1L;
	public static boolean isDialogCompleted = true;
	private static boolean isSomeoneLogged = false;
	private static boolean isPlayerAParticipantOfCurrentGame = false;
	private long gameID;
	
	private ServerResponseHandler handler;
	private WebHighLevelInterface web;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		gameID = getSelectedGameID();
		
		this.handler = new ServerResponseHandler(this);
		web = new WebHighLevel(handler, this);
		web.downloadGameDetails(gameID);
		
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
		DatabaseInterface database = new Database(GameDetailsActivity.this);
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
		
		UrbanGame selectedGame = null;
		DatabaseInterface di = new Database(getApplicationContext());
		selectedGame = di.getGameInfo(idOfSelectedGame);
		
		// formatter:off
		ImageView  ivGameLogo              = (ImageView) findViewById(R.id.imageViewGameLogo);
		TextView   tvGameName              = (TextView)  findViewById(R.id.textViewGameName);
		TextView   tvOperatorName          = (TextView)  findViewById(R.id.textViewOperatorName);
		TextView   textViewNumberOfPlayers = (TextView)  findViewById(R.id.textViewPlayers);
		TextView   textViewTime            = (TextView)  findViewById(R.id.textViewTime);
		TextView   textViewDifficulty      = (TextView)  findViewById(R.id.textViewDifficulty);
		TextView   tvWinningStrategy       = (TextView)  findViewById(R.id.textViewWinningStrategy);
		TextView   tvPrizes                = (TextView)  findViewById(R.id.textViewPrizes);
		TextView   tvGameDesc              = (TextView)  findViewById(R.id.textViewGameDescription);
		// formatter:on
		
		if (selectedGame != null) {
			ivGameLogo.setImageDrawable(selectedGame.getGameLogoDrawable(getResources()));
			
			tvGameName.setText(selectedGame.getTitle());
			tvOperatorName.setText(selectedGame.getOperatorName());
			String players;
			if (selectedGame.getMaxPlayers() == null || selectedGame.getMaxPlayers() == 0) {
				players = Integer.toString(selectedGame.getPlayers()) + " " + getString(R.string.string_players);
			}
			else {
				players = Integer.toString(selectedGame.getPlayers()) + "/"
					+ Integer.toString(selectedGame.getMaxPlayers()) + " " + getString(R.string.string_players);
			}
			textViewNumberOfPlayers.setText(players);
			TimeLeftBuilder timeLeftBuilder = new TimeLeftBuilder(getResources(), selectedGame.getStartDate(),
				selectedGame.getEndDate());
			textViewTime.setText(timeLeftBuilder.getLeftTime());
			textViewDifficulty.setText(getResources().getStringArray(R.array.difficulty_levels)[selectedGame
				.getDifficulty()]);
			tvPrizes.setText(selectedGame.getPrizesInfo());
			tvGameDesc.setText(selectedGame.getDescription());
			tvWinningStrategy.setText(selectedGame.getWinningStrategy());
		}
		
		//setting headers
		setHeaderText(R.id.prizesHeader, R.string.header_prizes);
		setHeaderText(R.id.descriptionHeader, R.string.header_description);
		
		this.supportInvalidateOptionsMenu();
		Log.i(TAG, "onResume completed");
	}
	
	private void setHeaderText(int headerID, int textID) {
		View header = findViewById(headerID);
		TextView headerText = (TextView) header.findViewById(R.id.TextViewMyGamesHeader);
		headerText.setText(textID);
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
		UrbanGameDialog.DialogBuilder dialogBuilder = new UrbanGameDialog.DialogBuilder(this);
		dialogBuilder.setTitle(!isPlayerAParticipantOfCurrentGame ? R.string.dialog_join_title
			: R.string.dialog_leave_title);
		dialogBuilder.setMessage(R.string.dialog_join_leave_message);
		dialogBuilder.setCancelable(false);
		dialogBuilder.setPositiveButton(R.string.dialog_join_leave_positive_button,
			new UrbanGameDialogOnClickListener() {
				private static final long serialVersionUID = 1L;
				
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
			new UrbanGameDialogOnClickListener() {
				private static final long serialVersionUID = 1L;
				
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
		
		web.joinCurrentPlayerToTheGame(getSelectedGameID());
		
		isPlayerAParticipantOfCurrentGame = true;
		startActivityAfterCompletedJoinAction();
	}
	
	private void startActivityAfterCompletedJoinAction() {
		Intent intent = new Intent(GameDetailsActivity.this, ActiveGameActivity.class);
		startActivity(intent);
	}
	
	private void leavePlayerFromGame() {
		web.leaveCurrentPlayerFromTheGame(getSelectedGameID());
		
		isPlayerAParticipantOfCurrentGame = false;
		setJoinLeaveButtonText((Button) findViewById(R.id.buttonJoinLeaveGame));
	}
	
	@Override
	public void onWebServerResponse(Message message) {
		// TODO implement on response behavior
		// FIXME setting views should be moved here
	}
}