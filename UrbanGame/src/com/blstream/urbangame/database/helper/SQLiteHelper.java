package com.blstream.urbangame.database.helper;

//formatter: off
import static com.blstream.urbangame.database.DatabaseDefinitions.*;
//formatter: on

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.dbobjects.DBSQLite;
import com.blstream.urbangame.database.dbobjects.DBWrapper;



public class SQLiteHelper extends SQLiteOpenHelper implements SQLInterface {
	
	public static final String DATABASE_NAME = "urban_game_no_encryption";
	
	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_GAMES_TABLE);
		db.execSQL(CREATE_USER_TABLE);
		db.execSQL(CREATE_USER_GAMES_SPECIFIC_TABLE);
		db.execSQL(CREATE_USER_LOGGED_IN_TABLE);
		db.execSQL(CREATE_TASKS_TABLE);
		db.execSQL(CREATE_GAMES_TASKS_TABLE);
		db.execSQL(CREATE_USER_TASKS_SPECIFIC_TABLE);
		db.execSQL(CREATE_TASKS_ABCD_TABLE);
		db.execSQL(CREATE_TASKS_ABCD_POSSIBLE_ANSWERS_TABLE);
		db.execSQL(CREATE_LOCATION_TASK_ANSWER_TABLE);
	}
	
	@Override
	public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(Database.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
			+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TASK_ANSWERS_TABLE_NAME);
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
		return new DBSQLite(super.getReadableDatabase());
	}
	
	@Override
	public DBWrapper getWrappedWritableDatabase() {
		return new DBSQLite(super.getWritableDatabase());
	}
	
	@Override
	public String getDatabaseName() {
		return DATABASE_NAME;
	}
}
