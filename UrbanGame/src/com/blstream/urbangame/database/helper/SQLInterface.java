package com.blstream.urbangame.database.helper;

import com.blstream.urbangame.database.dbobjects.DBWrapper;

public interface SQLInterface {
	/**
	 * @return readable database
	 */
	public DBWrapper getWrappedReadableDatabase();
	
	/**
	 * @return writable database
	 */
	public DBWrapper getWrappedWritableDatabase();
	
	/**
	 * Database Helper method access
	 */
	public void close();
	
	/**
	 * @return database name. It's different for SQLite and SQLCipher databases
	 */
	public String getDatabaseName();
}
