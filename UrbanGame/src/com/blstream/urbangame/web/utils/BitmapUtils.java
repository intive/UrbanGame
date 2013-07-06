package com.blstream.urbangame.web.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

class BitmapUtils {
	private final static String LOG = BitmapUtils.class.getSimpleName();
	private final static String FILE_NAME = "urbangame";
	private final Context context;
	
	BitmapUtils(Context context) {
		this.context = context;
	}
	
	public static void downloadAndSetBitmapFromLink(long feedId, String link, ImageView imageView) {
		new BitmapDownloader(feedId, imageView).execute(link);
	}
	
	/**
	 * Saving bitmap to file
	 */
	public void writeBitmapForFeed(long feedId, Bitmap bitmap) {
		bitmap.compress(Bitmap.CompressFormat.PNG, 0, getInternalOutputStream(feedId));
	}
	
	private OutputStream getInternalOutputStream(long feedId) {
		try {
			return context.openFileOutput(FILE_NAME + feedId, Context.MODE_PRIVATE);
		}
		catch (FileNotFoundException e) {
			Log.e(LOG, e.getMessage());
			return null;
		}
	}
	
	/**
	 * Reading bitmap from file
	 */
	public Bitmap readBitmapForFeed(long feedId) {
		return BitmapFactory.decodeStream(getInternalInputStream(feedId));
	}
	
	private InputStream getInternalInputStream(long feedId) {
		try {
			return context.openFileInput(FILE_NAME + feedId);
		}
		catch (FileNotFoundException e) {
			Log.e(LOG, e.getMessage());
			return null;
		}
	}
}