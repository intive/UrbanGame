package com.example.rest;

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

public abstract class WebDownloader {
	private HttpClient httpClient;
	
	public WebDownloader() {
		httpClient = HttpClientFactory.getHttpClient();
	}
	
	public String executeRequest(Uri requestUri) {
		try {
			return downloadDataFromUri(requestUri);
		}
		catch (IOException e) {
			return "";
		}
	}
	
	private String downloadDataFromUri(Uri requestUri) throws IOException {
		String requestString = requestUri.toString();
		HttpUriRequest httpUriRequest = getHttpRequest(requestString);
		InputStream inputStream = getInputStreamFromRequest(httpUriRequest);
		String result = convertInputStreamToString(inputStream);
		return result;
	}
	
	protected abstract HttpUriRequest getHttpRequest(String requestUri);
	
	private InputStream getInputStreamFromRequest(HttpUriRequest request) throws IllegalStateException, IOException {
		HttpResponse response = httpClient.execute(request);
		HttpEntity responseEntity = response.getEntity();
		InputStream responseInputStream = responseEntity.getContent();
		return responseInputStream;
	}
	
	private String convertInputStreamToString(InputStream inputStream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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

class WebDownloaderGET extends WebDownloader {
	@Override
	public HttpUriRequest getHttpRequest(String requestUri) {
		return new HttpGet(requestUri);
	}
}

class WebDownloaderPOST extends WebDownloader {
	@Override
	public HttpUriRequest getHttpRequest(String requestUri) {
		return new HttpPost(requestUri);
	}
	
}

class WebDownloaderPUT extends WebDownloader {
	@Override
	public HttpUriRequest getHttpRequest(String requestUri) {
		return new HttpPut(requestUri);
	}
	
}

class WebDownloaderDELETE extends WebDownloader {
	@Override
	public HttpUriRequest getHttpRequest(String requestUri) {
		return new HttpDelete(requestUri);
	}
}