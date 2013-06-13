package com.blstream.urbangame.database.entity;

public class Answer {
	
	private String answer;
	private boolean isSelected;
	
	public Answer(String answer, boolean selected) {
		setAnswer(answer);
		this.isSelected = selected;
	}
	
	public Answer(String answer) {
		setAnswer(answer);
		this.isSelected = false;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public void setSelected(boolean selected) {
		this.isSelected = selected;
	}
}