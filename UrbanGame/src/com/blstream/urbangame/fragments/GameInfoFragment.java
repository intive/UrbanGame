package com.blstream.urbangame.fragments;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.blstream.urbangame.GameDetailsActivity;
import com.blstream.urbangame.R;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.UrbanGame;

public class GameInfoFragment extends SherlockFragment {
	private static final String DATE_FORMAT = "E, MMM d 'at' h:mm a";
	private Activity activity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_game_details, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		// formatter:off
		Button 		joinLeaveGameButton 		= (Button) 		view.findViewById(R.id.buttonJoinLeaveGame);
		ImageView 	ivGameLogo 					= (ImageView) 	view.findViewById(R.id.imageViewGameLogo);
		TextView 	tvGameName 					= (TextView) 	view.findViewById(R.id.textViewGameName);
		TextView 	tvOperatorName 				= (TextView) 	view.findViewById(R.id.textViewOperatorName);
		TextView 	tvNumberOfTotalPlayers 		= (TextView) 	view.findViewById(R.id.textViewNumberOfTotalPlayers);
		TextView 	tvNumberOfCurrentPlayers 	= (TextView) 	view.findViewById(R.id.textViewNumberOfCurrentPlayers);
		TextView 	tvStartTime 				= (TextView) 	view.findViewById(R.id.textViewStartTime);
		TextView 	tvEndTime 					= (TextView) 	view.findViewById(R.id.textViewEndTime);
		RatingBar 	rbDifficulty 				= (RatingBar)	view.findViewById(R.id.ratingBarDifficulty);
		TextView 	tvPrizes 					= (TextView) 	view.findViewById(R.id.TextViewPrizes);
		TextView 	tvGameDesc 					= (TextView) 	view.findViewById(R.id.textViewGameDescription);
		TextView 	tvWinningStrategy 			= (TextView) 	view.findViewById(R.id.textViewWinningStrategy);
		// formatter:on
		
		configureCommonView(view, joinLeaveGameButton);
		
		UrbanGame selectedGame = getCurrentGame();
		if (selectedGame != null) {
			ivGameLogo.setImageDrawable(selectedGame.getGameLogoDrawable(getResources()));
			tvGameName.setText(selectedGame.getTitle());
			tvOperatorName.setText(selectedGame.getOperatorName());
			tvNumberOfTotalPlayers.setText(String.valueOf(selectedGame.getMaxPlayers()));
			tvNumberOfCurrentPlayers.setText(String.valueOf(selectedGame.getPlayers()));
			CharSequence parsedStartDate = formatDate(selectedGame.getStartDate());
			tvStartTime.setText(parsedStartDate);
			CharSequence parsedEndDate = formatDate(selectedGame.getEndDate());
			tvEndTime.setText(parsedEndDate);
			rbDifficulty.setRating(selectedGame.getDifficulty());
			tvPrizes.setText(selectedGame.getPrizesInfo());
			tvGameDesc.setText(selectedGame.getDescription());
			tvWinningStrategy.setText(selectedGame.getWinningStrategy());
		}
	}
	
	protected void configureCommonView(View view, Button joinLeaveGameButton) {
		joinLeaveGameButton.setVisibility(View.GONE);
		configureHeaders(view);
	}
	
	protected void configureHeaders(View view) {
		setHeaderText(view, R.id.detailsHeader, R.string.header_details);
		setHeaderText(view, R.id.PrizesHeader, R.string.header_prizes);
		setHeaderText(view, R.id.descriptionHeader, R.string.header_description);
	}
	
	protected CharSequence formatDate(Date selectedGame) {
		return DateFormat.format(DATE_FORMAT, selectedGame);
	}
	
	private void setHeaderText(View view, int headerID, int textID) {
		View header = view.findViewById(headerID);
		TextView headerText = (TextView) header.findViewById(R.id.TextViewMyGamesHeader);
		headerText.setText(textID);
	}
	
	private UrbanGame getCurrentGame() {
		Long gameID = getCurrentGameID();
		UrbanGame currentGame = getGameOfID(gameID);
		return currentGame;
	}
	
	protected Long getCurrentGameID() {
		Bundle bundle = getArguments();
		Long gameID = 1L;
		if (bundle != null) {
			gameID = bundle.getLong(GameDetailsActivity.GAME_KEY);
		}
		return gameID;
	}
	
	protected UrbanGame getGameOfID(Long gameID) {
		DatabaseInterface database = new Database(activity);
		UrbanGame currentGame = database.getGameInfo(gameID);
		database.closeDatabase();
		return currentGame;
	}
}