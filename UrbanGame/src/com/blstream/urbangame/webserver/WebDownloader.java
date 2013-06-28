package com.blstream.urbangame.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;

import android.net.Uri;
import android.util.Log;

/**
 * This is the most low-level class responsible for handling Internet
 * connection. It uses {@link HttpClientFactory} to get {@link HttpClient}, then
 * from provided URI request create concrete HTTP method. Request is used to
 * open connection with web server from which {@link InputStream} is opened.
 * After that input stream is read to String result, which is being returned.
 */
public abstract class WebDownloader {
	public final static String EMPTY_JSON = "{}";
	private final static String TAG = WebDownloader.class.getSimpleName();
	private HttpClient httpClient;
	
	public WebDownloader() {
		httpClient = HttpClientFactory.getHttpClient();
	}
	
	public String executeRequest(Uri requestUri) {
		try {
			return downloadDataFromUri(requestUri);
		}
		catch (IOException e) {
			Log.e(TAG, e.getMessage());
			Log.d(TAG, "Possible issues: host doesn't exist or no internet connection.");
			return EMPTY_JSON;
		}
	}
	
	private String downloadDataFromUri(Uri requestUri) throws IOException {
		String requestString = requestUri.toString();
		HttpUriRequest httpRequestMethod = getHttpRequestMethod(requestString);
		InputStreamReader inputStreamReader = getInputStreamReaderFromRequest(httpRequestMethod);
		String result = readInputStreamToString(inputStreamReader);
		return result;
	}
	
	protected abstract HttpUriRequest getHttpRequestMethod(String requestUri);
	
	public InputStreamReader getGsonInputStreamReaderFromUrl(String url) throws IllegalStateException, IOException {
		HttpUriRequest httpRequest = new HttpGet(url);
		InputStreamReader inputStreamReader = getInputStreamReaderFromRequest(httpRequest);
		return inputStreamReader;
	}
	
	private InputStreamReader getInputStreamReaderFromRequest(HttpUriRequest request) throws IllegalStateException,
		IOException {
		HttpResponse response = httpClient.execute(request);
		HttpEntity responseEntity = response.getEntity();
		InputStream responseInputStream = responseEntity.getContent();
		InputStreamReader inputStreamReader = new InputStreamReader(responseInputStream);
		return inputStreamReader;
	}
	
	private String readInputStreamToString(InputStreamReader inputStreamReader) throws IOException {
		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuilder sb = new StringBuilder();
		String line = reader.readLine();
		while (line != null) {
			sb.append(line);
			line = reader.readLine();
		}
		reader.close();
		return sb.toString();
	}
}

/**
 * Here are some specific class extending {@link WebDownloader} to perform
 * concrete queries to web server. By REST convention we need GET, POST, PUT and
 * DELETE of HTTP methods and these classes create request objects to execute
 * via http connection.
 */
class WebDownloaderGET extends WebDownloader {
	@Override
	public HttpUriRequest getHttpRequestMethod(String requestUri) {
		return new HttpGet(requestUri);
	}
}

class WebDownloaderPOST extends WebDownloader {
	@Override
	public HttpUriRequest getHttpRequestMethod(String requestUri) {
		return new HttpPost(requestUri);
	}
}

class WebDownloaderPUT extends WebDownloader {
	@Override
	public HttpUriRequest getHttpRequestMethod(String requestUri) {
		return new HttpPut(requestUri);
	}
}

class WebDownloaderDELETE extends WebDownloader {
	@Override
	public HttpUriRequest getHttpRequestMethod(String requestUri) {
		return new HttpDelete(requestUri);
	}
}