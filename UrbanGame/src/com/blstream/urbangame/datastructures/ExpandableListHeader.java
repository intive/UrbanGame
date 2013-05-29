package com.blstream.urbangame.datastructures;

import java.util.ArrayList;

/** Header for expandable lists */
public class ExpandableListHeader<T> {
	private String mTitle;
	
	private ArrayList<T> mArrayTaskInfo;
	
	public String getTitle() {
		return mTitle;
	}
	
	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	
	public ArrayList<T> getArrayChildren() {
		return mArrayTaskInfo;
	}
	
	public void setArrayChildren(ArrayList<T> mArrayTaskInfo) {
		this.mArrayTaskInfo = mArrayTaskInfo;
	}
	
}
