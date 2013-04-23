package com.blstream.urbangame.database.entity;

import java.util.Date;

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
}
