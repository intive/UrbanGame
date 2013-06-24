package com.blstream.urbangame.database.dbobjects;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * It's a wrapper class because we cannot extend final class
 */
public interface DBWrapper {
	
	public void execSQL(String sql);
	
	public long insert(String table, String nullColumnHack, ContentValues values);
	
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
		String having, String orderBy);
	
	public Cursor rawQuery(String sql, String[] selectionArgs);
	
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs);
	
	public int delete(String table, String whereClause, String[] whereArgs);
	
	public void beginTransaction();
	
	public void setTransactionSuccessful();
	
	public void endTransaction();
	
	public void close();
}
