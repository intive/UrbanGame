package com.blstream.urbangame.database.entity;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationTask extends Task {
	
	public LocationTask() {
		super();
		// TODO Auto-generated constructor stub
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
	 */
	public LocationTask(Long id, String title, String pictureBase64, String description, Boolean isRepetable,
		Boolean isHidden, Integer numberOfHidden, Date endTime, Integer maxPoints) {
		super(id, Task.TASK_TYPE_LOCATION, title, pictureBase64, description, isRepetable, isHidden, numberOfHidden,
			endTime, maxPoints);
	}
	
	public LocationTask(Parcel in) {
		super(in);
	}
	
	public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
		@Override
		public Task createFromParcel(Parcel in) {
			return new LocationTask(in);
		}
		
		@Override
		public Task[] newArray(int size) {
			return new Task[size];
		}
	};
}
