package com.blstream.urbangame.fragments;

import java.util.ArrayList;

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
import com.blstream.urbangame.ActiveTaskActivity;
import com.blstream.urbangame.R;
import com.blstream.urbangame.adapters.AnswersAdapter;
import com.blstream.urbangame.database.Database;
import com.blstream.urbangame.database.DatabaseInterface;
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.Answer;
import com.blstream.urbangame.database.entity.PlayerTaskSpecific;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.dialogs.AnswerDialog;
import com.blstream.urbangame.dialogs.AnswerDialog.DialogType;
import com.blstream.urbangame.example.DemoData;

public class ABCDTaskAnswerFragment extends SherlockFragment {
	
	private AnswersAdapter adapter;
	private Task task;
	private PlayerTaskSpecific playerTaskSpecific;
	private ArrayList<Answer> answers;
	AnswerDialog dialog;
	
	public class ServerResponseToSendedAnswers {
		public boolean noInternetConnection = false;
		public ArrayList<String> correctAnswers = null;
		public Integer points = 0;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		dialog = new AnswerDialog(activity);
		
		task = getTask(); // FIXME change to getting task from parcel
		DatabaseInterface database = new Database(activity);
		playerTaskSpecific = database.getPlayerTaskSpecific(task.getId(), database.getLoggedPlayerID());
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
		textViewQuestion.setText(((ABCDTask) task).getQuestion());
		
		answers = new ArrayList<Answer>();
		for (String element : ((ABCDTask) task).getAnswers()) {
			answers.add(new Answer(element));
		}
		adapter = new AnswersAdapter(getActivity(), R.layout.list_item_answer, answers);
		ListView list = (ListView) view.findViewById(R.id.listViewAnswers);
		list.setAdapter(adapter);
		
		return view;
	}
	
	private void onABCDAnswerSubmited(View view) {
		
		if (task.isRepetable() || (!playerTaskSpecific.isFinishedByUser())) {
			// If task is repeatable or user has not finished it yet.
			ArrayList<String> answers = new ArrayList<String>();
			for (Answer element : this.answers) {
				if (element.isSelected()) {
					answers.add(element.getAnswer());
				}
			}
			ProgressDialog progressDialog = new ProgressDialog(getActivity());
			progressDialog.show();
			
			ServerResponseToSendedAnswers serverResponse = sendAnswers((ABCDTask) task, answers);
			
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
	
	private Task getTask() {
		long taskID = 0L;
		Bundle arguments = getArguments();
		if (arguments != null) {
			taskID = arguments.getLong(ActiveTaskActivity.TASK_ID, taskID);
		}
		
		DatabaseInterface database = new Database(getActivity());
		return database.getTask(taskID);
	}
	
	// MOCK
	public ServerResponseToSendedAnswers sendAnswers(ABCDTask task, ArrayList<String> answers) {
		
		ServerResponseToSendedAnswers serverResponse = new ServerResponseToSendedAnswers();
		
		ArrayList<String> correctAnswers = null;
		
		correctAnswers = DemoData.getCorrectAnswers();
		
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
			
			serverResponse.correctAnswers = correctAnswers;
			serverResponse.points = points;
		}
		else {
			serverResponse.noInternetConnection = true;
		}
		
		return serverResponse;
	}
}