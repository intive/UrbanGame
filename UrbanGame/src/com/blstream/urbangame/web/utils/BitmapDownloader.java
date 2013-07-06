package com.blstream.urbangame.web.utils;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class BitmapDownloader extends AsyncTask<String, Void, Bitmap> {
	private final static String TAG = BitmapDownloader.class.getSimpleName();
	
	private long id;
	private ImageView imageView;
	private Drawable placeholder;
	
	public BitmapDownloader(long id, ImageView imageView) {
		this.id = id;
		this.imageView = imageView;
		this.placeholder = gerResources().getDrawable(android.R.drawable.gallery_thumb);
		
		setImageId(id);
	}
	
	private Resources gerResources() {
		return imageView.getContext().getResources();
	}
	
	private void setImageId(long id) {
		this.imageView.setTag(id);
	}
	
	@Override
	protected void onPreExecute() {
		imageView.setImageDrawable(placeholder);
	}
	
	@Override
	protected Bitmap doInBackground(String... inputUrls) {
		try {
			URL url = getDownloadURL(inputUrls);
			InputStream stream = url.openStream();
			return BitmapFactory.decodeStream(stream);
		}
		catch (Exception e) {
			logErrorIfNotNullPointerException(e);
			return null;
		}
	}
	
	private URL getDownloadURL(String... inputUrls) throws MalformedURLException {
		return new URL(inputUrls[0]);
	}
	
	private void logErrorIfNotNullPointerException(Exception e) {
		if (e != null) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		if (resultExists(result)) {
			long forId = getImageId();
			
			if (isIdForImage(forId) && imageViewExists()) {
				this.imageView.setImageBitmap(result);
			}
		}
	}
	
	private Long getImageId() {
		return (Long) imageView.getTag();
	}
	
	private boolean isIdForImage(long forId) {
		return forId == this.id;
	}
	
	private boolean resultExists(Bitmap result) {
		return result != null;
	}
	
	private boolean imageViewExists() {
		return imageView != null;
	}
}