package com.blstream.urbangame.webserver.json;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.LinkedList;

import com.blstream.urbangame.database.entity.UrbanGame;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class UrbanGameListDeserializer implements JsonDeserializer<UrbanGameContainer> {
	
	@Override
	public UrbanGameContainer deserialize(final JsonElement json, final Type typeOfT,
		final JsonDeserializationContext context) throws JsonParseException {
		
		LinkedList<UrbanGame> resultList = new LinkedList<UrbanGame>();
		
		JsonObject jsonObject = json.getAsJsonObject();
		
		JsonArray jsonArray = jsonObject.get("_embedded").getAsJsonArray();
		
		for (int i = 0; i < jsonArray.size(); i++) {
			UrbanGame urbanGame = new UrbanGame();
			JsonObject jsonObjectArrayElement = jsonArray.get(i).getAsJsonObject();
			
			urbanGame.setID(jsonObjectArrayElement.get("gid").getAsLong());
			urbanGame.setGameVersion(jsonObjectArrayElement.get("version").getAsDouble());
			urbanGame.setTitle(jsonObjectArrayElement.get("name").getAsString());
			urbanGame.setStartDate(new Date(jsonObjectArrayElement.get("startTime").getAsLong()));
			urbanGame.setEndDate(new Date(jsonObjectArrayElement.get("endTime").getAsLong()));
			urbanGame.setDifficulty(JsonParser.parseDifficulty(jsonObjectArrayElement.get("difficulty").getAsString()));
			urbanGame.setOperatorName(jsonObjectArrayElement.get("operatorName").getAsString());
			urbanGame.setMaxPlayers(jsonObjectArrayElement.get("maxPlayers").getAsInt());
			urbanGame.setPlayers(jsonObjectArrayElement.get("numberOfPlayers").getAsInt());
			urbanGame.setPrizesInfo(jsonObjectArrayElement.get("awards").getAsString());
			try {
				urbanGame.setGameLogoDrawable(JsonParser.drawableFromUrl(JsonParser.URBANGAME_URL
					+ jsonObjectArrayElement.get("operatorLogo").getAsString()));
			}
			catch (IOException e) {
				//no internet connection logo stays null
			}
			try {
				urbanGame.setGameLogoDrawable(JsonParser.drawableFromUrl(JsonParser.URBANGAME_URL
					+ jsonObjectArrayElement.get("image").getAsString()));
			}
			catch (IOException e) {
				//no internet connection, image stays null
			}
			
			urbanGame.setReward(!(urbanGame.getPrizesInfo() == null || urbanGame.getPrizesInfo().equals("")));
			
			resultList.add(urbanGame);
			
		}
		
		return new UrbanGameContainer(resultList);
	}
	
}
