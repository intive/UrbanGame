package com.blstream.urbangame.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.test.AndroidTestCase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blstream.urbangame.GamesListAdapter;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;
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
	
	private class TestDatabase implements DatabaseInterface {
		
		@Override
		public boolean insertGameShortInfo(UrbanGameShortInfo gameShotInfo) {
			// Not needed for this tests.
			return false;
		}
		
		@Override
		public boolean insertGameInfo(UrbanGame gameInfo) {
			// Not needed for this tests.
			return false;
		}
		
		@Override
		public List<UrbanGameShortInfo> getAllGamesShortInfo() {
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
			return list;
		}
		
		@Override
		public List<UrbanGameShortInfo> getAllGamesShortInfoOrderedByStartTime(int beforeFirst, int howMany) {
			//  Not needed for this tests.
			return null;
		}
		
		@Override
		public UrbanGameShortInfo getGameShortInfo(Long gameID) {
			// Not needed for this tests.
			return null;
		}
		
		@Override
		public UrbanGame getGameInfo(Long gameID) {
			// Not needed for this tests.
			return null;
		}
		
		@Override
		public boolean updateGame(UrbanGame game) {
			// Not needed for this tests.
			return false;
		}
		
		@Override
		public boolean updateGameShortInfo(UrbanGameShortInfo game) {
			// Not needed for this tests.
			return false;
		}
		
		@Override
		public boolean deleteGameInfoAndShortInfo(Long gameID) {
			// Not needed for this tests.
			return false;
		}
		
		@Override
		public boolean insertUser(Player player) {
			// Not needed for this tests.
			return false;
		}
		
		@Override
		public Player getPlayer(String email) {
			// Not needed for this tests.
			return null;
		}
		
		@Override
		public boolean updatePlayer(Player player) {
			// Not needed for this tests.
			return false;
		}
		
		@Override
		public boolean deletePlayer(String email) {
			// Not needed for this tests.
			return false;
		}
		
		@Override
		public boolean insertUserGameSpecific(PlayerGameSpecific playerGameSpecific) {
			// Not needed for this tests.
			return false;
		}
		
		@Override
		public PlayerGameSpecific getUserGameSpecific(String email, Long gameID) {
			// Not needed for this tests.
			return null;
		}
		
		@Override
		public boolean updateUserGameSpecific(PlayerGameSpecific playerGameSpecific) {
			// Not needed for this tests.
			return false;
		}
		
		@Override
		public boolean deleteUserGameSpecific(String email, Long gameID) {
			// Not needed for this tests.
			return false;
		}
		
		@Override
		public boolean wipeOutUserData(String email) {
			// Not needed for this tests.
			return false;
		}
		
		@Override
		public boolean setLoggedPlayer(String email) {
			//  Not needed for this tests.
			return false;
		}
		
		@Override
		public String getLoggedPlayerID() {
			//  Not needed for this tests.
			return null;
		}
		
		@Override
		public boolean setNoOneLogged() {
			//  Not needed for this tests.
			return false;
		}
		
		@Override
		public boolean insertPlayerTaskSpecific(PlayerTaskSpecific taskSpecific) {
			//  Not needed for this tests.
			return false;
		}
		
		@Override
		public PlayerTaskSpecific getPlayerTaskSpecific(Long taskID, String playerEmail) {
			//  Not needed for this tests.
			return null;
		}
		
		@Override
		public boolean updatePlayerTaskSpecific(PlayerTaskSpecific taskSpecific) {
			//  Not needed for this tests.
			return false;
		}
		
		@Override
		public boolean deletePlayerTaskSpecific(Long taskID, String playerEmail) {
			//  Not needed for this tests.
			return false;
		}
		
		@Override
		public boolean insertTaskForGame(Long gameID, Task task) {
			//  Not needed for this tests.
			return false;
		}
		
		@Override
		public List<Task> getTasksForGame(Long gameID) {
			//  Not needed for this tests.
			return null;
		}
		
		@Override
		public Task getTask(Long taskID) {
			//  Not needed for this tests.
			return null;
		}
		
		@Override
		public boolean updateTask(Task task) {
			//  Not needed for this tests.
			return false;
		}
		
		@Override
		public boolean deleteTask(Long taskID) {
			//  Not needed for this tests.
			return false;
		}
		
	}
	
	public GamesListAdapterTest() {
		super();
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		gamesListAdapter = new GamesListAdapter(getContext(), com.blstream.urbangame.R.layout.list_item_game,
			new TestDatabase());
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
	
	public void testSetDatabaseInterface() {
		TestDatabase database = new TestDatabase();
		
		gamesListAdapter.setDatabaseInterface(database);
		
		assertEquals(database, gamesListAdapter.getDatabaseInterface());
		
	}
	
}
