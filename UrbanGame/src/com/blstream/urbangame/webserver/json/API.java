package com.blstream.urbangame.webserver.json;

import com.google.gson.annotations.SerializedName;

public class API {
	@SerializedName("_links")
	public Links links;
	
	public class Links {
		@SerializedName("self")
		public Link self;
		
		@SerializedName("games")
		public Link games;
		
		@SerializedName("login")
		public Link login;
		
		@SerializedName("register")
		public Link register;
		
		@SerializedName("userGames")
		public Link userGames;
		
		public class Link {
			@SerializedName("href")
			public String href;
		}
	}
}