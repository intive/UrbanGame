package com.blstream.urbangame.webserver.deserialization;

import java.util.ArrayList;
import java.util.List;

import com.blstream.urbangame.database.entity.UrbanGameShortInfo;

/*
 * This class is an auxiliary class to make it easy to deserialize
 * result of "games" server query using GSON.
 */

public class GamesResponse extends JsonResponse {
	
	private UrbanGameShortInfo[] _embedded;
	
	public List<UrbanGameShortInfo> getUrbanGameShortInfoList() {
		ArrayList<UrbanGameShortInfo> urbanGameShortInfoList = new ArrayList<UrbanGameShortInfo>();
		
		for (UrbanGameShortInfo EmbeddedGame : _embedded) {
			urbanGameShortInfoList.add(EmbeddedGame);
		}
		
		if (urbanGameShortInfoList.isEmpty()) return null;
		
		return urbanGameShortInfoList;
	}
	
}
