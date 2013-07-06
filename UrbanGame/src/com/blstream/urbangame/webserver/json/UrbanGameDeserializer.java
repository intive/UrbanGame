package com.blstream.urbangame.webserver.json;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

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
			urbanGame.setGameLogoDrawable(drawableFromUrl(JsonParser.URBANGAME_URL
				+ jsonObject.get("operatorLogo").getAsString()));
		}
		catch (IOException e) {
			//no internet connection logo stays null
		}
		urbanGame.setStartDate(new Date(jsonObject.get("startTime").getAsLong()));
		urbanGame.setEndDate(new Date(jsonObject.get("endTime").getAsLong()));
		urbanGame.setWinningStrategy(jsonObject.get("winning").getAsString());
		urbanGame.setDifficulty(parseDifficulty(jsonObject.get("difficulty").getAsString()));
		urbanGame.setPrizesInfo(jsonObject.get("awards").getAsString());
		try {
			urbanGame.setGameLogoDrawable(drawableFromUrl(JsonParser.URBANGAME_URL
				+ jsonObject.get("image").getAsString()));
		}
		catch (IOException e) {
			//no internet connection, image stays null
		}
		
		urbanGame.setReward(!(urbanGame.getPrizesInfo() == null || urbanGame.getPrizesInfo().equals("")));
		return urbanGame;
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
