package com.blstream.urbangame.webserver.deserialization;

import com.google.gson.annotations.SerializedName;

/*
 * This class is a base class for all JSON responses from server.
 */
public class JsonResponse {
	
	@SerializedName("_links")
	Links links;
	
	public String getLinkFromResource(String resourceName) {
		return links.getLinkFromResource(resourceName);
	}
	
}
