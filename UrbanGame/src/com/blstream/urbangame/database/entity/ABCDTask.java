package com.blstream.urbangame.database.entity;

import java.util.ArrayList;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class ABCDTask extends Task {
	
	private String question;
	private ArrayList<Answer> listOfAnswers;
	
	public ABCDTask() {
		super();
		question = null;
		listOfAnswers = null;
	}
	
	/**
	 * @param id
	 * @param title
	 * @param pictureBase64
	 * @param description
	 * @param isRepetable
	 * @param isHidden
	 * @param numberOfHidden
	 * @param endTime
	 * @param maxPoints
	 * @param question
	 * @param answers
	 */
	public ABCDTask(Long id, String title, String pictureBase64, String description, Boolean isRepetable,
		Boolean isHidden, Integer numberOfHidden, Date endTime, Integer maxPoints, String question, String[] answers) {
		super(id, Task.TASK_TYPE_ABCD, title, pictureBase64, description, isRepetable, isHidden, numberOfHidden,
			endTime, maxPoints);
		this.question = question;
		setAnswers(answers);
	}
	
	/**
	 * @return the answers
	 */
	public String[] getAnswers() {
		if (listOfAnswers == null) return null;
		else {
			String[] answersString = new String[listOfAnswers.size()];
			for (int i = 0; i < listOfAnswers.size(); i++) {
				answersString[i] = listOfAnswers.get(i).getAnswer();
			}
			return answersString;
		}
	}
	
	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(String[] answers) {
		if (answers == null) {
			this.listOfAnswers = null;
		}
		else {
			this.listOfAnswers = new ArrayList<Answer>();
			for (String element : answers) {
				listOfAnswers.add(new Answer(element));
			}
		}
	}
	
	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	
	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public ABCDTask(Parcel in) {
		super(in);
		question = in.readString();
		int size = in.readInt();
		listOfAnswers = new ArrayList<Answer>(size);
		for (int i = 0; i < size; i++) {
			listOfAnswers.add(new Answer(in));
		}
	}
	
	public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
		@Override
		public Task createFromParcel(Parcel in) {
			return new ABCDTask(in);
		}
		
		@Override
		public Task[] newArray(int size) {
			return new Task[size];
		}
	};
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeString(question);
		out.writeInt(listOfAnswers.size());
		for (Answer answer : listOfAnswers) {
			answer.writeToParcel(out, flags);
		}
	}
	
	public ArrayList<Answer> getAnswersList() {
		return listOfAnswers;
	}
	
	public void setAnswersList(ArrayList<Answer> answers) {
		listOfAnswers = answers;
	}
}
