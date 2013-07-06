package com.blstream.urbangame.webserver.json;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import com.blstream.urbangame.database.entity.UrbanGame;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class UrbanGameDeserializer implements JsonDeserializer<UrbanGame> {
	
	@Override
	public UrbanGame deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
		throws JsonParseException {
		
		UrbanGame urbanGame = new UrbanGame();
		
		JsonObject jsonObject = json.getAsJsonObject();
		urbanGame.setID(jsonObject.get("gid").getAsLong());
		urbanGame.setGameVersion(jsonObject.get("version").getAsDouble());
		urbanGame.setTitle(jsonObject.get("name").getAsString());
		urbanGame.setLocation(jsonObject.get("location").getAsString());
		urbanGame.setOperatorName(jsonObject.get("operatorName").getAsString());
		try {
			urbanGame.setGameLogoDrawable(JsonParser.drawableFromUrl(JsonParser.URBANGAME_URL
				+ jsonObject.get("operatorLogo").getAsString()));
		}
		catch (IOException e) {
			//no internet connection logo stays null
		}
		urbanGame.setStartDate(new Date(jsonObject.get("startTime").getAsLong()));
		urbanGame.setEndDate(new Date(jsonObject.get("endTime").getAsLong()));
		urbanGame.setWinningStrategy(jsonObject.get("winning").getAsString());
		urbanGame.setDifficulty(JsonParser.parseDifficulty(jsonObject.get("difficulty").getAsString()));
		urbanGame.setPrizesInfo(jsonObject.get("awards").getAsString());
		try {
			urbanGame.setGameLogoDrawable(JsonParser.drawableFromUrl(JsonParser.URBANGAME_URL
				+ jsonObject.get("image").getAsString()));
		}
		catch (IOException e) {
			//no internet connection, image stays null
		}
		
		urbanGame.setReward(!(urbanGame.getPrizesInfo() == null || urbanGame.getPrizesInfo().equals("")));
		return urbanGame;
	}
}
