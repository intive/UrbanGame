package com.blstream.urbangame.database.dbobjects;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBSQLite implements DBWrapper {
	
	SQLiteDatabase db;
	
	public DBSQLite(SQLiteDatabase db) {
		this.db = db;
	}
	
	@Override
	public void execSQL(String sql) {
		db.execSQL(sql);
	}
	
	@Override
	public long insert(String table, String nullColumnHack, ContentValues values) {
		return db.insert(table, nullColumnHack, values);
	}
	
	@Override
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
		String having, String orderBy) {
		return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	}
	
	@Override
	public Cursor rawQuery(String sql, String[] selectionArgs) {
		return db.rawQuery(sql, selectionArgs);
	}
	
	@Override
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		return db.update(table, values, whereClause, whereArgs);
	}
	
	@Override
	public int delete(String table, String whereClause, String[] whereArgs) {
		return db.delete(table, whereClause, whereArgs);
	}
	
	@Override
	public void beginTransaction() {
		db.beginTransaction();
	}
	
	@Override
	public void setTransactionSuccessful() {
		db.setTransactionSuccessful();
	}
	
	@Override
	public void endTransaction() {
		db.endTransaction();
	}
	
	@Override
	public void close() {
		db.close();
	}
}
