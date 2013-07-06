package com.blstream.urbangame.webserver.json;

import java.util.List;

import com.blstream.urbangame.database.entity.Task;

public class TaskContainer {
	
	private List<Task> taskList;
	
	public TaskContainer(List<Task> list) {
		setTaskList(list);
	}
	
	public List<Task> getTaskList() {
		return taskList;
	}
	
	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}
	
}
