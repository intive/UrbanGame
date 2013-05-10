package com.blstream.urbangame.test;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.test.AndroidTestCase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blstream.urbangame.GamesListAdapter;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;

public class GamesListAdapterTest extends AndroidTestCase {
	
	private GamesListAdapter gamesListAdapter;
	
	private final int idTextViewGameName = com.blstream.urbangame.R.id.textViewGameName;
	private final int idTextViewOperatorName = com.blstream.urbangame.R.id.textViewOperatorName;
	private final int idTextViewLocation = com.blstream.urbangame.R.id.textViewLocation;
	private final int idTextViewNumberOfCurrentPlayers = com.blstream.urbangame.R.id.textViewNumberOfCurrentPlayers;
	private final int idTextViewNumberOfTotalPlayers = com.blstream.urbangame.R.id.textViewNumberOfTotalPlayers;
	private final int idTextViewStartDate = com.blstream.urbangame.R.id.textViewStartTime;
	
	private static final String LOCATION = "London";
	private static final String GAME_NAME = "Sightseeing London";
	private static final String OPERATOR_NAME = "London City Hall";
	private static final int NUMBER_OF_PLAYERS = 20;
	private static final int NUMBER_OF_MAX_PLAYERS = 100;
	
	public GamesListAdapterTest() {
		super();
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		ArrayList<UrbanGameShortInfo> list = new ArrayList<UrbanGameShortInfo>();
		UrbanGameShortInfo game = new UrbanGameShortInfo();
		game.setLocation(LOCATION);
		game.setTitle(GAME_NAME);
		game.setOperatorName(OPERATOR_NAME);
		game.setMaxPlayers(NUMBER_OF_MAX_PLAYERS);
		game.setPlayers(NUMBER_OF_PLAYERS);
		game.setReward(true);
		game.setStartDate(new Date());
		game.setEndDate(new Date());
		list.add(game);
		
		gamesListAdapter = new GamesListAdapter(getContext(), com.blstream.urbangame.R.layout.list_item_game, list);
	}
	
	/**
	 * Sets text of TextView that is sub-view of the parameter view and is
	 * identified by the parameter resource.
	 * 
	 * @param view
	 * @param resource
	 * @param text
	 */
	private void setTextOfTextView(View view, int resource, String text) {
		TextView textView = (TextView) view.findViewById(resource);
		textView.setText(text);
	}
	
	/**
	 * Gets text of TextView that is sub-view of the parameter view and is
	 * identified by the parameter resource.
	 * 
	 * @param view
	 * @param resource
	 * @return
	 */
	private String getTextOfTextView(View view, int resource) {
		return ((TextView) view.findViewById(resource)).getText().toString();
	}
	
	private View createEmptyView() {
		View view = null;
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(com.blstream.urbangame.R.layout.list_item_game, null);
		
		setTextOfTextView(view, idTextViewGameName, "");
		setTextOfTextView(view, idTextViewOperatorName, "");
		setTextOfTextView(view, idTextViewLocation, "");
		setTextOfTextView(view, idTextViewNumberOfCurrentPlayers, "");
		setTextOfTextView(view, idTextViewNumberOfTotalPlayers, "");
		setTextOfTextView(view, idTextViewStartDate, "");
		
		return view;
	}
	
	public void testGetView() {
		View view = createEmptyView();
		
		view = gamesListAdapter.getView(0, null, null);
		
		assertEquals(GAME_NAME, getTextOfTextView(view, idTextViewGameName));
		assertEquals(LOCATION, getTextOfTextView(view, idTextViewLocation));
		assertEquals(OPERATOR_NAME, getTextOfTextView(view, idTextViewOperatorName));
		assertEquals(NUMBER_OF_PLAYERS, Integer.valueOf(getTextOfTextView(view, idTextViewNumberOfCurrentPlayers))
			.intValue());
		assertEquals(NUMBER_OF_MAX_PLAYERS, Integer.valueOf(getTextOfTextView(view, idTextViewNumberOfTotalPlayers))
			.intValue());
	}
	
}
