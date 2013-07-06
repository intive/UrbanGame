package com.blstream.urbangame.webserver.json;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

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
			urbanGame.setDifficulty(parseDifficulty(jsonObjectArrayElement.get("difficulty").getAsString()));
			urbanGame.setOperatorName(jsonObjectArrayElement.get("operatorName").getAsString());
			urbanGame.setMaxPlayers(jsonObjectArrayElement.get("maxPlayers").getAsInt());
			urbanGame.setPlayers(jsonObjectArrayElement.get("numberOfPlayers").getAsInt());
			urbanGame.setPrizesInfo(jsonObjectArrayElement.get("awards").getAsString());
			try {
				urbanGame.setGameLogoDrawable(drawableFromUrl(JsonParser.URBANGAME_URL
					+ jsonObjectArrayElement.get("operatorLogo").getAsString()));
			}
			catch (IOException e) {
				//no internet connection logo stays null
			}
			try {
				urbanGame.setGameLogoDrawable(drawableFromUrl(JsonParser.URBANGAME_URL
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
	
	//translates difficulty from string to int
	private int parseDifficulty(String difficulty) {
		int result = 0;
		if (difficulty.equals("very easy")) {
			result = 1;
		}
		else if (difficulty.equals("easy")) {
			result = 2;
		}
		else if (difficulty.equals("medium")) {
			result = 3;
		}
		else if (difficulty.equals("hard")) {
			result = 4;
		}
		else if (difficulty.equals("extremely hard")) {
			result = 5;
		}
		return result;
	}
	
	private static Drawable drawableFromUrl(String url) throws IOException {
		Bitmap x;
		
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.connect();
		InputStream input = connection.getInputStream();
		
		x = BitmapFactory.decodeStream(input);
		return new BitmapDrawable(x);
	}
}
