package com.blstream.urbangame.webserver.deserialization;

import java.util.ArrayList;
import java.util.List;

import com.blstream.urbangame.database.entity.Task;
import com.google.gson.annotations.SerializedName;

/*
 * This class is an auxiliary class to make it easy to deserialize
 * result of "tasks" server query using GSON.
 */

public class TasksResponse extends JsonResponse {
	
	@SerializedName("_embedded")
	private Task[] embedded;
	
	public List<Task> getTaskList() {
		ArrayList<Task> taskList = new ArrayList<Task>();
		
		for (Task task : embedded) {
			taskList.add(task);
		}
		
		if (taskList.isEmpty()) return null;
		
		return taskList;
	}
}
