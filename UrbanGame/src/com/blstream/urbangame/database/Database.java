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

import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;
import com.blstream.urbangame.database.entity.Task;
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
	private static final String TASKS_TABLE_NAME = "tasks";
	private static final String GAMES_TASKS_TABLE_NAME = "gamesTasks";
	private static final String USER_TASKS_SPECIFIC_TABLE_NAME = "userTasksSpecific";
	private static final String TASKS_ABCD_TABLE_NAME = "tasksABCD";
	private static final String TASKS_ABCD_POSSIBLE_ANSWERS_TABLE_NAME = "tasksPossibleAnswersABCD";
	
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
	private static final String USER_LOGGED_IN_KEY_EMAIL = "ULIEmail";
	
	// ---- Tasks
	private static final String TASKS_KEY_ID = "tid";
	private static final String TASKS_KEY_TYPE = "TType";
	private static final String TASKS_KEY_TITLE = "TTitle";
	private static final String TASKS_KEY_MAX_POINTS = "TMaxPoints";
	private static final String TASKS_KEY_REPETABLE = "TRepetable";
	private static final String TASKS_KEY_IS_HIDDEN = "THidden";
	private static final String TASKS_KEY_NUMBER_OF_HIDDEN = "TNumberOfHidden";
	private static final String TASKS_KEY_END_TIME = "TEndTime";
	private static final String TASKS_KEY_PICTURE = "TPicture";
	private static final String TASKS_KEY_DESCRIPTION = "TDescription";
	
	// ---- Games Tasks
	private static final String GAMES_TASKS_KEY_GAME_ID = "GTgid";
	private static final String GAMES_TASKS_KEY_TASK_ID = "GTtid";
	
	// ---- Users tasks specific
	private static final String USER_TASKS_SPECIFIC_KEY_PLAYER_EMAIL = "UTSPlayerEmail";
	private static final String USER_TASKS_SPECIFIC_KEY_TASK_ID = "UTSTaskID";
	private static final String USER_TASKS_SPECIFIC_KEY_POINTS = "UTSPoints";
	private static final String USER_TASKS_SPECIFIC_KEY_IS_FINISHED = "UTSIsFisnished";
	private static final String USER_TASKS_SPECIFIC_KEY_ARE_CHANGES = "UTSAreChanges";
	private static final String USER_TASKS_SPECIFIC_KEY_WAS_HIDDEN = "UTSWasHidden";
	
	// ---- Tasks ABCD
	private static final String TASKS_ABCD_KEY_ID = "TAID";
	private static final String TASKS_ABCD_KEY_TASK_ID = "TAtaskID";
	private static final String TASKS_ABCD_KEY_QUESTION = "TAtaskQuestion";
	
	// ---- Task ABCD answers
	private static final String TASKS_ABCD_POSSIBLE_ANSWERS_KEY_ID = "TAPAID";
	private static final String TASKS_ABCD_POSSIBLE_ANSWERS_KEY_TASK_POSSIBLE_ANSWER = "TAPAtaskPossibleAnswer";
	
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
		+ USER_LOGGED_IN_KEY_EMAIL + " TEXT PRIMARY KEY" + ")";
	
	private static final String CREATE_TASKS_TABLE = "CREATE TABLE " + TASKS_TABLE_NAME + " (" + TASKS_KEY_ID
		+ " INTEGER PRIMARY KEY, " + TASKS_KEY_DESCRIPTION + " TEXT, " + TASKS_KEY_END_TIME + " INTEGER, "
		+ TASKS_KEY_IS_HIDDEN + " INTEGER, " + TASKS_KEY_MAX_POINTS + " INTEGER, " + TASKS_KEY_NUMBER_OF_HIDDEN
		+ " INTEGER, " + TASKS_KEY_PICTURE + " TEXT, " + TASKS_KEY_REPETABLE + " INTEGER, " + TASKS_KEY_TITLE
		+ " TEXT, " + TASKS_KEY_TYPE + " INTEGER" + ")";
	
	private static final String CREATE_GAMES_TASKS_TABLE = "CREATE TABLE " + GAMES_TASKS_TABLE_NAME + " ("
		+ GAMES_TASKS_KEY_GAME_ID + " INTEGER, " + GAMES_TASKS_KEY_TASK_ID + " INTEGER, " + "PRIMARY KEY ("
		+ GAMES_TASKS_KEY_GAME_ID + ", " + GAMES_TASKS_KEY_TASK_ID + ")" + " FOREIGN KEY (" + GAMES_TASKS_KEY_GAME_ID
		+ ") REFERENCES " + GAMES_TABLE_NAME + " (" + GAMES_KEY_ID + "), " + " FOREIGN KEY (" + GAMES_TASKS_KEY_TASK_ID
		+ ") REFERENCES " + TASKS_TABLE_NAME + "(" + TASKS_KEY_ID + ")" + ")";
	
	private static final String CREATE_USER_TASKS_SPECIFIC_TABLE = "CREATE TABLE " + USER_TASKS_SPECIFIC_TABLE_NAME
		+ " (" + USER_TASKS_SPECIFIC_KEY_PLAYER_EMAIL + " TEXT, " + USER_TASKS_SPECIFIC_KEY_TASK_ID + " INTEGER, "
		+ USER_TASKS_SPECIFIC_KEY_ARE_CHANGES + " TEXT, " + USER_TASKS_SPECIFIC_KEY_IS_FINISHED + " TEXT, "
		+ USER_TASKS_SPECIFIC_KEY_POINTS + " INTEGER, " + USER_TASKS_SPECIFIC_KEY_WAS_HIDDEN + " TEXT, "
		+ "PRIMARY KEY (" + USER_TASKS_SPECIFIC_KEY_PLAYER_EMAIL + ", " + USER_TASKS_SPECIFIC_KEY_TASK_ID + "), "
		+ " FOREIGN KEY (" + USER_TASKS_SPECIFIC_KEY_TASK_ID + ") REFERENCES " + TASKS_TABLE_NAME + " (" + TASKS_KEY_ID
		+ "), " + " FOREIGN KEY (" + USER_TASKS_SPECIFIC_KEY_PLAYER_EMAIL + ") REFERENCES " + USER_TABLE_NAME + " ("
		+ USER_KEY_EMAIL + ") " + ")";
	
	private static final String CREATE_TASKS_ABCD_TABLE = "CREATE TABLE " + TASKS_ABCD_TABLE_NAME + " ("
		+ TASKS_ABCD_KEY_TASK_ID + " INTEGER AUTOINCREMENT, " + TASKS_ABCD_KEY_TASK_ID + " INTEGER, "
		+ TASKS_ABCD_KEY_QUESTION + " TEXT UNIQUE NOT NULL, " + "FOREIGN KEY (" + TASKS_ABCD_KEY_TASK_ID + ") "
		+ "REFERENCES " + TASKS_TABLE_NAME + " (" + TASKS_KEY_ID + ") " + ")";
	
	private static final String CREATE_TASKS_ABCD_POSSIBLE_ANSWERS_TABLE = "CREATE TABLE "
		+ TASKS_ABCD_POSSIBLE_ANSWERS_TABLE_NAME + " (" + TASKS_ABCD_POSSIBLE_ANSWERS_KEY_ID + " INTEGER, "
		+ TASKS_ABCD_POSSIBLE_ANSWERS_KEY_TASK_POSSIBLE_ANSWER + " INTEGER, " + "PRIMARY KEY ("
		+ TASKS_ABCD_POSSIBLE_ANSWERS_KEY_ID + ", " + TASKS_ABCD_POSSIBLE_ANSWERS_KEY_TASK_POSSIBLE_ANSWER + "), "
		+ "FOREIGN KEY (" + TASKS_ABCD_POSSIBLE_ANSWERS_KEY_ID + ") " + "REFERENCES " + TASKS_ABCD_TABLE_NAME + " ("
		+ TASKS_ABCD_KEY_ID + ") " + ")";
	
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
		db.execSQL(CREATE_TASKS_TABLE);
		db.execSQL(CREATE_GAMES_TASKS_TABLE);
		db.execSQL(CREATE_USER_TASKS_SPECIFIC_TABLE);
		db.execSQL(CREATE_TASKS_ABCD_TABLE);
		db.execSQL(CREATE_TASKS_ABCD_POSSIBLE_ANSWERS_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(Database.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
			+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TASKS_ABCD_POSSIBLE_ANSWERS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TASKS_ABCD_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_USER_TASKS_SPECIFIC_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_GAMES_TASKS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TASKS_TABLE);
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
		db.beginTransaction();
		String userTaskSpecificDeletionSQL = "DELETE FROM " + USER_TASKS_SPECIFIC_TABLE_NAME + " WHERE "
			+ USER_TASKS_SPECIFIC_KEY_TASK_ID + " IN (SELECT " + GAMES_TASKS_KEY_TASK_ID + " FROM "
			+ GAMES_TASKS_TABLE_NAME + " WHERE " + GAMES_TASKS_KEY_GAME_ID + "=" + gameID.longValue() + ")" + ")";
		String abcdPossibleAnswersDeletionSQL = "DELETE FROM " + TASKS_ABCD_POSSIBLE_ANSWERS_TABLE_NAME + " WHERE "
			+ TASKS_ABCD_POSSIBLE_ANSWERS_KEY_ID + " IN (SELECT " + TASKS_ABCD_KEY_ID + " FROM "
			+ TASKS_ABCD_TABLE_NAME + " WHERE " + TASKS_ABCD_KEY_TASK_ID + " IN (SELECT " + GAMES_TASKS_KEY_TASK_ID
			+ " FROM " + GAMES_TASKS_TABLE_NAME + " WHERE " + GAMES_TASKS_KEY_GAME_ID + "=" + gameID.longValue() + ")"
			+ ")";
		String abcdTaskDeletionSQL = "DELETE FROM " + TASKS_ABCD_TABLE_NAME + " WHERE " + TASKS_ABCD_KEY_TASK_ID
			+ " IN (SELECT " + GAMES_TASKS_KEY_TASK_ID + " FROM " + GAMES_TASKS_TABLE_NAME + " WHERE "
			+ GAMES_TASKS_KEY_GAME_ID + "=" + gameID.longValue() + ")";
		String tasksDeletionSQL = "DELETE FROM " + TASKS_TABLE_NAME + " WHERE " + TASKS_KEY_ID + " IN (SELECT "
			+ GAMES_TASKS_KEY_TASK_ID + " FROM " + GAMES_TASKS_TABLE_NAME + " WHERE " + GAMES_TASKS_KEY_GAME_ID + "="
			+ gameID.longValue() + ")";
		db.rawQuery(userTaskSpecificDeletionSQL, null);
		db.rawQuery(abcdPossibleAnswersDeletionSQL, null);
		db.rawQuery(abcdTaskDeletionSQL, null);
		db.rawQuery(tasksDeletionSQL, null);
		boolean isTasksDeletionSuccessful = db.delete(GAMES_TASKS_TABLE_NAME, GAMES_TASKS_KEY_GAME_ID + "=?",
			new String[] { gameID.longValue() + "" }) != 0;
		boolean isUserGameSpecificDeletionSuccessful = db.delete(USER_GAMES_SPECIFIC_TABLE_NAME,
			USER_GAMES_SPECIFIC_KEY_GAME_ID + "=?", new String[] { gameID.longValue() + "" }) != -1;
		boolean isSucessful = db
			.delete(GAMES_TABLE_NAME, GAMES_KEY_ID + "=?", new String[] { gameID.longValue() + "" }) != 0;
		if (isTasksDeletionSuccessful && isSucessful && isUserGameSpecificDeletionSuccessful) {
			db.setTransactionSuccessful();
		}
		db.endTransaction();
		db.close();
		return isTasksDeletionSuccessful && isSucessful && isUserGameSpecificDeletionSuccessful;
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
			&& db.query(USER_LOGGED_IN_TABLE_NAME, new String[] { USER_LOGGED_IN_KEY_EMAIL }, null, null, null, null,
				null).getCount() == 0;
		if (isSuccesful) {
			ContentValues values = new ContentValues();
			values.put(USER_LOGGED_IN_KEY_EMAIL, email);
			isSuccesful = db.insert(USER_LOGGED_IN_TABLE_NAME, null, values) != -1;
			db.setTransactionSuccessful();
		}
		db.endTransaction();
		return isSuccesful;
	}
	
	@Override
	public String getLoggedPlayerID() {
		SQLiteDatabase db = this.getReadableDatabase(DATABASE_PASS);
		Cursor cursor = db.query(USER_LOGGED_IN_TABLE_NAME, new String[] { USER_LOGGED_IN_KEY_EMAIL }, null, null,
			null, null, null);
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
	
	// TASKS METHODS
	@Override
	public boolean insertTaskForGame(Long gameID, Task task) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		
		boolean isDataOk;
		isDataOk = isTaskOk(task);
		
		if (isDataOk) {
			db.beginTransaction();
			ContentValues values = new ContentValues();
			values.put(GAMES_TASKS_KEY_GAME_ID, gameID);
			values.put(GAMES_TASKS_KEY_TASK_ID, task.getId());
			
			boolean isInsert1OK = db.insert(GAMES_TASKS_TABLE_NAME, null, values) != -1;
			
			values = new ContentValues();
			values.put(TASKS_KEY_ID, task.getId());
			values.put(TASKS_KEY_TITLE, task.getTitle());
			values.put(TASKS_KEY_TYPE, task.getType());
			values.put(TASKS_KEY_PICTURE, task.getPictureBase64());
			values.put(TASKS_KEY_DESCRIPTION, task.getDescription());
			values.put(TASKS_KEY_REPETABLE, booleanToString(task.getIsRepetable()));
			values.put(TASKS_KEY_IS_HIDDEN, booleanToString(task.getIsHidden()));
			values.put(TASKS_KEY_NUMBER_OF_HIDDEN, task.getNumberOfHidden());
			values.put(TASKS_KEY_END_TIME, dateToLong(task.getEndTime()));
			values.put(TASKS_KEY_MAX_POINTS, task.getMaxPoints());
			
			boolean isInsert2OK = db.insert(TASKS_TABLE_NAME, null, values) != -1;
			
			if (isInsert1OK && isInsert2OK) {
				if (task instanceof ABCDTask) {
					boolean isInsert3OK = insertTaskABCD(db, (ABCDTask) task);
					if (isInsert3OK) {
						db.setTransactionSuccessful();
					}
				}
				else {
					db.setTransactionSuccessful();
				}
			}
			db.endTransaction();
			db.close();
			
			return isInsert1OK && isInsert2OK;
		}
		else return false;
	}
	
	private boolean insertTaskABCD(SQLiteDatabase db, ABCDTask abcdTask) {
		ContentValues values = new ContentValues();
		values.put(TASKS_ABCD_KEY_TASK_ID, abcdTask.getId());
		values.put(TASKS_ABCD_KEY_QUESTION, abcdTask.getQuestion());
		long newId = db.insert(TASKS_ABCD_TABLE_NAME, null, values);
		boolean isInsert1OK = newId != -1;
		boolean isInsert2OK = false;
		if (isInsert1OK) {
			String[] answers = abcdTask.getAnswers();
			
			isInsert2OK = true;
			for (String s : answers) {
				values = new ContentValues();
				values.put(TASKS_ABCD_POSSIBLE_ANSWERS_KEY_ID, newId);
				values.put(TASKS_ABCD_POSSIBLE_ANSWERS_KEY_TASK_POSSIBLE_ANSWER, s);
				isInsert2OK = db.insert(TASKS_ABCD_POSSIBLE_ANSWERS_TABLE_NAME, null, values) != -1;
				if (!isInsert2OK) {
					break;
				}
			}
		}
		return isInsert2OK;
	}
	
	private boolean isTaskOk(Task task) {
		return task.getId() != null && task.getEndTime() != null && task.getTitle() != null && task.getType() != null;
	}
	
	@Override
	public List<Task> getTasksForGame(Long gameID) {
		SQLiteDatabase db = this.getReadableDatabase(DATABASE_PASS);
		
		String query = "SELECT " + TASKS_KEY_ID + ", " + TASKS_KEY_TYPE + ", " + TASKS_KEY_TITLE + ", "
			+ TASKS_KEY_MAX_POINTS + ", " + TASKS_KEY_REPETABLE + ", " + TASKS_KEY_IS_HIDDEN + ", "
			+ TASKS_KEY_NUMBER_OF_HIDDEN + ", " + TASKS_KEY_END_TIME + ", " + TASKS_KEY_PICTURE + ", "
			+ TASKS_KEY_DESCRIPTION + " FROM " + "( SELECT * FROM " + GAMES_TASKS_TABLE_NAME + " WHERE "
			+ GAMES_TASKS_KEY_GAME_ID + "=" + gameID.longValue() + ")" + " INNER JOIN " + TASKS_TABLE_NAME + " ON "
			+ GAMES_TASKS_KEY_TASK_ID + "=" + TASKS_KEY_ID;
		
		Cursor cursor = db.rawQuery(query, null);
		
		List<Task> tasksList = null;
		if (cursor.moveToFirst()) {
			tasksList = new ArrayList<Task>();
			do {
				Task task = extractTaskInfroamtionFromCursorAndGetAdditionalIfNeeded(cursor, db);
				tasksList.add(task);
			}
			while (cursor.moveToNext());
		}
		return tasksList;
	}
	
	@Override
	public Task getTask(Long taskID) {
		SQLiteDatabase db = this.getReadableDatabase(DATABASE_PASS);
		String[] taskColumns = { TASKS_KEY_ID, TASKS_KEY_TYPE, TASKS_KEY_TITLE, TASKS_KEY_MAX_POINTS,
			TASKS_KEY_REPETABLE, TASKS_KEY_IS_HIDDEN, TASKS_KEY_NUMBER_OF_HIDDEN, TASKS_KEY_END_TIME,
			TASKS_KEY_PICTURE, TASKS_KEY_DESCRIPTION };
		
		Cursor cursor = db.query(TASKS_TABLE_NAME, taskColumns, TASKS_KEY_ID + "=?", new String[] { taskID.longValue()
			+ "" }, null, null, null, null);
		
		Task task;
		if (cursor != null && cursor.moveToFirst()) {
			task = extractTaskInfroamtionFromCursorAndGetAdditionalIfNeeded(cursor, db);
		}
		else {
			task = null;
		}
		db.close();
		return task;
	}
	
	private enum TasksFields {
		ID(0), TYPE(1), TITLE(2), MAX_POINTS(3), REPETABLE(4), IS_HIDDEN(5), NUMBER_OF_HIDDEN(6), END_TIME(7), PICTURE(
			8), DESCRIPTION(9);
		int value;
		
		private TasksFields(int x) {
			value = x;
		}
	}
	
	private Task extractTaskInfroamtionFromCursorAndGetAdditionalIfNeeded(Cursor cursor, SQLiteDatabase db) {
		Long id = cursor.getLong(TasksFields.ID.value);
		Integer type = cursor.getInt(TasksFields.TYPE.value);
		String title = cursor.getString(TasksFields.TITLE.value);
		String pictureBase64 = cursor.getString(TasksFields.PICTURE.value);
		String description = cursor.getString(TasksFields.DESCRIPTION.value);
		Boolean isRepetable = stringToBoolean(cursor.getString(TasksFields.REPETABLE.value));
		Boolean isHidden = stringToBoolean(cursor.getString(TasksFields.IS_HIDDEN.value));
		Integer numberOfHidden = cursor.getInt(TasksFields.NUMBER_OF_HIDDEN.value);
		Date endTime = longToDate(cursor.getLong(TasksFields.END_TIME.value));
		Integer maxPoints = cursor.getInt(TasksFields.MAX_POINTS.value);
		
		if (type == Task.TASK_TYPE_LOCATION) return new LocationTask(id, title, pictureBase64, description,
			isRepetable, isHidden, numberOfHidden, endTime, maxPoints);
		else {
			String[] taskQuestionTableColumns = new String[] { TASKS_ABCD_KEY_ID, TASKS_ABCD_KEY_TASK_ID,
				TASKS_ABCD_KEY_QUESTION };
			String[] taskAnswersTableColumns = new String[] { TASKS_ABCD_POSSIBLE_ANSWERS_KEY_ID,
				TASKS_ABCD_POSSIBLE_ANSWERS_KEY_TASK_POSSIBLE_ANSWER };
			
			Cursor cursor2 = db.query(TASKS_ABCD_TABLE_NAME, taskQuestionTableColumns, TASKS_ABCD_KEY_TASK_ID + "=?",
				new String[] { id.longValue() + "" }, null, null, null);
			String question = cursor2.getString(ABCDTaskFields.QUESTION.value);
			long idOfAnswerSet = cursor2.getLong(ABCDTaskFields.ID.value);
			String[] answers = null;
			
			cursor2 = db.query(TASKS_ABCD_POSSIBLE_ANSWERS_TABLE_NAME, taskAnswersTableColumns,
				TASKS_ABCD_POSSIBLE_ANSWERS_KEY_ID + "=?", new String[] { idOfAnswerSet + "" }, null, null, null);
			if (cursor2.moveToFirst()) {
				int i = 0;
				answers = new String[cursor2.getCount()];
				do {
					answers[i] = cursor2.getString(ABCDTaskFields.ANSWER.value);
					i++;
				}
				while (cursor2.moveToNext());
			}
			return new ABCDTask(id, title, pictureBase64, description, isRepetable, isHidden, numberOfHidden, endTime,
				maxPoints, question, answers);
		}
	}
	
	private enum ABCDTaskFields {
		ID(0), TASK_ID(1), QUESTION(2), ANSWER(1);
		int value;
		
		private ABCDTaskFields(int x) {
			value = x;
		}
	}
	
	@Override
	public boolean updateTask(Task task) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		
		boolean isDataOk = task.getId() != null;
		boolean updateOK = false;
		if (isDataOk) {
			db.beginTransaction();
			ContentValues values = new ContentValues();
			putTaskInValues(task, values);
			
			updateOK = db.update(TASKS_TABLE_NAME, values, TASKS_KEY_ID + "=?", new String[] { task.getId().longValue()
				+ "" }) == 1;
			
			if (task instanceof ABCDTask) {
				updateOK = updateOK && updateABCDTaskSpecialData(db, (ABCDTask) task);
			}
			if (updateOK) {
				db.setTransactionSuccessful();
			}
		}
		db.close();
		return updateOK;
	}
	
	private boolean updateABCDTaskSpecialData(SQLiteDatabase db, ABCDTask task) {
		boolean isOk = true;
		ContentValues values;
		if (task.getQuestion() != null) {
			values = new ContentValues();
			values.put(TASKS_ABCD_KEY_QUESTION, task.getQuestion());
			isOk = isOk
				&& db.update(TASKS_ABCD_TABLE_NAME, values, TASKS_ABCD_KEY_TASK_ID + "=?", new String[] { task.getId()
					.longValue() + "" }) == 1;
		}
		if (task.getAnswers() != null) {
			Cursor cursor = db.query(TASKS_ABCD_TABLE_NAME, new String[] { TASKS_ABCD_KEY_ID }, TASKS_ABCD_KEY_TASK_ID
				+ "=?", new String[] { task.getId().longValue() + "" }, null, null, null);
			
			String abcdPossibleAnswersDeletionSQL = "DELETE FROM " + TASKS_ABCD_POSSIBLE_ANSWERS_TABLE_NAME + " WHERE "
				+ TASKS_ABCD_POSSIBLE_ANSWERS_KEY_ID + " IN (SELECT " + TASKS_ABCD_KEY_ID + " FROM "
				+ TASKS_ABCD_TABLE_NAME + " WHERE " + TASKS_ABCD_KEY_TASK_ID + "=" + task.getId().longValue() + ")";
			
			db.execSQL(abcdPossibleAnswersDeletionSQL);
			
			if (isOk = (isOk && cursor.moveToFirst())) {
				long id = cursor.getLong(0);
				for (String s : task.getAnswers()) {
					values = new ContentValues();
					values.put(TASKS_ABCD_POSSIBLE_ANSWERS_KEY_TASK_POSSIBLE_ANSWER, s);
					values.put(TASKS_ABCD_POSSIBLE_ANSWERS_KEY_ID, id);
					isOk = db.insert(TASKS_ABCD_POSSIBLE_ANSWERS_TABLE_NAME, null, values) != -1;
				}
			}
		}
		return isOk;
	}
	
	private void putTaskInValues(Task task, ContentValues values) {
		if (task.getId() != null) {
			values.put(TASKS_KEY_ID, task.getId());
		}
		if (task.getTitle() != null) {
			values.put(TASKS_KEY_TITLE, task.getTitle());
		}
		if (task.getType() != null) {
			values.put(TASKS_KEY_TYPE, task.getType());
		}
		if (task.getPictureBase64() != null) {
			values.put(TASKS_KEY_PICTURE, task.getPictureBase64());
		}
		if (task.getDescription() != null) {
			values.put(TASKS_KEY_DESCRIPTION, task.getDescription());
		}
		if (task.getIsRepetable() != null) {
			values.put(TASKS_KEY_REPETABLE, booleanToString(task.getIsRepetable()));
		}
		if (task.getIsHidden() != null) {
			values.put(TASKS_KEY_IS_HIDDEN, booleanToString(task.getIsHidden()));
		}
		if (task.getNumberOfHidden() != null) {
			values.put(TASKS_KEY_NUMBER_OF_HIDDEN, task.getNumberOfHidden());
		}
		if (task.getEndTime() != null) {
			values.put(TASKS_KEY_END_TIME, dateToLong(task.getEndTime()));
		}
		if (task.getMaxPoints() != null) {
			values.put(TASKS_KEY_MAX_POINTS, task.getMaxPoints());
		}
	}
	
	@Override
	public boolean deleteTask(Long taskID) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		db.beginTransaction();
		String userTaskSpecificDeletionSQL = "DELETE FROM " + USER_TASKS_SPECIFIC_TABLE_NAME + " WHERE "
			+ USER_TASKS_SPECIFIC_KEY_TASK_ID + "=" + taskID.longValue();
		String abcdPossibleAnswersDeletionSQL = "DELETE FROM " + TASKS_ABCD_POSSIBLE_ANSWERS_TABLE_NAME + " WHERE "
			+ TASKS_ABCD_POSSIBLE_ANSWERS_KEY_ID + " IN (SELECT " + TASKS_ABCD_KEY_ID + " FROM "
			+ TASKS_ABCD_TABLE_NAME + " WHERE " + TASKS_ABCD_KEY_TASK_ID + "=" + taskID.longValue() + ")";
		String abcdTaskDeletionSQL = "DELETE FROM " + TASKS_ABCD_TABLE_NAME + " WHERE " + TASKS_ABCD_KEY_TASK_ID + "="
			+ taskID.longValue();
		db.rawQuery(userTaskSpecificDeletionSQL, null);
		db.rawQuery(abcdPossibleAnswersDeletionSQL, null);
		db.rawQuery(abcdTaskDeletionSQL, null);
		boolean isSucessful = db
			.delete(TASKS_TABLE_NAME, TASKS_KEY_ID + "=?", new String[] { taskID.longValue() + "" }) != 0;
		db.close();
		return isSucessful;
	}
	
	@Override
	public boolean insertPlayerTaskSpecific(PlayerTaskSpecific taskSpecific) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		
		boolean isDataOk;
		isDataOk = isTaskSpecificOk(taskSpecific);
		
		if (isDataOk) {
			ContentValues values = new ContentValues();
			values.put(USER_TASKS_SPECIFIC_KEY_TASK_ID, taskSpecific.getTaskID());
			values.put(USER_TASKS_SPECIFIC_KEY_PLAYER_EMAIL, taskSpecific.getPlayerEmail());
			values.put(USER_TASKS_SPECIFIC_KEY_ARE_CHANGES, booleanToString(taskSpecific.getAreChanges()));
			values.put(USER_TASKS_SPECIFIC_KEY_IS_FINISHED, booleanToString(taskSpecific.getIsFinishedByUser()));
			values.put(USER_TASKS_SPECIFIC_KEY_POINTS, taskSpecific.getPoints());
			values.put(USER_TASKS_SPECIFIC_KEY_WAS_HIDDEN, booleanToString(taskSpecific.getWasHidden()));
			
			boolean isInsertOK = db.insert(USER_TASKS_SPECIFIC_TABLE_NAME, null, values) != -1;
			
			db.close();
			return isInsertOK;
		}
		else return false;
	}
	
	private boolean isTaskSpecificOk(PlayerTaskSpecific taskSpecific) {
		return taskSpecific.getTaskID() != null && taskSpecific.getPlayerEmail() != null;
	}
	
	@Override
	public PlayerTaskSpecific getPlayerTaskSpecific(Long taskID, String playerEmail) {
		SQLiteDatabase db = this.getReadableDatabase(DATABASE_PASS);
		String[] taskColumns = { USER_TASKS_SPECIFIC_KEY_PLAYER_EMAIL, USER_TASKS_SPECIFIC_KEY_TASK_ID,
			USER_TASKS_SPECIFIC_KEY_POINTS, USER_TASKS_SPECIFIC_KEY_IS_FINISHED, USER_TASKS_SPECIFIC_KEY_ARE_CHANGES,
			USER_TASKS_SPECIFIC_KEY_WAS_HIDDEN };
		
		Cursor cursor = db.query(USER_TASKS_SPECIFIC_TABLE_NAME, taskColumns, USER_TASKS_SPECIFIC_KEY_PLAYER_EMAIL
			+ "=? AND " + USER_TASKS_SPECIFIC_KEY_TASK_ID + "=?",
			new String[] { playerEmail, taskID.longValue() + "" }, null, null, null, null);
		
		PlayerTaskSpecific taskSpecific;
		if (cursor != null && cursor.moveToFirst()) {
			taskSpecific = taskSpecificFromCursor(cursor);
		}
		else {
			taskSpecific = null;
		}
		db.close();
		return taskSpecific;
	}
	
	private PlayerTaskSpecific taskSpecificFromCursor(Cursor cursor) {
		return new PlayerTaskSpecific(cursor.getString(TaskSpecificFields.PLAYER_EMAIL.value),
			cursor.getLong(TaskSpecificFields.TASK_ID.value), cursor.getInt(TaskSpecificFields.POINTS.value),
			stringToBoolean(cursor.getString(TaskSpecificFields.IS_FINISHED.value)),
			stringToBoolean(cursor.getString(TaskSpecificFields.ARE_CHANGES.value)),
			stringToBoolean(cursor.getString(TaskSpecificFields.WAS_HIDDEN.value)));
	}
	
	private enum TaskSpecificFields {
		PLAYER_EMAIL(0), TASK_ID(1), POINTS(2), IS_FINISHED(3), ARE_CHANGES(4), WAS_HIDDEN(5);
		int value;
		
		private TaskSpecificFields(int x) {
			value = x;
		}
	}
	
	@Override
	public boolean updatePlayerTaskSpecific(PlayerTaskSpecific taskSpecific) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		
		boolean isDataOk = isTaskSpecificOk(taskSpecific);
		boolean updateOK = false;
		if (isDataOk) {
			ContentValues values = new ContentValues();
			putTaskSpecificInValues(taskSpecific, values);
			
			updateOK = db.update(USER_TASKS_SPECIFIC_TABLE_NAME, values, USER_TASKS_SPECIFIC_KEY_PLAYER_EMAIL
				+ "=? AND " + USER_TASKS_SPECIFIC_KEY_TASK_ID + "=?", new String[] { taskSpecific.getPlayerEmail(),
				taskSpecific.getTaskID().longValue() + "" }) == 1;
		}
		db.close();
		return updateOK;
	}
	
	private void putTaskSpecificInValues(PlayerTaskSpecific taskSpecific, ContentValues values) {
		if (taskSpecific.getTaskID() != null) {
			values.put(USER_TASKS_SPECIFIC_KEY_TASK_ID, taskSpecific.getTaskID());
		}
		if (taskSpecific.getPlayerEmail() != null) {
			values.put(USER_TASKS_SPECIFIC_KEY_PLAYER_EMAIL, taskSpecific.getPlayerEmail());
		}
		if (taskSpecific.getAreChanges() != null) {
			values.put(USER_TASKS_SPECIFIC_KEY_ARE_CHANGES, booleanToString(taskSpecific.getAreChanges()));
		}
		if (taskSpecific.getIsFinishedByUser() != null) {
			values.put(USER_TASKS_SPECIFIC_KEY_IS_FINISHED, booleanToString(taskSpecific.getIsFinishedByUser()));
		}
		if (taskSpecific.getPoints() != null) {
			values.put(USER_TASKS_SPECIFIC_KEY_POINTS, taskSpecific.getPoints());
		}
		if (taskSpecific.getWasHidden() != null) {
			values.put(USER_TASKS_SPECIFIC_KEY_WAS_HIDDEN, booleanToString(taskSpecific.getWasHidden()));
		}
	}
	
	@Override
	public boolean deletePlayerTaskSpecific(Long taskID, String playerEmail) {
		SQLiteDatabase db = this.getWritableDatabase(DATABASE_PASS);
		boolean isSucessful = db
			.delete(USER_TASKS_SPECIFIC_TABLE_NAME, USER_TASKS_SPECIFIC_KEY_PLAYER_EMAIL + "=? AND "
				+ USER_TASKS_SPECIFIC_KEY_TASK_ID + "=?", new String[] { playerEmail, taskID.longValue() + "" }) != 0;
		db.close();
		return isSucessful;
	}
	// TASKS METHODS END
}