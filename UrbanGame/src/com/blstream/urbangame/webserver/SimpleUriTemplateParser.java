package com.blstream.urbangame.webserver;

import java.util.HashMap;

public class SimpleUriTemplateParser {
	
	private final StringBuilder builder;
	private String[] params = null;
	private HashMap<String, String> parameters;
	
	public SimpleUriTemplateParser(String uri) {
		builder = new StringBuilder();
		String[] temp = uri.split("{");
		builder.append(temp[0]);
		if (temp.length > 1) {
			params = temp[1].substring(1, temp[1].length() - 2).split(",");
			parameters = new HashMap<String, String>(params.length);
			for (String element : params) {
				parameters.put(element, "");
			}
		}
	}
	
	public String[] getListOfParameters() {
		return params;
	}
	
	public boolean putParameter(String parameterName, String value) {
		if (parameters.containsKey(parameterName)) {
			parameters.put(parameterName, value);
			return true;
		}
		else return false;
	}
	
	public String getUri() {
		if (params != null) {
			builder.append("?");
			for (String element : params) {
				builder.append(element);
				builder.append("=");
				builder.append(parameters.get(element));
				builder.append("&");
			}
			builder.deleteCharAt(builder.length() - 1);
		}
		
		return builder.toString();
	}
	
}
