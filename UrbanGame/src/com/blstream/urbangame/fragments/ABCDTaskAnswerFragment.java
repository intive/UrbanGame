package com.blstream.urbangame.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.blstream.urbangame.R;
import com.blstream.urbangame.adapters.AnswersAdapter;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.dialogs.AnswerDialog;
import com.blstream.urbangame.dialogs.AnswerDialog.DialogType;
import com.blstream.urbangame.helpers.Pair;
import com.blstream.urbangame.web.WebHighLevel;
import com.blstream.urbangame.web.WebHighLevelInterface;
import com.blstream.urbangame.webserver.ServerResponseHandler;
import com.blstream.urbangame.webserver.WebResponse;
import com.blstream.urbangame.webserver.WebServer.QueryType;
import com.blstream.urbangame.webserver.WebServerNotificationListener;

public class ABCDTaskAnswerFragment extends SherlockFragment implements WebServerNotificationListener, OnClickListener {
	private AnswersAdapter adapter;
	private ABCDTask task;
	private PlayerTaskSpecific playerTaskSpecific;
	private String[] answers;
	private ProgressDialog progressDialog;
	private ListView list;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		progressDialog = new ProgressDialog(activity);
		task = getSelectedTask();
		playerTaskSpecific = getCurrentPlayerTaskSpecific();
		answers = task.getAnswers();
	}
	
	private ABCDTask getSelectedTask() {
		return getArguments().getParcelable(Task.TASK_KEY);
	}
	
	private PlayerTaskSpecific getCurrentPlayerTaskSpecific() {
		DatabaseInterface database = new Database(getActivity());
		long taskID = task.getId();
		String loggedPlayerID = database.getLoggedPlayerID();
		
		return getPlayerTaskSpecificOrCreateIfNooneInDB(database, taskID, loggedPlayerID);
	}
	
	private PlayerTaskSpecific getPlayerTaskSpecificOrCreateIfNooneInDB(DatabaseInterface database, long taskID,
		String loggedPlayerID) {
		PlayerTaskSpecific playerTaskSpecific = getPlayerTaskSpecificFromDB(database, taskID, loggedPlayerID);
		if (doesPlayerTaskSpecificNotExist(playerTaskSpecific)) {
			playerTaskSpecific = createNewPlayerTaskSpecific(taskID, loggedPlayerID);
		}
		return playerTaskSpecific;
	}
	
	private PlayerTaskSpecific getPlayerTaskSpecificFromDB(DatabaseInterface database, long taskID, String loggedPlayerID) {
		return database.getPlayerTaskSpecific(taskID, loggedPlayerID);
	}
	
	private boolean doesPlayerTaskSpecificNotExist(PlayerTaskSpecific playerTaskSpecific) {
		return playerTaskSpecific == null;
	}
	
	private PlayerTaskSpecific createNewPlayerTaskSpecific(long taskID, String loggedPlayerID) {
		return new PlayerTaskSpecific(loggedPlayerID, taskID, 0, false, false, task.isHidden(), null,
			PlayerTaskSpecific.ACTIVE, null);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_answer_abcd, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		Button button = (Button) view.findViewById(R.id.buttonSubmitAnswer);
		button.setOnClickListener(this);
		
		TextView textViewQuestion = (TextView) view.findViewById(R.id.textViewQuestion);
		textViewQuestion.setText(task.getQuestion());
		
		list = (ListView) view.findViewById(R.id.listViewAnswers);
		populateListView();
	}
	
	private void populateListView() {
		adapter = getAnswersAdapter();
		disableAnswersIfTaskNotRepeatable();
		list.setAdapter(adapter);
	}
	
	protected void disableAnswersIfTaskNotRepeatable() {
		if (!task.isRepetable()) {
			adapter.setEnabled(!playerTaskSpecific.isFinishedByUser());
		}
	}
	
	private AnswersAdapter getAnswersAdapter() {
		List<Pair<String, Boolean>> answersList = createAnswersList();
		return new AnswersAdapter(getActivity(), R.layout.list_item_answer, answersList);
	}
	
	private List<Pair<String, Boolean>> createAnswersList() {
		boolean[] selections = playerTaskSpecific.getSelectedAnswers();
		if (isTaskNewOrUpdated(answers, selections)) return getAllAnswersFalse();
		else return getAllAnswersFromSelections(selections);
	}
	
	private List<Pair<String, Boolean>> getAllAnswersFalse() {
		return getAllAnswersFromSelections(new boolean[answers.length]);
	}
	
	private List<Pair<String, Boolean>> getAllAnswersFromSelections(boolean[] selections) {
		List<Pair<String, Boolean>> list = new ArrayList<Pair<String, Boolean>>();
		for (int i = 0; i < answers.length; i++) {
			list.add(new Pair<String, Boolean>(answers[i], selections[i]));
		}
		return list;
	}
	
	private boolean isTaskNewOrUpdated(String[] answers, boolean[] selections) {
		return selections == null || answers.length != selections.length;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		updateSelectionsForPlayerTaskSpecific();
		playerTaskSpecific.setSelectedAnswers(getSelections());
	}
	
	private void updateSelectionsForPlayerTaskSpecific() {
		PlayerTaskSpecific updatePlayerTaskSpecific = getUpdatedPlayerTaskSpecific();
		updatePlayerTaskSpecificInDB(updatePlayerTaskSpecific);
	}
	
	private PlayerTaskSpecific getUpdatedPlayerTaskSpecific() {
		PlayerTaskSpecific updatePlayerTaskSpecific = new PlayerTaskSpecific();
		updatePlayerTaskSpecific.setPlayerEmail(playerTaskSpecific.getPlayerEmail());
		updatePlayerTaskSpecific.setTaskID(playerTaskSpecific.getTaskID());
		updatePlayerTaskSpecific.setSelectedAnswers(getSelections());
		return updatePlayerTaskSpecific;
	}
	
	private boolean[] getSelections() {
		List<Pair<String, Boolean>> list = adapter.getItems();
		boolean[] selections = new boolean[list.size()];
		for (int i = 0; i < list.size(); i++) {
			selections[i] = list.get(i).second;
		}
		return selections;
	}
	
	private void updatePlayerTaskSpecificInDB(PlayerTaskSpecific updatePlayerTaskSpecific) {
		DatabaseInterface db = new Database(getActivity());
		db.updatePlayerTaskSpecific(updatePlayerTaskSpecific);
		db.closeDatabase();
	}
	
	@Override
	public void onClick(View v) {
		onABCDAnswerSubmited(v);
	}
	
	private void onABCDAnswerSubmited(View view) {
		if (canAnswersBeSubmitted()) {
			progressDialog.show();
			
			WebHighLevelInterface web = new WebHighLevel(new ServerResponseHandler(this), getActivity());
			web.sendAnswersForABCDTask(task, getAnswersList());
		}
	}
	
	private boolean canAnswersBeSubmitted() {
		return task.isRepetable() || !playerTaskSpecific.isFinishedByUser();
	}
	
	private ArrayList<String> getAnswersList() {
		boolean[] selections = getSelections();
		ArrayList<String> answers = new ArrayList<String>();
		
		for (int i = 0; i < this.answers.length; i++) {
			if (selections[i]) {
				answers.add(this.answers[i]);
			}
		}
		
		return answers;
	}
	
	@Override
	public void onWebServerResponse(Message message) {
		progressDialog.dismiss();
		
		// FIXME get response from message
		WebResponse serverResponse = new WebResponse(QueryType.SendAnswersForABCDTask);
		
		addPointsForPlayerTaskSpecific(serverResponse);
		showAnswerDialog(serverResponse);
		configureTask(serverResponse);
	}
	
	private void addPointsForPlayerTaskSpecific(WebResponse serverResponse) {
		playerTaskSpecific.setPoints(serverResponse.points);
		playerTaskSpecific.setIsFinishedByUser(true);
		playerTaskSpecific.setSelectedAnswers(getSelections());
		updatePlayerTaskSpecificInDB(playerTaskSpecific);
	}
	
	private void showAnswerDialog(WebResponse serverResponse) {
		Integer maxPoints = task.getMaxPoints();
		DialogType dialogType;
		
		if (serverResponse.points == 0) {
			dialogType = DialogType.WRONG_ANSWER;
		}
		else if (serverResponse.points == maxPoints) {
			dialogType = DialogType.RIGHT_ANSWER;
		}
		else {
			dialogType = DialogType.PARTIALLY_RIGHT_ANSWER;
		}
		new AnswerDialog(getActivity()).showDialog(dialogType, serverResponse.points, maxPoints);
	}
	
	private void configureTask(WebResponse serverResponse) {
		if (!task.isRepetable()) {
			adapter.setCorrectAnswers(serverResponse.correctAnswers);
		}
	}
}