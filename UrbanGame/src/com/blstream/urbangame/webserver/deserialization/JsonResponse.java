package com.blstream.urbangame.webserver.deserialization;

/*
 * This class is a base class for all JSON responses from server.
 */
public class JsonResponse {
	
	Links _links;
	
	public String getLinkFromResource(String resourceName) {
		return _links.getLinkFromResource(resourceName);
	}
	
}
