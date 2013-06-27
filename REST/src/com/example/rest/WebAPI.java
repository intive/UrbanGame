package com.example.rest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.net.Uri;

import com.google.gson.Gson;

/**
 * This class is used to obtain specific path for required resource in
 * WebServer. Root path provides basic links to other resources, due to them
 * more specific path can be obtained.
 */
//FIXME implementation needed
public class WebAPI {
	private final static String base = "http://urbangame.patronage.blstream.com/api";
	private final static ExecutorService executor = Executors.newCachedThreadPool();
	
	private Gson gson;
	private API baseAPI;
	private WebDownloader webDownloader;
	
	public WebAPI(WebDownloader webDownloader) {
		this.gson = new Gson();
		this.webDownloader = webDownloader;
	}
	
	public Uri getAllGamesUri() {
		Uri uri = Uri.parse(base + getBaseAPI().links.games.href);
		return uri;
	}
	
	private API getBaseAPI() {
		if (baseAPI == null) {
			baseAPI = getJsonClassFromUrl(base, API.class);
		}
		return baseAPI;
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
		private String url;
		private Class<T> cls;
		
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