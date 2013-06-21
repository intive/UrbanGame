package com.blstream.urbangame.database.helper;

//formatter: off
import static com.blstream.urbangame.database.DatabaseDefinitions.*;
//formatter: on

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.dbobjects.DBSQLCipher;
import com.blstream.urbangame.database.dbobjects.DBWrapper;



public class SQLCipherHelper extends SQLiteOpenHelper implements SQLInterface {
	
	public static final String DATABASE_NAME = "urban_game_encryption";
	private static final String DATABASE_PASS = "adslkfnalskgfasdqwfuaiheofidjkslfa";
	
	public SQLCipherHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		SQLiteDatabase.loadLibs(context);
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
		db.execSQL("DROP TABLE IF EXISTS " + TASKS_ABCD_POSSIBLE_ANSWERS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TASKS_ABCD_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + USER_TASKS_SPECIFIC_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + GAMES_TASKS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + USER_GAMES_SPECIFIC_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + GAMES_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + USER_LOGGED_IN_TABLE_NAME);
		onCreate(db);
	}
	
	@Override
	public DBWrapper getWrappedReadableDatabase() {
		return new DBSQLCipher(super.getReadableDatabase(DATABASE_PASS));
	}
	
	@Override
	public DBWrapper getWrappedWritableDatabase() {
		return new DBSQLCipher(super.getWritableDatabase(DATABASE_PASS));
	}
	
	@Override
	public String getDatabaseName() {
		return DATABASE_NAME;
	}
}
