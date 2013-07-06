package com.blstream.urbangame.webserver.json;

import java.util.List;

import com.blstream.urbangame.database.entity.UrbanGame;

public class UrbanGameContainer {
	
	private List<UrbanGame> gameList;
	
	public UrbanGameContainer(List<UrbanGame> list) {
		gameList = list;
	}
	
	public List<UrbanGame> getGameList() {
		return gameList;
	}
	
	public void setGameList(List<UrbanGame> gameList) {
		this.gameList = gameList;
	}
	
}
