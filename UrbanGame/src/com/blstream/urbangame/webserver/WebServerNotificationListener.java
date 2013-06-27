package com.blstream.urbangame.webserver;

import android.os.Message;

/**
 * This client interface is provided for all activities which execute
 * {@link WebServer} methods. Each of them should implement this interface and
 * pass themselves to web server constructor in order to get response for
 * invoking this server methods.
 */
public interface WebServerNotificationListener {
	/**
	 * Will be called after performed some method by server
	 * 
	 * @param message result of executing server action
	 */
	public void onWebServerResponse(Message message);
}