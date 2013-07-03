package com.blstream.urbangame.webserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.blstream.urbangame.R;
import com.blstream.urbangame.webserver.json.API;
import com.google.gson.Gson;

/**
 * This class is used to obtain specific path for required resource in
 * WebServer. Root path provides basic links to other resources, due to them
 * more specific path can be obtained.
 */
//FIXME implementation needed
public class WebAPI {
	private final static ExecutorService executor = Executors.newCachedThreadPool();
	private String base = "http://urbangame.patronage.blstream.com";
	private static final String API_STRING = "/api";
	
	private final Gson gson;
	private API baseAPI;
	private final WebDownloader webDownloader;
	
	public WebAPI(Context context, WebDownloader webDownloader) {
		this.gson = new Gson();
		this.webDownloader = webDownloader;
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		base = sharedPrefs.getString(context.getString(R.string.key_server_name),
			context.getString(R.string.settings_server_default_server_name));
	}
	
	public Uri getAllGamesUri() {
		Uri uri = null;
		try {
			uri = Uri.parse(base + getBaseAPI().links.games.href);
		}
		catch (Exception e) {
			uri = Uri.parse(base);
		}
		return uri;
	}
	
	private API getBaseAPI() {
		if (baseAPI == null) {
			baseAPI = getJsonClassFromUrl(base + API_STRING, API.class);
		}
		return baseAPI;
	}
	
	public Uri getLoginUri() {
		Uri uri = null;
		try {
			uri = Uri.parse(base + getBaseAPI().links.login.href);
		}
		catch (Exception e) {
			uri = Uri.parse(base);
		}
		return uri;
	}
	
	public Uri getRegisterUri() {
		Uri uri = null;
		try {
			uri = Uri.parse(base + getBaseAPI().links.register.href);
		}
		catch (Exception e) {
			uri = Uri.parse(base);
		}
		return uri;
	}
	
	private <T> T getJsonClassFromUrl(String url, Class<T> cls) {
		Future<T> future = executor.submit(new GsonFetcher<T>(url, cls));
		try {
			return future.get();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * GsonFetcher is builder class to produce and return some specified class
	 * T, which represents JSON object. What is the most important, this class
	 * is ready to be invoked as a background, separate thread.
	 * 
	 * @param <T> class to return
	 */
	private final class GsonFetcher<T> implements Callable<T> {
		private final String url;
		private final Class<T> cls;
		
		public GsonFetcher(String url, Class<T> cls) {
			this.url = url;
			this.cls = cls;
		}
		
		@Override
		public T call() throws IllegalStateException, IOException {
			InputStreamReader reader = webDownloader.getGsonInputStreamReaderFromUrl(url);
			T result = gson.fromJson(reader, cls);
			return result;
		}
	}
}