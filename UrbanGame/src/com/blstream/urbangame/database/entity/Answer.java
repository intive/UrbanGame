package com.blstream.urbangame.database.entity;

public class Answer {
	
	private String answer;
	private boolean isTrue;
	
	public Answer(String answer, boolean isTrue) {
		setAnswer(answer);
		setIsTrue(isTrue);
	}
	
	public Answer(String answer) {
		setAnswer(answer);
		setIsTrue(false);
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public void setIsTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}
	
	public boolean isTrue() {
		return isTrue;
	}

}
