package com.blstream.urbangame.tasks.test;

import java.util.ArrayList;

import android.test.AndroidTestCase;

import com.blstream.urbangame.adapters.TaskListExpandableListAdapter;
import com.blstream.urbangame.database.entity.ABCDTask;
import com.blstream.urbangame.database.entity.LocationTask;
import com.blstream.urbangame.database.entity.Task;
import com.blstream.urbangame.datastructures.ExpandableListHeader;

public class TaskListExpandableListAdapterTest extends AndroidTestCase {
	
	private final String ACTIVE_HEADER_TITLE = "Active";
	private final ExpandableListHeader<Task> active;
	private final ABCDTask abcdTask;
	
	private TaskListExpandableListAdapter adapter;
	private final ArrayList<ExpandableListHeader<Task>> arrayOfHeaders;
	
	public TaskListExpandableListAdapterTest() {
		super();
		arrayOfHeaders = new ArrayList<ExpandableListHeader<Task>>();
		active = new ExpandableListHeader<Task>();
		active.setTitle(ACTIVE_HEADER_TITLE);
		ArrayList<Task> tasks = new ArrayList<Task>();
		abcdTask = new ABCDTask();
		tasks.add(abcdTask);
		tasks.add(new LocationTask());
		active.setArrayChildren(tasks);
		arrayOfHeaders.add(active);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		adapter = new TaskListExpandableListAdapter(getContext(), arrayOfHeaders);
	}
	
	public void testGetGroupCount() {
		assertEquals(1, adapter.getGroupCount());
	}
	
	public void testGetChildrenCount() {
		assertEquals(2, adapter.getChildrenCount(0));
	}
	
	public void testGetGroup() {
		assertEquals(ACTIVE_HEADER_TITLE, adapter.getGroup(0));
	}
	
	public void testGetChild() {
		assertEquals(abcdTask, adapter.getChild(0, 0));
	}
	
}
