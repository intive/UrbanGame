package com.blstream.urbangame.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper implements DatabaseInterface {
	
	// database standard info
	public static final String DATABASE_NAME = "urban_game";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_PASS = "adslkfnalskgfasdqwfuaiheofidjkslfa";
	
	// tables
	private static final String GAMES_TABLE_NAME = "games";
	private static final String USER_TABLE_NAME = "user";
	private static final String USER_GAMES_SPECIFIC_TABLE_NAME = "userGamesSpecific";
	private static final String USER_LOGGED_IN_TABLE_NAME = "userLoggedIn";
	
	// tables columns
	// ---- Games
	private static final String GAMES_KEY_ID = "gid";
	private static final String GAMES_KEY_VERSION = "GVersion";
	private static final String GAMES_KEY_TITLE = "GTitle";
	private static final String GAMES_KEY_DESCRIPTION = "GDescription";
	private static final String GAMES_KEY_GAME_ICON = "GIcon";
	private static final String GAMES_KEY_OPERATOR_ICON = "GIconOperator";
	private static final String GAMES_KEY_OPERATOR_NAME = "GOperatorName";
	private static final String GAMES_KEY_NUMBER_OF_PLAYERS = "GNumberOfPlayers";
	private static final String GAMES_KEY_NUMBER_OF_MAX_PLAYERS = "GNumberOfMAXPlayers";
	private static final String GAMES_KEY_DIFFICULTY = "GDifficulty";
	private static final String GAMES_KEY_CITY_NAME = "GCityName";
	private static final String GAMES_KEY_START_DATE = "GStartDate";
	private static final String GAMES_KEY_END_DATE = "GEndDate";
	private static final String GAMES_KEY_COMMENTS = "GComments";
	private static final String GAMES_KEY_REWARD = "GReward";
	private static final String GAMES_KEY_WINNING_STRATEGY = "GWinningStrategy";
	private static final String GAMES_KEY_PRIZE_INFO = "GPrizeInfo";
	private static final String GAMES_KEY_DETAILS_LINK = "GDetailsLink";
	
	// ---- User
	private static final String USER_KEY_EMAIL = "UEmail";
	private static final String USER_KEY_PASSWORD = "UPassword";
	private static final String USER_KEY_DISPLAY_NAME = "UDispName";
	private static final String USER_KEY_AVATAR = "UAvatar";
	
	// ---- User games specific
	private static final String USER_GAMES_SPECIFIC_KEY_EMAIL = "UGSEmail";
	private static final String USER_GAMES_SPECIFIC_KEY_GAME_ID = "UGSGameID";
	private static final String USER_GAMES_SPECIFIC_KEY_RANK = "UGSRank";
	private static final String USER_GAMES_SPECIFIC_KEY_GAME_ACTIVE_OBSERVED = "UGSGameActiveObserved";
	
	// ---- User logging table
	private static final String USER_LOGGED_IN_EMAIL = "ULIEmail";
	
	// tables creation strings
	private static final String CREATE_GAMES_TABLE = "CREATE TABLE " + GAMES_TABLE_NAME + " (" + GAMES_KEY_ID
		+ " INTEGER PRIMARY KEY, " + GAMES_KEY_VERSION + " REAL, " + GAMES_KEY_TITLE + " TEXT, "
		+ GAMES_KEY_DESCRIPTION + " TEXT, " + GAMES_KEY_GAME_ICON + " TEXT, " + GAMES_KEY_OPERATOR_ICON + " TEXT, "
		+ GAMES_KEY_OPERATOR_NAME + " TEXT, " + GAMES_KEY_NUMBER_OF_PLAYERS + " INTEGER, "
		+ GAMES_KEY_NUMBER_OF_MAX_PLAYERS + " INTEGER, " + GAMES_KEY_DIFFICULTY + " INTEGER, " + GAMES_KEY_CITY_NAME
		+ " TEXT, " + GAMES_KEY_START_DATE + " INTEGER, " + GAMES_KEY_WINNING_STRATEGY + " TEXT, " + GAMES_KEY_END_DATE
		+ " INTEGER, " + GAMES_KEY_COMMENTS + " TEXT, " + GAMES_KEY_REWARD + " TEXT, " + GAMES_KEY_PRIZE_INFO
		+ " TEXT, " + GAMES_KEY_DETAILS_LINK + " TEXT" + ")";
	
	private static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE_NAME + " (" + USER_KEY_EMAIL
		+ " TEXT PRIMARY KEY, " + USER_KEY_PASSWORD + " TEXT, " + USER_KEY_DISPLAY_NAME + " TEXT," + USER_KEY_AVATAR
		+ " TEXT" + ")";
	
	private static final String CREATE_USER_GAMES_SPECIFIC_TABLE = "CREATE TABLE " + USER_GAMES_SPECIFIC_TABLE_NAME
		+ " (" + USER_GAMES_SPECIFIC_KEY_EMAIL + " TEXT, " + USER_GAMES_SPECIFIC_KEY_GAME_ID + " INTEGER, "
		+ USER_GAMES_SPECIFIC_KEY_RANK + " INTEGER, " + USER_GAMES_SPECIFIC_KEY_GAME_ACTIVE_OBSERVED + " INTEGER, "
		+ "PRIMARY KEY (" + USER_GAMES_SPECIFIC_KEY_EMAIL + ", " + USER_GAMES_SPECIFIC_KEY_GAME_ID + ")"
		+ " FOREIGN KEY (" + USER_GAMES_SPECIFIC_KEY_EMAIL + ") " + "REFERENCES " + USER_TABLE_NAME + " ("
		+ USER_KEY_EMAIL + ") " + " FOREIGN KEY (" + USER_GAMES_SPECIFIC_KEY_GAME_ID + ") " + "REFERENCES "
		+ GAMES_TABLE_NAME + " (" + GAMES_KEY_ID + ") " + ")";
	
	private static final String CREATE_USER_LOGGED_IN_TABLE = "CREATE TABLE " + USER_LOGGED_IN_TABLE_NAME + " ("
		+ USER_LOGGED_IN_EMAIL + " TEXT PRIMARY KEY" + ")";
	
	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		SQLiteDatabase.loadLibs(context);
		//		Uri path = Uri.parse("android.resource://com.blstream.urbangame/raw/icudt46l.zip");
		//		SQLiteDatabase.loadLibs(context, new File(path.toString()));
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("PRAGMA key = " + DATABASE_PASS);
		db.execSQL(CREATE_GAMES_TABLE);
		db.execSQL(CREATE_USER_TABLE);
		db.execSQL(CREATE_USER_GAMES_SPECIFIC_TABLE);
		db.execSQL(CREATE_USER_LOGGED_IN_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(Database.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
			+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + USER_GAMES_SPECIFIC_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + GAMES_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_USER_LOGGED_IN_TABLE);
		onCreate(db);
	}
	
	// GAMES METHODS
	@Override
	public boolean insertGameShortInfo(UrbanGameShortInfo game) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		boolean isDataOk = true;
		
		isDataOk = areShortGameInfoFieldsOK(game);
		
		if (isDataOk) {
			ContentValues values = new ContentValues();
			values.put(GAMES_KEY_ID, game.getID());
			values.put(GAMES_KEY_TITLE, game.getTitle());
			values.put(GAMES_KEY_GAME_ICON, game.getGameLogoBase64());
			values.put(GAMES_KEY_OPERATOR_ICON, game.getOperatorLogoBase64());
			values.put(GAMES_KEY_OPERATOR_NAME, game.getOperatorName());
			values.put(GAMES_KEY_NUMBER_OF_PLAYERS, game.getPlayers());
			values.put(GAMES_KEY_NUMBER_OF_MAX_PLAYERS, game.getMaxPlayers());
			values.put(GAMES_KEY_CITY_NAME, game.getLocation());
			values.put(GAMES_KEY_START_DATE, dateToLong(game.getStartDate()));
			values.put(GAMES_KEY_END_DATE, dateToLong(game.getEndDate()));
			values.put(GAMES_KEY_REWARD, booleanToString(game.getReward()));
			values.put(GAMES_KEY_DETAILS_LINK, game.getDetailsLink());
			
			boolean isInsertOK = db.insert(GAMES_TABLE_NAME, null, values) != -1;
			db.close();
			
			return isInsertOK;
		}
		else return false;
	}
	
	@Override
	public boolean insertGameInfo(UrbanGame game) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		
		boolean isDataOk = true;
		
		isDataOk = areGameInfoFieldsOK(game);
		
		if (isDataOk) {
			ContentValues values = new ContentValues();
			values.put(GAMES_KEY_ID, game.getID());
			values.put(GAMES_KEY_TITLE, game.getTitle());
			values.put(GAMES_KEY_GAME_ICON, game.getGameLogoBase64());
			values.put(GAMES_KEY_OPERATOR_ICON, game.getOperatorLogoBase64());
			values.put(GAMES_KEY_OPERATOR_NAME, game.getOperatorName());
			values.put(GAMES_KEY_NUMBER_OF_PLAYERS, game.getPlayers());
			values.put(GAMES_KEY_NUMBER_OF_MAX_PLAYERS, game.getMaxPlayers());
			values.put(GAMES_KEY_CITY_NAME, game.getLocation());
			values.put(GAMES_KEY_START_DATE, dateToLong(game.getStartDate()));
			values.put(GAMES_KEY_REWARD, booleanToString(game.getReward()));
			values.put(GAMES_KEY_DETAILS_LINK, game.getDetailsLink());
			values.put(GAMES_KEY_VERSION, game.getGameVersion());
			values.put(GAMES_KEY_WINNING_STRATEGY, game.getWinningStrategy());
			values.put(GAMES_KEY_DESCRIPTION, game.getDescription());
			values.put(GAMES_KEY_DIFFICULTY, game.getDifficulty());
			values.put(GAMES_KEY_END_DATE, dateToLong(game.getEndDate()));
			values.put(GAMES_KEY_PRIZE_INFO, game.getPrizesInfo());
			values.put(GAMES_KEY_COMMENTS, game.getComments());
			
			boolean isInDB = db.query(GAMES_TABLE_NAME, new String[] { GAMES_KEY_ID }, GAMES_KEY_ID + "=?",
				new String[] { game.getID().longValue() + "" }, null, null, null).moveToFirst();
			
			boolean isInsertOK;
			if (isInDB) {
				isInsertOK = db.update(GAMES_TABLE_NAME, values, GAMES_KEY_ID + "=?", new String[] { game.getID()
					.longValue() + "" }) != 0;
			}
			else {
				long iasd = db.insert(GAMES_TABLE_NAME, null, values);
				isInsertOK = iasd != -1;
			}
			
			db.close();
			
			return isInsertOK;
		}
		else return false;
	}
	
	private List<UrbanGameShortInfo> getAllGamesShortInfoParameterized(String orderLimitQuery) {
		List<UrbanGameShortInfo> gameList = null;
		
		String query = "SELECT " + GAMES_KEY_ID + ", " + GAMES_KEY_TITLE + ", " + GAMES_KEY_GAME_ICON + ", "
			+ GAMES_KEY_OPERATOR_ICON + ", " + GAMES_KEY_OPERATOR_NAME + ", " + GAMES_KEY_NUMBER_OF_PLAYERS + ", "
			+ GAMES_KEY_NUMBER_OF_MAX_PLAYERS + ", " + GAMES_KEY_CITY_NAME + ", " + GAMES_KEY_START_DATE + ", "
			+ GAMES_KEY_END_DATE + ", " + GAMES_KEY_REWARD + ", " + GAMES_KEY_DETAILS_LINK + " FROM "
			+ GAMES_TABLE_NAME + orderLimitQuery;
		
		SQLiteDatabase db = this.getReadableDatabase(DATABASE_PASS);
		Cursor cursor = db.rawQuery(query, null);
		
		if (cursor.moveToFirst()) {
			gameList = new ArrayList<UrbanGameShortInfo>();
			do {
				UrbanGameShortInfo game = gameShortInfoFromCursor(cursor);
				gameList.add(game);
			}
			while (cursor.moveToNext());
		}
		db.close();
		return gameList;
	}
	
	// OK
	@Override
	public List<UrbanGameShortInfo> getAllGamesShortInfo() {
		return getAllGamesShortInfoParameterized("");
	}
	
	@Override
	public List<UrbanGameShortInfo> getAllGamesShortInfoOrderedByStartTime(int beforeFirst, int howMany) {
		String orderingLimitingQuery = " ORDER BY " + GAMES_KEY_START_DATE + " LIMIT " + beforeFirst + ", " + howMany;
		return getAllGamesShortInfoParameterized(orderingLimitingQuery);
	}
	
	@Override
	public UrbanGameShortInfo getGameShortInfo(Long gameID) {
		SQLiteDatabase db = this.getReadableDatabase(DATABASE_PASS);
		String[] gamesColumns = { GAMES_KEY_ID, GAMES_KEY_TITLE, GAMES_KEY_GAME_ICON, GAMES_KEY_OPERATOR_ICON,
			GAMES_KEY_OPERATOR_NAME, GAMES_KEY_NUMBER_OF_PLAYERS, GAMES_KEY_NUMBER_OF_MAX_PLAYERS, GAMES_KEY_CITY_NAME,
			GAMES_KEY_START_DATE, GAMES_KEY_END_DATE, GAMES_KEY_REWARD, GAMES_KEY_DETAILS_LINK };
		
		Cursor cursor = db.query(GAMES_TABLE_NAME, gamesColumns, GAMES_KEY_ID + "=?", new String[] { gameID.longValue()
			+ "" }, null, null, null, null);
		
		UrbanGameShortInfo game;
		if (cursor != null && cursor.moveToFirst()) {
			game = gameShortInfoFromCursor(cursor);
		}
		else {
			game = null;
		}
		db.close();
		return game;
	}
	
	@Override
	public UrbanGame getGameInfo(Long gameID) {
		SQLiteDatabase db = this.getReadableDatabase(DATABASE_PASS);
		String[] gamesColumns = { GAMES_KEY_ID, GAMES_KEY_TITLE, GAMES_KEY_GAME_ICON, GAMES_KEY_OPERATOR_ICON,
			GAMES_KEY_OPERATOR_NAME, GAMES_KEY_NUMBER_OF_PLAYERS, GAMES_KEY_NUMBER_OF_MAX_PLAYERS, GAMES_KEY_CITY_NAME,
			GAMES_KEY_START_DATE, GAMES_KEY_END_DATE, GAMES_KEY_REWARD, GAMES_KEY_DETAILS_LINK, GAMES_KEY_VERSION,
			GAMES_KEY_WINNING_STRATEGY, GAMES_KEY_DIFFICULTY, GAMES_KEY_PRIZE_INFO, GAMES_KEY_DESCRIPTION,
			GAMES_KEY_COMMENTS };
		
		Cursor cursor = db.query(GAMES_TABLE_NAME, gamesColumns, GAMES_KEY_ID + "=?", new String[] { gameID.longValue()
			+ "" }, null, null, null, null);
		
		UrbanGame game;
		if (cursor != null && cursor.moveToFirst()) {
			game = new UrbanGame();
			game.setPrimaryInfo(gameShortInfoFromCursor(cursor));
			setRestOfDataForGameInfo(game, cursor);
		}
		else {
			game = null;
		}
		db.close();
		return game;
	}
	
	@Override
	public boolean updateGameShortInfo(UrbanGameShortInfo game) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		
		boolean isDataOk = areShortGameInfoFieldsOK(game);
		boolean updateOK = false;
		if (isDataOk) {
			ContentValues values = new ContentValues();
			putUrbanGameShortInfoInValues(game, values);
			
			updateOK = db.update(GAMES_TABLE_NAME, values, GAMES_KEY_ID + "=?", new String[] { game.getID().longValue()
				+ "" }) == 1;
		}
		db.close();
		return updateOK;
	}
	
	@Override
	public boolean updateGame(UrbanGame game) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		
		boolean isDataOk = areGameInfoFieldsOK(game);
		boolean isUpdateOk = false;
		if (isDataOk) {
			ContentValues values = new ContentValues();
			
			putUrbanGameInfoInValues(game, values);
			
			isUpdateOk = db.update(GAMES_TABLE_NAME, values, GAMES_KEY_ID + "=?", new String[] { game.getID()
				.longValue() + "" }) == 1;
		}
		db.close();
		return isUpdateOk;
	}
	
	@Override
	public boolean deleteGameInfoAndShortInfo(Long gameID) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		boolean isSucessful = db
			.delete(GAMES_TABLE_NAME, GAMES_KEY_ID + "=?", new String[] { gameID.longValue() + "" }) != 0;
		db.close();
		return isSucessful;
	}
	
	private boolean areShortGameInfoFieldsOK(UrbanGameShortInfo game) {
		boolean isOK = game.getTitle() != null
			&& game.getOperatorName() != null
			&& game.getPlayers() != null
			&& game.getPlayers().intValue() >= 0
			&& (game.getMaxPlayers() == null || (game.getMaxPlayers() != null && game.getMaxPlayers().intValue() >= 0 && game
				.getPlayers() <= game.getMaxPlayers()))
			&& game.getLocation() != null
			&& game.getDetailsLink() != null
			&& (game.getStartDate() != null && game.getStartDate().after(new GregorianCalendar(2012, 12, 31).getTime()))
			&& game.getEndDate() != null;
		return isOK;
	}
	
	private boolean areGameInfoFieldsOK(UrbanGame game) {
		boolean isOK = areShortGameInfoFieldsOK(game.getPrimaryInfo()) && game.getGameVersion() != null
			&& game.getGameVersion() >= 0.0 && (game.getDifficulty() == null || game.getDifficulty() >= 0)
			&& (game.getDifficulty() == null || game.getDifficulty() <= 5) && game.getDescription() != null
			&& game.getWinningStrategy() != null;
		return isOK;
	}
	
	/**
	 * @param date - date to be parsed into long
	 * @return - date in long in format yyyymmddhhmmss
	 */
	private Long dateToLong(Date date) {
		if (date == null) return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}
	
	/**
	 * @param longDate - long in format yyyymmddhhmmss
	 * @return Date object derived from long parameter
	 */
	private Date longToDate(long longDate) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(longDate);
		return c.getTime();
	}
	
	private String booleanToString(Boolean reward) {
		String ret = null;
		if (reward != null) {
			ret = reward ? "true" : "false";
		}
		return ret;
	}
	
	private Boolean stringToBoolean(String reward) {
		Boolean ret = null;
		if (reward != null) {
			ret = reward.equalsIgnoreCase("true");
		}
		return ret;
	}
	
	private UrbanGameShortInfo gameShortInfoFromCursor(Cursor cursor) {
		UrbanGameShortInfo game = new UrbanGameShortInfo(cursor.getLong(GamesFields.ID.value),
			cursor.getString(GamesFields.TITLE.value), cursor.getString(GamesFields.OPERATOR_NAME.value),
			cursor.getInt(GamesFields.NUMBER_OF_PLAYERS.value), cursor.getInt(GamesFields.NUMBER_OF_MAX_PLAYERS.value),
			longToDate(cursor.getLong(GamesFields.START_DATE.value)),
			longToDate(cursor.getLong(GamesFields.END_DATE.value)),
			stringToBoolean(cursor.getString(GamesFields.REWARD.value)), cursor.getString(GamesFields.CITY_NAME.value),
			cursor.getString(GamesFields.ICON.value), cursor.getString(GamesFields.OPERATOR_ICON.value),
			cursor.getString(GamesFields.DETAILS_LINK.value));
		return game;
	}
	
	private void setRestOfDataForGameInfo(UrbanGame game, Cursor cursor) {
		game.setGameVersion(cursor.getDouble(GamesFields.VERSION.value));
		game.setWinningStrategy(cursor.getString(GamesFields.WINNING_STRATEGY.value));
		game.setDifficulty(cursor.getInt(GamesFields.DIFFICULTY.value));
		game.setPrizesInfo(cursor.getString(GamesFields.PRIZE_INFO.value));
		game.setDescription(cursor.getString(GamesFields.DESCRIPTION.value));
		game.setComments(cursor.getString(GamesFields.COMMENTS.value));
	}
	
	private enum GamesFields {
		ID(0), TITLE(1), ICON(2), OPERATOR_ICON(3), OPERATOR_NAME(4), NUMBER_OF_PLAYERS(5), NUMBER_OF_MAX_PLAYERS(6), CITY_NAME(
			7), START_DATE(8), END_DATE(9), REWARD(10), DETAILS_LINK(11), VERSION(12), WINNING_STRATEGY(13), DIFFICULTY(
			14), PRIZE_INFO(15), DESCRIPTION(16), COMMENTS(17);
		int value;
		
		private GamesFields(int x) {
			value = x;
		}
	}
	
	private void putUrbanGameShortInfoInValues(UrbanGameShortInfo game, ContentValues values) {
		if (game.getID() != null) {
			values.put(GAMES_KEY_ID, game.getID());
		}
		if (game.getTitle() != null) {
			values.put(GAMES_KEY_TITLE, game.getTitle());
		}
		if (game.getOperatorName() != null) {
			values.put(GAMES_KEY_OPERATOR_NAME, game.getOperatorName());
		}
		if (game.getPlayers() != null) {
			values.put(GAMES_KEY_NUMBER_OF_PLAYERS, game.getPlayers());
		}
		if (game.getMaxPlayers() != null) {
			values.put(GAMES_KEY_NUMBER_OF_MAX_PLAYERS, game.getMaxPlayers());
		}
		if (game.getStartDate() != null) {
			values.put(GAMES_KEY_START_DATE, dateToLong(game.getStartDate()));
		}
		if (game.getEndDate() != null) {
			values.put(GAMES_KEY_END_DATE, dateToLong(game.getEndDate()));
		}
		if (game.getReward() != null) {
			values.put(GAMES_KEY_REWARD, booleanToString(game.getReward()));
		}
		if (game.getGameLogoBase64() != null) {
			values.put(GAMES_KEY_GAME_ICON, game.getGameLogoBase64());
		}
		if (game.getOperatorLogoBase64() != null) {
			values.put(GAMES_KEY_OPERATOR_ICON, game.getOperatorLogoBase64());
		}
		if (game.getLocation() != null) {
			values.put(GAMES_KEY_CITY_NAME, game.getLocation());
		}
		if (game.getDetailsLink() != null) {
			values.put(GAMES_KEY_DETAILS_LINK, game.getDetailsLink());
		}
	}
	
	private void putUrbanGameInfoInValues(UrbanGame game, ContentValues values) {
		putUrbanGameShortInfoInValues(game.getPrimaryInfo(), values);
		
		if (game.getGameVersion() != null) {
			values.put(GAMES_KEY_VERSION, game.getGameVersion());
		}
		if (game.getWinningStrategy() != null) {
			values.put(GAMES_KEY_WINNING_STRATEGY, game.getWinningStrategy());
		}
		if (game.getDifficulty() != null) {
			values.put(GAMES_KEY_DIFFICULTY, game.getDifficulty());
		}
		if (game.getPrizesInfo() != null) {
			values.put(GAMES_KEY_PRIZE_INFO, game.getPrizesInfo());
		}
		if (game.getDescription() != null) {
			values.put(GAMES_KEY_DESCRIPTION, game.getDescription());
		}
		if (game.getComments() != null) {
			values.put(GAMES_KEY_COMMENTS, game.getComments());
		}
	}
	
	// GAMES METHODS END
	
	// USER METHODS
	@Override
	public boolean insertUser(Player player) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		
		boolean isDataOk = true;
		
		isDataOk = player.getEmail() != null;
		
		if (isDataOk) {
			ContentValues values = new ContentValues();
			values.put(USER_KEY_EMAIL, player.getEmail());
			values.put(USER_KEY_DISPLAY_NAME, player.getDisplayName());
			values.put(USER_KEY_AVATAR, player.getAvatarBase64());
			values.put(USER_KEY_PASSWORD, player.getPassword());
			
			boolean isInsertOK = db.insert(USER_TABLE_NAME, null, values) != -1;
			db.close();
			
			return isInsertOK;
		}
		else return false;
	}
	
	@Override
	public Player getPlayer(String email) {
		SQLiteDatabase db = this.getReadableDatabase(DATABASE_PASS);
		String[] playerColumns = { USER_KEY_EMAIL, USER_KEY_DISPLAY_NAME, USER_KEY_AVATAR, USER_KEY_PASSWORD };
		
		Cursor cursor = db.query(USER_TABLE_NAME, playerColumns, USER_KEY_EMAIL + "=?", new String[] { email }, null,
			null, null, null);
		
		Player player;
		if (cursor != null && cursor.moveToFirst()) {
			player = new Player(cursor.getString(0), cursor.getString(3), cursor.getString(1), cursor.getString(2));
		}
		else {
			player = null;
		}
		db.close();
		return player;
	}
	
	@Override
	public boolean updatePlayer(Player player) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		
		boolean isDataOk = player.getEmail() != null;
		boolean updateOK = false;
		if (isDataOk) {
			ContentValues values = new ContentValues();
			if (player.getEmail() != null) {
				values.put(USER_KEY_EMAIL, player.getEmail());
			}
			if (player.getAvatarBase64() != null) {
				values.put(USER_KEY_AVATAR, player.getAvatarBase64());
			}
			if (player.getDisplayName() != null) {
				values.put(USER_KEY_DISPLAY_NAME, player.getDisplayName());
			}
			if (player.getPassword() != null) {
				values.put(USER_KEY_PASSWORD, player.getPassword());
			}
			
			updateOK = db.update(USER_TABLE_NAME, values, USER_KEY_EMAIL + "=?", new String[] { player.getEmail() }) == 1;
		}
		db.close();
		return updateOK;
	}
	
	@Override
	public boolean deletePlayer(String email) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		boolean isSucessful = db.delete(USER_TABLE_NAME, USER_KEY_EMAIL + "=?", new String[] { email }) != 0;
		db.close();
		return isSucessful;
	}
	
	@Override
	public boolean insertUserGameSpecific(PlayerGameSpecific playerGameSpecific) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		
		boolean isDataOk = true;
		
		isDataOk = playerGameSpecific.getPlayerEmail() != null && playerGameSpecific.getGameID() != null;
		
		if (isDataOk) {
			ContentValues values = new ContentValues();
			values.put(USER_GAMES_SPECIFIC_KEY_EMAIL, playerGameSpecific.getPlayerEmail());
			values.put(USER_GAMES_SPECIFIC_KEY_GAME_ID, playerGameSpecific.getGameID());
			values.put(USER_GAMES_SPECIFIC_KEY_RANK, playerGameSpecific.getRank());
			values.put(USER_GAMES_SPECIFIC_KEY_GAME_ACTIVE_OBSERVED, playerGameSpecific.getState());
			
			boolean isInsertOK = db.insert(USER_GAMES_SPECIFIC_TABLE_NAME, null, values) != -1;
			db.close();
			
			return isInsertOK;
		}
		else return false;
	}
	
	@Override
	public PlayerGameSpecific getUserGameSpecific(String email, Long gameID) {
		SQLiteDatabase db = this.getReadableDatabase(DATABASE_PASS);
		String[] playerGamesSpecificColumns = { USER_GAMES_SPECIFIC_KEY_EMAIL, USER_GAMES_SPECIFIC_KEY_GAME_ID,
			USER_GAMES_SPECIFIC_KEY_RANK, USER_GAMES_SPECIFIC_KEY_GAME_ACTIVE_OBSERVED };
		
		Cursor cursor = db.query(USER_GAMES_SPECIFIC_TABLE_NAME, playerGamesSpecificColumns,
			USER_GAMES_SPECIFIC_KEY_EMAIL + "=? AND " + USER_GAMES_SPECIFIC_KEY_GAME_ID + "=?", new String[] { email,
				gameID.longValue() + "" }, null, null, null, null);
		
		PlayerGameSpecific playerGameSpecific;
		if (cursor != null && cursor.moveToFirst()) {
			playerGameSpecific = new PlayerGameSpecific(cursor.getInt(2), cursor.getString(0), cursor.getLong(1),
				cursor.getInt(3));
		}
		else {
			playerGameSpecific = null;
		}
		db.close();
		return playerGameSpecific;
	}
	
	@Override
	public boolean updateUserGameSpecific(PlayerGameSpecific playerGameSpecific) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		
		boolean isDataOk = playerGameSpecific.getPlayerEmail() != null && playerGameSpecific.getGameID() != null;
		boolean updateOK = false;
		if (isDataOk) {
			ContentValues values = new ContentValues();
			if (playerGameSpecific.getGameID() != null) {
				values.put(USER_GAMES_SPECIFIC_KEY_GAME_ID, playerGameSpecific.getGameID());
			}
			if (playerGameSpecific.getPlayerEmail() != null) {
				values.put(USER_GAMES_SPECIFIC_KEY_EMAIL, playerGameSpecific.getPlayerEmail());
			}
			if (playerGameSpecific.getRank() != null) {
				values.put(USER_GAMES_SPECIFIC_KEY_RANK, playerGameSpecific.getRank());
			}
			if (playerGameSpecific.getState() != null) {
				values.put(USER_GAMES_SPECIFIC_KEY_GAME_ACTIVE_OBSERVED, playerGameSpecific.getState());
			}
			
			updateOK = db.update(USER_GAMES_SPECIFIC_TABLE_NAME, values, USER_GAMES_SPECIFIC_KEY_EMAIL + "=? AND "
				+ USER_GAMES_SPECIFIC_KEY_GAME_ID + "=?", new String[] { playerGameSpecific.getPlayerEmail(),
				playerGameSpecific.getGameID().longValue() + "" }) == 1;
		}
		db.close();
		return updateOK;
	}
	
	@Override
	public boolean deleteUserGameSpecific(String email, Long gameID) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		boolean isSucessful = db.delete(USER_GAMES_SPECIFIC_TABLE_NAME, USER_GAMES_SPECIFIC_KEY_EMAIL + "=? AND "
			+ USER_GAMES_SPECIFIC_KEY_GAME_ID + "=?", new String[] { email, gameID.longValue() + "" }) != 0;
		db.close();
		return isSucessful;
	}
	
	@Override
	public boolean wipeOutUserData(String email) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		db.beginTransaction();
		boolean isSucessful = db.delete(USER_GAMES_SPECIFIC_TABLE_NAME, USER_GAMES_SPECIFIC_KEY_EMAIL + "=?",
			new String[] { email }) != 0;
		boolean isSuccessful2 = db.delete(USER_TABLE_NAME, USER_KEY_EMAIL + "=?", new String[] { email }) != 0;
		if (isSucessful && isSuccessful2) {
			db.setTransactionSuccessful();
		}
		db.endTransaction();
		db.close();
		return isSucessful && isSuccessful2;
	}
	
	@Override
	public boolean setLoggedPlayer(String email) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		db.beginTransaction();
		boolean isSuccesful = email != null;
		isSuccesful = isSuccesful
			&& db.query(USER_LOGGED_IN_TABLE_NAME, new String[] { USER_LOGGED_IN_EMAIL }, null, null, null, null, null)
				.getCount() == 0;
		if (isSuccesful) {
			ContentValues values = new ContentValues();
			values.put(USER_LOGGED_IN_EMAIL, email);
			isSuccesful = db.insert(USER_LOGGED_IN_TABLE_NAME, null, values) != -1;
			db.setTransactionSuccessful();
		}
		db.endTransaction();
		return isSuccesful;
	}
	
	@Override
	public String getLoggedPlayerID() {
		SQLiteDatabase db = this.getReadableDatabase(DATABASE_PASS);
		Cursor cursor = db.query(USER_LOGGED_IN_TABLE_NAME, new String[] { USER_LOGGED_IN_EMAIL }, null, null, null,
			null, null);
		String playerEmail = null;
		if (cursor.moveToFirst()) {
			playerEmail = cursor.getString(0);
		}
		return playerEmail;
	}
	
	@Override
	public boolean setNoOneLogged() {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		int count = db.delete(USER_LOGGED_IN_TABLE_NAME, null, null);
		boolean successful = count == 1 || count == 0;
		return successful;
	}
	
	// USER METHODS END
}