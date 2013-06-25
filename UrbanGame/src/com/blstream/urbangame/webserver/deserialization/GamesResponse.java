package com.blstream.urbangame.webserver.deserialization;

import java.util.ArrayList;
import java.util.List;

import com.blstream.urbangame.database.entity.UrbanGameShortInfo;
import com.google.gson.annotations.SerializedName;

/*
 * This class is an auxiliary class to make it easy to deserialize
 * result of "games" server query using GSON.
 */

public class GamesResponse extends JsonResponse {
	
	@SerializedName("_embedded")
	private UrbanGameShortInfo[] embedded;
	
	public List<UrbanGameShortInfo> getUrbanGameShortInfoList() {
		ArrayList<UrbanGameShortInfo> urbanGameShortInfoList = new ArrayList<UrbanGameShortInfo>();
		
		for (UrbanGameShortInfo EmbeddedGame : embedded) {
			urbanGameShortInfoList.add(EmbeddedGame);
		}
		
		if (urbanGameShortInfoList.isEmpty()) return null;
		
		return urbanGameShortInfoList;
	}
	
}
