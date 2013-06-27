package com.example.rest;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * This class produces thread safe HTTP client which can handle multiple
 * connections at the same time and configure connection properties even with
 * client representation
 */
public class HttpClientFactory {
	private static final String CLIENT_NAME = "Android";
	private static final String PROTOCOL = "http";
	private static final int PORT = 80;
	private static final int NUMBER_OF_MAX_CONNECTIONS = 5;
	private static final int WAITING_TIMEOUT = 15 * 1000;	// 15s
	
	private static HttpClientFactory instance;
	
	private AbstractHttpClient httpClient;
	
	private HttpClientFactory() {
		ThreadSafeClientConnManager connectionManager = getConnectionManager();
		HttpParams clientParams = getClientParams();
		
		httpClient = new DefaultHttpClient(connectionManager, clientParams);
	}
	
	/**
	 * ThreadSafeClientConnManager is class, which we can then use to configure
	 * the HTTP client object with specified parameters like connection
	 * properties and scheme registry
	 */
	private ThreadSafeClientConnManager getConnectionManager() {
		SchemeRegistry schemeRegistry = getSchemeRegistry();
		HttpParams connManagerParams = getConnectionManagerParams();
		
		return new ThreadSafeClientConnManager(connManagerParams, schemeRegistry);
	}
	
	/**
	 * Scheme registry is responsible for resolving a URI scheme and port number
	 * to a TCP socket created by an appropriate socket factory
	 */
	private SchemeRegistry getSchemeRegistry() {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme(PROTOCOL, PlainSocketFactory.getSocketFactory(), PORT));
		return schemeRegistry;
	}
	
	/**
	 * Connection manager parameters are sets of settings configuring connection
	 * properties such a number of max connections keeping by this connection
	 */
	private HttpParams getConnectionManagerParams() {
		HttpParams connManagerParams = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(connManagerParams, NUMBER_OF_MAX_CONNECTIONS);
		ConnManagerParams.setMaxConnectionsPerRoute(connManagerParams, new ConnPerRouteBean(NUMBER_OF_MAX_CONNECTIONS));
		return connManagerParams;
	}
	
	/**
	 * Client parameters describing client details visible to server and
	 * containing parameters like for e.g. maximum time waiting for response
	 */
	private HttpParams getClientParams() {
		HttpParams clientParams = new BasicHttpParams();
		HttpProtocolParams.setUserAgent(clientParams, CLIENT_NAME);
		HttpConnectionParams.setConnectionTimeout(clientParams, WAITING_TIMEOUT);
		HttpConnectionParams.setSoTimeout(clientParams, WAITING_TIMEOUT);
		return clientParams;
	}
	
	public static HttpClient getHttpClient() {
		if (instance == null) {
			instance = new HttpClientFactory();
		}
		return instance.httpClient;
	}
}