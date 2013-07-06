package com.blstream.urbangame.webserver.json;

import java.util.List;

import org.json.JSONException;

import com.blstream.urbangame.database.entity.UrbanGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonParser {
	
	public static String URBANGAME_URL = "http://urbangame.patronage.blstream.com/";
	
	public UrbanGame getUrbanGameFromJSON(String urbanGaneInJSON) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UrbanGame.class, new UrbanGameDeserializer());
		Gson gson = gsonBuilder.create();
		
		// Parse JSON to Java
		return gson.fromJson(urbanGaneInJSON, UrbanGame.class);
		
	}
	
	public List<UrbanGame> getUrbanGameListFromJSON(String urbanGameListInJSON) throws JSONException {
		// Configure GSON
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(UrbanGameContainer.class, new UrbanGameListDeserializer());
		Gson gson = gsonBuilder.create();
		
		// Parse JSON to Java
		UrbanGameContainer container = gson.fromJson(urbanGameListInJSON, UrbanGameContainer.class);
		
		return container.getGameList();
	}
	
}
