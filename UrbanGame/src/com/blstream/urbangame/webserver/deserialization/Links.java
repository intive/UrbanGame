package com.blstream.urbangame.webserver.deserialization;

import android.util.Log;

import com.blstream.urbangame.webserver.helper.WebResource;

public class Links {
	
	private final String TAG = Links.class.getSimpleName();
	
	Link self;
	Link games;
	Link login;
	Link register;
	Link userGames;
	Link tasks;
	
	public String getLinkFromResource(String resourceName) {
		
		if (resourceName.equals(WebResource.RESOURCE_SELF)) return self.getHref();
		if (resourceName.equals(WebResource.RESOURCE_GAMES)) return games.getHref();
		if (resourceName.equals(WebResource.RESOURCE_TASKS)) return tasks.getHref();
		
		Log.e(TAG, "Incorrect resourceName " + resourceName);
		
		return null;
	}
	
	private class Link {
		private String href;
		
		public String getHref() {
			return href;
		}
	}
	
}
