package com.blstream.urbangame.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import android.content.Context;

import com.blstream.urbangame.R;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.Player;
import com.blstream.urbangame.database.entity.PlayerGameSpecific;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.database.entity.UrbanGame;
import com.blstream.urbangame.database.helper.Base64ImageCoder;

public class DemoData {
	
	private final String USER = "demo@demo.com";
	private final String USER_PASSWORD = "demo";
	private final String WINNING_STRATEGY = "First in ranking";
	private final String PRIZE_INFO = "Winner will get a dwarf.";
	private final String DESCRIPTION = "You embark on a jurney to search every corner of our beautiful town to find dwarves.";
	@SuppressWarnings("deprecation")
	private final Date START_DATE_1 = new Date(113, 5, 20);
	@SuppressWarnings("deprecation")
	private final Date START_DATE_2 = new Date(113, 6, 8);
	@SuppressWarnings("deprecation")
	private final Date END_DATE_1 = new Date(113, 6, 15);
	@SuppressWarnings("deprecation")
	private final Date END_DATE_2 = new Date(113, 6, 13);
	private static final long FIRST_GAME_ID = 1000000L;
	private static final long FIRST_TASK_ID = 1000000L;
	private final String TASK_DESCRIPTION = "Yet another dwarf got lost...";
	
	public static final String[] answers = { "Nothing", "Guitar", "Newspaper", "Cards" };
	
	private final Context context;
	
	private ArrayList<String> listOfGamesNames;
	private ArrayList<String> listOfOperatorsNames;
	private ArrayList<String> listOfTasksNames;
	
	public DemoData(Context context) {
		this.context = context;
		initLists();
	}
	
	public void insertDataIntoDatabase() {
		DatabaseInterface database = new Database(context);
		
		Player player = new Player(USER, USER_PASSWORD, "Demo", (String) null);
		boolean success;
		
		success = database.getPlayer(USER) == null;
		
		if (success) {
			database.insertUser(player);
			insertGames(database);
			insertTasks(database);
			
		}
		
		database.closeDatabase();
	}
	
	private void insertGames(DatabaseInterface database) {
		
		Random random = new Random();
		UrbanGame urbanGame;
		PlayerGameSpecific playerGameSpecific;
		
		Integer numberOfPlayers;
		Integer maxNumberOfPlayers;
		String operatorName;
		Integer difficulty;
		boolean reward;
		String prizesInfo;
		String comments = "";
		String location = "Wroclaw";
		String detailsLink = "no link";
		String gameLogo = Base64ImageCoder.convertImage(context.getResources().getDrawable(R.drawable.ic_launcher_big));
		String operatorLogo = Base64ImageCoder.convertImage(context.getResources().getDrawable(
			R.drawable.mock_logo_operator));
		Date startDate;
		
		for (long i = 0; i < 10; i++) {
			numberOfPlayers = random.nextInt(100);
			difficulty = random.nextInt(3) + 1;
			if (i % 2 == 0) {
				maxNumberOfPlayers = numberOfPlayers + random.nextInt(100);
				operatorName = listOfOperatorsNames.get(0);
				reward = true;
				prizesInfo = PRIZE_INFO;
				startDate = START_DATE_1;
				playerGameSpecific = new PlayerGameSpecific(random.nextInt(20) + 1, USER, Long.valueOf(i
					+ FIRST_GAME_ID), "", false);
			}
			else {
				maxNumberOfPlayers = null;
				operatorName = listOfOperatorsNames.get(1);
				reward = false;
				prizesInfo = "";
				startDate = START_DATE_2;
				playerGameSpecific = new PlayerGameSpecific(USER, Long.valueOf(i + FIRST_GAME_ID), "some changes", true);
			}
			
			urbanGame = new UrbanGame(Long.valueOf(i + FIRST_GAME_ID), 1., listOfGamesNames.get((int) i), operatorName,
				WINNING_STRATEGY, numberOfPlayers, maxNumberOfPlayers, startDate, END_DATE_1, difficulty, reward,
				prizesInfo, DESCRIPTION, gameLogo, operatorLogo, comments, location, detailsLink);
			
			database.insertGameInfo(urbanGame);
			database.insertUserGameSpecific(playerGameSpecific);
			
		}
	}
	
	private void insertTasks(DatabaseInterface database) {
		
		Random random = new Random();
		Task task;
		PlayerTaskSpecific playerTaskSpecific;
		
		String question = "What this dwarf is holding?";
		String picture = Base64ImageCoder.convertImage(context.getResources().getDrawable(R.drawable.mock_task_image));
		boolean isRepetable;
		boolean isHidden;
		Integer maxPoints;
		Integer numberOfHidden = 0;
		Date endDate;
		boolean isFinishedByUser;
		Integer points;
		boolean areChanges;
		String changes;
		Integer status;
		
		for (long j = 0; j < 7; j++) {
			isRepetable = random.nextBoolean();
			isHidden = random.nextBoolean();
			points = random.nextInt(15);
			maxPoints = points + random.nextInt(10);
			isFinishedByUser = random.nextBoolean();
			areChanges = random.nextBoolean();
			if (areChanges) {
				changes = "some changes";
			}
			else {
				changes = "";
			}
			status = Integer.valueOf((int) j % 4);
			
			if (j % 2 == 0) {
				endDate = END_DATE_2;
				task = new ABCDTask(Long.valueOf(FIRST_TASK_ID + j), listOfTasksNames.get((int) j), picture,
					TASK_DESCRIPTION, isRepetable, isHidden, numberOfHidden, endDate, maxPoints, question, answers);
			}
			else {
				endDate = END_DATE_1;
				task = new LocationTask(Long.valueOf(FIRST_TASK_ID + j), listOfTasksNames.get((int) j), picture,
					TASK_DESCRIPTION, isRepetable, isHidden, numberOfHidden, endDate, maxPoints);
			}
			playerTaskSpecific = new PlayerTaskSpecific(USER, Long.valueOf(FIRST_TASK_ID + j), points,
				isFinishedByUser, areChanges, isHidden, changes, status, null);
			
			database.insertTaskForGame(Long.valueOf(FIRST_GAME_ID), task);
			database.insertPlayerTaskSpecific(playerTaskSpecific);
		}
		
	}
	
	private void initLists() {
		//games names
		listOfGamesNames = new ArrayList<String>();
		listOfGamesNames.add("Dwarves hunting");
		listOfGamesNames.add("Another dwarves adventure");
		listOfGamesNames.add("Dwarves story");
		listOfGamesNames.add("Yet another dwarves game that has very very very long name");
		listOfGamesNames.add("Dwarves everywhere");
		listOfGamesNames.add("Where the dwarves go?");
		listOfGamesNames.add("Dwarves adventure");
		listOfGamesNames.add("In search of dwarves");
		listOfGamesNames.add("Dwarves on vacation");
		listOfGamesNames.add("City dwarves");
		
		//operators names
		listOfOperatorsNames = new ArrayList<String>();
		listOfOperatorsNames.add("City hall");
		listOfOperatorsNames.add("BLStream");
		
		//tasks names
		listOfTasksNames = new ArrayList<String>();
		listOfTasksNames.add("Musical dwarf");
		listOfTasksNames.add("Missing dwarf");
		listOfTasksNames.add("Dwarf riding on a hippo");
		listOfTasksNames.add("Hungry dwarf");
		listOfTasksNames.add("Dwarf riding on a horse");
		listOfTasksNames.add("Dwarf biker");
		listOfTasksNames.add("Sleeping dwarf");
	}
	
	public void deletePlayer() {
		DatabaseInterface database = new Database(context);
		
		database.deletePlayer(USER);
		database.closeDatabase();
	}
	
	public static Long getGameId() {
		return Long.valueOf(FIRST_GAME_ID);
	}
	
	public static Long getTaskId() {
		return Long.valueOf(FIRST_TASK_ID);
	}
	
	public static ArrayList<String> getCorrectAnswers() {
		ArrayList<String> correctAnswers = null;
		Random random = new Random();
		
		if (random.nextBoolean()) {
			correctAnswers = new ArrayList<String>();
			correctAnswers.add(answers[1]);
			correctAnswers.add(answers[3]);
		}
		
		return correctAnswers;
	}
}
