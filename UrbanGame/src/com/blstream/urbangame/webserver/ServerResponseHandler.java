package com.blstream.urbangame.webserver;

import java.lang.ref.WeakReference;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * This handler is message receiver to be used in every client activity where
 * {@link WebServer} is used. We can register this handler in
 * {@link WebServerNotificationListener} class and pass it to web sever. After
 * every query to this server, handler will collect data changes from server and
 * notify web server notification listener using appropriate interface method.
 */
public class ServerResponseHandler extends Handler {
	/**
	 * Handler keeps weak reference to listener in order to prevent from memory
	 * leaks and is able to be safely removed by garbage collector when it's not
	 * needed anymore.
	 */
	private final WeakReference<? extends WebServerNotificationListener> weakReferenceToWebServerNotificationListener;
	
	public ServerResponseHandler(WebServerNotificationListener webServerNotificationListener) {
		this.weakReferenceToWebServerNotificationListener = new WeakReference<WebServerNotificationListener>(
			webServerNotificationListener);
	}
	
	/**
	 * This method is invoked every time when web server called
	 * handler.sendMessage(...) on this handler. In it's body we notify listener
	 * with obtained data as a {@link Message} object, which can store another
	 * object and a {@link Bundle} which is just a bunch of pairs <key, value>
	 */
	@Override
	public void handleMessage(Message message) {
		WebServerNotificationListener webServerNotificationListener = weakReferenceToWebServerNotificationListener
			.get();
		if (webServerNotificationListener != null) {
			webServerNotificationListener.onWebServerResponse(message);
		}
	}
}