package com.blstream.urbangame.database;

/**
 * This class is intended to contain all database table's names, table creation
 * strings etc.
 */
public class DatabaseDefinitions {
	// database standard info
	public static final int DATABASE_VERSION = 1;
	
	// tables
	public static final String GAMES_TABLE_NAME = "games";
	public static final String USER_TABLE_NAME = "user";
	public static final String USER_GAMES_SPECIFIC_TABLE_NAME = "userGamesSpecific";
	public static final String USER_LOGGED_IN_TABLE_NAME = "userLoggedIn";
	public static final String TASKS_TABLE_NAME = "tasks";
	public static final String GAMES_TASKS_TABLE_NAME = "gamesTasks";
	public static final String USER_TASKS_SPECIFIC_TABLE_NAME = "userTasksSpecific";
	public static final String TASKS_ABCD_TABLE_NAME = "tasksABCD";
	public static final String TASKS_ABCD_POSSIBLE_ANSWERS_TABLE_NAME = "tasksPossibleAnswersABCD";
	
	// tables columns
	// ---- Games
	public static final String GAMES_KEY_ID = "gid";
	public static final String GAMES_KEY_VERSION = "GVersion";
	public static final String GAMES_KEY_TITLE = "GTitle";
	public static final String GAMES_KEY_DESCRIPTION = "GDescription";
	public static final String GAMES_KEY_GAME_ICON = "GIcon";
	public static final String GAMES_KEY_OPERATOR_ICON = "GIconOperator";
	public static final String GAMES_KEY_OPERATOR_NAME = "GOperatorName";
	public static final String GAMES_KEY_NUMBER_OF_PLAYERS = "GNumberOfPlayers";
	public static final String GAMES_KEY_NUMBER_OF_MAX_PLAYERS = "GNumberOfMAXPlayers";
	public static final String GAMES_KEY_DIFFICULTY = "GDifficulty";
	public static final String GAMES_KEY_CITY_NAME = "GCityName";
	public static final String GAMES_KEY_START_DATE = "GStartDate";
	public static final String GAMES_KEY_END_DATE = "GEndDate";
	public static final String GAMES_KEY_COMMENTS = "GComments";
	public static final String GAMES_KEY_REWARD = "GReward";
	public static final String GAMES_KEY_WINNING_STRATEGY = "GWinningStrategy";
	public static final String GAMES_KEY_PRIZE_INFO = "GPrizeInfo";
	public static final String GAMES_KEY_DETAILS_LINK = "GDetailsLink";
	
	// ---- User
	public static final String USER_KEY_EMAIL = "UEmail";
	public static final String USER_KEY_PASSWORD = "UPassword";
	public static final String USER_KEY_DISPLAY_NAME = "UDispName";
	public static final String USER_KEY_AVATAR = "UAvatar";
	
	// ---- User games specific
	public static final String USER_GAMES_SPECIFIC_KEY_EMAIL = "UGSEmail";
	public static final String USER_GAMES_SPECIFIC_KEY_GAME_ID = "UGSGameID";
	public static final String USER_GAMES_SPECIFIC_KEY_RANK = "UGSRank";
	public static final String USER_GAMES_SPECIFIC_KEY_GAME_ACTIVE_OBSERVED = "UGSGameActiveObserved";
	public static final String USER_GAMES_SPECIFIC_KEY_CHANGES = "UGSChanges";
	public static final String USER_GAMES_SPECIFIC_KEY_HAS_CHANGES = "UGSHasChanges";
	
	// ---- User logging table
	public static final String USER_LOGGED_IN_KEY_EMAIL = "ULIEmail";
	
	// ---- Tasks
	public static final String TASKS_KEY_ID = "tid";
	public static final String TASKS_KEY_TYPE = "TType";
	public static final String TASKS_KEY_TITLE = "TTitle";
	public static final String TASKS_KEY_MAX_POINTS = "TMaxPoints";
	public static final String TASKS_KEY_REPETABLE = "TRepetable";
	public static final String TASKS_KEY_IS_HIDDEN = "THidden";
	public static final String TASKS_KEY_NUMBER_OF_HIDDEN = "TNumberOfHidden";
	public static final String TASKS_KEY_END_TIME = "TEndTime";
	public static final String TASKS_KEY_PICTURE = "TPicture";
	public static final String TASKS_KEY_DESCRIPTION = "TDescription";
	
	// ---- Games Tasks
	public static final String GAMES_TASKS_KEY_GAME_ID = "GTgid";
	public static final String GAMES_TASKS_KEY_TASK_ID = "GTtid";
	
	// ---- Users tasks specific
	public static final String USER_TASKS_SPECIFIC_KEY_PLAYER_EMAIL = "UTSPlayerEmail";
	public static final String USER_TASKS_SPECIFIC_KEY_TASK_ID = "UTSTaskID";
	public static final String USER_TASKS_SPECIFIC_KEY_POINTS = "UTSPoints";
	public static final String USER_TASKS_SPECIFIC_KEY_IS_FINISHED = "UTSIsFisnished";
	public static final String USER_TASKS_SPECIFIC_KEY_ARE_CHANGES = "UTSAreChanges";
	public static final String USER_TASKS_SPECIFIC_KEY_WAS_HIDDEN = "UTSWasHidden";
	public static final String USER_TASKS_SPECIFIC_KEY_CHANGES = "UTSChanges";
	public static final String USER_TASKS_SPECIFIC_KEY_STATUS = "UTSStatus";
	
	// ---- Tasks ABCD
	public static final String TASKS_ABCD_KEY_ID = "TAID";
	public static final String TASKS_ABCD_KEY_TASK_ID = "TAtaskID";
	public static final String TASKS_ABCD_KEY_QUESTION = "TAtaskQuestion";
	
	// ---- Task ABCD answers
	public static final String TASKS_ABCD_POSSIBLE_ANSWERS_KEY_ID = "TAPAID";
	public static final String TASKS_ABCD_POSSIBLE_ANSWERS_KEY_TASK_POSSIBLE_ANSWER = "TAPAtaskPossibleAnswer";
	
	// tables creation strings
	public static final String CREATE_GAMES_TABLE = "CREATE TABLE " + GAMES_TABLE_NAME + " (" + GAMES_KEY_ID
		+ " INTEGER PRIMARY KEY, " + GAMES_KEY_VERSION + " REAL, " + GAMES_KEY_TITLE + " TEXT, "
		+ GAMES_KEY_DESCRIPTION + " TEXT, " + GAMES_KEY_GAME_ICON + " TEXT, " + GAMES_KEY_OPERATOR_ICON + " TEXT, "
		+ GAMES_KEY_OPERATOR_NAME + " TEXT, " + GAMES_KEY_NUMBER_OF_PLAYERS + " INTEGER, "
		+ GAMES_KEY_NUMBER_OF_MAX_PLAYERS + " INTEGER, " + GAMES_KEY_DIFFICULTY + " INTEGER, " + GAMES_KEY_CITY_NAME
		+ " TEXT, " + GAMES_KEY_START_DATE + " INTEGER, " + GAMES_KEY_WINNING_STRATEGY + " TEXT, " + GAMES_KEY_END_DATE
		+ " INTEGER, " + GAMES_KEY_COMMENTS + " TEXT, " + GAMES_KEY_REWARD + " TEXT, " + GAMES_KEY_PRIZE_INFO
		+ " TEXT, " + GAMES_KEY_DETAILS_LINK + " TEXT" + ")";
	
	public static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE_NAME + " (" + USER_KEY_EMAIL
		+ " TEXT PRIMARY KEY, " + USER_KEY_PASSWORD + " TEXT, " + USER_KEY_DISPLAY_NAME + " TEXT," + USER_KEY_AVATAR
		+ " TEXT" + ")";
	
	public static final String CREATE_USER_GAMES_SPECIFIC_TABLE = "CREATE TABLE " + USER_GAMES_SPECIFIC_TABLE_NAME
		+ " (" + USER_GAMES_SPECIFIC_KEY_EMAIL + " TEXT, " + USER_GAMES_SPECIFIC_KEY_GAME_ID + " INTEGER, "
		+ USER_GAMES_SPECIFIC_KEY_RANK + " INTEGER, " + USER_GAMES_SPECIFIC_KEY_GAME_ACTIVE_OBSERVED + " INTEGER, "
		+ USER_GAMES_SPECIFIC_KEY_CHANGES + " TEXT, " + USER_GAMES_SPECIFIC_KEY_HAS_CHANGES + " TEXT, "
		+ "PRIMARY KEY (" + USER_GAMES_SPECIFIC_KEY_EMAIL + ", " + USER_GAMES_SPECIFIC_KEY_GAME_ID + ")"
		+ " FOREIGN KEY (" + USER_GAMES_SPECIFIC_KEY_EMAIL + ") " + "REFERENCES " + USER_TABLE_NAME + " ("
		+ USER_KEY_EMAIL + ") " + " FOREIGN KEY (" + USER_GAMES_SPECIFIC_KEY_GAME_ID + ") " + "REFERENCES "
		+ GAMES_TABLE_NAME + " (" + GAMES_KEY_ID + ") " + ")";
	
	public static final String CREATE_USER_LOGGED_IN_TABLE = "CREATE TABLE " + USER_LOGGED_IN_TABLE_NAME + " ("
		+ USER_LOGGED_IN_KEY_EMAIL + " TEXT PRIMARY KEY" + ")";
	
	public static final String CREATE_TASKS_TABLE = "CREATE TABLE " + TASKS_TABLE_NAME + " (" + TASKS_KEY_ID
		+ " INTEGER PRIMARY KEY, " + TASKS_KEY_DESCRIPTION + " TEXT NOT NULL, " + TASKS_KEY_END_TIME + " INTEGER, "
		+ TASKS_KEY_IS_HIDDEN + " INTEGER, " + TASKS_KEY_MAX_POINTS + " INTEGER NOT NULL, "
		+ TASKS_KEY_NUMBER_OF_HIDDEN + " INTEGER, " + TASKS_KEY_PICTURE + " TEXT, " + TASKS_KEY_REPETABLE
		+ " INTEGER NOT NULL, " + TASKS_KEY_TITLE + " TEXT, " + TASKS_KEY_TYPE + " INTEGER" + ")";
	
	public static final String CREATE_GAMES_TASKS_TABLE = "CREATE TABLE " + GAMES_TASKS_TABLE_NAME + " ("
		+ GAMES_TASKS_KEY_GAME_ID + " INTEGER, " + GAMES_TASKS_KEY_TASK_ID + " INTEGER, " + "PRIMARY KEY ("
		+ GAMES_TASKS_KEY_GAME_ID + ", " + GAMES_TASKS_KEY_TASK_ID + ")" + " FOREIGN KEY (" + GAMES_TASKS_KEY_GAME_ID
		+ ") REFERENCES " + GAMES_TABLE_NAME + " (" + GAMES_KEY_ID + "), " + " FOREIGN KEY (" + GAMES_TASKS_KEY_TASK_ID
		+ ") REFERENCES " + TASKS_TABLE_NAME + "(" + TASKS_KEY_ID + ")" + ")";
	
	public static final String CREATE_USER_TASKS_SPECIFIC_TABLE = "CREATE TABLE " + USER_TASKS_SPECIFIC_TABLE_NAME
		+ " (" + USER_TASKS_SPECIFIC_KEY_PLAYER_EMAIL + " TEXT, " + USER_TASKS_SPECIFIC_KEY_TASK_ID + " INTEGER, "
		+ USER_TASKS_SPECIFIC_KEY_ARE_CHANGES + " TEXT, " + USER_TASKS_SPECIFIC_KEY_IS_FINISHED + " TEXT NOT NULL, "
		+ USER_TASKS_SPECIFIC_KEY_POINTS + " INTEGER, " + USER_TASKS_SPECIFIC_KEY_WAS_HIDDEN + " TEXT, "
		+ USER_TASKS_SPECIFIC_KEY_CHANGES + " TEXT, " + USER_TASKS_SPECIFIC_KEY_STATUS + " INTEGER, " + "PRIMARY KEY ("
		+ USER_TASKS_SPECIFIC_KEY_PLAYER_EMAIL + ", " + USER_TASKS_SPECIFIC_KEY_TASK_ID + "), " + " FOREIGN KEY ("
		+ USER_TASKS_SPECIFIC_KEY_TASK_ID + ") REFERENCES " + TASKS_TABLE_NAME + " (" + TASKS_KEY_ID + "), "
		+ " FOREIGN KEY (" + USER_TASKS_SPECIFIC_KEY_PLAYER_EMAIL + ") REFERENCES " + USER_TABLE_NAME + " ("
		+ USER_KEY_EMAIL + ") " + ")";
	
	public static final String CREATE_TASKS_ABCD_TABLE = "CREATE TABLE " + TASKS_ABCD_TABLE_NAME + " ("
		+ TASKS_ABCD_KEY_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + TASKS_ABCD_KEY_TASK_ID + " INTEGER, "
		+ TASKS_ABCD_KEY_QUESTION + " TEXT NOT NULL, " + "FOREIGN KEY (" + TASKS_ABCD_KEY_TASK_ID + ") "
		+ "REFERENCES " + TASKS_TABLE_NAME + " (" + TASKS_KEY_ID + ") " + ")";
	
	public static final String CREATE_TASKS_ABCD_POSSIBLE_ANSWERS_TABLE = "CREATE TABLE "
		+ TASKS_ABCD_POSSIBLE_ANSWERS_TABLE_NAME + " (" + TASKS_ABCD_POSSIBLE_ANSWERS_KEY_ID + " INTEGER, "
		+ TASKS_ABCD_POSSIBLE_ANSWERS_KEY_TASK_POSSIBLE_ANSWER + " INTEGER, " + "PRIMARY KEY ("
		+ TASKS_ABCD_POSSIBLE_ANSWERS_KEY_ID + ", " + TASKS_ABCD_POSSIBLE_ANSWERS_KEY_TASK_POSSIBLE_ANSWER + "), "
		+ "FOREIGN KEY (" + TASKS_ABCD_POSSIBLE_ANSWERS_KEY_ID + ") " + "REFERENCES " + TASKS_ABCD_TABLE_NAME + " ("
		+ TASKS_ABCD_KEY_ID + ") " + ")";
}
