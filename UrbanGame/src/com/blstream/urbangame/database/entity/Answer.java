package com.blstream.urbangame.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Answer implements Parcelable {
	
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
	
	public Answer(Parcel in) {
		answer = in.readString();
		isSelected = in.readByte() == 1;
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
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(answer);
		dest.writeByte((byte) (isSelected ? 1 : 0));
	}
}