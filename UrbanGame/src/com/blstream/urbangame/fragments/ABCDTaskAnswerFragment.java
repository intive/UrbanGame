package com.blstream.urbangame.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

public class ABCDTaskAnswerFragment extends SherlockFragment {
	
	private AnswersAdapter adapter;
	private ABCDTask task;
	private PlayerTaskSpecific playerTaskSpecific;
	private String[] answers;
	private boolean[] selections;
	AnswerDialog dialog;
	
	public static class ServerResponseToSendedAnswers {
		public boolean noInternetConnection = false;
		public ArrayList<String> correctAnswers = null;
		public Integer points = 0;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		dialog = new AnswerDialog(activity);
		
		task = getArguments().getParcelable(Task.TASK_KEY);
		DatabaseInterface database = new Database(activity);
		playerTaskSpecific = database.getPlayerTaskSpecific(task.getId(), database.getLoggedPlayerID());
		if (playerTaskSpecific == null) {
			playerTaskSpecific = new PlayerTaskSpecific(database.getLoggedPlayerID(), task.getId(), 0, false, false,
				task.isHidden(), null, PlayerTaskSpecific.ACTIVE, null);
		}
		database.closeDatabase();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_answer_abcd, container, false);
		
		Button button = (Button) view.findViewById(R.id.buttonSubmitAnswer);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onABCDAnswerSubmited(v);
			}
		});
		
		TextView textViewQuestion = (TextView) view.findViewById(R.id.textViewQuestion);
		textViewQuestion.setText(task.getQuestion());
		
		answers = task.getAnswers();
		selections = playerTaskSpecific.getSelectedAnswers();
		List<Pair<String, Boolean>> adapterAnswersAndSelectionsList = makeListFromArrays(answers, selections);
		adapter = new AnswersAdapter(getActivity(), R.layout.list_item_answer, adapterAnswersAndSelectionsList);
		ListView list = (ListView) view.findViewById(R.id.listViewAnswers);
		list.setAdapter(adapter);
		
		return view;
	}
	
	private List<Pair<String, Boolean>> makeListFromArrays(String[] answers, boolean[] selections) {
		List<Pair<String, Boolean>> list = new ArrayList<Pair<String, Boolean>>();
		if (selections == null || answers.length != selections.length) { //this is when some task update could provide more or less answers in task update
			for (String answer : answers) {
				list.add(new Pair<String, Boolean>(answer, false));
			}
		}
		else {
			for (int i = 0; i < answers.length; i++) {
				list.add(new Pair<String, Boolean>(answers[i], selections[i]));
			}
		}
		return list;
	}
	
	@Override
	public void onDestroyView() {
		
		List<Pair<String, Boolean>> list = adapter.getItems();
		boolean[] selections = new boolean[list.size()];
		for (int i = 0; i < list.size(); i++) {
			selections[i] = list.get(i).second;
		}
		
		//database update
		PlayerTaskSpecific updatePlayerTaskSpecific = new PlayerTaskSpecific();
		updatePlayerTaskSpecific.setPlayerEmail(playerTaskSpecific.getPlayerEmail());
		updatePlayerTaskSpecific.setTaskID(playerTaskSpecific.getTaskID());
		updatePlayerTaskSpecific.setSelectedAnswers(selections);
		DatabaseInterface db = new Database(getActivity());
		db.updatePlayerTaskSpecific(updatePlayerTaskSpecific);
		db.closeDatabase();
		
		// parcelable content update
		playerTaskSpecific.setSelectedAnswers(selections);
		
		super.onDestroyView();
	}
	
	private void onABCDAnswerSubmited(View view) {
		
		if (task.isRepetable() || (!playerTaskSpecific.isFinishedByUser())) {
			// If task is repeatable or user has not finished it yet.
			ArrayList<String> answers = new ArrayList<String>();
			for (int i = 0; i < this.answers.length; i++) {
				if (selections[i]) {
					answers.add(this.answers[i]);
				}
			}
			ProgressDialog progressDialog = new ProgressDialog(getActivity());
			progressDialog.show();
			
			WebHighLevelInterface web = new WebHighLevel(getActivity());
			ServerResponseToSendedAnswers serverResponse = web.sendAnswersForABCDTask(task, answers);
			
			progressDialog.dismiss();
			
			if (serverResponse.noInternetConnection) {
				// If there is no connection to the Internet.
				dialog.showDialog(DialogType.NO_INTERNET_CONNECTION, null, null);
			}
			else {
				playerTaskSpecific.setPoints(serverResponse.points);
				playerTaskSpecific.setIsFinishedByUser(true);
				
				Database database = new Database(getActivity());
				database.updatePlayerTaskSpecific(playerTaskSpecific);
				database.closeDatabase();
				
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
				dialog.showDialog(dialogType, serverResponse.points, maxPoints);
				
				if (!task.isRepetable()) {
					//show answers
					adapter.setCorrectAnswers(serverResponse.correctAnswers);
				}
			}
		}
	}
}
