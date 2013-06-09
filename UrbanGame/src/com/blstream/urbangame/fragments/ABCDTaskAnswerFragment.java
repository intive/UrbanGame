package com.blstream.urbangame.fragments;

import java.util.ArrayList;

import android.app.Activity;
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
import com.blstream.urbangame.webserver.mock.MockWebServer;

public class ABCDTaskAnswerFragment extends SherlockFragment {
	
	private AnswersAdapter adapter;
	private Task task;
	private PlayerTaskSpecific playerTaskSpecific;
	private ArrayList<Answer> answers;
	AnswerDialog dialog;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		dialog = new AnswerDialog(activity);
		
		task = getTask();
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
				if (task.getType() == Task.TASK_TYPE_ABCD) {
					onABCDAnswerSubmited(v);
				}
			}
		});
		
		if (task.getType() == Task.TASK_TYPE_ABCD) {
			TextView textViewQuestion = (TextView) view.findViewById(R.id.textViewQuestion);
			textViewQuestion.setText(((ABCDTask) task).getQuestion());
			
			answers = new ArrayList<Answer>();
			for (String element : ((ABCDTask) task).getAnswers()) {
				answers.add(new Answer(element));
			}
			adapter = new AnswersAdapter(getActivity(), R.layout.list_item_answer, answers);
			ListView list = (ListView) view.findViewById(R.id.listViewAnswers);
			list.setAdapter(adapter);
		}
		
		return view;
	}
	
	private void onABCDAnswerSubmited(View view) {
		
		if (task.isRepetable() || (!playerTaskSpecific.isFinishedByUser())) {
			// If task is repeatable or user has not finished it yet.
			ArrayList<String> answers = new ArrayList<String>();
			for (Answer element : this.answers) {
				if (element.isTrue()) {
					answers.add(element.getAnswer());
				}
			}
			
			// FIXME mock replace with asking server
			MockWebServer mockWebServer = new MockWebServer();
			ArrayList<String> correctAnswers = mockWebServer.sendAnswers(getActivity(), (ABCDTask) task, answers);
			
			if (correctAnswers == null) {
				// If there is no connection to the Internet.
				dialog.showDialog(DialogType.NO_INTERNET_CONNECTION, null, null);
			}
			else {
				Database database = new Database(getActivity());
				playerTaskSpecific = database.getPlayerTaskSpecific(task.getId(), database.getLoggedPlayerID());
				database.closeDatabase();
				
				Integer points = playerTaskSpecific.getPoints();
				Integer maxPoints = task.getMaxPoints();
				DialogType dialogType;
				
				if (points == 0) {
					dialogType = DialogType.WRONG_ANSWER;
				}
				else if (points == maxPoints) {
					dialogType = DialogType.RIGHT_ANSWER;
				}
				else {
					dialogType = DialogType.PARTIALLY_RIGHT_ANSWER;
				}
				dialog.showDialog(dialogType, points, maxPoints);
				
				if (!task.isRepetable()) {
					//show answers
					adapter.setCorrectAnswers(correctAnswers);
				}
			}
		}
	}
	
	private Task getTask() {
		//FIXME mock
		long taskID = DemoData.getTaskId();
		Bundle arguments = getArguments();
		if (arguments != null) {
			taskID = arguments.getLong(ActiveTaskActivity.TASK_ID, taskID);
		}
		
		DatabaseInterface database = new Database(getActivity());
		return database.getTask(taskID);
	}
}