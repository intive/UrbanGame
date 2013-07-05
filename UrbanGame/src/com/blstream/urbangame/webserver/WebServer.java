package com.blstream.urbangame.webserver;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;

import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.example.DemoData;
import com.blstream.urbangame.web.WebHighLevelInterface;

/**
 * Basic class for downloading and updating data from web server.
 */
// formatter:off
/*
 * Example of use
  
  class A extends Activity implements WebServerNotificationListener {
		private ServerResponseHandler handler;
		private WebServer webServer;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			...
		
			this.handler = new ServerResponseHandler(this);
			this.webServer = new WebServer(handler);
			
			...
		}
		
		public void doWhatever() {
			webServer.doWhatever();
		}
		
		@Override
		public void onWebServerResponse(Message message) {
			WebResponse webResponse = (WebResponse) message.obj;
			Bundle bundle = message.getData();
			...
		}
  }
 */
// formatter:on
// TODO add required query types and corresponding methods
public class WebServer implements WebHighLevelInterface {
	private final ServerResponseHandler handler;
	private final Context context;
	
	public WebServer(Context context, ServerResponseHandler handler) {
		this.handler = handler;
		this.context = context;
	}
	
	// formatter:off
	public enum QueryType {
		DownloadGamesList, DownloadUsersGames, DownloadGameDetails, DownloadTasksForGame, DownloadTaskDetails,
		LoginUser, RegisterPlayer, JoinCurrentPlayerToTheGame, LeaveCurrentPlayerFromTheGame,
		SendAnswersForABCDTask, SendAnswerForLocationTask, GetCorrectAnswerForGpsTask
	}
	// formatter:on
	
	public void getAllGames() {
		new WebRequest(context, this, QueryType.DownloadGamesList).execute();
	}
	
	/// and so on...
	
	public void onWebServerResponse(WebResponse webResponse) {
		Message message = getMessageWithResponse(webResponse);
		configureMessageBundle(message);
		
		handler.sendMessage(message);
	}
	
	protected Message getMessageWithResponse(WebResponse webResponse) {
		Message message = new Message();
		message.obj = webResponse;
		return message;
	}
	
	protected void configureMessageBundle(Message message) {
		Bundle bundle = new Bundle();
		
		// TODO pack required data in bundle
		/**
		 * Here we can configure message as we wish. We can add here a bunch of
		 * pairs <key, value> describing content of response for our client that
		 * it'll have sufficient information about notification response
		 */
		message.setData(bundle);
	}
	
	@Override
	public void downloadGamesList() {
		new WebRequest(context, this, QueryType.DownloadGamesList).execute();
	}
	
	@Override
	public void downloadUsersGames() {
		new WebRequest(context, this, QueryType.DownloadUsersGames).execute();
	}
	
	@Override
	public void downloadGameDetails(Long selectedGameID) {
		new WebRequest(context, this, QueryType.DownloadGameDetails, selectedGameID).execute();
	}
	
	@Override
	public void joinCurrentPlayerToTheGame(Long selectedGameID) {
		new WebRequest(context, this, QueryType.JoinCurrentPlayerToTheGame, selectedGameID).execute();
	}
	
	@Override
	public void leaveCurrentPlayerFromTheGame(Long selectedGameID) {
		new WebRequest(context, this, QueryType.LeaveCurrentPlayerFromTheGame, selectedGameID).execute();
	}
	
	@Override
	public void loginUser(String email, String password) {
		new WebRequest(context, this, QueryType.LoginUser, email, password).execute();
	}
	
	@Override
	public void registerPlayer(String email, String displayName, String password) {
		new WebRequest(context, this, QueryType.RegisterPlayer, email, password, displayName).execute();
	}
	
	@Override
	public void downloadTasksForGame(long gameID) {
		new WebRequest(context, this, QueryType.DownloadTasksForGame, gameID).execute();
	}
	
	@Override
	public void sendAnswersForABCDTask(ABCDTask task, ArrayList<String> answers) {
		
		/*
		 * TODO Parse response and insert data in WebResponse
		 */
		WebResponse webResponse = new WebResponse(QueryType.SendAnswersForABCDTask);
		ArrayList<String> correctAnswers = DemoData.getCorrectAnswers();
		if (correctAnswers != null) {
			
			int maxPoints = task.getMaxPoints();
			int points = 0;
			int numberOfCorrectAnswers = 0;
			
			for (String element : correctAnswers) {
				if (answers.contains(element)) {
					numberOfCorrectAnswers++;
				}
			}
			
			if (numberOfCorrectAnswers == correctAnswers.size()) {
				points = maxPoints;
			}
			else {
				points = maxPoints / correctAnswers.size() * numberOfCorrectAnswers;
			}
			
			webResponse.correctAnswers = correctAnswers;
			webResponse.points = points;
		}
		
		new WebRequest(context, this, QueryType.SendAnswersForABCDTask, task, answers).execute();
	}
	
	@Override
	public void sendAnswerForLocationTask(Task task, Location location) {
		new WebRequest(context, this, QueryType.SendAnswersForABCDTask, task, location).execute();
	}
	
	@Override
	public void getCorrectAnswerForGpsTask(Task task) {
		new WebRequest(context, this, QueryType.GetCorrectAnswerForGpsTask, task).execute();
	}
}